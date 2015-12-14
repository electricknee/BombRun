import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
/**
 * Created by zakary on 12/13/15.
 */
public class HostServer {
    int PORT;

    public HostServer(int port){
        this.PORT = port;
    }

    public void runServer() throws IOException {
        ServerSocket listener = new ServerSocket(PORT);
        try{
            while(true){
                System.out.println("waiting to find client");
                Socket socket = listener.accept();
                System.out.println("found client");
                break;

            }
        }catch(IOException e){
          System.out.println("IOException");
        }
    }

}
