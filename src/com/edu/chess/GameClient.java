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
	static JButton buttonGiveIn = new JButton("����");
	static JButton buttonStart = new JButton("��ʼ");
	JButton buttonAskRegret = new JButton("�������");
	JTextField textIp = new JTextField("127.0.0.1");//IP
	JTextField textPort = new JTextField("3004");//�Է��˿�
	JLabel lb1=new JLabel("����Է�IP:");
	JLabel lb2=new JLabel("����Է��˿�:");
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
		 this.setTitle("�й�����");
		 this.setVisible(true);
		 this.setResizable(true);//�ı��С
		 buttonGiveIn.setEnabled(false);
		 buttonAskRegret.setEnabled(false);
		 buttonStart.setEnabled(true);
		 setVisible(true);
		 this.addWindowListener(new WindowAdapter() {//���ڹر��¼�
			 public void windowClosing(WindowEvent e){
				 try{
					 gamePanel.rt.send("quit|");
					 System.exit(0);
				 }catch(Exception ex){
					 ex.printStackTrace();
				 }
			 }
		});
		 		 
		 buttonGiveIn.addMouseListener(new MouseAdapter() {//�����¼�
			 public void mouseClicked(MouseEvent e){
				 try{
					 gamePanel.rt.send("lose|");//����������Ϣ
				 }catch(Exception ex){
					 ex.printStackTrace();
				 }
			 }
		});
		 
		buttonAskRegret.addMouseListener(new MouseAdapter() {
			 public void mouseClicked(MouseEvent e){
				 if(gamePanel.list.size()==0){
					 JOptionPane.showMessageDialog(null, "���ܻ���");
					 return ;
				 }
				 
				 if(gamePanel.list.size()==1){
					int flag = gamePanel.LocalPlayer==REDPLAYER?REDPLAYER:BLACKPLAYER;
					if(flag==REDPLAYER){//������Ǻ췽���ж���һ���ǲ��ǶԷ��µģ�����ǣ����ܻ���
						if(gamePanel.list.get(0).index<16){
							 JOptionPane.showMessageDialog(null, "���ܻ���");
							 return ;
						}
					}else{
						if(gamePanel.list.get(0).index>=16){
							 JOptionPane.showMessageDialog(null, "���ܻ���");
							 return ;
						}
					}
					
				 }
				 
				 gamePanel.rt.send("ask|");//���������������
				 
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
