package model.gameInfo;

import accountServer.authentification.Authentification;
import main.ApplicationContext;
import matchmaker.MatchMaker;
import model.authInfo.Leader;
import model.gameInfo.utils.*;
import model.gameObjects.Cell;
import model.gameObjects.Food;
import model.gameObjects.GameField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
        for(Cell cell: player.getCells()){
            double newX = cell.getLocation().getX() +
                    Math.signum(dx - cell.getLocation().getX()) * Math.pow(dx - cell.getLocation().getX(), 2/3)*
                    cell.getSpeed() / GameConstants.SERVER_FPS;
            double newY = cell.getLocation().getY() +
                    Math.signum(dy - cell.getLocation().getY()) * Math.pow(dy - cell.getLocation().getY(), 2/3)*
                    cell.getSpeed() / GameConstants.SERVER_FPS;
            if(newX - cell.getRadius() < 0 || newX + cell.getRadius() > field.getWidth() ||
                    newY - cell.getRadius() < 0 || newY + cell.getRadius() > field.getHeight()){
                return;
            } else {
                cell.setNewLocation(new Location(newX, newY));
            }
        }
    }

    @Override
    public void tick() {
        for(Player player: players){
            move(player);
            checkFoodCollisions(player);
            checkCellCollisions(player);
        }
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
