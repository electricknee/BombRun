import javax.swing.*;
import java.awt.*;

/**
 * Created by zakary on 6/28/15.
 */
public class BoardFrame extends JFrame{

    public BoardFrame(int windowSize){
        this.setBackground(Color.LIGHT_GRAY);
        this.setResizable(false);
        this.setSize(windowSize,windowSize);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
