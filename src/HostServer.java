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
    private BoardController boardController;
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
        this.boardController = new BoardController(this.board);
    }

    public void initServer() throws IOException {
        ServerSocket listener = new ServerSocket(PORT);
        try{

                System.out.println("waiting to find client");
                socket_to_client = listener.accept();
                System.out.println("found client");

                // send out board
                os = socket_to_client.getOutputStream();
                oos = new ObjectOutputStream(os);
                // read movements in
                is = socket_to_client.getInputStream();
                isr = new InputStreamReader(is);

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
          case 'u':     //System.out.println("Read UP, moving player 2"); // UP
                        boardController.playerAction(2,BoardController.movement.UP);
                        break;
          case 'd':     //System.out.println("Read DOWN, moving player 2"); // DOWN
                        boardController.playerAction(2,BoardController.movement.DOWN);
                        break;
          case 'l':     //System.out.println("Read LEFT, moving player 2"); // LEFT
                        boardController.playerAction(2,BoardController.movement.LEFT);
                        break;
          case 'r':     //System.out.println("Read RIGHT, moving player 2"); // RIGHT
                        boardController.playerAction(2,BoardController.movement.RIGHT);
                        break;
          case 'b':     //System.out.println("Read BOMB, p2"); // BOMB
                        boardController.playerAction(2,BoardController.movement.BOMB);
                        break;
          default:      System.out.println("Error in Server Read");
                        return;
        }
      }catch(IOException e){
         e.printStackTrace();
      }
    }

}
