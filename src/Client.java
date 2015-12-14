import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.io.ObjectInputStream;
import java.lang.ClassNotFoundException;
/**
 * Created by zakary on 12/13/15.
 */
public class Client {
    public enum movement{
      UP, DOWN, LEFT, RIGHT, BOMB
    }

    int PORT;
    String serverAddr;

    public Client(int port){
      this.PORT = port;

    }
    public void sendMoveToServer(movement m){
        int send_digit;
        switch(m){
          case UP:    send_digit = 0;
                      break;
          case DOWN:  send_digit = 1;
                      break;
          case LEFT:  send_digit = 2;
                      break;
          case RIGHT: send_digit = 3;
                      break;
          case BOMB:  send_digit = 4;
                      break;
          default:    System.out.println("Error in Client");
                      return;
        }
    }

    public void connectToServer() throws IOException{
        try{
            Cell recvCell;
            System.out.println("connecting to server...");
            Socket s = new Socket("localhost", PORT);
            System.out.println("connected!");
            InputStream is = s.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            System.out.println("trying to write read Cell...");
            try{
              recvCell = (Cell) ois.readObject();
              recvCell.print();
            }catch(ClassNotFoundException ex){
              System.out.println("ClassNotFound");
            }


        } catch(IOException e){
          System.out.println("IOException- Connection Lost");
        }
    }

}
