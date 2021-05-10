package map;

import element.entity.Duke;
import element.entity.Troll;

import java.util.Arrays;
import java.util.Random;

public class MapGenerator {
    public Map map;
    public static Long seed;
    public Random rand;

    public void generate() {
        Map.mapX = rand.nextInt(31) + 20;
        Map.mapY = rand.nextInt(31) + 20;
        Map.map = new int[Map.mapX * Map.mapY];

        generateRoom();
        generateFloor();
        generateCeiling();
    }

    public void generateRoom() {
        Room room = new Room(rand, 0, 0, Map.mapX, Map.mapY);
        map.firstRoom = room;
        room.add();
        room.addObstacles();
        generateElements(room);
    }

    public void generateFloor() {
        Map.floorMap = new int[Map.mapX * Map.mapY];
        for (int mapIndex = 0; mapIndex < Map.floorMap.length; mapIndex++) {
            Map.floorMap[mapIndex] = rand.nextInt(8) + 1;
        }
    }

    public void generateCeiling() {
        Map.ceilingMap = new int[Map.mapX * Map.mapY];
        for (int mapIndex = 0; mapIndex < Map.ceilingMap.length; mapIndex++) {
            Map.ceilingMap[mapIndex] = rand.nextInt(8) + 1;
        }
    }

    public void generateElements(Room room) {
        for (int i = 0; i < rand.nextInt(16) + 10; i++) {
            int[] emptyCell = room.getEmptyCell();
            if (rand.nextBoolean()) {
                Map.elements.add(new Duke(emptyCell[0] * Map.wallSize + Map.wallSize / 2f, emptyCell[1] * Map.wallSize + Map.wallSize / 2f));
            } else {
                Map.elements.add(new Troll(emptyCell[0] * Map.wallSize + Map.wallSize / 2f, emptyCell[1] * Map.wallSize + Map.wallSize / 2f));
            }
        }
    }

    public MapGenerator(Map map, long seed) {
        this.map = map;
        MapGenerator.seed = seed;
        rand = new Random(MapGenerator.seed);
    }

    public MapGenerator(Map map) {
        this.map = map;
        if (seed == null) {
            Random seedGenerator = new Random();
            seed = seedGenerator.nextLong();
        }
        rand = new Random(seed);
    }
}
