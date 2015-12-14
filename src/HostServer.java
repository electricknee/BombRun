import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
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

                Cell testCell = new Cell(7,9);
                OutputStream os = socket.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                System.out.println("writing cell...");
                oos.writeObject(testCell);
            }
        }catch(IOException e){
          System.out.println("IOException");
        }
    }

}
