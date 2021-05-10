package element.entity;

import util.Texture;

public class Duke extends Entity {
    public Duke(float posX, float posY) {
        super(posX, posY);
        super.setHealth(20);
        super.speed = 100;
        this.sizeX = 0.6f;
        super.sprite = new Texture("duke.bmp");
    }
}
