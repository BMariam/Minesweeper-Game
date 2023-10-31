package minesweepergame;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;

public class Field {
	private boolean isMine;
	private boolean isFlag;
	private boolean isOpen;
	public String boardValue;

	public static String flag = "\uD83D\uDEA9";
	public static String mine = "\uD83D\uDCA3";

	public JButton fieldButton;

	public Field() {
		fieldButton = new JButton("");
		fieldButton.setVisible(true);
		fieldButton.setFont(new Font("Arial", Font.PLAIN, 12));
		reset();
	}

	public void reset() {
		isFlag = false;
		isOpen = false;
		fieldButton.setBackground(new Color(0,255,127));
		fieldButton.setForeground(Color.BLACK);
		fieldButton.setText("");
	}

	public void setFlag() {
		fieldButton.setText(flag);
		fieldButton.setBackground(Color.LIGHT_GRAY);
		fieldButton.setForeground(Color.RED);
		--GameField.flagCount;
		isOpen = true;
		isFlag = true;
	}

	public void removeFlag() {
		reset();
		++GameField.flagCount;
	}

	public void handleRightClick() {
		if (!isOpen && GameField.flagCount > 0) {
			setFlag();
		} else if (isOpen && isFlag) {
			removeFlag();
		}
		MinesweeperGame.flagCount.setText(flag + " :  " + String.valueOf(GameField.flagCount));
	}

	public void handleLeftClick() {
		if (isFlag) {
			removeFlag();
			MinesweeperGame.flagCount.setText(flag + " :  " + String.valueOf(GameField.flagCount));
			return;
		}
		fieldButton.setForeground(Color.BLACK);
		if ((!"0".equals(boardValue)) && (!isFlag)) {
			fieldButton.setText(boardValue);
		} else if ("0".equals(boardValue)) {
			fieldButton.setText("");
		}
		fieldButton.setBackground(Color.LIGHT_GRAY);
		isOpen = true;
		--GameField.closedFieldCount;
	}

	public void setIsMine(boolean isMine) {
		this.isMine = isMine;
	}

	public boolean getIsFlag() {
		return isFlag;
	}

	public boolean getIsMine() {
		return isMine;
	}

	public boolean getIsOpen() {
		return isOpen;
	}
}
