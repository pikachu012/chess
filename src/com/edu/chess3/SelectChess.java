package com.edu.chess3;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

public class SelectChess extends MouseAdapter {
	public static final short REDPLAYER = 1;
	public static final short BLACKPLAYER = 0;
	public static boolean isMyTurn =true; 
	public static short LocalPlayer=REDPLAYER;
	public static Chess firstChess = null;
	public static Chess secondChess = null;
	public static int x1, x2, y1, y2;
	private int tempX, tempY;
	private boolean isFirstClick = true;
	private int[][] map = ChessBoard.map;
	Rule re = new Rule();
	RuleNet rt = new RuleNet();

	@Override
	public void mouseClicked(MouseEvent e) {
		if (isMyTurn == false) {
			GameClient.mess.append("现在该对方走棋" + "\r\n");
			GameClient.gamePanel.repaint();
			return;
		}
		selectedChess(e);
		GameClient.gamePanel.repaint();
	}

	private void selectedChess(MouseEvent e) {

		int index1, index2;// 保存第一次和第二次被单击的棋子对应数组下标
		if (isFirstClick) {// 第一次点击
			firstChess = analyse(e.getX(), e.getY());
			x1 = tempX;// 记录点击处位于棋盘第几行
			y1 = tempY;
			if (firstChess != null) {
				if (firstChess.player != LocalPlayer) {
					GameClient.mess.append("那方"+ LocalPlayer + "\r\n");
					GameClient.mess.append("点击成对方棋子了" + "\r\n");
					return ;
				}
				isFirstClick = false;
			}
		} else {
			secondChess = analyse(e.getX(), e.getY());
			x2 = tempX;
			y2 = tempY;
			if (secondChess != null) {// 如果第二次点击选中了棋子
				if (secondChess.player == LocalPlayer) {// 如果第二次点击的棋子是自己的棋子，则对第一次选中的棋子进行更换
					firstChess = secondChess;
					x1 = tempX;
					y1 = tempY;
					secondChess = null;
					return;
				}
			}

			if (secondChess == null) {// 如果目标处没有棋子,判断是否可以走棋
				if (re.IsAbleToMove(firstChess, x2, y2)) {
					index1 = map[x1][y1];
					map[x1][y1] = -1;
					map[x2][y2] = index1;
					ChessBoard.chess[index1].setPos(x2, y2);
					// send
					RuleNet.send("move" + "|" + index1 + "|" + (9 - x2) + "|" + (8 - y2) + "|" + (9 - x1) + "|"
							+ (8 - y1) + "|" + "-1|");
					ChessBoard.list.add(new Node(index1, x2, y2, x1, y1, -1));// 存储我方下棋信息
					// 置第一次选中标记量为空
					isFirstClick = true;
					GameClient.gamePanel.repaint();
					rt.SetMyTurn(false);// 该对方了
				} else {
					GameClient.mess.append("不符合走棋规则" + "\r\n");
				}
				return;
			}

			if (secondChess != null && re.IsAbleToMove(firstChess, x2, y2)) {// 可以吃子
				isFirstClick = true;
				index1 = map[x1][y1];
				index2 = map[x2][y2];
				map[x1][y1] = -1;
				map[x2][y2] = index1;
				ChessBoard.chess[index1].setPos(x2, y2);
				ChessBoard.chess[index2] = null;
				GameClient.gamePanel.repaint();
				RuleNet.send("move" + "|" + index1 + "|" + (9 - x2) + "|" + (8 - y2) + "|" + (9 - x1) + "|" + (8 - y1)
						+ "|" + index2 + "|");
				ChessBoard.list.add(new Node(index1, x2, y2, x1, y1, index2));// 记录我方下棋信息
				if (index2 == 0) {// 被吃掉的是将
					GameClient.mess.append("红方胜利" + "\r\n");
					JOptionPane.showConfirmDialog(null, "红方胜利", "提示", JOptionPane.DEFAULT_OPTION);
					// send
					RuleNet.send("succ" + "|" + "红方赢了" + "|");
					return;
				}
				if (index2 == 16) {// 被吃掉的是帅
					GameClient.mess.append("黑方胜利" + "\r\n");
					JOptionPane.showConfirmDialog(null, "黑方胜利", "提示", JOptionPane.DEFAULT_OPTION);
					// send
					RuleNet.send("succ" + "|" + "黑方赢了" + "|");
					return;
				}
				rt.SetMyTurn(false);// 该对方了
			} else {// 不能吃子
				GameClient.mess.append("不能吃子" + "\r\n");
			}

		}

	}

	private Chess analyse(int x, int y) {
		int index_x = -1, index_y = -1;// 记录点击处是第几行第几列
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

		if (index_x == -1 && index_y == -1) {// 没有点击到任何棋盘可点击处
			return null;
		}

		if (map[index_x][index_y] == -1) {
			// System.out.println("点击了("+tempX+","+tempY+")");
			return null;
		} else {
			// System.out.println("点击了("+tempX+","+tempY+"),此处的棋子是"+chess[map[index_x][index_y]].typeName);
			return ChessBoard.chess[map[index_x][index_y]];
		}

	}

}
