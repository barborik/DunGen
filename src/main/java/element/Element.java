package element;

import util.Texture;

public class Element {
    public Texture sprite;
    public double posX, posY;
    public float size = 1f;

    public Element() {
    }

    public Element(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
    }
}
