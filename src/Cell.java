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

    //--------------------------------------------------------------------------
    public Cell(int x, int y){

        this.row = x;
        this.column = y;
    }
    /* Copy old cell into new cell, for display in the client*/
    public static void copyCell(Cell newCell, Cell oldCell){
        if(oldCell.hasBomb()){
            newCell.setBomb();
        }else{
            newCell.clearBomb();
        }

        if(oldCell.isBlocked()){
            newCell.setBlocked(true);
        }else{
            newCell.setBlocked(false);
        }

        if(oldCell.hasPlayer()){
            newCell.addPlayer(oldCell.getPlayer());
        }else{
            newCell.removePlayer();
        }

        if(oldCell.hasFire()){
            newCell.setFire(true);
        }else{
            newCell.setFire(false);
        }

        if(oldCell.hasOrangeFire()){
            newCell.setOrangeFire(true);
        }else{
            newCell.setOrangeFire(false);
        }

        if(oldCell.hasBarrel()){
            newCell.addBarrel();
        }else{
            newCell.removeBarrel();
        }
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
    public void setBomb(){
        this.bomb = new Bomb();
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
        if(this.isBlocked()
        || this.hasBomb()
        || this.hasPlayer()
        || this.hasFire()
        || this.hasBarrel())
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
