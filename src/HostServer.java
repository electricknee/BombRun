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
/**
 * Created by zakary on 12/13/15.
 */
public class HostServer {
    int PORT;
    Socket socket_to_client;
    OutputStream os;
    ObjectOutputStream oos;

    InputStream is;
    InputStreamReader isr;

    public HostServer(int port){
        this.PORT = port;
    }

    public void runServer() throws IOException {
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

                // test sending object, delete this
                //System.out.println("writing cell...");
                //oos.writeObject(testCell);
                readMoveFromClient();
                readMoveFromClient();

        }catch(IOException e){
          System.out.println("IOException");
        }
    }

    //alter to accomodate for multiple clients
    public void readMoveFromClient() throws IOException{  // only read single char
      // read from client and call movement on board
      try{
        // wait until the client sends a character
        char in_char = (char) isr.read();
        switch(in_char){
          case 'u':   System.out.println("Read UP"); // UP
                      break;
          case 'd':   System.out.println("Read DOWN"); // DOWN
                      break;
          case 'l':   System.out.println("Read LEFT"); // LEFT
                      break;
          case 'r':   System.out.println("Read RIGHT"); // RIGHT
                      break;
          case 'b':   System.out.println("Read BOMB"); // BOMB
                      break;
          default:    System.out.println("Error in Server Read");
                      return;
        }
      }catch(IOException e){
        System.out.println("IOException- While Reading from Client");
      }
    }

}
