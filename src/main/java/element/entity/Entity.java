package element.entity;

import element.Element;
import util.Collider;

public class Entity extends Element {
    public Collider collider;
    public int speed;
    protected int health;

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Entity() {
    }

    public Entity(double posX, double posY) {
        super(posX, posY);
        this.collider = new Collider(this);
    }
}
