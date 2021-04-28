package element.entity;

public class Player extends Entity {
    public Player(double posX, double posY, double viewAngle) {
        super(posX, posY, viewAngle);
        super.setHealth(100);
        super.speed = 200;
        super.size = 0.1f;
    }
}
