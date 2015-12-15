import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.TimerTask;
import java.util.Timer;
/**
 * Created by zakary on 12/13/15.
 */
public class HostServer {
    //construct server with port
    //call server.initServer

    int PORT;
    Board board;
    int rowSize;
    Socket socket_to_client;
    // send objects out
    OutputStream os;
    ObjectOutputStream oos;
    // recieve movements
    InputStream is;
    InputStreamReader isr;

    public HostServer(int port,Board brd){
        this.board = brd;
        this.PORT = port;
        this.rowSize = brd.getRowSize();
    }

    public void initServer() throws IOException {
        ServerSocket listener = new ServerSocket(PORT);
        try{

                System.out.println("waiting to find client");
                socket_to_client = listener.accept();
                System.out.println("found client");


                // send out board -> make sure board is Serializable
                os = socket_to_client.getOutputStream();
                oos = new ObjectOutputStream(os);
                // read movements in
                is = socket_to_client.getInputStream();
                isr = new InputStreamReader(is);
                /* //Testing
                readMoveFromClient();
                Board testBoard = new Board(7);
                sendBoardtoClient(testBoard);
                */
        }catch(IOException e){
           e.printStackTrace();
        }
    }

    public void sendBoardtoClient(Board board) throws IOException{
      try{
          System.out.println("trying to send board to client");
          oos.writeObject(board);
      }catch(IOException e){
           e.printStackTrace();
      }
    }

    //alter to accomodate for multiple clients
    public void readMoveFromClient() throws IOException{  // only read single char
      // read from client and call movement on board
      try{
        // wait until the client sends a character
        char in_char = (char) isr.read();
        switch(in_char){
          case 'u':     System.out.println("Read UP"); // UP
                        moveUp();
                        break;
          case 'd':     System.out.println("Read DOWN"); // DOWN
                        moveDown();
                        break;
          case 'l':     System.out.println("Read LEFT"); // LEFT
                        moveLeft();
                        break;
          case 'r':     System.out.println("Read RIGHT"); // RIGHT
                        moveRight();
                        break;
          case 'b':     System.out.println("Read BOMB"); // BOMB
                        setBomb();
                        break;
          default:      System.out.println("Error in Server Read");
                        return;
        }
      }catch(IOException e){
         e.printStackTrace();
      }
    }

    public void moveDown(){
        int currentIndex = board.getPlayerIndex(2);
        if (board.movePlayer(currentIndex, 2)) {
            board.setPlayerIndex(currentIndex + rowSize, 2);
            board.repaint();
        }
    }
    public void moveUp(){
        int currentIndex = board.getPlayerIndex(2);
        if (board.movePlayer(currentIndex, 1)) {
            board.setPlayerIndex(currentIndex - rowSize, 2);
            board.repaint();
        }
    }
    public void moveLeft(){
        int currentIndex = board.getPlayerIndex(2);
        if (board.movePlayer(currentIndex, 4)) {
            board.setPlayerIndex(currentIndex - 1, 2);
            board.repaint();
        }

    }
    public void moveRight(){
        int currentIndex = board.getPlayerIndex(2);
        if (board.movePlayer(currentIndex, 3)) {
            board.setPlayerIndex(currentIndex + 1, 2);
            board.repaint();
        }
    }
    public void setBomb(){
        int ind = Main.gameBoard.getPlayerIndex(2);
        Player player = Main.gameBoard.getPlayer(ind);
        if (player.getBombCount() > 0 && !player.isDead()) {
            board.setBomb(ind, 3);
            player.subBomb();
            Timer timer = new Timer();
            timer.schedule(new replenishBombTask(2),3000);
        }
    }
    public class replenishBombTask extends TimerTask{
        private int ID;
        public replenishBombTask(int ID){
            this.ID = ID;
        }
        public void run(){
            System.out.print("replenishBombTask:");
            Player P = Main.gameBoard.getPlayer(Main.gameBoard.getPlayerIndex(ID));
            P.addBomb();
        }
    }
}
