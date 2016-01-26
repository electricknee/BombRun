public class BoardArray{
    /*  Conversions
        'e' empty
        'q' dead player
        'a' player 1
        'b' player 2
        'c' player 3
        'd' player 4
        'v' bomb
        'f' fire
        'o' orangeFire
        'n' barrel
        'm' block
    */

    /* Does not create any new objects, space must already be allocated */
    public static void convertBoardtoArray(Board board, char[] bArray){

        for(int i=0 ; i < board.getBoardSize() ; i++){
            Cell cp = board.getCell(i);

            if(cp.isBlocked()){
                bArray[i] = 'm';

            }else if(cp.hasBomb()){
                bArray[i] = 'v';

            }else if(cp.hasBarrel()){
                bArray[i] = 'n';

            }else if(cp.hasOrangeFire()){
                bArray[i] = 'o';

            }else if(cp.hasFire()){
                bArray[i] = 'f';


            }else if(cp.hasPlayer()){
                switch(cp.getPlayer().getIdentity()){
                    case(0):    bArray[i] = 'q'; break;
                    case(1):    bArray[i] = 'a'; break;
                    case(2):    bArray[i] = 'b'; break;
                    case(3):    bArray[i] = 'c'; break;
                    case(4):    bArray[i] = 'd'; break;
                }
            }else{
                bArray[i] = 'e';
            }
        }
        return;
    }

    /* Does not create any new objects, space must already be allocated */
    public static void writeArraytoBoard(char[] Arr, Board board){

        for(int i=0 ; i < board.getBoardSize() ; i++){
            board.getCell(i).clearCell();   // remove all old objects

            switch(Arr[i]){
                case('q'):  board.addPlayer(i,new Player(0));   break;
                case('a'):  board.addPlayer(i,new Player(1));   break;
                case('b'):  board.addPlayer(i,new Player(2));   break;
                case('c'):  board.addPlayer(i,new Player(3));   break;
                case('d'):  board.addPlayer(i,new Player(4));   break;
                case('e'):  board.getCell(i).clearCell();       break;
                case('v'):  board.getCell(i).setBomb();         break;
                case('o'):  board.setOrangeFire(i,true);        break;
                case('f'):  board.setFire(i);                   break;
                case('m'):  board.blockCell(i);                 break;
                case('n'):  board.getCell(i).addBarrel();       break;
            }
        }
        return;
    }
}
