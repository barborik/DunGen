package map;

import element.Element;
import element.entity.Duke;
import element.entity.Entity;
import element.entity.Troll;
import util.Texture;

import java.util.ArrayList;

public class Map {
    public static Texture tileset;
    public MapGenerator mapGenerator;
    public static int mapX = 8, mapY = 8, wallSize;
    public static int[] map = {
            4, 4, 4, 4, 2, 3, 4, 4,
            4, 0, 0, 4, 0, 0, 0, 4,
            4, 0, 0, 4, 0, 0, 0, 4,
            4, 0, 0, 0, 0, 0, 0, 4,
            4, 4, 4, 4, 0, 0, 0, 4,
            8, 0, 0, 0, 0, 0, 0, 4,
            7, 0, 0, 0, 0, 0, 0, 4,
            4, 6, 4, 1, 1, 1, 1, 4,
    };
    public static ArrayList<Element> elements = new ArrayList<>();

    public Map() {
        tileset = new Texture("tile_set.bmp");
        wallSize = tileset.textureHeight;
        /*mapGenerator = new MapGenerator(0L);
        mapGenerator.generateMap();*/

        elements.add(new Duke(300, 200, 0));
        elements.add(new Troll(300, 400, 0));
    }
}
