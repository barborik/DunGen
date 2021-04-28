package element.entity;

import element.Element;
import util.Collider;

public class Entity extends Element {
    public Collider collider;
    public int speed;
    protected double viewAngle;
    protected int health;

    public double getViewAngle() {
        return viewAngle;
    }

    public void setViewAngle(double viewAngle) {
        this.viewAngle = viewAngle;
        if (this.viewAngle > 2 * Math.PI) this.viewAngle -= 2 * Math.PI;
        if (this.viewAngle < 0) this.viewAngle += 2 * Math.PI;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Entity() {
    }

    public Entity(double posX, double posY, double viewAngle) {
        super(posX, posY);
        this.viewAngle = viewAngle;
        this.collider = new Collider(this);
    }
}
