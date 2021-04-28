package element.entity;

import util.Texture;

public class Troll extends Entity {
    public Troll(float posX, float posY, float viewAngle) {
        super(posX, posY, viewAngle);
        super.setHealth(20);
        super.speed = 100;
        super.size = 0.5f;
        super.sprite = new Texture("troll.bmp");
    }
}
