package map;

import java.util.Random;

public class Room {
    private final Random rand;
    public int x1, y1, x2, y2;

    public void add() {
        for (int row = y1; row < y2; row++) {
            for (int column = x1; column < x2; column++) {
                int wallTextureIndex = rand.nextInt(8) + 1;
                if (row == y1 || row == y2 - 1) {
                    Map.map[row * Map.mapX + column] = wallTextureIndex;
                } else if (column == 0 || column == x2 - 1) {
                    Map.map[row * Map.mapX + column] = wallTextureIndex;
                }
            }
        }
    }

    public void addObstacles() {
        for (int row = y1; row < y2; row++) {
            for (int column = x1; column < x2; column++) {
                int wallTextureIndex = rand.nextInt(8) + 1;
                if (rand.nextFloat() < 1 / 3f) { // 33% chance
                    Map.map[row * Map.mapX + column] = wallTextureIndex;
                }
            }
        }
    }

    public int[] getEmptyCell() {
        int randX, randY;
        do {
            randX = rand.nextInt(Map.mapX);
            randY = rand.nextInt(Map.mapY);
        } while (Map.map[randY * Map.mapX + randX] != 0);
        return new int[]{randX, randY};
    }

    public Room(Random rand, int x1, int y1, int x2, int y2) {
        this.rand = rand;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
}
