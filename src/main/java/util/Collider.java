package util;

import element.entity.Entity;
import map.Map;

public class Collider {
    private final Entity entity;

    private int getOperator(double num) {
        if (num > 0) return 1;
        if (num < 0) return -1;
        return 0;
    }

    public void checkCollision(double moveXOff, double moveYOff) {
        try {
            int gridPosX = (int) (entity.posX / Map.wallSize), gridMoveX = (int) ((entity.posX + entity.sizeX / 2 * Map.wallSize * getOperator(moveXOff)) / Map.wallSize);
            int gridPosY = (int) (entity.posY / Map.wallSize), gridMoveY = (int) ((entity.posY + entity.sizeX / 2 * Map.wallSize * getOperator(moveYOff)) / Map.wallSize);

            if (Map.map[gridPosY * Map.mapX + gridMoveX] == 0) entity.posX += moveXOff;
            if (Map.map[gridMoveY * Map.mapX + gridPosX] == 0) entity.posY += moveYOff;
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    public Collider(Entity entity) {
        this.entity = entity;
    }
}
