import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.io.ByteArrayOutputStream;
import java.util.TimerTask;
import java.util.Timer;
import java.net.*;

/**
 * Created by zakary on 12/13/15.
 */
public class HostServer implements Runnable{

    private BoardController boardController;
    DatagramSocket recvSocket;
    DatagramSocket sndSocket;
    byte[] sendData = new byte[1024];
    byte[] recvData = new byte[1024];

    public HostServer(int port,Board brd){

        this.boardController = new BoardController(Main.gameBoard);
        try{
            sndSocket = new DatagramSocket();
            recvSocket = new DatagramSocket(9999);
        }catch(SocketException e){
            e.printStackTrace();
        }
    }

    public void run(){
        while(true){
            try{
                readMoveFromClient();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void sendBoardtoClient(Board board) throws IOException{
      //InetAddress addr = InetAddress.getLocalHost();
      InetAddress addr = InetAddress.
        getByName("2601:86:c100:9ef0:1dec:552a:be1b:83c4");

      char[] Arr = new char[Main.BSIZE];
      BoardArray.convertBoardtoArray(board,Arr);
      String temp = new String(Arr);

      sendData = temp.getBytes();
      DatagramPacket out = new DatagramPacket(
                                    sendData,
                                    sendData.length,
                                    Main.global_address,
                                    9991);
      sndSocket.send(out);
    }

    //alter to accomodate for multiple clients
    public void readMoveFromClient() throws IOException{

      try{
        // wait until the client sends a character
        /* maybe:
            a - p2 up
            b - p2 down
            ...
            f - p3 up
            ...
        */
        //char in_char = (char) isr.read();

        // read the Datagram
        DatagramPacket receivePacket = new DatagramPacket(recvData,recvData.length);
        recvSocket.receive(receivePacket);
        String temp = new String(recvData);

        switch(temp.charAt(0)){
          case 'u':     boardController.playerAction(
                                    2,BoardController.movement.UP);
                        break;
          case 'd':     boardController.playerAction(
                                    2,BoardController.movement.DOWN);
                        break;
          case 'l':     boardController.playerAction(
                                    2,BoardController.movement.LEFT);
                        break;
          case 'r':     boardController.playerAction(
                                    2,BoardController.movement.RIGHT);
                        break;
          case 'b':     boardController.playerAction(
                                    2,BoardController.movement.BOMB);
                        break;
          default:      System.out.println("Error in Server Read");
                        return;
        }
      }catch(IOException e){
         e.printStackTrace();
      }



    }

}
