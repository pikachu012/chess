package com.edu.chess;
/**
 * ͨ�Ź���
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
	public static boolean isMyTurn = true;//����Ƿ��Լ�ִ��
	private boolean flag=ChessBoard.flag;
	private int x1=ChessBoard.x1,x2=ChessBoard.x2,y1=ChessBoard.y1,y2=ChessBoard.y2;

	//�����ӻ��˵���һ����λ�ã���������δ����ǰ������λ����Ϣ���
		private void rebackChess(int index,int x,int y,int oldX,int oldY){
			chess[index].setPos(oldX, oldY);
			map[oldX][oldY] = index;//���ӷŻص�(oldX,oldY)
			map[x][y] = -1;//������ԭ������λ����Ϣ���
		}
		//��һ�������˵������·Żص����̣��������˵����index���������±꣬��x�У���y��
		private void resetChess(int index,int x,int y){
			short temp = index<16?BLACKPLAYER:REDPLAYER;//�洢���ķ�������
			String name = null;//�洢�����ϵ���
			switch(index){//���������������õ������������
			case 0:name="��";break;
			case 1:;
			case 2:name="ʿ";break;
			case 3:;
			case 4:name="��";break;
			case 5:;
			case 6:name="��";break;
			case 7:;
			case 8:name="��";break;
			case 9:;
			case 10:name="��";break;
			case 11:;
			case 12:;
			case 13:;
			case 14:;
			case 15:name="��";break;
			case 16:name="˧";break;
			case 17:;
			case 18:name="��";break;
			case 19:;
			case 20:name="��";break;
			case 21:;
			case 22:name="��";break;
			case 23:;
			case 24:name="��";break;
			case 25:;
			case 26:name="��";break;
			case 27:;
			case 28:;
			case 29:;
			case 30:;
			case 31:name="��";break;
			}

			chess[index] = new Chess(temp,name,x,y);
			map[x][y] = index;//�����ӷŻص�����
		}
		public boolean SetMyTurn(boolean b) {

		isMyTurn = b;
		if(b){
			message = "������ʼ����";
		}else{
			message = "�Է�����˼��";
		}                                                                                                                                                                          
		return b;
	}
	public static void send(String str) {
		DatagramSocket s = null;
		try{
			s = new DatagramSocket();
			byte[] buffer;
			buffer = new String(str).getBytes();
//			InetAddress ia = InetAddress.getLocalHost();//��ȡ������ַ
			InetAddress ia = InetAddress.getByName(ip );//��ȡĿ���ַ		
			System.out.println("�������ӵ�ip��"+ip);
			DatagramPacket dgp = new DatagramPacket(buffer, buffer.length,ia,otherPort);
			s.send(dgp);
			System.out.println("������Ϣ:"+str);
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
			System.out.println("���ǿͻ��ˣ��Ұ󶨵Ķ˿���"+receivePort);
			DatagramSocket s = new DatagramSocket(receivePort);
			byte[] data = new byte[100];
			DatagramPacket dgp = new DatagramPacket(data, data.length);
			while(flag==true){
				s.receive(dgp);
				String strData = new String(data);
				String[] array = new String[6];
				array = strData.split("\\|");
				if(array[0].equals("join")){//�Ծֱ����룬���Ǻڷ�
					LocalPlayer = BLACKPLAYER;
					GameClient.gamePanel.startNewGame(LocalPlayer);
					if(LocalPlayer==REDPLAYER){
						SetMyTurn(true);
					}else{
						SetMyTurn(false);
					}
					//���������ɹ���Ϣ
					send("conn|");

				}else if(array[0].equals("conn")){//�ҳɹ�������˵ĶԾ֣������ɹ������Ǻ췽
					LocalPlayer = REDPLAYER;
					GameClient.gamePanel.startNewGame(LocalPlayer);
					if(LocalPlayer==REDPLAYER){
						SetMyTurn(true);
					}else{
						SetMyTurn(false);
					}

				}else if(array[0].equals("succ")){
					if(array[1].equals("�ڷ�Ӯ��")){
						if(LocalPlayer==REDPLAYER)
							JOptionPane.showConfirmDialog(null, "�ڷ�Ӯ�ˣ���������¿�ʼ","������",JOptionPane.DEFAULT_OPTION);
						else
							JOptionPane.showConfirmDialog(null, "�ڷ�Ӯ�ˣ���������¿�ʼ","��Ӯ��",JOptionPane.DEFAULT_OPTION);						
					}
					if(array[1].equals("�췽Ӯ��")){
						if(LocalPlayer==REDPLAYER)
							JOptionPane.showConfirmDialog(null, "�췽Ӯ�ˣ���������¿�ʼ","��Ӯ��",JOptionPane.DEFAULT_OPTION);
						else
							JOptionPane.showConfirmDialog(null, "�췽Ӯ�ˣ���������¿�ʼ","������",JOptionPane.DEFAULT_OPTION);						
					}
					message = "��������¿���";
					GameClient.buttonStart.setEnabled(true);//���Ե����ʼ��ť��
					//
				}else if(array[0].equals("move")){
					//�Է���������Ϣ��move|����������|x|y|oldX|oldY|������������
					System.out.println("������Ϣ:"+array[0]+"|"+array[1]+"|"+array[2]+"|"+array[3]+"|"+array[4]+"|"+array[5]+"|"+array[6]+"|");
					int index = Short.parseShort(array[1]);
					x2 = Short.parseShort(array[2]);
					y2 = Short.parseShort(array[3]);
					//					String z = array[4];//�Է��ϲ������������Ϣ
					//					message = x2 + ":" +y2;
					int oldX = Short.parseShort(array[4]);//�����ƶ�ǰ��������
					int oldY = Short.parseShort(array[5]);//�����ƶ�ǰ��������
					int eatChessIndex = Short.parseShort(array[6]);//���Ե�����������
					list.add(new Node(index,x2,y2,oldX,oldY,eatChessIndex));//��¼������Ϣ
					message = "�Է�������\""+chess[index].typeName+"\"�ƶ�����("+x2+","+y2+")\n���ڸ�������";
					Chess c = chess[index];
					x1 = c.x;
					y1 = c.y;

					index = map[x1][y1];

					int index2 = map[x2][y2];
					map[x1][y1] = -1;
					map[x2][y2] = index;
					chess[index].setPos(x2, y2);
					if(index2!=-1){// ��������ӣ���ȡ�±��Ե�������
						chess[index2] = null;
					}
					GameClient.gamePanel.repaint();
					isMyTurn = true;
				}else if(array[0].equals("quit")){
					JOptionPane.showConfirmDialog(null, "�Է��˳��ˣ���Ϸ������","��ʾ",JOptionPane.DEFAULT_OPTION);
					message = "�Է��˳��ˣ���Ϸ������";
					GameClient.buttonStart.setEnabled(true);//���Ե����ʼ��ť��
				}else if(array[0].equals("lose")){
					JOptionPane.showConfirmDialog(null, "��ϲ�㣬�Է������ˣ�","��Ӯ��",JOptionPane.DEFAULT_OPTION);
					SetMyTurn(false);
					GameClient.buttonStart.setEnabled(true);//���Ե����ʼ��ť��
				}else if(array[0].equals("ask")){//�Է��������

					String msg = "�Է�������壬�Ƿ�ͬ�⣿";
					int type = JOptionPane.YES_NO_OPTION;
					String title = "�������";
					int choice = 0;
					choice = JOptionPane.showConfirmDialog(null, msg,title,type);
					if(choice==1){//��,�ܾ�����
						send("refuse|");
					}else if(choice == 0){//��,ͬ�����
						send("agree|");
						message = "ͬ���˶Է��Ļ��壬�Է�����˼��";
						SetMyTurn(false);//�Է�����

						Node temp = list.get(list.size()-1);//��ȡ�������һ�������Ϣ
						list.remove(list.size()-1);//�Ƴ�
						if(LocalPlayer==REDPLAYER){//�������Ǻ췽

							if(temp.index>=16){//��һ�������µģ���Ҫ��������
								rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//��������
								if(temp.eatChessIndex!=-1){//�����һ�������ӣ������Ե������·Żص�����
									resetChess(temp.eatChessIndex, temp.x, temp.y);//�����Ե����ӷŻ�����
								}
								temp = list.get(list.size()-1);
								list.remove(list.size()-1);

								rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//��������
								if(temp.eatChessIndex!=-1){//�����һ�������ӣ������Ե������·Żص�����
									resetChess(temp.eatChessIndex, temp.x, temp.y);//�����Ե����ӷŻ�����
								}
							}else{//��һ���ǶԷ��µģ���Ҫ����һ��

								rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//��������
								if(temp.eatChessIndex!=-1){//�����һ�������ӣ������Ե������·Żص�����
									resetChess(temp.eatChessIndex, temp.x, temp.y);//�����Ե����ӷŻ�����
								}

							}

						}else{//�������Ǻڷ�

							if(temp.index<16){//��һ�������µģ���Ҫ��������

								rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//��������
								if(temp.eatChessIndex!=-1){//�����һ�������ӣ������Ե������·Żص�����
									resetChess(temp.eatChessIndex, temp.x, temp.y);//�����Ե����ӷŻ�����
								}

								temp = list.get(list.size()-1);
								list.remove(list.size()-1);

								rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//��������
								if(temp.eatChessIndex!=-1){//�����һ�������ӣ������Ե������·Żص�����
									resetChess(temp.eatChessIndex, temp.x, temp.y);//�����Ե����ӷŻ�����
								}

							}else{//��һ���ǶԷ��µģ���Ҫ����һ��

								rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//��������
								if(temp.eatChessIndex!=-1){//�����һ�������ӣ������Ե������·Żص�����
									resetChess(temp.eatChessIndex, temp.x, temp.y);//�����Ե����ӷŻ�����
								}

							}

						}

						GameClient.gamePanel.repaint();
					}

				}else if(array[0].equals("agree")){//�Է�ͬ�����

					JOptionPane.showMessageDialog(null, "�Է�ͬ������Ļ�������");
					Node temp = list.get(list.size()-1);//��ȡ�������һ�������Ϣ
					list.remove(list.size()-1);//�Ƴ�
					if(LocalPlayer==REDPLAYER){//�������Ǻ췽

						if(temp.index>=16){//��һ�������µģ�����һ������

							rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//��������
							if(temp.eatChessIndex!=-1){//�����һ�������ӣ������Ե������·Żص�����
								resetChess(temp.eatChessIndex, temp.x, temp.y);//�����Ե����ӷŻ�����
							}
						}else{//��һ���ǶԷ��µģ���Ҫ��������
							//��һ�λ��ˣ���ʱ���˵���״̬���Ҹ��������ֵ��Է������״̬
							rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//��������
							if(temp.eatChessIndex!=-1){//�����һ�������ӣ������Ե������·Żص�����
								resetChess(temp.eatChessIndex, temp.x, temp.y);//�����Ե����ӷŻ�����
							}

							temp = list.get(list.size()-1);
							list.remove(list.size()-1);
							//�ڶ��λ��ˣ���ʱ���˵���״̬������һ�θ��ֵ��������״̬
							rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//��������
							if(temp.eatChessIndex!=-1){//�����һ�������ӣ������Ե������·Żص�����
								resetChess(temp.eatChessIndex, temp.x, temp.y);//�����Ե����ӷŻ�����
							}

						}

					}else{//�������Ǻڷ�

						if(temp.index<16){//��һ�������µģ�����һ������

							rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//��������
							if(temp.eatChessIndex!=-1){//�����һ�������ӣ������Ե������·Żص�����
								resetChess(temp.eatChessIndex, temp.x, temp.y);//�����Ե����ӷŻ�����
							}
						}else{//��һ���ǶԷ��µģ���Ҫ��������
							//��һ�λ��ˣ���ʱ���˵���״̬���Ҹ��������ֵ��Է������״̬
							rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//��������
							if(temp.eatChessIndex!=-1){//�����һ�������ӣ������Ե������·Żص�����
								resetChess(temp.eatChessIndex, temp.x, temp.y);//�����Ե����ӷŻ�����
							}

							temp = list.get(list.size()-1);
							list.remove(list.size()-1);
							//�ڶ��λ��ˣ���ʱ���˵���״̬������һ�θ��ֵ��������״̬
							rebackChess(temp.index, temp.x, temp.y, temp.oldX, temp.oldY);//��������
							if(temp.eatChessIndex!=-1){//�����һ�������ӣ������Ե������·Żص�����
								resetChess(temp.eatChessIndex, temp.x, temp.y);//�����Ե����ӷŻ�����
							}

						}


					}
					SetMyTurn(true);
					GameClient.gamePanel.repaint();
				}else if(array[0].equals("refuse")){//�Է��ܾ�����

					JOptionPane.showMessageDialog(null, "�Է��ܾ�����Ļ�������");					

				}



				//				System.out.println(new String(data));
				//s.send(dgp);
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	}


