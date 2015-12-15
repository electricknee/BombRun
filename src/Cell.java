import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zakary on 6/28/15.
 */
public class Cell implements java.io.Serializable{

    private int cellSize = 10;
    private int row;
    private int column;
    private boolean block = false;
    private Player player = null;
    private Bomb bomb = null;
    private boolean fire;
    private boolean orangeFire = false;
    private int firecount=0;
    private boolean barrel=false;

    //-------------------------------------------------------------------------------------
    public Cell(int x, int y){

        this.row = x;
        this.column = y;
    }
    public void print(){
      System.out.print("cell location\nx = ");
      System.out.println(row);
      System.out.print("y = ");
      System.out.println(column);
    }
    public void setBomb(int index, int time){
        this.bomb = new Bomb(index,time);
    }
    public void clearBomb(){
        this.bomb = null;
    }
    public boolean hasBomb(){
        if (this.bomb == null) return false;
        else return true;
    }
    public void setBlocked(boolean B){
        this.block = B;
    }
    public boolean isBlocked(){
        return this.block;
    }
    public boolean isObstructed(){
        if(this.block || this.hasBomb() || this.hasPlayer() || this.hasFire() || this.hasBarrel())
            return true;
        else
            return false;
    }
    public void addPlayer(Player p){
        this.player = p;
    }
    public Player getPlayer(){
        return this.player;
    }
    public void removePlayer(){
        this.player = null;
    }
    public boolean hasPlayer(){
        if(this.player == null) return false;
        else return true;
    }
    public void setFire(boolean B){
        if(B){
            firecount++;
            this.fire=true;
        }
        else{
            if (firecount>0) {
                firecount--;
            }
            if(firecount==0){
                this.fire = false;
            }
            //this.orangeFire=false;
        }
    }
    public boolean hasFire(){
        return this.fire;
    }
    public void cancelDet(){
        this.bomb.cancelDet();
    }
    public void setOrangeFire(boolean B){
        this.orangeFire=B;
    }
    public boolean hasOrangeFire(){
        return this.orangeFire;
    }
    public void clearCell(){
        this.player=null;
        if(this.bomb != null){
            cancelDet();
            this.bomb=null;
        }
        this.fire=false;
        this.orangeFire=false;
        this.barrel=false;
    }
    public boolean hasBarrel(){
        return barrel;
    }
    public void addBarrel(){
        this.barrel=true;
    }
    public void removeBarrel(){
        this.barrel=false;
    }
    public int getFireCount(){
        return firecount;
    }
}
