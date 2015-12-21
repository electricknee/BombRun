import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zakary on 6/28/15.
 */
public class Player implements java.io.Serializable{

    private Board board;
    private boolean dead=false;
    private int bombCount=3;
    private int identity;

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public int getBombCount() {
        return bombCount;
    }
    public void setBombCount(int bombCount) {
        this.bombCount = bombCount;
    }
    public void addBomb(){
        this.bombCount++;
    }
    public void subBomb(){
        this.bombCount--;
        //System.out.println("(post)Player "+identity+" bombCount = "+bombCount);
    }
    public Player(int ID){
        this.identity=ID;
        board = Main.gameBoard;

    }
    public void setDead(boolean B){
        this.dead=B;
        if(B){
            this.identity = 0;
        }
    }
    public boolean isDead(){
        return this.dead;
    }
}
