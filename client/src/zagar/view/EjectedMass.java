package zagar.view;

import org.jetbrains.annotations.NotNull;
import zagar.Game;
import zagar.GameConstants;

import java.awt.*;

/**
 * Created by Max on 19.12.2016.
 */
public class EjectedMass {
    public double x, y;
    public float size;
    private int r=0, g=0, b=255;
    @NotNull
    public float sizeRender;
    public double xRender;
    public double yRender;
    public int mass;
    private float rotation = 0;

    public EjectedMass(double x, double y) {
        this.x = x;
        this.y = y;
        this.size = (float)Math.sqrt(GameConstants.EJECT_PIECE_MASS/Math.PI);
        this.xRender = this.x;
        this.yRender = this.y;
        this.sizeRender = this.size;
    }

    @Override
    public int hashCode(){
        return (int)(x + y * 13);
    }

    public void tick() {
        this.xRender -= (this.xRender - x) / 5f;
        this.yRender -= (this.yRender - y) / 5f;
        this.sizeRender -= (this.sizeRender - size) / 9f;
        this.mass = GameConstants.EJECT_PIECE_MASS;
        this.rotation += (1f / (Math.max(this.mass, 20) * 2));
    }

    public void render(@NotNull Graphics2D g, float scale) {
        if (Game.player.size() > 0){
            int size = (int) ((this.sizeRender * 2f * scale) * Game.zoom);

            float avgX = 0;
            float avgY = 0;

            for (Cell c : Game.player) {
                if (c != null) {
                    avgX += c.xRender;
                    avgY += c.yRender;
                }
            }
            Color color = new Color(this.r, this.g, this.b);
            g.setColor(color);


            avgX /= Game.player.size();
            avgY /= Game.player.size();

            int x = (int) (((this.xRender - avgX) * Game.zoom) + GameFrame.size.width / 2 - size / 2);
            int y = (int) (((this.yRender - avgY) * Game.zoom) + GameFrame.size.height / 2 - size / 2);

            if (x < -size - 30 || x > GameFrame.size.width + 30 || y < -size - 30 || y > GameFrame.size.height + 30) {
                return;
            }

            int massRender = GameConstants.EJECT_PIECE_MASS;
            Polygon hexagon = new Polygon();
            int a = massRender / 20 + 5;
            a = Math.min(a, 50);
            for (int i = 0; i < a; i++) {
                float pi = 3.14f;
                int pointX = (int) ((x + (size / 2) * Math.cos(rotation + i * 2 * pi / a)) + size / 2);
                int pointY = (int) ((y + (size / 2) * Math.sin(rotation + i * 2 * pi / a)) + size / 2);
                hexagon.addPoint(pointX, pointY);
            }
            g.fillPolygon(hexagon);

        }
    }



    @Override
    public String toString() {
        return "EjectedMass{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
