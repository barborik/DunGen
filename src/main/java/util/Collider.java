package util;

import element.entity.Entity;
import map.Map;

public class Collider {
    Entity entity;

    private int getOperator(double num) {
        if (num > 0) return 1;
        if (num < 0) return -1;
        return 0;
    }

    public void checkCollision(float moveXOff, float moveYOff) {
        try {
            int gridPosX = (int) (entity.getPosX() / Map.wallSize), gridMoveX = (int) ((entity.getPosX() + entity.getSize() * getOperator(moveXOff)) / Map.wallSize);
            int gridPosY = (int) (entity.getPosY() / Map.wallSize), gridMoveY = (int) ((entity.getPosY() + entity.getSize() * getOperator(moveYOff)) / Map.wallSize);

            if (Map.map[gridPosY * Map.mapX + gridMoveX] == 0) {
                entity.setPosX(entity.getPosX() + moveXOff);
            }
            if (Map.map[gridMoveY * Map.mapX + gridPosX] == 0) {
                entity.setPosY(entity.getPosY() + moveYOff);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    public Collider(Entity entity) {
        this.entity = entity;
    }
}
