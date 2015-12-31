import java.io.IOException;
import java.util.Date;
import java.lang.ClassNotFoundException;
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
    //updated server
    DatagramSocket sndSocket;
    DatagramSocket recvSocket;
    DatagramPacket receivePacket;
    byte[] receiveBuf = new byte[1024];
    byte[] sendData = new byte[1024];
    public static String currentArray;
    

    public Client(int port){
      try{
          sndSocket = new DatagramSocket();
          recvSocket = new DatagramSocket(9991);
      }catch(SocketException e){
          e.printStackTrace();
      }
    }

    public char[] getArrFromServer(){	// remove the return just update the currentArray static
        char[] Arr = new char[Main.BSIZE];
        DatagramPacket receivePacket = new DatagramPacket(
                                            receiveBuf,receiveBuf.length);
        try{
            recvSocket.receive(receivePacket);
        }catch(IOException e){
            e.printStackTrace();
        }
        // recieveBuf has the data in byte array
	
        String temp = currentArray = new String(receiveBuf);
        return temp.toCharArray();
    }

    // Call this from Controller Class to send moves recieved from keyboard
    public void sendMoveToServer(movement m) throws IOException{
        String temp;
        switch(m){
          case UP:      temp = "u";
                        break;
          case DOWN:    temp = "d";
                        break;
          case LEFT:    temp = "l";
                        break;
          case RIGHT:   temp = "r";
                        break;
          case BOMB:    temp = "b";
                        break;
          default:      System.out.println("Error in Client");
                        return;
        }
        // Socket to client has already been created
        try{

             sendData = temp.getBytes();
             DatagramPacket out = new DatagramPacket(
                                        sendData,
                                        sendData.length,
                                        Main.global_address,
                                        9999);
             sndSocket.send(out);
        }catch(IOException e){
           e.printStackTrace();
        }
    }

}
