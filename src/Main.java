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

    public static void main(String [ ] args) throws IOException{

        boolean server=false;
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

        JButton button = new JButton("RESET GAME");
        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("pressed reset");
                gameBoard.printBoard();
                gameBoard.ResetBoard();
            }
        });

        frame.add(BorderLayout.SOUTH,button);


        //frame.getJMenuBar().add(button);
        Controller myController = new Controller();
        myController.addKeyBindings();

        // Temporary Barrels
       for(int x=0;x<10;x=x+2)
            for (int i = 1;i<=5;i++)
                gameBoard.blockCell((10 + i*2)+11*x);
        // end temp

        gameBoard.addPlayer(0, new Player(2));
        gameBoard.addPlayer(11*11-1,new Player(1));
        gameBoard.repaint();

        frame.setVisible(true);

        if(server){
            HostServer hostServer = new HostServer(9998);
            hostServer.runServer();
        } else{
            Client client = new Client(9998);
            client.connectToServer();
        }

    }
}
