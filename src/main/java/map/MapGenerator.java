package map;

import java.util.Random;

public class MapGenerator {
    private final Random rand;
    public static long seed;

    public void generateMap() {
    }

    public MapGenerator(long seed) {
        MapGenerator.seed = seed;
        rand = new Random(seed);
    }

    public MapGenerator() {
        Random randSeed = new Random();
        seed = randSeed.nextLong();
        rand = new Random(seed);
    }
}
