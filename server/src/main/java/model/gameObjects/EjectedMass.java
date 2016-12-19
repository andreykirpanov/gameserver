package model.gameObjects;

import model.gameInfo.GameConstants;
import model.gameInfo.Location;

import java.rmi.registry.LocateRegistry;

/**
 * Created by Max on 19.12.2016.
 */
public class EjectedMass extends Cell {

    private Location origin;
    private Location distination;

    public EjectedMass(Location origin, Location distination){
        super(origin, GameConstants.EJECT_PIECE_MASS);
        this.origin = origin;
        this.distination = distination;
    }

    public Location getDistination(){return distination;}

}
