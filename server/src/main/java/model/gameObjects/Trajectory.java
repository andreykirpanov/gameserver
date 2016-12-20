package model.gameObjects;

import model.gameInfo.GameConstants;
import model.gameInfo.Location;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Max on 19.12.2016.
 */
public class Trajectory {
    private static final Logger log = LogManager.getLogger(Trajectory.class);
    private Cell cell;
    private Location destination;
    private int width;
    private int height;
    private boolean achieved;

    public Trajectory(Cell cell, Location destination,int w,int h){
        this.cell = cell;
        this.destination = destination;
        this.achieved = false;
        this.width = w;
        this.height = h;
    }

    public Location UpdateLocation() {
        //log.info("range " + cell.getLocation().range(destination));
        if (cell.getLocation().range(destination) < 30) {
            achieved = true;
            return destination;
        }
        double newX = cell.getLocation().getX() +
                3*Math.signum(destination.getX() - cell.getLocation().getX()) *
                        cell.getSpeed() / GameConstants.SERVER_FPS;
        double newY = cell.getLocation().getY() +
                3*Math.signum(destination.getY() - cell.getLocation().getY()) *
                        cell.getSpeed() / GameConstants.SERVER_FPS;
        if (newX - cell.getRadius() < 0 || newX + cell.getRadius() > width ||
                newY - cell.getRadius() < 0 || newY + cell.getRadius() > height) {
            achieved = true;
            return destination;
        } else {
            return new Location(newX, newY);
        }
    }
    public boolean isAchieved(){return achieved;}
}
