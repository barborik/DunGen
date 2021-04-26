import util.Texture;

public class Map {
    Texture tileset;
    public int mapX = 8, mapY = 8, wallSize = 64;
    public int[] map = {
            4, 4, 4, 4, 2, 3, 4, 4,
            4, 0, 0, 4, 0, 0, 0, 4,
            4, 0, 0, 4, 0, 0, 0, 5,
            4, 0, 0, 0, 0, 0, 0, 5,
            4, 4, 4, 4, 0, 0, 0, 5,
            2, 0, 0, 0, 0, 0, 0, 3,
            2, 0, 0, 0, 0, 0, 0, 3,
            4, 5, 4, 1, 1, 1, 1, 4,
    };

    public Map() {
        tileset = new Texture("tile_set.bmp");
    }
}
