package Client;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextMeasurer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JPanel;

class Client extends JFrame implements ActionListener {
	JTextField txtMessage;
	JTextArea textPane; //显示
	JButton btnSend;
	JButton btnNewButton;
	private JTextField txtServerIpAddress;
	private JTextField txtPort;
	private JLabel label_1;
	private JTextField textFieldName;
	
	//定义全局变量
	private static String ClientName;//客户端名字
	InetAddress IP = null; //服务器IP
	int port;//服务器端口
	private static boolean isSendMsg = false; //判断客户端是否发送了数据
	Socket Send;
	public Client() throws Exception{
		getContentPane().setLayout(null);
		
		//this.getContentPane().setBackground(Color.white); //给主JFrame设置颜色
		
		this.setTitle("聊天客户端");
		this.setSize(720, 550);
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					if(JOptionPane.showConfirmDialog(null, "<html><font size = 3>确定退出吗？</html>","系统提示",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.INFORMATION_MESSAGE) == 0) {
							System.exit(0);
					}else {
						return;
					}
				}
			});
		
		
		txtMessage = new JTextField();
		txtMessage.setText("Message");
		txtMessage.setBounds(144, 22, 353, 35);
		getContentPane().add(txtMessage);
		txtMessage.setColumns(10);
		
		textPane = new JTextArea();
		textPane.setLineWrap(true);
		textPane.setBounds(35, 177, 636, 319);
		getContentPane().add(textPane);
		
//		JScrollPane AddDownMenu = new JScrollPane();
//		AddDownMenu.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); //设置水平滚动条自动出现
//		AddDownMenu.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); //设置垂直滚动条自动出现
//		AddDownMenu.setBounds(36, 152, 636, 250);
//		AddDownMenu.setVisible(true);
		
		btnSend = new JButton("Send");
		btnSend.setBounds(518, 21, 153, 37);
		btnSend.setEnabled(false);
		btnSend.setBackground(Color.white);
		getContentPane().add(btnSend);
		
		txtServerIpAddress = new JTextField();
		txtServerIpAddress.setText("localhost");
		txtServerIpAddress.setBounds(144, 73, 153, 35);
		getContentPane().add(txtServerIpAddress);
		txtServerIpAddress.setColumns(10);
		
		txtPort = new JTextField();
		txtPort.setText("7788");
		txtPort.setBounds(431, 78, 66, 35);
		getContentPane().add(txtPort);
		txtPort.setColumns(10);
		
		btnNewButton = new JButton("Connect");
		btnNewButton.setBounds(518, 126, 153, 37);
		btnNewButton.setBackground(Color.white);
		getContentPane().add(btnNewButton);
		
		JLabel lblServerPort = new JLabel("Server Port：");
		lblServerPort.setFont(new Font("宋体", Font.PLAIN, 17));
		lblServerPort.setBounds(307, 72, 116, 36);
		getContentPane().add(lblServerPort);
		
		JLabel lblServerIp = new JLabel("Server IP\uFF1A");
		lblServerIp.setFont(new Font("宋体", Font.PLAIN, 17));
		lblServerIp.setBounds(35, 71, 98, 36);
		getContentPane().add(lblServerIp);
		
		JLabel label = new JLabel("发送:");
		label.setFont(new Font("宋体", Font.PLAIN, 17));
		label.setBounds(49, 19, 98, 36);
		getContentPane().add(label);
		
		label_1 = new JLabel("用户名:");
		label_1.setFont(new Font("宋体", Font.PLAIN, 17));
		label_1.setBounds(49, 129, 98, 36);
		getContentPane().add(label_1);
		
		textFieldName = new JTextField();
		textFieldName.setText("");
		textFieldName.setColumns(10);
		textFieldName.setBounds(144, 129, 353, 35);
		getContentPane().add(textFieldName);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 702, 401);
		
		
//		getContentPane().add(panel);
		btnNewButton.addActionListener(this);
		btnSend.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e){
				String StringMessage = txtMessage.getText();
				
				// TODO Auto-generated method stub
				try {
					OutputStream Msg = Send.getOutputStream();//创建数据流
					String SendMessage = ClientName+":"+StringMessage; //在发送的信息头部加上客户端用户名
					Msg.write(SendMessage.getBytes()); //向服务器发送数据
					isSendMsg = true; //设置判断是否是自己发送了一条信息
					
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		this.setVisible(true);
		
	}
	
	
	public static void main(String[] args) throws SocketException  {
		
		try {
			Client client = new Client();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btnNewButton) {
					if(textFieldName.getText().trim().equals("")) {
						textPane.append("请输入用户名！\n");
					}else {
						this.setTitle("客户端："+textFieldName.getText());
						//设置初始化输入框不可编辑
						textFieldName.setEnabled(false);
						txtPort.setEnabled(false);
						txtServerIpAddress.setEnabled(false);
						
						ClientName = textFieldName.getText(); //传输客户端设置的用户名
						try {
							//获取服务器IP地址
							IP = InetAddress.getByName(txtServerIpAddress.getText());
							//获取服务器端口
							port = Integer.valueOf(txtPort.getText());
							
							Send = new Socket(IP, port); //建立与服务器的连接
							textPane.append(" 已获取IP 和Port ，与服务器连接成功！\n");
							btnSend.setEnabled(true);//与服务器成功连接后解锁Send按钮
							btnNewButton.setEnabled(false); //使Connect按钮不能再次单击
						}catch(UnknownHostException e1) {
							textPane.append("建立链接失败，请检查服务端是否启动！\n");
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
							}
					}).start();
					}
			}
		}
	}



