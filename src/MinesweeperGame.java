package minesweepergame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MinesweeperGame {
    static JLabel flagCount;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 475);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        JPanel field = new JPanel();

        topPanel.setBackground(Color.YELLOW);

        GridLayout topGridLayout = new GridLayout(0, 4);
        topGridLayout.setHgap(40);
        final GridLayout[] centerGridLayout = {new GridLayout(8, 8)};

        GameField[] gameField = {new GameField(8, 8)};
        for (int i = 0; i < gameField[0].field.length; ++i) {
            for (int j = 0; j < gameField[0].field[i].length; ++j) {
                field.add(gameField[0].field[i][j].fieldButton);
            }
        }
		
        JLabel mineCount = new JLabel("    " + Field.mine + " :  " + String.valueOf(GameField.mineCount));
        flagCount = new JLabel(Field.flag + " :  " + String.valueOf(GameField.flagCount));
        flagCount.setForeground(Color.RED);

        JButton restartGame = new JButton("Restart game");
        restartGame.setBackground(new Color(0,255,127));
        restartGame.setMargin(new Insets(5, 0, 5, 0));

        String[] difficulty = {"8 * 8 (10 mines)",
                "16 * 16 (40 mines)",
                "24 * 24 (90 mines)"};
        JComboBox changeDifficulty = new JComboBox(difficulty);
        changeDifficulty.setMaximumSize(changeDifficulty.getPreferredSize());

        restartGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                gameField[0].resetGame();
            }
        });

        changeDifficulty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
				int height = gameField[0].field.length;
				int weight = gameField[0].field[0].length;
				
				if (!((0 == changeDifficulty.getSelectedIndex() && 8 == height)
					|| (1 == changeDifficulty.getSelectedIndex() && 16 == height)
					|| (2 == changeDifficulty.getSelectedIndex() && 24 == height))) {
                	for (int i = 0; i < gameField[0].field.length; ++i) {
                    	for (int j = 0; j < gameField[0].field[i].length; ++j) {
                        	field.remove(gameField[0].field[i][j].fieldButton);
                    	}
                	}
					if (0 == changeDifficulty.getSelectedIndex()) {
						height = weight = 8;
						frame.setSize(650, 475);
					} else if (1 == changeDifficulty.getSelectedIndex()) {
						height = weight = 16;
						frame.setSize(750, 500);
					} else if (2 == changeDifficulty.getSelectedIndex()){
						height = weight = 24;
						frame.setSize(1150, 700);
					}
					gameField[0] = new GameField(height, weight);
					centerGridLayout[0].setRows(height);
					centerGridLayout[0].setColumns(weight);
					for (int i = 0; i < gameField[0].field.length; ++i) {
						for (int j = 0; j < gameField[0].field[i].length; ++j) {
							field.add(gameField[0].field[i][j].fieldButton);
						}
					}
					mineCount.setText("    " + Field.mine + " :  " + String.valueOf(GameField.mineCount));
					flagCount.setText(Field.flag + " :  " + String.valueOf(GameField.flagCount));
					field.revalidate();
					field.repaint();
				}
            }
        });

        topPanel.add(mineCount);
        topPanel.add(flagCount);
        topPanel.add(restartGame);
        topPanel.add(changeDifficulty);

        topPanel.setLayout(topGridLayout);
        field.setLayout(centerGridLayout[0]);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(field, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}
