package com.edu.chess;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

public class Chess {

	public static final short REDPLAYER = 1;
	public static final short BLACKPLAYER = 0;
	public short player;
	public String typeName;
	public int x,y;//�����ͼ��Ӧ�Ķ�ά������±�
	private Image chessImage;//����ͼ��
	
	public Chess(short player,String typeName,int x,int y){
		this.player = player;
		this.typeName = typeName;
		this.x = x;
		this.y = y;
		if(player == REDPLAYER){
			switch (typeName){
			case "˧":
				chessImage = Toolkit.getDefaultToolkit().getImage("image//7.png");
				break;
			case "��":
				chessImage = Toolkit.getDefaultToolkit().getImage("image//6.png");
				break;
			case "��":
				chessImage = Toolkit.getDefaultToolkit().getImage("image//5.png");
				break;
			case "��":
				chessImage = Toolkit.getDefaultToolkit().getImage("image//3.png");
				break;
			case "��":
				chessImage = Toolkit.getDefaultToolkit().getImage("image//4.png");
				break;
			case "��":
				chessImage = Toolkit.getDefaultToolkit().getImage("image//2.png");
				break;
			case "��":
				chessImage = Toolkit.getDefaultToolkit().getImage("image//1.png");
				break;			
			}
		}else{
			switch(typeName){
			case "��":
				chessImage = Toolkit.getDefaultToolkit().getImage("image//14.png");
				break;
			case "ʿ":
				chessImage = Toolkit.getDefaultToolkit().getImage("image//13.png");
				break;
			case "��":
				chessImage = Toolkit.getDefaultToolkit().getImage("image//12.png");
				break;
			case "��":
				chessImage = Toolkit.getDefaultToolkit().getImage("image//10.png");
				break;
			case "��":
				chessImage = Toolkit.getDefaultToolkit().getImage("image//11.png");
				break;
			case "��":
				chessImage = Toolkit.getDefaultToolkit().getImage("image//9.png");
				break;
			case "��":
				chessImage = Toolkit.getDefaultToolkit().getImage("image//8.png");
				break;			
			}
		}
	}
	
	public void setPos(int x,int y){
		this.x = x;
		this.y = y;
	}
	
	public void ReversePos(){
		x = 9 - x;
		y = 8 - y;
	}
	
	protected void paint(Graphics g,JPanel i){
		g.drawImage(chessImage, y*68, x*60, 50, 50,(ImageObserver)i);
	}
	
	//�滭ѡ�п�
	public void DrawSelectedChess(Graphics g){
		g.drawRect(y*68, x*60,50, 50);
	}
	
}
