package element.entity;

import util.Texture;

public class Duke extends Entity {
    public Duke(float posX, float posY, float viewAngle) {
        super(posX, posY, viewAngle);
        super.setHealth(20);
        super.speed = 100;
        super.sprite = new Texture("duke.bmp");
    }
}
