package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

class Socks{
	/*
	 * 建立监听服务
	 * @lixuehui
	 */
	private volatile static String MainMsg;//保存用户的当前发送信息，用于转发回送所有用户
	private volatile static boolean isChange; //用于判断聊天信息是否更新
	private volatile static int ThreadMax = 0; //最大回发送线程数
	private volatile static int NowThread = 0 ;//目前已开启线程数
	private volatile static int NowSendPort;//当前MainMsg存储信息的发送端口
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
												//String SendPort = "端口"+String.valueOf(NowSendPort)+":";//设置回送信息中目标客户端端口
												//String PortMsg = SendPort + MainMsg ;
												String PortMsg = "[端口"+port+"] "+MainMsg; //回送信息加上客户端端口号
												Data.write(PortMsg.getBytes());
												
												System.out.println("发送成功");
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											//初始化接收数据的Byte数组
											while(size != -1) { 
												GetMsg[size] = (byte)0;
												size--;
											}
											size = GetMsg.length-1;
											System.out.println("目前已连接用户："+ThreadMax+" 回信线程数："+NowThread);
										}
										
									}
									
							});
						
							ReSend.start(); //执行线程
							//ReSend.sleep(10000000);
							System.out.println("目前已连接用户："+ThreadMax+" 回信线程数："+NowThread);
						}else {
							System.out.println("目前已连接用户："+ThreadMax+" 回信线程数："+NowThread+"已达最允许线程数！");
						}
						
						
						int len = GetData.read(GetMsg);//从流中获取数据并存放在GetMsg 变量中，返回数据长度；
						while(len != -1) {
							System.out.println("接收到"+port+"端口发送的："+new String(GetMsg,0,len));
							NowSendPort = port;//存储当前线程的客户端端口
							MainMsg = new String(GetMsg,0,len); //将接收的消息保存在公共变量里
							isChange = true;//更改状态
							
						
							len = GetData.read(GetMsg);
						}
						
						
					}
					
				}catch(Exception e) {
					System.out.println(e.getMessage());
					System.out.println("断开连接！");
					try {
						Server.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					ThreadMax -=1;
					
				}
				
		});
			
			thread.start();//执行线程！
	}
	
	}
}



public class Server {
	public static void main(String[] args) {
		try {
			Socks so = new Socks();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
