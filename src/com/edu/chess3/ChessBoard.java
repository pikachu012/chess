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
	public static Chess[] chess = new Chess[32];//棋子数组
	public static int[][] map = new int[10][9];//存储棋盘布局信息数组10行9列
	public Image bufferImage;
	public static boolean flag = false;
	public static   ArrayList<Node> list = new ArrayList<Node>();//存储棋谱

	//初始化棋盘布局信息为空
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
		System.out.println("能帮我连接到"+ip+"吗");
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

	//初始化棋子布局
	private void initChess(){
		//布置黑方棋子
		chess[0] = new Chess(BLACKPLAYER,"将",0,4);//第0行第4列
		map[0][4] = 0;
		chess[1] = new Chess(BLACKPLAYER,"士",0,3);//第0行第3列
		map[0][3] = 1;
		chess[2] = new Chess(BLACKPLAYER,"士",0,5);//第0行第5列
		map[0][5] = 2;
		chess[3] = new Chess(BLACKPLAYER,"象",0,2);//第0行第2列
		map[0][2] = 3;
		chess[4] = new Chess(BLACKPLAYER,"象",0,6);//第0行第6列
		map[0][6] = 4;
		chess[5] = new Chess(BLACKPLAYER,"马",0,1);//第0行第1列
		map[0][1] = 5;
		chess[6] = new Chess(BLACKPLAYER,"马",0,7);//第0行第7列
		map[0][7] = 6;
		chess[7] = new Chess(BLACKPLAYER,"车",0,0);//第0行第0列
		map[0][0] = 7;
		chess[8] = new Chess(BLACKPLAYER,"车",0,8);//第0行第8列
		map[0][8] = 8;
		chess[9] = new Chess(BLACKPLAYER,"炮",2,1);//第2行第1列
		map[2][1] = 9;
		chess[10] = new Chess(BLACKPLAYER,"炮",2,7);//第2行第7列
		map[2][7] = 10;
		for(int i=0;i<5;i++){//5个黑方卒布局
			chess[11+i] = new Chess(BLACKPLAYER,"卒",3,i*2);
			map[3][i*2] = 11+i;
		}


		//布置红方棋子
		chess[16] = new Chess(REDPLAYER,"帅",9,4);//第9行第4列
		map[9][4] = 16;
		chess[17] = new Chess(REDPLAYER,"仕",9,3);//第9行第3列
		map[9][3] = 17;
		chess[18] = new Chess(REDPLAYER,"仕",9,5);//第9行第5列
		map[9][5] = 18;
		chess[19] = new Chess(REDPLAYER,"相",9,2);//第9行第2列
		map[9][2] = 19;
		chess[20] = new Chess(REDPLAYER,"相",9,6);//第9行第6列
		map[9][6] = 20;
		chess[21] = new Chess(REDPLAYER,"马",9,1);//第9行第1列
		map[9][1] = 21;
		chess[22] = new Chess(REDPLAYER,"马",9,7);//第9行第7列
		map[9][7] = 22;
		chess[23] = new Chess(REDPLAYER,"车",9,0);//第9行第0列
		map[9][0] = 23;
		chess[24] = new Chess(REDPLAYER,"车",9,8);//第9行第8列
		map[9][8] = 24;
		chess[25] = new Chess(REDPLAYER,"炮",7,1);//第7行第1列
		map[7][1] = 25;
		chess[26] = new Chess(REDPLAYER,"炮",7,7);//第7行第7列
		map[7][7] = 26;

		for(int i=0;i<5;i++){//5个红方兵布局
			chess[27+i] = new Chess(REDPLAYER,"兵",6,i*2);
			map[6][i*2] = 27+i;
		}

	}

	//倒置棋盘
	private void reverseBoard(){
		//对棋子的位置进行互换
		for(int i=0;i<32;i++){
			if(chess[i]!=null){
				chess[i].ReversePos();
			}
		}

		//对两方的棋盘信息进行倒置互换
		for(int i=0;i<5;i++){
			for(int j=0;j<9;j++){
				int temp = map[i][j];
				map[i][j] = map[9-i][8-j];
				map[9-i][8-j] = temp;
			}
		}	

	}

	//对场景对象进行绘画
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
