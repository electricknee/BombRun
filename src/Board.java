import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Graphics2D;
/**
 * Created by zakary on 6/28/15.
 */
public class Board extends JComponent implements java.io.Serializable{

    public int rowSize = 20;
    public int boardSize = rowSize*rowSize;
    private int cellSize = 50;
    private Cell[] boardCells;
    private int player1Index=0;
    private int player2Index=0;
//-------------------------------------------------------------------------------------
    public void printPlayers(){
        System.out.println("Player indexes");
        System.out.println(player1Index);
        System.out.println(player2Index);
    }
    public void printBoard(){
        System.out.println("Board Printing");
        for(int i=0;i<boardSize;i++){
            System.out.print("[");
            System.out.print(i);
            System.out.print("] ");

            Cell cp = boardCells[i];
            if(cp.isBlocked()){
                System.out.print("Block ");
            }
            if(cp.hasBomb()){
                System.out.print("Bomb ");
            }
            if(cp.hasBarrel()){
                System.out.print("Barrel ");
            }
            if(cp.hasPlayer()){
                System.out.print("Player ");
            }
            if(cp.hasFire()){
                System.out.print("Fire ");
            }
            if(cp.hasOrangeFire()){
                System.out.print("Orange Fire ");
            }
            System.out.print("\n");
        }
    }
    public int getPlayerIndex(int ID){
            if(ID==1)
                return player1Index;
            else if(ID==2)
                return player2Index;

        return 0; // should not trip
    }
    public void setPlayerIndex(int Index,int ID){
            if(ID==1)
                player1Index=Index;
            else if(ID==2)
                player2Index=Index;
    }
    public int getRowSize(){
        return this.rowSize;
    }
    public Board(int rs,boolean withBarrels){
        this.rowSize = rs;
        this.boardSize = rs*rs;

        boardCells = new Cell[boardSize];
        // create all cell in an array
        int count = 0;
        for(int x=1; x < rowSize+1; x++) {
            for (int y = 1; y < rowSize + 1; y++) {
                boardCells[count] = new Cell(x, y);
                count++;
            }
        }
        ResetBoard(withBarrels);
    }

    public void paint(Graphics g){

        int row=1;
        int x=1;
        for (int col=x; x<boardSize+1; x++){
            int index = (row-1)*rowSize+(col-1); // calculate the current index
            Cell cell = boardCells[index];
            g.setColor(new Color(169, 173, 185));
            g.fillRect(col*cellSize,row*cellSize,cellSize,cellSize);
            // Mark the blocked Squares
            if (cell.isBlocked()){
                g.setColor(new Color(54,54,54));
                g.fillRect(col*cellSize,row*cellSize,cellSize,cellSize);

            }
            else if(cell.hasBarrel()){
                drawBarrel(g,col*cellSize,row*cellSize);
                //g.setColor(Color.GRAY);
                //g.fillRect(col*cellSize,row*cellSize,cellSize,cellSize);
            }
            else if(cell.hasOrangeFire()){
                g.setColor(Color.ORANGE);
                g.fillRect(col*cellSize,row*cellSize,cellSize,cellSize);
            }
            else if(cell.hasFire()){
                g.setColor(Color.RED);
                g.fillRect(col*cellSize,row*cellSize,cellSize,cellSize);
            }

            else if(cell.hasPlayer()){
                if(cell.getPlayer().isDead()) {
                    g.setColor(Color.lightGray);
                }
                else{ // player not dead
                    int currentID = cell.getPlayer().getIdentity();
                    if(currentID==1){
                        g.setColor(Color.MAGENTA);
                    }else if(currentID==2){
                        g.setColor(Color.blue);
                    }

                }
                g.fillRect(col * cellSize + 4, row * cellSize + 4, cellSize - 8, cellSize - 8);
            }
            else if(cell.hasBomb()){
                drawBomb(g,col*cellSize+cellSize/2,row*cellSize+cellSize/2,cellSize/4,Color.black);
            }
            // Draw the black frame for each square
            g.setColor(Color.black);
            g.drawRect(col*cellSize,row*cellSize,cellSize,cellSize);

            if (x%rowSize == 0){ // reset to new row
                row++;
                col=0;
            }
            col++;
        }


    }
    public void blockCell(int index){
        boardCells[index].setBlocked(true);
        this.repaint();
    }
    public void addPlayer(int index,Player p){
        boardCells[index].addPlayer(p);
        this.setPlayerIndex(index, p.getIdentity());
        this.repaint();
    }
    public boolean hasBomb(int index){
        if (boardCells[index].hasBomb()){
            return true;
        }
        else return false;
    }
    public void removePlayer(int index){
        boardCells[index].removePlayer();
    }
    public boolean movePlayer(int oldIndex,int dir) {
        if(boardCells[oldIndex].getPlayer().isDead()) return false;
        int newIndex = 0;

        switch (dir) {
            case 1:
                if (oldIndex < rowSize) return false;
                else {
                    newIndex = oldIndex - rowSize;
                    break;
                }
            case 2:
                if (oldIndex >= boardSize - rowSize) return false;
                else {
                    newIndex = oldIndex + rowSize;
                    break;
                }
            case 3:
                if ((oldIndex + 1) % rowSize == 0) return false;
                else {
                    newIndex = oldIndex + 1;
                    break;
                }
            case 4:
                if (oldIndex % rowSize == 0) return false;
                else {
                    newIndex = oldIndex - 1;
                    break;
                }
        }
        // check if the square is unoccupied
        if (boardCells[newIndex].isObstructed() ) { // checks if there is player bomb or wall in the way
            return false;
        }
        Player temp = boardCells[oldIndex].getPlayer();
        this.addPlayer(newIndex,temp);
        boardCells[oldIndex].removePlayer();
        this.repaint();

        this.setPlayerIndex(newIndex, temp.getIdentity());
        return true;

    }
    public void setBomb(int index,int time){
        boardCells[index].setBomb(index,time);
    }
    public void cleanBomb(int index){
        boardCells[index].clearBomb();
        repaint();
    }
    public void detonateHelper(boolean B,int centerIndex, int size){
            if(centerIndex < 0 || centerIndex >= boardSize) return;
            if (B) {
                this.setOrangeFire(centerIndex, true);
                //System.out.println("SetFire "+centerIndex);
            }else {
                this.setOrangeFire(centerIndex, false);
                //System.out.println("Clean Fire "+centerIndex);
            }
            if(boardCells[centerIndex].hasPlayer()){
                boardCells[centerIndex].getPlayer().setDead(true);
            }

        for(int i=1;i <= size;i++){ // right flare
            int currentIndex = centerIndex+i;
            if(currentIndex%rowSize==0) break;
            Cell cell = boardCells[currentIndex];
            if (cell.isBlocked()) break; // check for edge case or wall
            if (B) {
                if(cell.hasBarrel()){ // break barrel
                    cell.removeBarrel();
                    cell.setFire(true);
                    //System.out.println("SetFire "+currentIndex);
                    break;
                }
                //System.out.println("SetFire "+currentIndex);

                this.setFire(currentIndex); // set fire to square
                if (cell.hasPlayer()) {
                    cell.getPlayer().setDead(true);
                }
                if(cell.hasBomb()){ //chain bomb event
                    cell.cancelDet();
                    cell.clearBomb();
                    detonate(currentIndex,size);

                }
            }
            else {
                this.extinguishFire(currentIndex);
                //System.out.println("Clean fire " + currentIndex);
            }
        }
        for(int i=1;i <= size;i++){ // left flare
            int currentIndex = centerIndex-i;
            if((currentIndex+1)%rowSize==0) break;

            Cell cell = boardCells[currentIndex];
            if(cell.isBlocked()) break; // check for edge case
            if(B) {
                if(cell.hasBarrel()){
                    cell.removeBarrel();
                    cell.setFire(true);
                    //System.out.println("SetFire "+currentIndex);
                    break;
                }
                //System.out.println("SetFire "+currentIndex);

                this.setFire(currentIndex);
                if (cell.hasPlayer()) {
                    cell.getPlayer().setDead(true);
                }
                if(cell.hasBomb()){ //chain bomb event
                    cell.cancelDet();
                    cell.clearBomb();
                    detonate(currentIndex,size);

                }
            }
            else {
                this.extinguishFire(currentIndex);
                //System.out.println("Clean fire " + currentIndex);
            }
        }
        for(int i=1; i<=size;i++){ // top flare
            int currentIndex = centerIndex - (i*rowSize);
            if(currentIndex < 0) break;

            Cell cell = boardCells[currentIndex];
            if(cell.isBlocked()) break; // check for edge case
            if(B) {
                if(cell.hasBarrel()){
                    cell.removeBarrel();
                    this.setFire(currentIndex);
                    //System.out.println("SetFire "+currentIndex);

                    break;
                }
                //System.out.println("SetFire "+currentIndex);

                this.setFire(currentIndex);
                if (cell.hasPlayer()) {
                    cell.getPlayer().setDead(true);
                }
                if(cell.hasBomb()){ //chain bomb event
                    cell.cancelDet();
                    cell.clearBomb();
                    detonate(currentIndex,size);

                }
            }
            else {
                //System.out.println("Clean fire " + currentIndex);
                this.extinguishFire(currentIndex);
            }
        }
        for(int i=1;i<=size;i++){ // bottom flare
            int currentIndex = centerIndex + (i*rowSize);
            if(currentIndex >= boardSize) break;

            Cell cell = boardCells[currentIndex];
            if(cell.isBlocked()) break; // check for edge case
            if(B) {
                if(cell.hasBarrel()){
                    cell.removeBarrel();
                    cell.setFire(true);
                    //System.out.println("SetFire "+currentIndex);
                    break;
                }
                //System.out.println("SetFire "+currentIndex);

                this.setFire(currentIndex);
                if (cell.hasPlayer()) {
                    cell.getPlayer().setDead(true);
                }
                if(cell.hasBomb()){ //chain bomb event
                    cell.cancelDet();
                    cell.clearBomb();
                    detonate(currentIndex,size);

                }
            }
            else {
                this.extinguishFire(currentIndex);
                //System.out.println("Clean fire "+ currentIndex);
            }
        }
        this.repaint();
    }
    public void detonate(int centerIndex, int size){
        Timer timer = new Timer();
        timer.schedule(new extingushTask(centerIndex,size),300); // schedule extinguish in 1 second
        this.detonateHelper(true,centerIndex,size); // set the fire on

    }
    class extingushTask extends TimerTask {
        private int centerIndex, size;
        public extingushTask(int centerIndex, int size){
            this.centerIndex=centerIndex;
            this.size=size;
        }
        public void run(){
            Main.gameBoard.detonateHelper(false,centerIndex,size);
        }
    }
    public void setFire(int index){
        boardCells[index].setFire(true);
    }
    public void extinguishFire(int index){

        boardCells[index].setFire(false);
    }
    public void setOrangeFire(int index, boolean B){
        if(B) {
            boardCells[index].setOrangeFire(true);
        }
        else{
            boardCells[index].setOrangeFire(false);
        }
    }
    public Player getPlayer(int index){
        return boardCells[index].getPlayer();
    }
    public void ResetBoard(boolean withBarrels){
        for (int x=0;x<boardSize;x++){
            boardCells[x].clearCell();
        }
        this.addPlayer(0,new Player(1));
        this.addPlayer(boardSize-1,new Player(2));
        if(withBarrels){
            this.addRandomBarrels();
        }
        repaint();
    }
    public void addRandomBarrels(){
        Random random = new Random();
        for(int x=rowSize/2;x<boardSize-rowSize/2;x++){
            if(!boardCells[x].isBlocked()){
                int randInt = random.nextInt(99);
                if(randInt>50){
                    boardCells[x].addBarrel();
                }
            }
        }
    }
    public void printFire(){
        for(int x=0;x<boardSize;x++){
            if(boardCells[x].hasFire()){
                System.out.println("Firecount at "+x+" is "+boardCells[x].getFireCount());
            }
        }
    }
    private void drawBomb(Graphics g, int x, int y, int radius, Color color){
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.BLACK);
        Stroke temp = g2d.getStroke();
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLACK);
        g2d.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
        g2d.setColor(color);
        g.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);
        g.drawLine(x, y - radius, x, y - (radius * 4 / 3));
        g2d.setStroke(temp);
        g.setColor(Color.RED);
        g.fillOval(x-radius/3,y-(radius*3/2),radius/2,radius/2);

    }
    private void drawBarrel(Graphics g, int x, int y){
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(new Color(87,35,7));
        g.fillRect(x,y,cellSize,cellSize);
        g.setColor(new Color(130, 75,9));
        Stroke temp = ((Graphics2D) g).getStroke();
        ((Graphics2D) g).setStroke(new BasicStroke(4));
        g2d.drawLine(x,y,x+cellSize,y+cellSize);
        g2d.drawLine(x+cellSize,y,x,y+cellSize);
        g.drawRect(x,y,cellSize,cellSize);
        ((Graphics2D) g).setStroke(temp);
    }
}
