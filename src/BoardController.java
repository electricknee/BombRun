import java.util.TimerTask;
import java.util.Timer;
import java.io.Serializable;

/*  Nothing happens if an action is not possible*/
public class BoardController implements Serializable{

    public enum movement{
        UP, DOWN, LEFT, RIGHT, BOMB
    }
    private Board board;
    private int rowSize;

    public BoardController(Board b){
        this.board = b;
        this.rowSize = board.rowSize;
    }

    public void playerAction(int playerID, movement m ){
        //System.out.print("Called playerAction\nPlayerID\n");
        //System.out.print(playerID); System.out.print("\n");

        int currentIndex = board.getPlayerIndex(playerID);
        switch (m){
            case UP:    if (board.movePlayer(currentIndex, 1)) {
                            board.setPlayerIndex(currentIndex - rowSize, playerID);
                            board.repaint();
                        }
                        break;

            case DOWN:  if (board.movePlayer(currentIndex, 2)) {
                            board.setPlayerIndex(currentIndex + rowSize, playerID);
                            board.repaint();
                        }
                        break;

            case LEFT:  if (board.movePlayer(currentIndex, 4)) {
                            board.setPlayerIndex(currentIndex - 1, playerID);
                            board.repaint();
                        }
                        break;

            case RIGHT: if (board.movePlayer(currentIndex, 3)) {
                            board.setPlayerIndex(currentIndex + 1, playerID);
                            board.repaint();
                        }
                        break;

            case BOMB:  Player player = Main.gameBoard.getPlayer(currentIndex);
                        if (player.getBombCount() > 0 && !player.isDead()) {
                            board.setBomb(currentIndex, 3);
                            player.subBomb();
                            Timer timer = new Timer();
                            timer.schedule(new replenishBombTask(playerID),3000);
                        }
                        break;

            default:    System.out.println("Error in BoardController");
                        return;
        }
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
