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
    private BoardController boardController;

    public Controller(int num, Client cl) { // client controller
        //System.out.println("Creating Client controller");
        this.board = Main.gameBoard;
        this.client = cl;
        //System.out.println("ex");
    }

    public Controller(int num) { // server controller
        //System.out.println("Creating Server controller");
        this.board = Main.gameBoard;
        this.rowSize = board.getRowSize();
        this.player_number = num;
        this.boardController = new BoardController(this.board);
    }
/*----------------------------------------------------------------------------*/
    public void addKeyBindings() {

        JComponent comp = Main.gameBoard;

        comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "down1");
        comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "up1");
        comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "left1");
        comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "right1");
        comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_O, 0), "bomb1");

        // assign actions to the key bindings in the action map
        comp.getActionMap().put("down1", new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                if(player_number==1){ // server player
                    boardController.playerAction(1,BoardController.movement.DOWN);
                }
                else{   // send to server
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
                boardController.playerAction(1,BoardController.movement.UP);
              }
              else{
                try{
                    client.sendMoveToServer(Client.movement.UP);
                  }catch(IOException e){
                    e.printStackTrace();
                  }
              }
            }
        });
        comp.getActionMap().put("left1", new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                if(player_number==1){ // server player
                    boardController.playerAction(1,BoardController.movement.LEFT);
                }
                else{
                  try{
                      client.sendMoveToServer(Client.movement.LEFT);
                    }catch(IOException e){
                      e.printStackTrace();
                    }
                }
            }
        });
        comp.getActionMap().put("right1", new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
              if(player_number==1){ // server player
                  boardController.playerAction(1,BoardController.movement.RIGHT);
                }
                else{
                  try{
                      client.sendMoveToServer(Client.movement.RIGHT);
                    }catch(IOException e){
                      e.printStackTrace();
                    }
                }
            }
        });
        comp.getActionMap().put("bomb1", new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                if(player_number==1){ // server player
                    boardController.playerAction(1,BoardController.movement.BOMB);
                }
                else{
                  try{
                      client.sendMoveToServer(Client.movement.BOMB);
                    }catch(IOException e){
                      e.printStackTrace();
                    }
                }
            }
        });

    }
}
