import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.*;
import java.net.InetAddress;
/**
 * Created by zakary on 6/28/15.
 */
public class Main {

    public static Board gameBoard;
    public static final int RSIZE = 20;
    public static final int BSIZE = RSIZE*RSIZE;
    public static boolean server = false;
    public static HostServer hostServer;
    public static Controller myController;
    public static InetAddress global_address;
    // should work with IPv4 and IPv6 addresses: colons / periods

    private static InputStreamReader in;
    private static BufferedReader br;

    private static final String HOST_IP_STRING =
        "2601:86:c100:9ef0:564:8f1c:842b:67d9";

    public static void main(String [] args)
        throws IOException, ClassNotFoundException{

        in = new InputStreamReader(System.in);
        br = new BufferedReader(in);

        if(args.length == 1){
            if(args[0].equals("localhost")){
                System.out.println("Running Local");
                global_address =  InetAddress.getLocalHost();
            } else{
                System.out.println("Command Line Error");
                System.exit(1);
            }
        }else{
            System.out.println("Enter IP Address of other Participant. Press Enter");
            String ip_str = br.readLine() ;
            global_address =  InetAddress.getByName(ip_str);
        }

        System.out.println("Enter (1)Server or (2)Client. Press Enter");
        int result = Integer.parseInt( br.readLine() );

        if(result == 1){
            server = true;
        }
        else if(result == 2){
            server = false;
        }
        else{
            System.out.println("Input Error");
            return;
        }

        boolean barrels = false;
        if(!server){// only create barrels on server
            barrels = false;
        }

        BoardFrame frame = new BoardFrame(200);
        frame.setLayout(new BorderLayout());
        gameBoard = new Board(RSIZE,barrels);
        frame.add(BorderLayout.CENTER,gameBoard);

        if(server){/*--------------------------------------SERVER-------------*/

            JButton button = new JButton("RESET GAME");
            button.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    gameBoard.ResetBoard(true);
                }
            });

            frame.add(BorderLayout.SOUTH,button);

            // block cells here -> gameBoard.blockCell(index);

            gameBoard.addPlayer(0, new Player(1));
            gameBoard.addPlayer(gameBoard.boardSize-1, new Player(2));
            gameBoard.repaint();

            frame.setVisible(true);
        }/*-------------------------------------------------------------------*/

        Client client;


        if(server){/*--------------------------------------SERVER-------------*/

            hostServer = new HostServer(9998,gameBoard);
            myController = new Controller(1);
            myController.addKeyBindings();

            Thread constant_board_sender = new Thread(hostServer);
            constant_board_sender.start();
            gameBoard.universalRepaint();

        } else{/*--------------------------------------CLIENT-----------------*/

            client = new Client(9998); // client used to send moves to Server

            myController = new Controller(2,client);
            myController.addKeyBindings();
            char[] tArr = null;

            while(true){
                tArr = client.getArrFromServer();
		gameBoard.repaint();
               /* if(tArr != null){
                    BoardArray.writeArraytoBoard(tArr, gameBoard);
                    gameBoard.repaint();
                    tArr=null;
                }*/
            }
        }

    }
}
