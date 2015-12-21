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
import java.net.*;
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

    //updated server
    DatagramSocket sndSocket;
    DatagramSocket recvSocket;
    DatagramPacket receivePacket;
    byte[] receiveBuf = new byte[1024];
    byte[] sendData = new byte[1024];

    public Client(int port){
      this.PORT = port;
      try{
          sndSocket = new DatagramSocket();
          recvSocket = new DatagramSocket(9991);
      }catch(SocketException e){
          e.printStackTrace();
      }
    }
    // "2601:86:c100:9ef0:dcb:7f20:3fd6:3e2"
    public void connectToServer(String ip_string)
        throws IOException, ClassNotFoundException{
        try{
            addr = InetAddress.getByName(ip_string);
            String TEMP_LOCAL_HOST_STRING = "localhost";
            //Cell recvCell;
            //System.out.println("connecting to server...");
            socket_to_server = new Socket(TEMP_LOCAL_HOST_STRING, PORT);
            // read objects in prep
            //System.out.println("connected!");
            is = socket_to_server.getInputStream();
            ois = new ObjectInputStream(is);

            // send movements out prep
            os = socket_to_server.getOutputStream();

        } catch(IOException e){
          e.printStackTrace();
      } //catch(IOException | ClassNotFoundException e){ use this
    }
    public char[] getArrFromServer(){   // reads single packet and returns board array
        char[] Arr = new char[Main.BSIZE];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuf,receiveBuf.length);
        try{
            recvSocket.receive(receivePacket);
        }catch(IOException e){
            e.printStackTrace();
        }
        // recieveBuf has the data in byte array
        String temp = new String(receiveBuf);
        //System.out.printf("getArrFromServer) String recieved: %s\n",temp);
        return temp.toCharArray();
    }

    public Board getBoardfromServer()
    throws ClassNotFoundException, IOException{
        try{

            //System.out.println("Waiting to recieve Board...");
            tempBoard = (Board) ois.readObject();
            //System.out.println("getBoardfromServer Function:");
            //System.out.printf("Recieved board with update count %d",update_count);
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
        String temp;
        switch(m){
          case UP:      send_digit = 'u'; // UP
                        temp = "u";
                        break;
          case DOWN:    send_digit = 'd'; // DOWN
                        temp = "d";
                        break;
          case LEFT:    send_digit = 'l'; // LEFT
                        temp = "l";
                        break;
          case RIGHT:   send_digit = 'r'; // RIGHT
                        temp = "r";
                        break;
          case BOMB:    send_digit = 'b'; // BOMB
                        temp = "b";
                        break;
          default:      System.out.println("Error in Client");
                        return;
        }
        // Socket to client has already been created
        try{
            os.write(send_digit);
            os.flush();

            // send Datagram
             InetAddress addr = InetAddress.getLocalHost();
             System.out.println("Sending [move] from Datagram Socket:");
             System.out.print(temp+"\n");
             DatagramPacket out = new DatagramPacket(sendData,sendData.length,addr,9999);
             sndSocket.send(out);
        }catch(IOException e){
           e.printStackTrace();
        }
    }



}
