package element.entity;

import util.Collider;

public class Entity {
    public Collider collider;
    protected float posX, posY, viewAngle = 0;
    protected int health, speed, size;

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public float getViewAngle() {
        return viewAngle;
    }

    public int getHealth() {
        return health;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public void setViewAngle(float viewAngle) {
        this.viewAngle = viewAngle;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Entity() {
    }

    public Entity(float posX, float posY, float viewAngle) {
        this.posX = posX;
        this.posY = posY;
        this.viewAngle = viewAngle;
        this.collider = new Collider(this);
    }
}
