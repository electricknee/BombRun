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
import java.util.TimerTask;
import java.util.Timer;
import java.net.*;

/**
 * Created by zakary on 12/13/15.
 */
public class HostServer implements Runnable{

    private BoardController boardController;
    int update_count=0;
    int PORT;
    Board board;
    int rowSize;
    Socket socket_to_client;
    // send objects out
    OutputStream os;
    ObjectOutputStream oos;
    // recieve movements
    InputStream is;
    InputStreamReader isr;

    DatagramSocket recvSocket;
    DatagramSocket sndSocket;
    byte[] sendData = new byte[1024];
    byte[] recvData = new byte[1024];

    public HostServer(int port,Board brd){
        this.board = brd;
        this.PORT = port;
        this.rowSize = brd.getRowSize();
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
                //System.out.printf("Sending Board: (update %d)",update_count);
                update_count++;
                ///Main.gameBoard.printBoard();
                readMoveFromClient();
                //Thread.sleep(5000);

            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void initServer() throws IOException {
        ServerSocket listener = new ServerSocket(PORT);
        try{

                //System.out.println("waiting to find client");
                socket_to_client = listener.accept();
                //System.out.println("found client");

                // send out board
                os = socket_to_client.getOutputStream();
                oos = new ObjectOutputStream(os);
                // read movements in
                is = socket_to_client.getInputStream();
                isr = new InputStreamReader(is);



        }catch(IOException e){
           e.printStackTrace();
        }
    }

    public void sendBoardtoClient(Board board) throws IOException{
/*
      try{
         //System.out.println("sendBoardtoClient Function:");
         //Main.gameBoard.printBoard();
          oos.writeObject(Main.gameBoard);
          oos.reset();
      }catch(IOException e){
           e.printStackTrace();
      }
*/
      // send with DatagramSocket

      //InetAddress addr = InetAddress.getLocalHost();
      InetAddress addr = InetAddress.
        getByName("2601:86:c100:9ef0:1dec:552a:be1b:83c4");

      char[] Arr = new char[Main.BSIZE];
      BoardArray.convertBoardtoArray(board,Arr);
      String temp = new String(Arr);

      //System.out.println("Sending [board] from Datagram Socket:");
      //System.out.print(temp+"\n");

      sendData = temp.getBytes();
      DatagramPacket out = new DatagramPacket(sendData,sendData.length,addr,9991);
      sndSocket.send(out);
     //System.out.println("send complete");
    }

    //alter to accomodate for multiple clients
    public void readMoveFromClient() throws IOException{  // only read single char
      // read from client and call movement on board
    //System.out.println("Trying to read...");
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
        //System.out.println("recieved move datagram");
        //System.out.print("move read= "+temp+"\n");

        switch(temp.charAt(0)){
          case 'u':     boardController.playerAction(2,BoardController.movement.UP);
                        break;
          case 'd':     boardController.playerAction(2,BoardController.movement.DOWN);
                        break;
          case 'l':     boardController.playerAction(2,BoardController.movement.LEFT);
                        break;
          case 'r':     boardController.playerAction(2,BoardController.movement.RIGHT);
                        break;
          case 'b':     boardController.playerAction(2,BoardController.movement.BOMB);
                        break;
          default:      System.out.println("Error in Server Read");
                        return;
        }
      }catch(IOException e){
         e.printStackTrace();
      }



    }

}
