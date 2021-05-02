package map;

public class Room {
    int posX;
    int posY;
    // ^^^ position of origin (upper left corner)
    int sizeX;
    int sizeY;

    public Room(int posX, int posY, int sizeX, int sizeY) {
        this.posX = posX;
        this.posY = posY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }
}
