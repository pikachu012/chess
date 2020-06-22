package com.edu.chess;
/**
 * 通信规则
 */
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class RuleNet implements Runnable{
	public static final short REDPLAYER = 1;
	public static final short BLACKPLAYER = 0;
	public static String ip=null;
	public static int otherPort=0;
	public static int receivePort=0;
	@SuppressWarnings("unused")
	private String message=ChessBoard.message;
	private Chess chess[]=ChessBoard.chess;
	private int[][] map=ChessBoard.map;
	private short LocalPlayer=ChessBoard.LocalPlayer;
	private ArrayList<Node> list=ChessBoard.list;
	public static boolean isMyTurn = true;//标记是否自己执子
	private boolean flag=ChessBoard.flag;
	private int x1=ChessBoard.x1,x2=ChessBoard.x2,y1=ChessBoard.y1,y2=ChessBoard.y2;

	//将棋子回退到上一步的位置，并把棋子未回退前的棋盘位置信息清空
		private void rebackChess(int index,int x,int y,int oldX,int oldY){
			chess[index].setPos(oldX, oldY);
			map[oldX][oldY] = index;//棋子放回到(oldX,oldY)
			map[x][y] = -1;//棋盘里原有棋子位置信息清除
		}
		//将一个被吃了的子重新放回到棋盘，传入参数说明：index棋子数组下标，第x行，第y列
		private void resetChess(int index,int x,int y){
			short temp = index<16?BLACKPLAYER:REDPLAYER;//存储是哪方的棋子
			String name = null;//存储棋子上的字
			switch(index){//根据棋子索引，得到棋子上面的字
			case 0:name="将";break;
			case 1:;
			case 2:name="士";break;
			case 3:;
			case 4:name="象";break;
			case 5:;
			case 6:name="马";break;
			case 7:;
			case 8:name="车";break;
			case 9:;
			case 10:name="炮";break;
			case 11:;
			case 12:;
			case 13:;
			case 14:;
			case 15:name="卒";break;
			case 16:name="帅";break;
			case 17:;
			case 18:name="仕";break;
			case 19:;
			case 20:name="相";break;
			case 21:;
			case 22:name="马";break;
			case 23:;
			case 24:name="车";break;
			case 25:;
			case 26:name="炮";break;
			case 27:;
			case 28:;
			case 29:;
			case 30:;
			case 31:name="兵";break;
			}

			chess[index] = new Chess(temp,name,x,y);
			map[x][y] = index;//将棋子放回到棋盘
		}
		public boolean SetMyTurn(boolean b) {

		isMyTurn = b;
		if(b){
			message = "请您开始走棋";
		}else{
			message = "对方正在思考";
		}                                                                                                                                                                          
		return b;
	}
	public static void send(String str) {
		DatagramSocket s = null;
		try{
			s = new DatagramSocket();
			byte[] buffer;
			buffer = new String(str).getBytes();
//			InetAddress ia = InetAddress.getLocalHost();//获取本机地址
			InetAddress ia = InetAddress.getByName(ip );//获取目标地址		
			System.out.println("请求连接的ip是"+ip);
			DatagramPacket dgp = new DatagramPacket(buffer, buffer.length,ia,otherPort);
			s.send(dgp);
			System.out.println("发送信息:"+str);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(s!=null){
				s.close();
			}
		}
	}
	
	
	@Override
	public void run() {
		try{
			System.out.println("我是客户端，我绑定的端口是"+receivePort);
			DatagramSocket s = new DatagramSocket(receivePort);
			byte[] data = new byte[100];
			DatagramPacket dgp = new DatagramPacket(data, data.length);
			while(flag==true){
				s.receive(dgp);
				String strData = new String(data);
				String[] array = new String[6];
				array = strData.split("\\|");
				if(array[0].equals("join")){//对局被加入，我是黑方
					LocalPlayer = BLACKPLAYER;
					GameClient.gamePanel.startNewGame(LocalPlayer);
					if(LocalPlayer==REDPLAYER){
						SetMyTurn(true);
					}else{
						SetMyTurn(false);
					}
					//发送联机成功信息
					send("conn|");

				}else if(array[0].equals("conn")){//我成功加入别人的对局，联机成功。我是红方
					LocalPlayer = REDPLAYER;
					GameClient.gamePanel.startNewGame(LocalPlayer);
					if(LocalPlayer==REDPLAYER){
						SetMyTurn(true);
					}else{
						SetMyTurn(false);
					}

				}else if(array[0].equals("succ")){
					if(array[1].equals("黑方赢了")){
						if(LocalPlayer==REDPLAYER)
							JOptionPane.showConfirmDialog(null, "黑方赢了，你可以重新开始","你输了",JOptionPane.DEFAULT_OPTION);
						else
							JOptionPane.showConfirmDialog(null, "黑方赢了，你可以重新开始","你赢了",JOptionPane.DEFAULT_OPTION);						
					}
					if(array[1].equals("红方赢了")){
						if(LocalPlayer==REDPLAYER)
							JOptionPane.showConfirmDialog(null, "红方赢了，你可以重新开始","你赢了",JOptionPane.DEFAULT_OPTION);
						else
							JOptionPane.showConfirmDialog(null, "红方赢了，你可以重新开始","你输了",JOptionPane.DEFAULT_OPTION);						
					}
					message = "你可以重新开局";
					GameClient.buttonStart.setEnabled(true);//可以点击开始按钮了
					//
				}else if(array[0].equals("move")){
					//对方的走棋信息，move|棋子索引号|x|y|oldX|oldY|背驰棋子索引
					System.out.println("接受信息:"+array[0]+"|"+array[1]+"|"+array[2]+"|"+array[3]+"|"+array[4]+"|"+array[5]+"|"+array[6]+"|");
					int index = Short.parseShort(array[1]);
					x2 = Short.parseShort(array[2]);
					y2 = Short.parseShort(array[3]);
					//					String z = array[4];//对方上步走棋的棋谱信息
					//					message = x2 + ":" +y2;
					int oldX = Short.parseShort(array[4]);//棋子移动前所在行数
					int oldY = Short.parseShort(array[5]);//棋子移动前所在列数
					int eatChessIndex = Short.parseShort(array[6]);//被吃掉的棋子索引
					list.add(new Node(index,x2,y2,oldX,oldY,eatChessIndex));//记录下棋信息
					message = "对方将棋子\""+chess[index].typeName+"\"移动到了("+x2+","+y2+")\n现在该你走棋";
					Chess c = chess[index];
					x1 = c.x;
					y1 = c.y;

					index = map[x1][y1];

					int index2 = map[x2][y2];
					map[x1][y1] = -1;
					map[x2][y2] = index;
					chess[index].setPos(x2, y2);
					if(index2!=-1){// 如果吃了子，则取下被吃掉的棋子
						chess[index2] = null;
					}
					GameClient.gamePanel.repaint();
					isMyTurn = true;
				}else if(array[0].equals("quit")){
					JOptionPane.showConfirmDialog(null, "对方退出了，游戏结束！","提示",JOptionPane.DEFAULT_OPTION);
					message = "对方退出了，游戏结束！";
					GameClient.buttonStart.setEnabled(true);//可以点击开始按钮了
				}else if(array[0].equals("lose")){
					JOptionPane.showConfirmDialog(null, "恭喜你，对方认输了！","你赢了",JOptionPane.DEFAULT_OPTION);
					SetMyTurn(false);
					GameClient.buttonStart.setEnabled(true);//可以点击开始按钮了
				}else if(array[0].equals("ask")){//对方请求悔棋

					String msg = "对方请求悔棋，是否同意？";
					int type = JOptionPane.YES_NO_OPTION;
					String title = "请求悔棋";
					int choice = 0;
					choice = JOptionPane.showConfirmDialog(null, msg,title,type);
					if(choice==1){//否,拒绝悔棋
						send("refuse|");
					}else if(choice == 0){//是,同意悔棋
						send("agree|");
						message = "同意了对方的悔棋，对方正在思考";
						SetMyTurn(false);//对方下棋

						Node temp = list.get(list.size()-1);//获取棋谱最后一步棋的信息
						list.remove(list.size()-1);//移除
						if(LocalPlayer==REDPLAYER){//假如我是红方

							if(temp.index>=16){//上一步是我下的，需要回退两步
								rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
								if(temp.eatChessIndex!=-1){//如果上一步吃了子，将被吃的子重新放回到棋盘
									resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
								}
								temp = list.get(list.size()-1);
								list.remove(list.size()-1);

								rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
								if(temp.eatChessIndex!=-1){//如果上一步吃了子，将被吃的子重新放回到棋盘
									resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
								}
							}else{//上一步是对方下的，需要回退一步

								rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
								if(temp.eatChessIndex!=-1){//如果上一步吃了子，将被吃的子重新放回到棋盘
									resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
								}

							}

						}else{//假如我是黑方

							if(temp.index<16){//上一步是我下的，需要回退两步

								rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
								if(temp.eatChessIndex!=-1){//如果上一步吃了子，将被吃的子重新放回到棋盘
									resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
								}

								temp = list.get(list.size()-1);
								list.remove(list.size()-1);

								rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
								if(temp.eatChessIndex!=-1){//如果上一步吃了子，将被吃的子重新放回到棋盘
									resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
								}

							}else{//上一步是对方下的，需要回退一步

								rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
								if(temp.eatChessIndex!=-1){//如果上一步吃了子，将被吃的子重新放回到棋盘
									resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
								}

							}

						}

						GameClient.gamePanel.repaint();
					}

				}else if(array[0].equals("agree")){//对方同意悔棋

					JOptionPane.showMessageDialog(null, "对方同意了你的悔棋请求");
					Node temp = list.get(list.size()-1);//获取棋谱最后一步棋的信息
					list.remove(list.size()-1);//移除
					if(LocalPlayer==REDPLAYER){//假如我是红方

						if(temp.index>=16){//上一步是我下的，回退一步即可

							rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
							if(temp.eatChessIndex!=-1){//如果上一步吃了子，将被吃的子重新放回到棋盘
								resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
							}
						}else{//上一步是对方下的，需要回退两步
							//第一次回退，此时回退到的状态是我刚下完棋轮到对方下棋的状态
							rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
							if(temp.eatChessIndex!=-1){//如果上一步吃了子，将被吃的子重新放回到棋盘
								resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
							}

							temp = list.get(list.size()-1);
							list.remove(list.size()-1);
							//第二次回退，此时回退到的状态是我上一次刚轮到我下棋的状态
							rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
							if(temp.eatChessIndex!=-1){//如果上一步吃了子，将被吃的子重新放回到棋盘
								resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
							}

						}

					}else{//假如我是黑方

						if(temp.index<16){//上一步是我下的，回退一步即可

							rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
							if(temp.eatChessIndex!=-1){//如果上一步吃了子，将被吃的子重新放回到棋盘
								resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
							}
						}else{//上一步是对方下的，需要回退两步
							//第一次回退，此时回退到的状态是我刚下完棋轮到对方下棋的状态
							rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
							if(temp.eatChessIndex!=-1){//如果上一步吃了子，将被吃的子重新放回到棋盘
								resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
							}

							temp = list.get(list.size()-1);
							list.remove(list.size()-1);
							//第二次回退，此时回退到的状态是我上一次刚轮到我下棋的状态
							rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//回退棋子
							if(temp.eatChessIndex!=-1){//如果上一步吃了子，将被吃的子重新放回到棋盘
								resetChess(temp.eatChessIndex, temp.x, temp.y);//将被吃的棋子放回棋盘
							}

						}


					}
					SetMyTurn(true);
					GameClient.gamePanel.repaint();
				}else if(array[0].equals("refuse")){//对方拒绝悔棋

					JOptionPane.showMessageDialog(null, "对方拒绝了你的悔棋请求");					

				}



				//				System.out.println(new String(data));
				//s.send(dgp);
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	}


