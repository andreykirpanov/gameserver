package model.gameInfo;

/**
 * Created by User on 10.10.2016.
 */
public class Location {
    private double x;
    private double y;

    public Location(double x, double y){
        setLocation(x,y);
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    //TODO: check whether x and y are set correctly
    public void setLocation(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double range(Location location){
        double dx = this.x-location.getX();
        double dy = this.y-location.getY();
        return Math.sqrt(dx*dx+dy*dy);
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x + " " +
                "y=" + y +
                '}';
    }


}
