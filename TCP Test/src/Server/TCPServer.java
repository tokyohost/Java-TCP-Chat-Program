package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

class Socks{
	/*
	 * 建立监听服务
	 * @lixuehui
	 */
	private volatile static String MainMsg;//用于保存用户的当前发送信息，用于回送所有用户
	private volatile static boolean isChange; //用于判断聊天信息是否更新
	private volatile static int ThreadMax = 0; //最大回发送线程数
	private volatile static int NowThread = 0 ;//目前已开启线程数
	public Socks() throws Exception{
		ServerSocket DS;
		DS= new ServerSocket(7788);//创建一个端口为7788 的Socket 的对象
		
		
		//使用死循环接收客户端发送的连接
		while(true) {
			//调用DS 的accept()的方法来创建连接
			System.out.println("服务器已运行，正在等待连接");
			Socket Server = DS.accept(); //等待连接
			ThreadMax+=1;
			
			String Message = null; //所有用户发送的信息临时存储，供所有线程调用
			Thread thread = new Thread(()->{ 
			//此处新开线程创建与客户端单独的连接
				try {
					int port = Server.getPort();//获取客户端的连接端口
					
					while(true) {
						System.out.println("与端口号："+port+"的用户建立连接成功！");
						
						InputStream GetData = Server.getInputStream();//获取客户端发送的数据
						byte[] GetMsg= new byte[4096];
						
						if(NowThread < ThreadMax) {
							Thread ReSend = new Thread(()->{
								int size = GetMsg.length-1;
								//新开线程返回客户端数据
								NowThread +=1;
									while(true) {	
										if(isChange) {
											try {
												System.out.println("开启发送线程");
												isChange = false;
												OutputStream Data = Server.getOutputStream();
//												String REPort = "您的端口号为"+String.valueOf(port);  
//												Data.write(REPort.getBytes());  //返回用户当前连接端口信息
//												String PortMsg = "端口"+ String.valueOf(port) +":"+ MainMsg ;
												String PortMsg = "收到消息："+ MainMsg ;
												Data.write(PortMsg.getBytes());
												
												System.out.println("发送成功");
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											while(size != -1) { //初始化数组
												GetMsg[size] = (byte)0;
												size--;
											}
											size = GetMsg.length-1;
											System.out.println("目前已连接用户："+ThreadMax+"回信线程数："+NowThread);
										}
										
										
									
									}
									
							});
						
							ReSend.start(); //执行线程
							//ReSend.sleep(10000000);
							System.out.println("目前已连接用户："+ThreadMax+"回信线程数："+NowThread);
						}else {
							System.out.println("目前已连接用户："+ThreadMax+"回信线程数："+NowThread+"已达最允许线程数！");
						}
						
						
						int len = GetData.read(GetMsg);//从流中获取数据并存放在GetMsg 变量中，返回数据长度；
						while(len != -1) {
							System.out.println("接收到客户端发送的："+new String(GetMsg,0,len));
							MainMsg = new String(GetMsg,0,len); //将接收的消息保存在公共变量里
							isChange = true;//更改状态
							
						
							len = GetData.read(GetMsg);
						}
						
						
						
					
						//String SendMessage = new String(GetMsg, GetMsg.length);//转换格式准备发送
						//System.out.println("正在准备向客户端发送数据！转换后的数据为："+SendMessage);
						
						//Thread.sleep(5000);
						//System.out.println("Close Connect now !");
//						Data.close();//关闭流
//						DS.close();//关闭流
//						Server.close();
//						GetData.close();
						//break;
					}
					
				}catch(Exception e) {
					System.out.println(e.getMessage());
				}
				
		});
			
			thread.start();//执行线程！
	}
	
	}
}



public class TCPServer {
	public static void main(String[] args) {
		try {
			Socks so = new Socks();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
