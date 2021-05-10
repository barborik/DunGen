package map;

import element.Element;
import util.Texture;

import java.util.ArrayList;

public class Map {
    public static Texture tileset;
    public static int mapX, mapY, wallSize;
    public static int[] map;
    public static int[] floorMap;
    public static int[] ceilingMap;
    public static ArrayList<Element> elements = new ArrayList<>();
    public MapGenerator mapGenerator;
    public Room firstRoom;
    public Room lastRoom;

    public Map() {
        tileset = new Texture("tile_set.bmp");
        wallSize = tileset.textureHeight;
        mapGenerator = new MapGenerator(this);
        mapGenerator.generate();
    }
}
