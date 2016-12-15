package protocol.model;

/**
 * @author apomosov
 */
public final class Food extends Cell {

  public Food(int cellid,int size,int x,int y){
    super(cellid,-1,false,size,x,y);
  }
}
