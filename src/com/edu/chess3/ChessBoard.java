package com.edu.chess3;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class ChessBoard extends JPanel {
	public static final short REDPLAYER = 1;
	public static final short BLACKPLAYER = 0;
	public static Chess[] chess = new Chess[32];//��������
	public static int[][] map = new int[10][9];//�洢���̲�����Ϣ����10��9��
	public Image bufferImage;
	public static boolean flag = false;
	public static   ArrayList<Node> list = new ArrayList<Node>();//�洢����

	//��ʼ�����̲�����ϢΪ��
	private void initMap(){
		int i,j;
		for(i=0;i<10;i++){
			for(j=0;j<9;j++){
				map[i][j]= -1;
			}
		}	
	}


	public ChessBoard(){
		initMap();
		addMouseListener(new SelectChess());
	}


	public void startJoin(String ip,int otherPort,int receivePort){
		flag = true;
		RuleNet.otherPort = otherPort;
		RuleNet.receivePort = receivePort;
		RuleNet.ip = ip;
		System.out.println("�ܰ������ӵ�"+ip+"��");
		RuleNet.send("join|");
		Thread th = new Thread(new RuleNet());
		th.start();
		
	}

	public void startNewGame(short player){
		initMap();
		initChess();
		if(player == BLACKPLAYER){
			reverseBoard();
		}
		repaint();
	}

	//��ʼ�����Ӳ���
	private void initChess(){
		//���úڷ�����
		chess[0] = new Chess(BLACKPLAYER,"��",0,4);//��0�е�4��
		map[0][4] = 0;
		chess[1] = new Chess(BLACKPLAYER,"ʿ",0,3);//��0�е�3��
		map[0][3] = 1;
		chess[2] = new Chess(BLACKPLAYER,"ʿ",0,5);//��0�е�5��
		map[0][5] = 2;
		chess[3] = new Chess(BLACKPLAYER,"��",0,2);//��0�е�2��
		map[0][2] = 3;
		chess[4] = new Chess(BLACKPLAYER,"��",0,6);//��0�е�6��
		map[0][6] = 4;
		chess[5] = new Chess(BLACKPLAYER,"��",0,1);//��0�е�1��
		map[0][1] = 5;
		chess[6] = new Chess(BLACKPLAYER,"��",0,7);//��0�е�7��
		map[0][7] = 6;
		chess[7] = new Chess(BLACKPLAYER,"��",0,0);//��0�е�0��
		map[0][0] = 7;
		chess[8] = new Chess(BLACKPLAYER,"��",0,8);//��0�е�8��
		map[0][8] = 8;
		chess[9] = new Chess(BLACKPLAYER,"��",2,1);//��2�е�1��
		map[2][1] = 9;
		chess[10] = new Chess(BLACKPLAYER,"��",2,7);//��2�е�7��
		map[2][7] = 10;
		for(int i=0;i<5;i++){//5���ڷ��䲼��
			chess[11+i] = new Chess(BLACKPLAYER,"��",3,i*2);
			map[3][i*2] = 11+i;
		}


		//���ú췽����
		chess[16] = new Chess(REDPLAYER,"˧",9,4);//��9�е�4��
		map[9][4] = 16;
		chess[17] = new Chess(REDPLAYER,"��",9,3);//��9�е�3��
		map[9][3] = 17;
		chess[18] = new Chess(REDPLAYER,"��",9,5);//��9�е�5��
		map[9][5] = 18;
		chess[19] = new Chess(REDPLAYER,"��",9,2);//��9�е�2��
		map[9][2] = 19;
		chess[20] = new Chess(REDPLAYER,"��",9,6);//��9�е�6��
		map[9][6] = 20;
		chess[21] = new Chess(REDPLAYER,"��",9,1);//��9�е�1��
		map[9][1] = 21;
		chess[22] = new Chess(REDPLAYER,"��",9,7);//��9�е�7��
		map[9][7] = 22;
		chess[23] = new Chess(REDPLAYER,"��",9,0);//��9�е�0��
		map[9][0] = 23;
		chess[24] = new Chess(REDPLAYER,"��",9,8);//��9�е�8��
		map[9][8] = 24;
		chess[25] = new Chess(REDPLAYER,"��",7,1);//��7�е�1��
		map[7][1] = 25;
		chess[26] = new Chess(REDPLAYER,"��",7,7);//��7�е�7��
		map[7][7] = 26;

		for(int i=0;i<5;i++){//5���췽������
			chess[27+i] = new Chess(REDPLAYER,"��",6,i*2);
			map[6][i*2] = 27+i;
		}

	}

	//��������
	private void reverseBoard(){
		//�����ӵ�λ�ý��л���
		for(int i=0;i<32;i++){
			if(chess[i]!=null){
				chess[i].ReversePos();
			}
		}

		//��������������Ϣ���е��û���
		for(int i=0;i<5;i++){
			for(int j=0;j<9;j++){
				int temp = map[i][j];
				map[i][j] = map[9-i][8-j];
				map[9-i][8-j] = temp;
			}
		}	

	}

	//�Գ���������л滭
	public void paint(Graphics g){
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		Image backgroundImage = Toolkit.getDefaultToolkit().getImage("image/chess.png");
		g.drawImage(backgroundImage,0,0,600,600,this);
		for(int i=0;i<32;i++){
			if(chess[i]!=null){
				chess[i].paint(g, this);
			}
		}
		if(SelectChess.firstChess!=null){
			SelectChess.firstChess.DrawSelectedChess(g);
		}
		if(SelectChess.secondChess!=null){
			SelectChess.secondChess.DrawSelectedChess(g);
		}
	}




}
