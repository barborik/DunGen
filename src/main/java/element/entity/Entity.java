package element.entity;

public class Entity {
    protected float posX, posY, viewAngle = 0;
    protected int health;

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

    public Entity() {
    }

    public Entity(float posX, float posY, float viewAngle) {
        this.posX = posX;
        this.posY = posY;
        this.viewAngle = viewAngle;
    }
}
