import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.*;

// should work with IPv4 and IPv6 addresses: colons and periods

/**
 * Created by zakary on 6/28/15.
 */
public class Main {

    public static Board gameBoard;
    private static final String HOST_IP_STRING =
        "2601:86:c100:9ef0:564:8f1c:842b:67d9";


    public static void main(String [ ] args)
        throws IOException, ClassNotFoundException{

        boolean server = false;
        System.out.println("Enter (1)Server or (2)Client");
        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(in);
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

        boolean barrels = true;
        if(!server){
            barrels = false;
        }

        BoardFrame frame = new BoardFrame(800);
        frame.setLayout(new BorderLayout());
        gameBoard = new Board(11,barrels);
        frame.add(BorderLayout.CENTER,gameBoard);

        if(server){/*--------------------------------------SERVER-------------*/

            JButton button = new JButton("RESET GAME");
            button.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    System.out.println("pressed reset");
                    gameBoard.ResetBoard(true);
                }
            });

            frame.add(BorderLayout.SOUTH,button);
            /*
            // Temporary Barrels
           for(int x=0;x<10;x=x+2)
                for (int i = 1;i<=5;i++)
                    gameBoard.blockCell((10 + i*2)+11*x);
            // end temp
            */
            gameBoard.addPlayer(0, new Player(1));
            gameBoard.addPlayer(gameBoard.boardSize-1, new Player(2));
            gameBoard.repaint();

            frame.setVisible(true);
        }/*-------------------------------------------------------------------*/

        Client client;
        HostServer hostServer;
        Controller myController;

        if(server){/*--------------------------------------SERVER-------------*/

            hostServer = new HostServer(9998,gameBoard);
            hostServer.initServer();
            myController = new Controller(1);
            myController.addKeyBindings();
            hostServer.sendBoardtoClient(gameBoard);
            System.out.println("Board Sent!");
            while(true){
                hostServer.readMoveFromClient();
            }
        } else{/*--------------------------------------CLIENT-----------------*/

            client = new Client(9998); // client used to send moves to Server
            client.connectToServer(HOST_IP_STRING);
            myController = new Controller(2,client);
            myController.addKeyBindings();


            Board updated_gameBoard = client.getBoardfromServer(); // works

            Board.copyBoard(gameBoard,updated_gameBoard);
            gameBoard.repaint();
/*
            frame.invalidate();
            frame.revalidate();
            gameBoard.repaint();
            gameBoard.setVisible(false);
            gameBoard.setVisible(true);
            gameBoard.repaint();
            gameBoard.printBoard();
            //frame.removeAll();
*/
            System.out.println("Board Recieved and set");
            //frame.setVisible(true);

            }
        /*
            Trying to make the board appear in the client here but it doesn't
            hasn't been able to update at all or change since it is set at first...
        */


    }
}
