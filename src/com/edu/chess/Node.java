package com.edu.chess;

//�洢���׵�ÿһ��
public class Node {
	int index;//�ƶ��������±�
	int x,y;//�����ƶ���λ��(x,y)
	int oldX,oldY;//�����ƶ�ǰλ��(oldX,oldY)
	int eatChessIndex;//���Ե��������±�
	
	//��������ƶ�����û�г��ӣ�eatChessIndex = -1;
	public Node(int index,int x,int y,int oldX,int oldY,int eatChessIndex){
		this.index = index;
		this.x = x;
		this.y = y;
		this.oldX = oldX;
		this.oldY = oldY;
		this.eatChessIndex = eatChessIndex;
	}
	
}