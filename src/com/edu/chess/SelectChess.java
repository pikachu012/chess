package com.edu.chess;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

public class SelectChess extends MouseAdapter {
	private String message = ChessBoard.message;
	private boolean isFirstClick = true;
	public static Chess firstChess = null;
	public static Chess secondChess = null;
	private boolean isMyTurn = true;
	private int x1,x2,y1,y2;
	private int tempX,tempY;
	private int[][] map=ChessBoard.map;
	private short LocalPlayer=ChessBoard.LocalPlayer;
	Rule re=new Rule();
	RuleNet rt=new RuleNet();
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (isMyTurn == false) {
			message = "���ڸöԷ�����";
			GameClient.gamePanel.repaint();
		}
		selectedChess(e);
		GameClient.gamePanel.repaint();
	}

	private void selectedChess(MouseEvent e) {

		int index1, index2;// �����һ�κ͵ڶ��α����������Ӷ�Ӧ�����±�
		if (isFirstClick) {// ��һ�ε��
			firstChess = analyse(e.getX(), e.getY());
			x1 = tempX;// ��¼�����λ�����̵ڼ���
			y1 = tempY;
			if (firstChess != null) {
				if (firstChess.player != LocalPlayer) {
					message = "����ɶԷ�������";
					return;
				}
				isFirstClick = false;
			}
		} else {
			secondChess = analyse(e.getX(), e.getY());
			x2 = tempX;
			y2 = tempY;
			if (secondChess != null) {// ����ڶ��ε��ѡ��������
				if (secondChess.player == LocalPlayer) {// ����ڶ��ε�����������Լ������ӣ���Ե�һ��ѡ�е����ӽ��и���
					firstChess = secondChess;
					x1 = tempX;
					y1 = tempY;
					secondChess = null;
					return;
				}
			}

			if (secondChess == null) {// ���Ŀ�괦û������,�ж��Ƿ��������
				if (re.IsAbleToMove(firstChess, x2, y2)) {
					index1 = map[x1][y1];
					map[x1][y1] = -1;
					map[x2][y2] = index1;
					ChessBoard.chess[index1].setPos(x2, y2);
					// send
					RuleNet.send("move" + "|" + index1 + "|" + (9 - x2) + "|" + (8 - y2) + "|" + (9 - x1) + "|" + (8 - y1) + "|"
							+ "-1|");
					ChessBoard.list.add(new Node(index1, x2, y2, x1, y1, -1));// �洢�ҷ�������Ϣ
					// �õ�һ��ѡ�б����Ϊ��
					isFirstClick = true;
					GameClient.gamePanel.repaint();
					rt.SetMyTurn(false);// �öԷ���
				} else {
					message = "�������������";
				}
				return;
			}

			if (secondChess != null && re.IsAbleToMove(firstChess, x2, y2)) {// ���Գ���
				isFirstClick = true;
				index1 = map[x1][y1];
				index2 = map[x2][y2];
				map[x1][y1] = -1;
				map[x2][y2] = index1;
				ChessBoard.chess[index1].setPos(x2, y2);
				ChessBoard.chess[index2] = null;
				GameClient.gamePanel.repaint();
				RuleNet.send("move" + "|" + index1 + "|" + (9 - x2) + "|" + (8 - y2) + "|" + (9 - x1) + "|" + (8 - y1) + "|"
						+ index2 + "|");
				ChessBoard.list.add(new Node(index1, x2, y2, x1, y1, index2));// ��¼�ҷ�������Ϣ
				if (index2 == 0) {// ���Ե����ǽ�
					message = "�췽Ӯ��";
					JOptionPane.showConfirmDialog(null, "�췽Ӯ��", "��ʾ", JOptionPane.DEFAULT_OPTION);
					// send
					RuleNet.send("succ" + "|" + "�췽Ӯ��" + "|");
					return;
				}
				if (index2 == 16) {// ���Ե�����˧
					message = "�ڷ�Ӯ��";
					JOptionPane.showConfirmDialog(null, "�ڷ�Ӯ��", "��ʾ", JOptionPane.DEFAULT_OPTION);
					// send
					RuleNet.send("succ" + "|" + "�ڷ�Ӯ��" + "|");
					return;
				}
				rt.SetMyTurn(false);// �öԷ���
			} else {// ���ܳ���
				message = "���ܳ���";
			}

		}

	}

	private Chess analyse(int x, int y) {
		int index_x = -1, index_y = -1;// ��¼������ǵڼ��еڼ���
		for (int i = 0; i <= 9; i++) {
			for (int j = 0; j <= 8; j++) {
				Rectangle r = new Rectangle(j * 68, i * 60, 50, 50);
				if (r.contains(x, y)) {
					index_x = i;
					index_y = j;
					break;
				}
			}
		}
		tempX = index_x;
		tempY = index_y;

		if (index_x == -1 && index_y == -1) {// û�е�����κ����̿ɵ����
			return null;
		}

		if (map[index_x][index_y] == -1) {
			// System.out.println("�����("+tempX+","+tempY+")");
			return null;
		} else {
			// System.out.println("�����("+tempX+","+tempY+"),�˴���������"+chess[map[index_x][index_y]].typeName);
			return ChessBoard.chess[map[index_x][index_y]];
		}

	}

}
