package element.entity;

public class Player extends Entity {
    private double viewAngle;

    public double getViewAngle() {
        return viewAngle;
    }

    public void setViewAngle(double viewAngle) {
        this.viewAngle = viewAngle;
        if (this.viewAngle > 2 * Math.PI) this.viewAngle -= 2 * Math.PI;
        if (this.viewAngle < 0) this.viewAngle += 2 * Math.PI;
    }

    public Player(double posX, double posY, double viewAngle) {
        super(posX, posY);
        this.viewAngle = viewAngle;
        super.setHealth(100);
        super.speed = 200;
        super.sizeX = 0.3f;
    }
}
