import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

public class GameField {
    private int weight;
    private int height;
    private boolean isGameStopped;
    public static int closeBoardCount;
    public static int flagCount;
    public static int mineCount;

    public Field[][] field;

    public GameField(int weight, int height) {
        this.weight = weight;
        this.height = height;
        this.closeBoardCount = weight * height;
        this.mineCount = (int) (weight * height / 6.4);
        this.flagCount = mineCount;
        this.isGameStopped = false;

        field = new Field[height][weight];
        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j < field[i].length; ++j) {
                field[i][j] = new Field();
            }
        }

        mineIndex();
        setBoardValues();

        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j < field[i].length; ++j) {
                int finalI = i;
                int finalJ = j;
                field[i][j].fieldButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        if (!isGameStopped) {
                            if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
                                field[finalI][finalJ].handleRightClick();
                            } else if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
                                field[finalI][finalJ].handleLeftClick();
                                if ((!field[finalI][finalJ].getIsFlag()) && field[finalI][finalJ].getIsOpen()) {
                                    openFields(finalI, finalJ);
                                }
                                if (field[finalI][finalJ].getIsMine() && field[finalI][finalJ].getIsOpen()) {
                                    checkForMineClick(finalI, finalJ);
                                } else {
                                    checkForWin();
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    private Set<Integer> getMines() {
        Random random = new Random();

        Set<Integer> mines = new LinkedHashSet<Integer>();
        while (mines.size() < mineCount) {
            mines.add(random.nextInt(weight * height));
        }

        return mines;
    }

    private void mineIndex() {
        Set mines = getMines();
        for (Object index : mines) {
            int rowIndex = (int) ((int) index / weight);
            int columnIndex = (int) index - rowIndex * weight;
            field[rowIndex][columnIndex].setIsMine(true);
            field[rowIndex][columnIndex].boardValue = Field.mine;
        }
    }

    private int neighborMineCount(int y, int x) {
        if (field[y][x].getIsMine()) {
            return 0;
        }
        int neighbors = 0;
        for (int i = y - 1; i <= y + 1; ++i) {
            for (int j = x - 1; j <= x + 1; ++j) {
                if (i < 0 || i >= height) {
                    continue;
                }
                if (j < 0 || j >= weight) {
                    continue;
                }
                if (x == j && y == i) {
                    continue;
                }
                if (field[i][j].getIsMine()) {
                    ++neighbors;
                }
            }
        }
        return neighbors;
    }

    private void setBoardValues() {
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < weight; ++j) {
                if (!field[i][j].getIsMine()) {
                    field[i][j].boardValue = Integer.toString(neighborMineCount(i, j));
                }
            }
        }
    }

    private void openFields(int y, int x) {
        boolean isMineNeighbor = false;
        for (int i = y - 1; i <= y + 1; ++i) {
            for (int j = x - 1; j <= x + 1; ++j) {
                if ((i < 0 || i >= height) || (j < 0 || j >= weight) || (x == j && y == i)) {
                    continue;
                }
                if (field[i][j].getIsMine()) {
                    isMineNeighbor = true;
                    break;
                }
            }
        }
        if (!isMineNeighbor) {
            for (int i = y - 1; i <= y + 1; ++i) {
                for (int j = x - 1; j <= x + 1; ++j) {
                    if ((i < 0 || i >= height) || (j < 0 || j >= weight) || (x == j && y == i)) {
                        continue;
                    }
                    if (!field[i][j].getIsOpen()) {
                        field[i][j].handleLeftClick();
                        openFields(i, j);
                    }
                }
            }
        }
    }

    private void checkForMineClick(int i, int j) {
        if (field[i][j].getIsMine()) {
            field[i][j].fieldButton.setForeground(Color.RED);
            isGameStopped = true;
            ImageIcon loseImage = new ImageIcon(new ImageIcon(
                    "images/lose.png").getImage().getScaledInstance(
                            50, 50, Image.SCALE_DEFAULT));
            JOptionPane.showMessageDialog(null, "GAME OVER",
                    "", JOptionPane.PLAIN_MESSAGE, loseImage);
        }
    }

    private void checkForWin() {
        if (closeBoardCount == mineCount) {
            isGameStopped = true;
            ImageIcon winImage = new ImageIcon(new ImageIcon(
                    "images/win.png").getImage().getScaledInstance(
                    50, 50, Image.SCALE_DEFAULT));
            JOptionPane.showMessageDialog(null, "YOU WIN!",
                    "", JOptionPane.PLAIN_MESSAGE, winImage);
        }
    }

    public void resetGame() {
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < weight; ++j) {
                field[i][j].reset();
                field[i][j].fieldButton.setText("");
                field[i][j].setIsMine(false);
            }
        }
        this.closeBoardCount = weight * height;
        this.mineCount = (int) (weight * height / 6.4);
        this.flagCount = mineCount;
        this.isGameStopped = false;
        mineIndex();
        setBoardValues();
    }
}