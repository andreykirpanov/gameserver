package model.gameObjects;

import model.Location;

/**
 * Created by User on 10.10.2016.
 */
public class SmallFood {
    public Location location;
    private int size;

    public SmallFood(Location location, int size){
        this.location = location;
        setSize(size);
    }


    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
