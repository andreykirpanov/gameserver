package model.gameInfo;

import accountServer.authentification.Authentification;
import main.ApplicationContext;
import matchmaker.MatchMaker;
import model.authInfo.Leader;
import model.gameInfo.utils.*;
import model.gameObjects.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static model.gameInfo.GameConstants.MIN_MASS_FOR_ABILITIES;
import static model.gameInfo.GameConstants.EJECTED_DISTANCE;
import static model.gameInfo.GameConstants.EJECT_LOSE_MASS;
import static model.gameInfo.GameConstants.EJECTED_SPEED;
import static model.gameInfo.GameConstants.MAX_FOOD_ON_FIELD;

/**
 * @author apomosov
 */
public class GameSessionImpl implements GameSession {
    private final static Logger log = LogManager.getLogger(GameSessionImpl.class);
    private static final SequentialIDGenerator idGenerator = new SequentialIDGenerator();
    private final long id = idGenerator.next();
    @NotNull
    private final GameField field;
    @NotNull
    private final List<Player> players = new ArrayList<>();
    @NotNull
    private final FoodGenerator foodGenerator;
    @NotNull
    private final PlayerPlacer playerPlacer;
    @NotNull
    private final VirusGenerator virusGenerator;
    @NotNull
    private ConcurrentHashMap<PlayerCell,Trajectory> splited = new ConcurrentHashMap<>();

    public GameSessionImpl(@NotNull FoodGenerator foodGenerator, @NotNull PlayerPlacer playerPlacer, @NotNull VirusGenerator virusGenerator,
                           @NotNull GameField gameField) {
        this.foodGenerator = foodGenerator;
        this.playerPlacer = playerPlacer;
        this.virusGenerator = virusGenerator;
        field = gameField;
        //virusGenerator.generate();
        Thread foodGenerationTask = new Thread(foodGenerator);
        foodGenerationTask.start();
    }

    @Override
    public void offerNewLocation(Player player, double dx, double dy){
        if(player.cellsCount() < 2) {
            for (Cell cell : player.getCells()) {
                double newX = cell.getLocation().getX() +
                        Math.signum(dx - cell.getLocation().getX()) * Math.pow(dx - cell.getLocation().getX(), 2 / 3) *
                                cell.getSpeed() / GameConstants.SERVER_FPS;
                double newY = cell.getLocation().getY() +
                        Math.signum(dy - cell.getLocation().getY()) * Math.pow(dy - cell.getLocation().getY(), 2 / 3) *
                                cell.getSpeed() / GameConstants.SERVER_FPS;
                if (newX - cell.getRadius() < 0 || newX + cell.getRadius() > field.getWidth() ||
                        newY - cell.getRadius() < 0 || newY + cell.getRadius() > field.getHeight()) {
                    return;
                } else {
                    cell.setNewLocation(new Location(newX, newY));
                }
            }
        } else {
            Location[] newLocations = new Location[player.cellsCount()];
            int i=0;
            for (Cell cell : player.getCells()) {
                if (splited.containsKey(cell)) {
                    newLocations[i] = splited.get(cell).UpdateLocation();
                    i++;
                    if (splited.get(cell).isAchieved()) {
                        splited.remove(cell);
                        //log.info("####achieved");
                    }
                } else {
                    double newX = cell.getLocation().getX() +
                            Math.signum(dx - cell.getLocation().getX()) * Math.pow(dx - cell.getLocation().getX(), 2 / 3) *
                                    cell.getSpeed() / GameConstants.SERVER_FPS;
                    double newY = cell.getLocation().getY() +
                            Math.signum(dy - cell.getLocation().getY()) * Math.pow(dy - cell.getLocation().getY(), 2 / 3) *
                                    cell.getSpeed() / GameConstants.SERVER_FPS;
                    if (newX - cell.getRadius() < 0 || newX + cell.getRadius() > field.getWidth() ||
                            newY - cell.getRadius() < 0 || newY + cell.getRadius() > field.getHeight()) {
                        return;
                    } else {
                        //cell.setLocation(new Location(newX,newY));
                        newLocations[i] = new Location(newX, newY);
                        i++;
                    }
                }
            }
            for(i=0; i < player.cellsCount()-1; i++) {
                for (int j = i + 1; j < player.cellsCount(); j++) {
                    double size1 = 10*player.getCells().get(i).getRadius();
                    double size2 = 10*player.getCells().get(j).getRadius();
                    if (newLocations[i].range(newLocations[j]) < (size1 + size2) ) {
                        int signX = (int) Math.signum(newLocations[j].getX() - newLocations[i].getX());
                        int signY = (int) Math.signum(newLocations[j].getY() - newLocations[i].getY());
                        double avgX = newLocations[i].getX() + (newLocations[j].getX() - newLocations[i].getX()) * size1 / (size1 + size2);
                        double avgY = newLocations[i].getY() + (newLocations[j].getY() - newLocations[i].getY()) * size1 / (size1 + size2);
                        newLocations[i].setLocation(avgX - signX * size1 , avgY - signY * size1 );
                        newLocations[j].setLocation(avgX + signX * size2 , avgY + signY * size2 );
                    }
                }
            }
            for(i = 0; i < newLocations.length; i++){
                player.getCells().get(i).setNewLocation(newLocations[i]);
            }
        }
    }

    @Override
    public void split(Player player, double dx,double dy){
        List<Integer> indexes= new ArrayList<>();
        int last = player.getCells().size();
        int i = 0;
        for(Cell cell : player.getCells()){
            if( cell.getMass() > MIN_MASS_FOR_ABILITIES){
                indexes.add(i);
                i++;
            }
        }
        i = 0;
        for(Integer index: indexes) {
            PlayerCell pc = new PlayerCell(last + i,player.getCells().get(index).getLocation(),player.getName());
            player.getCells().add(pc);
            splited.put(pc,new Trajectory(pc,new Location(dx,dy),field.getWidth(),field.getHeight()));
            i++;
        }

    }

    @Override
    public void tick() {
        for(Player player: players){
            move(player);
            checkFoodCollisions(player);
            checkCellCollisions(player);
            updateScore(player);
        }
    }

    public void updateScore(Player player){
        int score = 0;
        for(Cell cell: player.getCells()){
            score += cell.getMass() / 10;
        }
        player.setPts(score);
    }

    private void move(Player player){
        for(Cell cell: player.getCells()){
            cell.setLocation(cell.getNewLocation());
        }
    }

    private void checkFoodCollisions(Player player){
        for(Cell cell: player.getCells()) {
            double lowerX = cell.getLocation().getX() - cell.getRadius() * 10;
            double upperX = cell.getLocation().getX() + cell.getRadius() * 10;
            double lowerY = cell.getLocation().getY() - cell.getRadius() * 10;
            double upperY = cell.getLocation().getY() + cell.getRadius() * 10;
            for(Food food: field.getFoods()){
                double x = food.getLocation().getX();
                double y = food.getLocation().getY();
                if( x > lowerX && x < upperX && y > lowerY && y < upperY){
                    cell.setMass(cell.getMass() + GameConstants.FOOD_MASS);
                    field.getFoodsToRemove().add(food);
                    field.getFoods().remove(food);
                }
            }
        }
    }

    private void checkCellCollisions(Player player){
        for(Cell cell: player.getCells()){
            double lowerX = cell.getLocation().getX() - cell.getRadius() * 10;
            double upperX = cell.getLocation().getX() + cell.getRadius() * 10;
            double lowerY = cell.getLocation().getY() - cell.getRadius() * 10;
            double upperY = cell.getLocation().getY() + cell.getRadius() * 10;
            for(Player currentPlayer: getPlayers()){
                if(currentPlayer.equals(player)){
                    break;
                }
                for(Cell currentCell: currentPlayer.getCells()){
                    double curLowerX = currentCell.getLocation().getX() - cell.getRadius() * 10;
                    double curUpperX = currentCell.getLocation().getX() + cell.getRadius() * 10;
                    double curLowerY = currentCell.getLocation().getY() - cell.getRadius() * 10;
                    double curUpperY = currentCell.getLocation().getY() + cell.getRadius() * 10;
                    if(((lowerX < curLowerX && curLowerX < upperX) || (lowerX < curUpperX && curUpperX < upperX))
                            && ((lowerY < curLowerY && curLowerY < upperY) || (lowerY < curUpperY && curUpperY < upperY))){
                        if((cell.getMass() < currentCell.getMass() && currentCell.getMass() < cell.getMass() * 1.5) ||
                                (currentCell.getMass() < cell.getMass() && cell.getMass() < currentCell.getMass()*1.5)){
                            //TODO: lock movements of both cells towards each other
                        } else {
                            if(cell.getMass() < currentCell.getMass()){
                                eatCell(cell, currentCell);
                            } else {
                                eatCell(currentCell, cell);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void eatCell(Cell small, Cell big){
        double smallX = small.getLocation().getX();
        double smallY = small.getLocation().getY();
        double bigX = big.getLocation().getX();
        double bigY = big.getLocation().getY();
        double dx;
        double dy;
        int scale = GameConstants.SCALE;
        if(smallX < bigX){
            dx = (smallX + small.getRadius() * scale) - (bigX - big.getRadius() * scale);
        } else {
            dx = (bigX + big.getRadius() * scale) - (smallX - small.getRadius() * scale);
        }
        if(smallY < bigY){
            dy = (smallY + small.getRadius() * scale) - (bigY - big.getRadius() * scale);
        } else {
            dy = (bigY + big.getRadius() * scale) - (smallY - small.getRadius() * scale);
        }
        if(dx < 0 || dy < 0){
            return;
        }
        if(dy * dx / scale / scale / Math.pow(2 * small.getRadius(), 2 ) > 1){
            big.setMass(big.getMass() + small.getMass());
            small.setMass(0);
        } else {
            big.setMass(big.getMass() + (int) (dy * dx / scale / scale / Math.pow(2 * small.getRadius(), 2) * small.getMass()));
            small.setMass(small.getMass() - (int) (dy * dx / scale / scale / Math.pow(2 * small.getRadius(), 2) * small.getMass()));
        }
    }

    @Override
    public void join(@NotNull Player player) {
        players.add(player);
        this.playerPlacer.place(player);
    }

    @Override
    public void leave(@NotNull Player player) {
        log.info("Player " + player.getName() + " leaved");
        ApplicationContext.get(MatchMaker.class).removePlayerSession(player.getId());
        Authentification.LB.updateScore(player.getId(),0);
        Authentification.tokenDAO.delete(Authentification.tokenDAO.getTokenByUserId(player.getId()));
        players.remove(player);
    }

    @Override
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    @Override
    public GameField getField() {
        return field;
    }

    @Override
    public String toString() {
        return "GameSessionImpl{" +
                "id=" + id +
                '}';
    }
}
