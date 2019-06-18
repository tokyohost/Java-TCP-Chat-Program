package Client;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.awt.font.TextMeasurer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

class Client extends JFrame implements ActionListener{
	 JTextField txtMessage;
	JTextArea textPane;
	JButton btnSend;
	private JTextField txtServerIpAddress;
	private JTextField txtPort;
	
	InetAddress IP = null;
	int port;
	private JButton btnNewButton;
	private static boolean isSendMsg = false;
	
	public Socket Send;
	public Client() throws Exception{
		getContentPane().setLayout(null);

		
		this.setSize(600, 450);
		txtMessage = new JTextField();
		txtMessage.setText("Message");
		txtMessage.setBounds(35, 21, 312, 35);
		getContentPane().add(txtMessage);
		txtMessage.setColumns(10);
		
		textPane = new JTextArea();
		textPane.setBounds(35, 151, 465, 207);
		JScrollPane ScrollPane = new JScrollPane(textPane);
		getContentPane().add(textPane);
		getContentPane().add(ScrollPane);
		
		btnSend = new JButton("Send");
		btnSend.setBounds(355, 20, 153, 37);
		getContentPane().add(btnSend);
		
		txtServerIpAddress = new JTextField();
		txtServerIpAddress.setText("localhost");
		txtServerIpAddress.setBounds(35, 77, 153, 35);
		getContentPane().add(txtServerIpAddress);
		txtServerIpAddress.setColumns(10);
		
		txtPort = new JTextField();
		txtPort.setText("7788");
		txtPort.setBounds(194, 77, 153, 35);
		getContentPane().add(txtPort);
		txtPort.setColumns(10);
		
		btnNewButton = new JButton("Connect");
		btnNewButton.setBounds(355, 78, 153, 37);
		getContentPane().add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					//获取服务器IP地址
					IP = InetAddress.getByName(txtServerIpAddress.getText());
					//获取服务器端口
					port = Integer.valueOf(txtPort.getText());
					
					Send = new Socket(IP, port); //建立与服务器的连接
					textPane.append(" 已获取IP 和Port ，与服务器连接成功！");
					
					btnNewButton.setEnabled(false); //使按钮不能再次单击
				}catch(UnknownHostException e1) {
					textPane.append("建立链接失败，请检查服务端是否启动！");
					System.out.println(e1.getMessage());
					textPane.append("\n"+e1.getMessage());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					textPane.append("建立链接失败，请检查服务端是否启动！");
					System.out.println(e1.getMessage());
					textPane.append("\n"+e1.getMessage());
				} 
				
				new Thread(new Runnable() {
					/*
					 * (non-Javadoc)
					 * @see java.lang.Runnable#run()
					 * 创建线程用于接收信息
					 */
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						
								try {
									System.out.println("正在等待接收服务器返回信息!");
									
									InputStream GetMsg = Send.getInputStream();
									byte[] buf =new byte[4096];
									int len = GetMsg.read(buf);
								
									while(len != -1) {
										
										if(isSendMsg) {
											textPane.append("\n "+"您：" + txtMessage.getText());
											isSendMsg = false;
										}else {
											textPane.append("\n "+new String(buf,0,len));
										}
										
										
										len = GetMsg.read(buf);
										
									}
									
								GetMsg.close();//关闭流
								}catch(IOException e) {
									e.getStackTrace();
								}
								
						//}
					}
				}).start();
				
			}
		});
		btnSend.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e){
				String StringMessage = txtMessage.getText();
				
				// TODO Auto-generated method stub
				try {
					//Socket Send = new Socket(IP, port); //建立连接
					
					OutputStream Msg = Send.getOutputStream();//创建数据流
					Msg.write(StringMessage.getBytes()); //向服务器发送数据
					isSendMsg = true; //设置判断是否是自己发送了一条信息
					//textPane.append("\n"+"您："+StringMessage);
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		this.setVisible(true);
		
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btnNewButton) {
			
			
		}
	}
	
	public static void main(String[] args) throws Exception{
		Client client = new Client();
		
	}
}


