import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.TimerTask;
import java.util.Timer;
import java.io.IOException;

/**
 * Created by zakary on 6/28/15.
 */
public class Controller implements java.io.Serializable{

    private Board board;
    private int rowSize;
    private int player_number;
    private Client client;

    public Controller(int num, Client cl) { // client controller
        System.out.println("Creating Client controller");
        this.board = Main.gameBoard;
        //this.rowSize = board.getRowSize();
        //this.player_number = num;
        this.client = cl;
        System.out.println("ex");
    }

    public Controller(int num) { // server controller
        System.out.println("Creating Server controller");
        this.board = Main.gameBoard;
        this.rowSize = board.getRowSize();
        this.player_number = num;
    }

    // key bindings
    public void addKeyBindings() {

            JComponent comp = Main.gameBoard;
        // add the key bindings for up, down, left and right to the input map
        comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "down1");
        comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "up1");
        comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "left1");
        comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "right1");
        comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_O, 0), "bomb1");

        // assign actions to the key bindings in the action map
        comp.getActionMap().put("down1", new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                if(player_number==1){ // server player
                  int target_index = board.getPlayerIndex(1);
                  if (board.movePlayer(target_index, 2)) {
                      board.setPlayerIndex(target_index + rowSize, 1);
                      board.repaint();
                  }
                }
                else{
                  try{
                      client.sendMoveToServer(Client.movement.DOWN);
                    }catch(IOException e){
                      e.printStackTrace();
                    }
                }
          }
        });
        comp.getActionMap().put("up1", new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
              if(player_number==1){ // server player
                int target_index = board.getPlayerIndex(1);
                  if (board.movePlayer(target_index, 1)) {
                      board.setPlayerIndex(target_index - rowSize, 1);
                      board.repaint();
                  }
              }
            }
        });
        comp.getActionMap().put("left1", new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                if(player_number==1){ // server player
                    int target_index = board.getPlayerIndex(1);
                    if (board.movePlayer(target_index, 4)) {
                        board.setPlayerIndex(target_index - 1, 1);
                        board.repaint();
                    }
                }
            }
        });
        comp.getActionMap().put("right1", new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
              if(player_number==1){ // server player
                int target_index = board.getPlayerIndex(1);
                    if (board.movePlayer(target_index, 3)) {
                        board.setPlayerIndex(target_index + 1, 1);
                        board.repaint();
                    }
                }
            }
        });
        comp.getActionMap().put("bomb1", new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                if(player_number==1){ // server player
                      int ind = Main.gameBoard.getPlayerIndex(1);
                      Player player = Main.gameBoard.getPlayer(ind);
                      if (player.getBombCount() > 0 && !player.isDead()) {
                          board.setBomb(ind, 3);
                          player.subBomb();
                          Timer timer = new Timer();
                          timer.schedule(new replenishBombTask(1),3000);
                      }
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
