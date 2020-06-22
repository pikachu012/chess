package com.edu.chess3;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

@SuppressWarnings("serial")
public class GameClient extends JFrame{

	static ChessBoard gamePanel = new ChessBoard();
	static JButton buttonGiveIn = new JButton("认输");
	static JButton buttonStart = new JButton("开始");
	JButton buttonAskRegret = new JButton("请求悔棋");
	JTextField textIp = new JTextField("127.0.0.1");//IP
	JTextField textPort = new JTextField("2333");//对方端口
	JLabel lb1=new JLabel("输入对方IP:");
	JLabel lb2=new JLabel("输入对方端口:");
	JLabel lb3=new JLabel("系统信息");
	private JScrollPane jsc;
	static JTextArea mess=new JTextArea(10,14);
	public static final short REDPLAYER = 1;
	public static final short BLACKPLAYER = 0;
	public GameClient(){
		 JPanel panelMain=new JPanel(new GridLayout(2,1));
		 JPanel panelBottom = new JPanel(new GridLayout(8,1));
		 JPanel panelText=new JPanel(new GridLayout());
		 mess.setEnabled(false);
		 jsc=new JScrollPane(mess);
		 panelText.add(jsc);
		 panelBottom.add(lb1);
		 panelBottom.add(textIp);
		 panelBottom.add(lb2);
		 panelBottom.add(textPort);
		 panelBottom.add(buttonGiveIn);
		 panelBottom.add(buttonAskRegret);
		 panelBottom.add(buttonStart);
		 panelBottom.add(lb3);
		 panelMain.add(panelBottom);
		 panelMain.add(panelText);
		 this.setLayout(new BorderLayout());
		 this.add(gamePanel,BorderLayout.CENTER);
		 this.add(panelMain,BorderLayout.EAST);
		 this.setSize(780,640);
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 this.setTitle("中国象棋");
		 this.setVisible(true);
		 this.setResizable(false);//改变大小
		 buttonGiveIn.setEnabled(false);
		 buttonAskRegret.setEnabled(false);
		 buttonStart.setEnabled(true);
		 setVisible(true);
		 GameClient.mess.append("程序处于等待联机状态!"+"\r\n");
		 this.addWindowListener(new WindowAdapter() {//窗口关闭事件
			 public void windowClosing(WindowEvent e){
				 try{
					RuleNet.send("quit|");
					 System.exit(0);
				 }catch(Exception ex){
					 ex.printStackTrace();
				 }
			 }
		});
		 		 
		 buttonGiveIn.addMouseListener(new MouseAdapter() {//认输事件
			 public void mouseClicked(MouseEvent e){
				 try{
					 RuleNet.send("lose|");//发送认输信息
				 }catch(Exception ex){
					 ex.printStackTrace();
				 }
			 }
		});
		 
		buttonAskRegret.addMouseListener(new MouseAdapter() {
			 public void mouseClicked(MouseEvent e){
				 if(ChessBoard.list.size()==0){
					 JOptionPane.showMessageDialog(null, "不能悔棋");
					 return ;
				 }
				 
				 if(ChessBoard.list.size()==1){
					int flag = SelectChess.LocalPlayer==REDPLAYER?REDPLAYER:BLACKPLAYER;
					if(flag==REDPLAYER){//如果我是红方，判断上一步是不是对方下的，如果是，不能悔棋
						if(ChessBoard.list.get(0).index<16){
							 JOptionPane.showMessageDialog(null, "不能悔棋");
							 return ;
						}
					}else{
						if(ChessBoard.list.get(0).index>=16){
							 JOptionPane.showMessageDialog(null, "不能悔棋");
							 return ;
						}
					}
					
				 }
				 
				 RuleNet.send("ask|");//发送请求悔棋请求
				 
			 }}
		);

		 
		 buttonStart.addMouseListener(new MouseAdapter() {
			 public void mouseClicked(MouseEvent e){
				 String ip = textIp.getText();
				 int otherPort = Integer.parseInt(textPort.getText());
				 int receivePort;
				 mess.setText("");
				 if(otherPort == 2333){
					 receivePort = 2334;                            
				 }else{
					 receivePort = 2333;
				 }
				 gamePanel.startJoin(ip, otherPort, receivePort);
				 buttonGiveIn.setEnabled(true);
				 buttonAskRegret.setEnabled(true);
				 buttonStart.setEnabled(false);
			 }
		});
		 
	}
	
	public static void main(String[] args) {
		new GameClient();
	}

}
