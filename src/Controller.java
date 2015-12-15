import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.TimerTask;
import java.util.Timer;

/**
 * Created by zakary on 6/28/15.
 */
public class Controller implements java.io.Serializable{
    private Board board;
    private int rowSize;
    private int player_number;

    public Controller(int num) {
        this.board = Main.gameBoard;
        this.rowSize = board.getRowSize();
        this.player_number = num;
    }

    // key bindings
    public void addKeyBindings() {

        JComponent comp = Main.gameBoard;

        // add the key bindings for up, down, left and right to the input map
        comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down1");
        comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up1");
        comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left1");
        comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right1");
        comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0), "bomb1");

        comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "down2");
        comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "up2");
        comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "left2");
        comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "right2");
        comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_O, 0), "bomb2");


        // assign actions to the key bindings in the action map
        comp.getActionMap().put("down1", new AbstractAction() {
            public void actionPerformed(ActionEvent event) {

                int tarI = board.getPlayerIndex(1);
                if (board.movePlayer(tarI, 2)) {
                    // update target index
                    board.setPlayerIndex(tarI + rowSize, 1);
                    board.repaint();
                }
            }
        });
        comp.getActionMap().put("up1", new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                int tarI = board.getPlayerIndex(1);
                if (board.movePlayer(tarI, 1)) {
                    // update target index
                    board.setPlayerIndex(tarI - rowSize, 1);
                    board.repaint();
                }
            }
        });
        comp.getActionMap().put("left1", new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                int tarI = board.getPlayerIndex(1);
                if (board.movePlayer(tarI, 4)) {
                    // update target index
                    board.setPlayerIndex(tarI - 1, 1);
                    board.repaint();
                }
            }
        });
        comp.getActionMap().put("right1", new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                int tarI = board.getPlayerIndex(1);
                if (board.movePlayer(tarI, 3)) {
                    // update target index
                    board.setPlayerIndex(tarI + 1, 1);
                    board.repaint();
                }
            }
        });
        comp.getActionMap().put("bomb1", new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                int ind = Main.gameBoard.getPlayerIndex(1);
                Player player = Main.gameBoard.getPlayer(ind);
                if (player.getBombCount() > 0 && !player.isDead()) {
                    // set a bomb
                    board.setBomb(ind, 3);
                    player.subBomb();
                    Timer timer = new Timer();
                    timer.schedule(new replenishBombTask(1),3000);
                }

            }
        });
        //-------------------------------------------------------------------------------------
        comp.getActionMap().put("down2", new AbstractAction() {
            public void actionPerformed(ActionEvent event) {

                int tarI = board.getPlayerIndex(2);
                if (board.movePlayer(tarI, 2)) {
                    // update target index
                    board.setPlayerIndex(tarI + rowSize, 2);
                    board.repaint();
                }
            }
        });
        comp.getActionMap().put("up2", new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                int tarI = board.getPlayerIndex(2);
                if (board.movePlayer(tarI, 1)) {
                    // update target index
                    board.setPlayerIndex(tarI - rowSize, 2);
                    board.repaint();
                }
            }
        });
        comp.getActionMap().put("left2", new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                int tarI = board.getPlayerIndex(2);
                if (board.movePlayer(tarI, 4)) {
                    // update target index
                    board.setPlayerIndex(tarI - 1, 2);
                    board.repaint();
                }
            }
        });
        comp.getActionMap().put("right2", new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                int tarI = board.getPlayerIndex(2);
                if (board.movePlayer(tarI, 3)) {
                    // update target index
                    board.setPlayerIndex(tarI + 1, 2);
                    board.repaint();
                }
            }
        });
        comp.getActionMap().put("bomb2", new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                int ind = Main.gameBoard.getPlayerIndex(2);
                Player player = Main.gameBoard.getPlayer(ind);
                if (player.getBombCount() > 0 && !player.isDead()) {
                    // set a bomb
                    board.setBomb(ind, 3);
                    player.subBomb();
                    Timer timer = new Timer();
                    timer.schedule(new replenishBombTask(2),3000);
                }
            }
        });
    }
    public class replenishBombTask extends TimerTask{
        private int ID;
        public replenishBombTask(int ID){
            this.ID = ID;
        }
        public void run(){
            Player P = Main.gameBoard.getPlayer(Main.gameBoard.getPlayerIndex(ID));
            P.addBomb();
        }
    }
}
