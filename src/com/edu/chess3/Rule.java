package com.edu.chess3;
/**
 * �������
 * @author ����
 *
 */
public class Rule {
	private int[][] map=ChessBoard.map;
	//�ж��Ƿ��������
	boolean IsAbleToMove(Chess firstChess, int x, int y) {

		int oldX,oldY;
		oldX = firstChess.x;
		oldY = firstChess.y;
		String chessName = firstChess.typeName;
		if(chessName.equals("��")||chessName.equals("˧")){
			//�����ҳ�����"��"ȥ��"˧"�������෴�����ж��Ƿ����������Ե����(������ͬһ�У������м�û��������)
			if(oldY==y&&(map[x][y]==0||map[x][y]==16)){
				for(int i=x+1;i<oldX;i++){
					if(map[i][y]!=-1){
						return false;
					}
				}
				return true;
			}

			if((x-oldX)*(y-oldY)!=0){//�����б����
				return false;
			}
			if(Math.abs(x-oldX)>1||Math.abs(y-oldY)>1){//������߻����߳���һ��
				return false;
			}
			if((x>2&&x<7)||y<3||y>5){//��������Ź�������
				return false;
			}
			return true;
		}

		if(chessName.equals("ʿ")||chessName.equals("��")){
			if((x-oldX)*(y-oldY)==0){//������߻�������
				return false;
			}
			if(Math.abs(x-oldX)>1||Math.abs(y-oldY)>1){//���������������λ��������1��������б��һ��
				return false;
			}
			if((x>2&&x<7)||y<3||y>5){//��������Ź�������
				return false;
			}
			return true;
		}

		if(chessName.equals("��")||chessName.equals("��")){
			if((x-oldX)*(y-oldY)==0){//������߻�������
				return false;
			}
			if(Math.abs(x-oldX)!=2||Math.abs(y-oldY)!=2){//���������������λ������ͬʱΪ2��������������
				return false;
			}
			if(x<5){//�����
				return false;
			}
			int i=0,j=0;//��¼����λ��
			if(x-oldX==2){//��������
				i=oldX+1;
			}
			if(x-oldX==-2){//��������
				i=oldX-1;
			}
			if(y-oldY==2){//��������
				j=oldY+1;
			}
			if(y-oldY==-2){//��������
				j=oldY-1;
			}
			if(map[i][j]!=-1){//��������
				return false;
			}

			return true;
		}

		if(chessName.equals("��")){
			if(Math.abs(x-oldX)*Math.abs(y-oldY)!=2){//�������λ������������λ����������2,���������������
				return false;
			}
			if(x-oldX==2){//����������������Һ���λ����Ϊ1������λ����Ϊ2
				if(map[oldX+1][oldY]!=-1){//����������
					return false;
				}				
			}
			if(x-oldX==-2){//����������������Һ���λ����Ϊ1������λ����Ϊ2
				if(map[oldX-1][oldY]!=-1){//����������
					return false;
				}				
			}
			if(y-oldY==2){//����������������Һ���λ����Ϊ2������λ����Ϊ1
				if(map[oldX][oldY+1]!=-1){//����������
					return false;
				}
			}

			if(y-oldY==-2){//����������������Һ���λ����Ϊ2������λ����Ϊ1
				if(map[oldX][oldY-1]!=-1){//����������
					return false;
				}
			}

			return true;	
		}

		if(chessName.equals("��")){
			if((x-oldX)*(y-oldY)!=0){//�������λ����������λ����ͬʱ����Ϊ0��˵������б�ߣ���return false
				return false;
			}
			if(x!=oldX){//����������ƶ�
				if(oldX>x){//���жϹ��̼�Ϊ����������²����м��Ƿ���������
					int t = x;
					x = oldX;
					oldX = t;
				}
				for(int i=oldX+1;i<x;i++){
					if(map[i][oldY]!=-1){//����м���������
						return false;
					}
				}
			}

			if(y!=oldY){//����������ƶ�
				if(oldY>y){//���жϹ��̼�Ϊ��������Ҳ����м��Ƿ���������
					int t = y;
					y = oldY;
					oldY = t;
				}
				for(int i=oldY+1;i<y;i++){
					if(map[oldX][i]!=-1){//����м���������
						return false;
					}
				}
			}

			return true;
		}

		if(chessName.equals("��")){
			boolean swapFlagX = false;//��¼���������Ƿ񽻻���
			boolean swapFlagY = false;//��¼���������Ƿ񽻻���
			if((x-oldX)*(y-oldY)!=0){//�������б��
				return false;
			}
			int c = 0;//��¼�����м��ж��ٸ���
			if(x!=oldX){//������������ƶ�
				if(oldX>x){//�򻯺����ж�
					int t = x;
					x = oldX;
					oldX = t;
					swapFlagX = true;
				}
				for(int i=oldX+1;i<x;i++){
					if(map[i][oldY]!=-1){//����м�����
						c += 1;
					}
				}

			}
			if(y!=oldY){//������Ǻ��� �ƶ�
				if(oldY>y){//�򻯺����ж�
					int t = y;
					y = oldY;
					oldY = t;
					swapFlagY = true;
				}
				for(int i=oldY+1;i<y;i++){
					if(map[oldX][i]!=-1){//����м�����
						c += 1;
					}
				}
			}

			if(c>1){//�м䳬��һ����
				return false;
			}

			if(c==0){//����м�û����
				if(swapFlagX==true){//���֮�佻��������Ҫ���½�������
					int t = x;
					x = oldX;
					oldX = t;
				}
				if(swapFlagY==true){
					int t = y;
					y = oldY;
					oldY = t;
				}
				if(map[x][y]!=-1){//���Ŀ�괦���Ӵ��ڣ������ƶ�
					return false;
				}
			}

			if(c==1){//����м�ֻ��һ����
				if(swapFlagX==true){//���֮�佻��������Ҫ���½�������
					int t = x;
					x = oldX;
					oldX = t;
				}
				if(swapFlagY==true){
					int t = y;
					y = oldY;
					oldY = t;
				}
				if(map[x][y]==-1){//���Ŀ�괦û�����ӣ������ܴ����
					return false;
				}
			}

			return true;
		}

		if(chessName.equals("��")||chessName.equals("��")){
			if((x-oldX)*(y-oldY)!=0){//���б��
				return false;
			}
			if(Math.abs(x-oldX)>1||Math.abs(y-oldY)>1){//���һ���ƶ���һ������
				return false;
			}

			if(oldX>=5){//�����δ���ӣ���ֻ�������ƶ�,���������ƶ�
				if(Math.abs(y-oldY)>0){//û���ӳ��������ƶ�
					return false;
				}
				if(x-oldX==1){//�������ƶ�
					return false;
				}
			}else{//����Ѿ����ӣ����Խ����������ƶ��������ܽ��������ƶ�
				if(x-oldX==1){//�������ƶ�
					return false;
				}
			}

			return true;
		}

		return false;
	}


}
