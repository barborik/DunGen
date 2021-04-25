package element.entity;

public class Player extends Entity {
    public Player(float posX, float posY, float viewAngle) {
        super(posX, posY, viewAngle);
        super.setHealth(100);
    }
}
