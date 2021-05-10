package element.entity;

import util.Texture;

public class Troll extends Entity {
    public Troll(float posX, float posY) {
        super(posX, posY);
        super.setHealth(20);
        super.speed = 100;
        super.sizeX = 0.5f;
        super.sizeY = 0.5f;
        super.sprite = new Texture("troll.bmp");
    }
}
