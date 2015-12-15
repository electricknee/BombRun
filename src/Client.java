import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.io.ObjectInputStream;
import java.lang.ClassNotFoundException;
import java.io.Writer;
import java.io.OutputStream;
/**
 * Created by zakary on 12/13/15.
 */
public class Client {
    public enum movement{
      UP, DOWN, LEFT, RIGHT, BOMB
    }

    int PORT;
    String serverAddr;
    Socket socket_to_server; // use for reading and writing
    Board tempBoard;
    //input
    InputStream is;
    ObjectInputStream ois;
    //output
    OutputStream os;

    public Client(int port){
      this.PORT = port;

    }

    public void connectToServer() throws IOException, ClassNotFoundException{
        try{
            //Cell recvCell;
            System.out.println("connecting to server...");
            socket_to_server = new Socket("localhost", PORT);
            // read objects in prep
            System.out.println("connected!");
            is = socket_to_server.getInputStream();
            ois = new ObjectInputStream(is);

            // send movements out prep
            os = socket_to_server.getOutputStream();

            // testing
            /*
            System.out.println("sending movement to server...");
            sendMoveToServer(movement.UP);
            Board recvBoard;
            recvBoard = getBoardfromServer();
            System.out.println(recvBoard.boardSize);
            */
        } catch(IOException e){
          e.printStackTrace();
      } //catch(IOException | ClassNotFoundException e){ use this
    }

    public Board getBoardfromServer() throws ClassNotFoundException, IOException{
        try{
            System.out.println("Waiting to recieve Board...");
            tempBoard = (Board) ois.readObject();
            System.out.println("Returning Board from function");
            return tempBoard;
        }catch(ClassNotFoundException ex){
          System.out.println("ClassNotFound Exception");
        }
        return tempBoard;
    }
    // Call this from Controller Class to send moves recieved from keyboard
    public void sendMoveToServer(movement m) throws IOException{
        char send_digit;
        switch(m){
          case UP:    send_digit = 'u'; // UP
                      break;
          case DOWN:  send_digit = 'd'; // DOWN
                      break;
          case LEFT:  send_digit = 'l'; // LEFT
                      break;
          case RIGHT: send_digit = 'r'; // RIGHT
                      break;
          case BOMB:  send_digit = 'b'; // BOMB
                      break;
          default:    System.out.println("Error in Client");
                      return;
        }
        // Socket to client has already been created
        try{
            System.out.println("writing to output stream");
            os.write(send_digit);
            os.flush();
        }catch(IOException e){
           e.printStackTrace();
        }
    }



}