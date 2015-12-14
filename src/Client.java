import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
/**
 * Created by zakary on 12/13/15.
 */
public class Client {
    int PORT;
    String serverAddr;

    public Client(int port){
      this.PORT = port;

    }

    public void connectToServer() throws IOException{
        try{
            System.out.println("connecting to server...");
            Socket s = new Socket("localhost", PORT);
            System.out.println("connected!");
        } catch(IOException e){
          System.out.println("IOException");
        }
    }

}
