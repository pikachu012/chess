package com.edu.chess3;
/**
 * 行棋规则
 * @author 流年
 *
 */
public class Rule {
	private int[][] map=ChessBoard.map;
	//判断是否可以落子
	boolean IsAbleToMove(Chess firstChess, int x, int y) {

		int oldX,oldY;
		oldX = firstChess.x;
		oldY = firstChess.y;
		String chessName = firstChess.typeName;
		if(chessName.equals("将")||chessName.equals("帅")){
			//如果玩家尝试用"将"去吃"帅"，或者相反，则判断是否符合两将相对的情况(必须在同一列，并且中间没有其他子)
			if(oldY==y&&(map[x][y]==0||map[x][y]==16)){
				for(int i=x+1;i<oldX;i++){
					if(map[i][y]!=-1){
						return false;
					}
				}
				return true;
			}

			if((x-oldX)*(y-oldY)!=0){//如果是斜着走
				return false;
			}
			if(Math.abs(x-oldX)>1||Math.abs(y-oldY)>1){//如果横走或竖走超过一格
				return false;
			}
			if((x>2&&x<7)||y<3||y>5){//如果超出九宫格区域
				return false;
			}
			return true;
		}

		if(chessName.equals("士")||chessName.equals("仕")){
			if((x-oldX)*(y-oldY)==0){//如果横走或者竖走
				return false;
			}
			if(Math.abs(x-oldX)>1||Math.abs(y-oldY)>1){//如果横向或者纵向的位移量大于1，即不是斜走一格
				return false;
			}
			if((x>2&&x<7)||y<3||y>5){//如果超出九宫格区域
				return false;
			}
			return true;
		}

		if(chessName.equals("相")||chessName.equals("象")){
			if((x-oldX)*(y-oldY)==0){//如果横走或者竖走
				return false;
			}
			if(Math.abs(x-oldX)!=2||Math.abs(y-oldY)!=2){//如果横向或者纵向的位移量不同时为2，即不是走田字
				return false;
			}
			if(x<5){//象过河
				return false;
			}
			int i=0,j=0;//记录象眼位置
			if(x-oldX==2){//象向下跳
				i=oldX+1;
			}
			if(x-oldX==-2){//象向上跳
				i=oldX-1;
			}
			if(y-oldY==2){//象向右跳
				j=oldY+1;
			}
			if(y-oldY==-2){//象向左跳
				j=oldY-1;
			}
			if(map[i][j]!=-1){//被堵象眼
				return false;
			}

			return true;
		}

		if(chessName.equals("马")){
			if(Math.abs(x-oldX)*Math.abs(y-oldY)!=2){//如果横向位移量乘以竖向位移量不等于2,即如果马不是走日字
				return false;
			}
			if(x-oldX==2){//如果马向下跳，并且横向位移量为1，纵向位移量为2
				if(map[oldX+1][oldY]!=-1){//如果被绊马脚
					return false;
				}				
			}
			if(x-oldX==-2){//如果马向上跳，并且横向位移量为1，纵向位移量为2
				if(map[oldX-1][oldY]!=-1){//如果被绊马脚
					return false;
				}				
			}
			if(y-oldY==2){//如果马向右跳，并且横向位移量为2，纵向位移量为1
				if(map[oldX][oldY+1]!=-1){//如果被绊马脚
					return false;
				}
			}

			if(y-oldY==-2){//如果马向左跳，并且横向位移量为2，纵向位移量为1
				if(map[oldX][oldY-1]!=-1){//如果被绊马脚
					return false;
				}
			}

			return true;	
		}

		if(chessName.equals("车")){
			if((x-oldX)*(y-oldY)!=0){//如果横向位移量和纵向位移量同时都不为0，说明车在斜走，故return false
				return false;
			}
			if(x!=oldX){//如果车纵向移动
				if(oldX>x){//将判断过程简化为纵向从上往下查找中间是否有其他子
					int t = x;
					x = oldX;
					oldX = t;
				}
				for(int i=oldX+1;i<x;i++){
					if(map[i][oldY]!=-1){//如果中间有其他子
						return false;
					}
				}
			}

			if(y!=oldY){//如果车横向移动
				if(oldY>y){//将判断过程简化为横向从左到右查找中间是否有其他子
					int t = y;
					y = oldY;
					oldY = t;
				}
				for(int i=oldY+1;i<y;i++){
					if(map[oldX][i]!=-1){//如果中间有其他子
						return false;
					}
				}
			}

			return true;
		}

		if(chessName.equals("炮")){
			boolean swapFlagX = false;//记录纵向棋子是否交换过
			boolean swapFlagY = false;//记录横向棋子是否交换过
			if((x-oldX)*(y-oldY)!=0){//如果棋子斜走
				return false;
			}
			int c = 0;//记录两子中间有多少个子
			if(x!=oldX){//如果炮是纵向移动
				if(oldX>x){//简化后续判断
					int t = x;
					x = oldX;
					oldX = t;
					swapFlagX = true;
				}
				for(int i=oldX+1;i<x;i++){
					if(map[i][oldY]!=-1){//如果中间有子
						c += 1;
					}
				}

			}
			if(y!=oldY){//如果炮是横向 移动
				if(oldY>y){//简化后续判断
					int t = y;
					y = oldY;
					oldY = t;
					swapFlagY = true;
				}
				for(int i=oldY+1;i<y;i++){
					if(map[oldX][i]!=-1){//如果中间有子
						c += 1;
					}
				}
			}

			if(c>1){//中间超过一个子
				return false;
			}

			if(c==0){//如果中间没有子
				if(swapFlagX==true){//如果之间交换过，需要重新交换回来
					int t = x;
					x = oldX;
					oldX = t;
				}
				if(swapFlagY==true){
					int t = y;
					y = oldY;
					oldY = t;
				}
				if(map[x][y]!=-1){//如果目标处有子存在，则不能移动
					return false;
				}
			}

			if(c==1){//如果中间只有一个子
				if(swapFlagX==true){//如果之间交换过，需要重新交换回来
					int t = x;
					x = oldX;
					oldX = t;
				}
				if(swapFlagY==true){
					int t = y;
					y = oldY;
					oldY = t;
				}
				if(map[x][y]==-1){//如果目标处没有棋子，即不能打空炮
					return false;
				}
			}

			return true;
		}

		if(chessName.equals("卒")||chessName.equals("兵")){
			if((x-oldX)*(y-oldY)!=0){//如果斜走
				return false;
			}
			if(Math.abs(x-oldX)>1||Math.abs(y-oldY)>1){//如果一次移动了一格以上
				return false;
			}

			if(oldX>=5){//如果兵未过河，则只能向上移动,不能左右移动
				if(Math.abs(y-oldY)>0){//没过河尝试左右移动
					return false;
				}
				if(x-oldX==1){//兵向下移动
					return false;
				}
			}else{//如果已经过河，可以进行上左右移动，但不能进行向下移动
				if(x-oldX==1){//兵向下移动
					return false;
				}
			}

			return true;
		}

		return false;
	}


}
