import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zakary on 6/28/15.
 */
public class Bomb {
    private Timer timer;
    private int bombIndex;

    public Bomb(){} // for display purpose in client, non-explosive ;)

    public Bomb(int index,int time){
        this.bombIndex = index;
        timer = new Timer();
        timer.schedule(new ExplodeTask(index),time*1000);
    }

    class ExplodeTask extends TimerTask {
        private int index;
        public ExplodeTask(int index){
            this.index = index;
        }

        public void run(){
            Main.gameBoard.cleanBomb(this.index); // visual rep
            Main.gameBoard.detonate(this.index,2);
        }
    }

    public void cancelDet(){
        this.timer.cancel();
    }
}
