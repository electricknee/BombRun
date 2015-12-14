import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by zakary on 6/28/15.
 */
public class Main {

    public static Board gameBoard;

    public static void main(String [ ] args){

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

        // Temporary
       for(int x=0;x<10;x=x+2)
            for (int i = 1;i<=5;i++)
                gameBoard.blockCell((10 + i*2)+11*x);
        // end temp
        
        gameBoard.addPlayer(0, new Player(2));
        gameBoard.addPlayer(11*11-1,new Player(1));
        gameBoard.repaint();

        frame.setVisible(true);

	frame.setVisible(true);


    }
}
