package element;

import util.Texture;

public class Element {
    public Texture sprite;
    public double posX, posY;
    public float sizeX;
    public float sizeY;

    public Element() {
    }

    public Element(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
        this.sizeX = 1f;
        this.sizeY = 1f;
    }
}
