import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.*;

/**
 * Created by zakary on 6/28/15.
 */
public class Main {

    public static Board gameBoard;

    public static void main(String [ ] args)
        throws IOException, ClassNotFoundException{

        boolean server = false;
        System.out.println("Enter (1)server or (2)client");
        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(in);
        int result = Integer.parseInt( br.readLine() );

        if(result == 1){
            server = true;
            System.out.println("Starting Server");
        }
        else if(result == 2){
            server = false;
            System.out.println("Starting Client");
        }
        else{
            System.out.println("Input Error");
        }

        BoardFrame frame = new BoardFrame(800);
        frame.setLayout(new BorderLayout());
        gameBoard = new Board(11);
        frame.add(BorderLayout.CENTER,gameBoard);

        if(server){/*--------------------------------------SERVER-------------*/
            JButton button = new JButton("RESET GAME");
            button.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    System.out.println("pressed reset");
                    //gameBoard.printBoard();
                    gameBoard.ResetBoard();
                }
            });

            frame.add(BorderLayout.SOUTH,button);

            // Temporary Barrels
           for(int x=0;x<10;x=x+2)
                for (int i = 1;i<=5;i++)
                    gameBoard.blockCell((10 + i*2)+11*x);
            // end temp

            gameBoard.addPlayer(0, new Player(1));
            gameBoard.addPlayer(gameBoard.boardSize-1, new Player(2));
            gameBoard.repaint();

            frame.setVisible(true);
        }/*-------------------------------------------------------------------*/
        Client client;
        HostServer hostServer;
        if(server){
            hostServer = new HostServer(9998,gameBoard);
            hostServer.initServer();
            Controller myController = new Controller(1);
            myController.addKeyBindings();
            while(true){
                hostServer.readMoveFromClient();
            }
        } else{
            client = new Client(9998); // client used to send moves to Server
            client.connectToServer();
            System.out.println("creating controller");
            Controller myController = new Controller(2,client);
            System.out.println("adding keybindings for client");
            myController.addKeyBindings();
        }



    }
}
