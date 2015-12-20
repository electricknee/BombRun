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
import java.net.InetAddress;
/**
 * Created by zakary on 12/13/15.
 */
public class Client {

    public enum movement{
      UP, DOWN, LEFT, RIGHT, BOMB
    }
    int update_count = 0;
    int PORT;
    String serverAddr;
    Socket socket_to_server; // use for reading and writing
    Board tempBoard;
    InetAddress addr;
    //input
    InputStream is;
    ObjectInputStream ois;
    //output
    OutputStream os;

    public Client(int port){
      this.PORT = port;

    }
    // "2601:86:c100:9ef0:dcb:7f20:3fd6:3e2"
    public void connectToServer(String ip_string)
        throws IOException, ClassNotFoundException{
        try{
            addr = InetAddress.getByName(ip_string);
            String TEMP_LOCAL_HOST_STRING = "localhost";
            //Cell recvCell;
            System.out.println("connecting to server...");
            socket_to_server = new Socket(TEMP_LOCAL_HOST_STRING, PORT);
            // read objects in prep
            System.out.println("connected!");
            is = socket_to_server.getInputStream();
            ois = new ObjectInputStream(is);

            // send movements out prep
            os = socket_to_server.getOutputStream();

        } catch(IOException e){
          e.printStackTrace();
      } //catch(IOException | ClassNotFoundException e){ use this
    }

    public Board getBoardfromServer() throws ClassNotFoundException, IOException{
        try{

            //System.out.println("Waiting to recieve Board...");
            tempBoard = (Board) ois.readObject();
            System.out.println("getBoardfromServer Function:");
            //tempBoard.printBoard();
            System.out.printf("Recieved board with update count %d",update_count);
            update_count++;
            return tempBoard;
        }catch(ClassNotFoundException ex){
            ex.printStackTrace();
        }
        return tempBoard;
    }
    // Call this from Controller Class to send moves recieved from keyboard
    public void sendMoveToServer(movement m) throws IOException{
        char send_digit;
        switch(m){
          case UP:      send_digit = 'u'; // UP
                        break;
          case DOWN:    send_digit = 'd'; // DOWN
                        break;
          case LEFT:    send_digit = 'l'; // LEFT
                        break;
          case RIGHT:   send_digit = 'r'; // RIGHT
                        break;
          case BOMB:    send_digit = 'b'; // BOMB
                        break;
          default:      System.out.println("Error in Client");
                        return;
        }
        // Socket to client has already been created
        try{
            os.write(send_digit);
            os.flush();
        }catch(IOException e){
           e.printStackTrace();
        }
    }



}
