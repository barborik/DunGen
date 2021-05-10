import element.Element;
import element.entity.Player;
import map.Map;
import org.lwjgl.opengl.GL11;
import util.CameraUtil;

public class Camera {
    private final Player player;
    private final int[] depthBuffer;
    private double rayX, rayY, rayAngle, xOff, yOff, verticalDist, horizontalDist, finalDist, lineHeight, lineOff;
    private int mapIndex;

    public static int fov = 75;

    public void render() {
        castRays();
        drawSprites();
        //Debug.debugDrawing(player);
    }

    private void castRays() {
        // by default, java (java Math lib) handles angles clockwise (or I may just be stupid and made a mistake along the way)
        // that means that some values have to be inverted
        double projectPlaneDist = 1 / Math.tan(Math.toRadians(fov / 2f)) * Window.width / 2;
        for (int ray = 0; ray < Window.width; ray++) { // the visibility cone
            //rayAngle = player.viewAngle -  Math.toRadians(fov) / 2 +  Math.toRadians(fov) * ray / Window.width;
            /*
            spent a whole evening figuring out the line below, gonna leave the line above there and nicely commented for my stupid future self
            https://stackoverflow.com/questions/24173966/raycasting-engine-rendering-creating-slight-distortion-increasing-towards-edges
            */
            rayAngle = Math.atan((ray - Window.width / 2f) / (projectPlaneDist)) + player.getViewAngle();

            if (rayAngle < 0) rayAngle += 2 * Math.PI;
            if (rayAngle > 2 * Math.PI) rayAngle -= 2 * Math.PI;

            // VERTICAL GRID LINES
            double rayTan = Math.tan(-rayAngle), verticalX, verticalY;

            if (rayAngle > Math.PI / 2 && rayAngle < 3 * Math.PI / 2) { // looking left
                rayX = player.posX - (player.posX % Map.wallSize) - 0.01f;
                // we need to subtract that small number there so the ray isn't directly at the cross section
                rayY = rayTan * (player.posX % Map.wallSize) + player.posY;
                xOff = -Map.wallSize;
                yOff = -xOff * rayTan;
            }

            if (rayAngle > 3 * Math.PI / 2 || rayAngle < Math.PI / 2) { // looking right
                rayX = player.posX + (Map.wallSize - player.posX % Map.wallSize);
                rayY = -rayTan * (Map.wallSize - player.posX % Map.wallSize) + player.posY;
                xOff = Map.wallSize;
                yOff = -xOff * rayTan;
            }

            for (int i = 0; i < Map.mapX + Map.mapY; i++) { // add offsets until the ray hits a wall
                try {
                    mapIndex = Map.map[((int) rayY / Map.wallSize) * Map.mapX + ((int) rayX / Map.wallSize)];
                } catch (ArrayIndexOutOfBoundsException ignored) {
                    mapIndex = 0;
                }
                if (mapIndex == 0) {
                    rayX += xOff;
                    rayY += yOff;
                } else {
                    break;
                }
            }

            verticalDist = CameraUtil.rayLength(player.posX, player.posY, rayX, rayY);
            verticalX = rayX;
            verticalY = rayY;

            // HORIZONTAL GRID LINES
            double rayCot = (-1 / Math.tan(rayAngle)), horizontalX, horizontalY;

            if (rayAngle > Math.PI) { // looking up
                rayX = rayCot * (player.posY % Map.wallSize) + player.posX;
                rayY = player.posY - (player.posY % Map.wallSize) - 0.01f;
                // we need to subtract that small number there so the ray isn't directly at the cross section
                yOff = -Map.wallSize;
                xOff = -yOff * rayCot;
            }

            if (rayAngle < Math.PI) { // looking down
                rayX = -rayCot * (Map.wallSize - player.posY % Map.wallSize) + player.posX;
                rayY = player.posY + (Map.wallSize - player.posY % Map.wallSize);
                yOff = Map.wallSize;
                xOff = -yOff * rayCot;
            }

            for (int i = 0; i < Map.mapX + Map.mapY; i++) { // add offsets until the ray hits a wall
                try {
                    mapIndex = Map.map[((int) rayY / Map.wallSize) * Map.mapX + ((int) rayX / Map.wallSize)];
                } catch (ArrayIndexOutOfBoundsException ignored) {
                    mapIndex = 0;
                }
                if (mapIndex == 0) {
                    rayX += xOff;
                    rayY += yOff;
                } else {
                    break;
                }
            }

            horizontalDist = CameraUtil.rayLength(player.posX, player.posY, rayX, rayY);
            horizontalX = rayX;
            horizontalY = rayY;

            // check which is shorter
            if (verticalDist < horizontalDist) {
                rayX = verticalX;
                rayY = verticalY;
                finalDist = verticalDist;
            }

            if (horizontalDist < verticalDist) {
                rayX = horizontalX;
                rayY = horizontalY;
                finalDist = horizontalDist;
            }

            // add to depth buffer
            depthBuffer[ray] = (int) finalDist;

            // final calculation of mapIndex
            try {
                mapIndex = Map.map[((int) rayY / Map.wallSize) * Map.mapX + ((int) rayX / Map.wallSize)];
            } catch (ArrayIndexOutOfBoundsException ignored) {
                mapIndex = 0;
            }

            // DRAW (PSEUDO) 3D!1!1!1!1!1!1!
            lineHeight = (Map.wallSize * Window.height) / (finalDist * Math.cos(rayAngle - player.getViewAngle())); // line height with fisheye correction
            lineOff = (Window.height / 2f) - (lineHeight / 2);
            drawFloorAndCeiling(ray);
            drawWalls(ray);
        }
    }

    private void drawWalls(int ray) {
        // calculate column and flip texture
        int xTexture;
        if (horizontalDist < verticalDist) {
            xTexture = (int) (rayX % Map.wallSize);
            if (rayAngle > Math.PI) xTexture = Map.wallSize - 1 - xTexture;
        } else {
            xTexture = (int) (rayY % Map.wallSize);
            if (rayAngle > Math.PI / 2 && rayAngle < 3 * Math.PI / 2) xTexture = Map.wallSize - 1 - xTexture;
        }

        GL11.glColor3f(1, 1, 1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, Map.tileset.textureID);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(mapIndex / (float) (Map.tileset.textureWidth / (Map.wallSize)) + xTexture / (float) Map.tileset.textureWidth, 0);
        GL11.glVertex2d(ray, lineOff);

        GL11.glTexCoord2f(mapIndex / (float) (Map.tileset.textureWidth / Map.wallSize) + 1f / Map.tileset.textureWidth + xTexture / (float) Map.tileset.textureWidth, 0);
        GL11.glVertex2d(ray + 1, lineOff);

        GL11.glTexCoord2f(mapIndex / (float) (Map.tileset.textureWidth / Map.wallSize) + 1f / Map.tileset.textureWidth + xTexture / (float) Map.tileset.textureWidth, 1);
        GL11.glVertex2d(ray + 1, lineHeight + lineOff);

        GL11.glTexCoord2f(mapIndex / (float) (Map.tileset.textureWidth / Map.wallSize) + xTexture / (float) Map.tileset.textureWidth, 1);
        GL11.glVertex2d(ray, lineHeight + lineOff);
        GL11.glEnd();
    }

    private void drawFloorAndCeiling(int ray) {
        // reference: https://permadi.com/1996/05/ray-casting-tutorial-12/
        // hopefully implement this in the future, solid colors for now
        // 8. 5. 2021 finally implemented :)
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glPointSize(Window.vidMode.width() / (float) Window.width + 1);
        GL11.glBegin(GL11.GL_POINTS);

        byte[] pixel;
        double rayFix = Math.cos(CameraUtil.fixAngle(rayAngle - player.getViewAngle()));
        double textureXAng = Math.cos(rayAngle) * (Window.height / 4.0) * Map.wallSize;
        double textureYAng = Math.sin(rayAngle) * (Window.height / 4.0) * Map.wallSize;

        for (int row = (int) (lineHeight + lineOff); row < Window.height; row++) {
            try {
                int dRow = row - Window.height / 2;
                int textureX = (int) (player.posX / 2 + textureXAng / dRow / rayFix);
                int textureY = (int) (player.posY / 2 + textureYAng / dRow / rayFix);
                int textureByteOff = ((textureY & (Map.wallSize - 1)) * Map.tileset.textureWidth + (textureX & (Map.wallSize - 1)));
                int mapIndex = (textureY / Map.wallSize) * Map.mapX + (textureX / Map.wallSize) + 1;

                // floor
                pixel = new byte[]{
                        Map.tileset.tileSet.get((textureByteOff + Map.wallSize * Map.floorMap[mapIndex]) * 4),
                        Map.tileset.tileSet.get((textureByteOff + Map.wallSize * Map.floorMap[mapIndex]) * 4 + 1),
                        Map.tileset.tileSet.get((textureByteOff + Map.wallSize * Map.floorMap[mapIndex]) * 4 + 2),
                };
                GL11.glColor3f(pixel[0] / 255f, pixel[1] / 255f, pixel[2] / 255f);
                GL11.glVertex2i(ray, row);

                // ceiling
                pixel = new byte[]{
                        Map.tileset.tileSet.get((textureByteOff + Map.wallSize * Map.ceilingMap[mapIndex]) * 4),
                        Map.tileset.tileSet.get((textureByteOff + Map.wallSize * Map.ceilingMap[mapIndex]) * 4 + 1),
                        Map.tileset.tileSet.get((textureByteOff + Map.wallSize * Map.ceilingMap[mapIndex]) * 4 + 2),
                };
                GL11.glColor3f(pixel[0] / 255f, pixel[1] / 255f, pixel[2] / 255f);
                GL11.glVertex2i(ray, Window.height - row);
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private void drawSprites() {
        CameraUtil.sortElements(player);
        for (Element element : Map.elements) {
            double spriteDirection = Math.atan2(element.posY - player.posY, element.posX - player.posX);

            while (spriteDirection - player.getViewAngle() > Math.PI) spriteDirection -= 2 * Math.PI;
            while (spriteDirection - player.getViewAngle() < -Math.PI) spriteDirection += 2 * Math.PI;

            double spriteDistance = CameraUtil.rayLength(player.posX, player.posY, element.posX, element.posY);
            double spriteSize = Window.height / spriteDistance;

            double xOff = (spriteDirection - player.getViewAngle()) * Window.width / Math.toRadians(fov) + Window.width / 2.0 - spriteSize * Map.wallSize * element.sizeX / 2.0;
            double yOff = Window.height / 2.0 - spriteSize * Map.wallSize * element.sizeY + spriteSize * Map.wallSize / 2.0;

            for (int spriteColumn = 0; spriteColumn < spriteSize * Map.wallSize * element.sizeX; spriteColumn++) {
                try {
                    if (spriteDistance < depthBuffer[(int) (xOff + spriteColumn)]) { // this column is behind a wall
                        GL11.glColor3f(1, 1, 1);
                        GL11.glBindTexture(GL11.GL_TEXTURE_2D, element.sprite.textureID);
                        GL11.glBegin(GL11.GL_QUADS);

                        GL11.glTexCoord2f((float) (1f / (Map.wallSize * spriteSize * element.sizeX) * spriteColumn), 0);
                        GL11.glVertex2d(xOff + spriteColumn, yOff);

                        GL11.glTexCoord2f((float) (1f / (Map.wallSize * spriteSize * element.sizeX + 1) * spriteColumn), 0);
                        GL11.glVertex2d(xOff + spriteColumn + 1, yOff);

                        GL11.glTexCoord2f((float) (1f / (Map.wallSize * spriteSize * element.sizeX + 1) * spriteColumn), 1);
                        GL11.glVertex2d(xOff + spriteColumn + 1, yOff + spriteSize * Map.wallSize * element.sizeY);

                        GL11.glTexCoord2f((float) (1f / (Map.wallSize * spriteSize * element.sizeX) * spriteColumn), 1);
                        GL11.glVertex2d(xOff + spriteColumn, yOff + spriteSize * Map.wallSize * element.sizeY);

                        GL11.glEnd();
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
        }
    }

    public Camera(Player player) {
        this.player = player;
        this.depthBuffer = new int[Window.width];
    }
}
