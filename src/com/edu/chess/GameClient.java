package com.edu.chess;

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
	JTextField textPort = new JTextField("3004");//对方端口
	JLabel lb1=new JLabel("输入对方IP:");
	JLabel lb2=new JLabel("输入对方端口:");
	public static final short REDPLAYER = 1;
	public static final short BLACKPLAYER = 0;
	public GameClient(){
		 JPanel panelBottom = new JPanel(new GridLayout(9,2));
		 panelBottom.add(lb1);
		 panelBottom.add(textIp);
		 panelBottom.add(lb2);
		 panelBottom.add(textPort);
		 panelBottom.add(buttonGiveIn);
		 panelBottom.add(buttonAskRegret);
		 panelBottom.add(buttonStart);
		 this.setLayout(new BorderLayout());
		 this.add(gamePanel,BorderLayout.CENTER);
		 this.add(panelBottom,BorderLayout.EAST);
		 this.setSize(780,640);
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 this.setTitle("中国象棋");
		 this.setVisible(true);
		 this.setResizable(true);//改变大小
		 buttonGiveIn.setEnabled(false);
		 buttonAskRegret.setEnabled(false);
		 buttonStart.setEnabled(true);
		 setVisible(true);
		 this.addWindowListener(new WindowAdapter() {//窗口关闭事件
			 public void windowClosing(WindowEvent e){
				 try{
					 gamePanel.rt.send("quit|");
					 System.exit(0);
				 }catch(Exception ex){
					 ex.printStackTrace();
				 }
			 }
		});
		 		 
		 buttonGiveIn.addMouseListener(new MouseAdapter() {//认输事件
			 public void mouseClicked(MouseEvent e){
				 try{
					 gamePanel.rt.send("lose|");//发送认输信息
				 }catch(Exception ex){
					 ex.printStackTrace();
				 }
			 }
		});
		 
		buttonAskRegret.addMouseListener(new MouseAdapter() {
			 public void mouseClicked(MouseEvent e){
				 if(gamePanel.list.size()==0){
					 JOptionPane.showMessageDialog(null, "不能悔棋");
					 return ;
				 }
				 
				 if(gamePanel.list.size()==1){
					int flag = gamePanel.LocalPlayer==REDPLAYER?REDPLAYER:BLACKPLAYER;
					if(flag==REDPLAYER){//如果我是红方，判断上一步是不是对方下的，如果是，不能悔棋
						if(gamePanel.list.get(0).index<16){
							 JOptionPane.showMessageDialog(null, "不能悔棋");
							 return ;
						}
					}else{
						if(gamePanel.list.get(0).index>=16){
							 JOptionPane.showMessageDialog(null, "不能悔棋");
							 return ;
						}
					}
					
				 }
				 
				 gamePanel.rt.send("ask|");//发送请求悔棋请求
				 
			 }
		});

		 
		 buttonStart.addMouseListener(new MouseAdapter() {
			 public void mouseClicked(MouseEvent e){
				 String ip = textIp.getText();
				 int otherPort = Integer.parseInt(textPort.getText());
				 int receivePort;
				 if(otherPort == 3003){
					 receivePort = 3004;                            
				 }else{
					 receivePort = 3003;
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
