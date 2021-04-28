import element.Element;
import element.entity.Entity;
import element.entity.Player;
import map.Map;
import org.lwjgl.opengl.GL11;
import util.CameraUtil;

public class Camera {
    private final Player player;
    private int depthBuffer[];
    static int fov = 70;

    public void castRays() {
        // by default, java (java Math lib) handles angles clockwise (or I may just be stupid and made a mistake along the way)
        // that means that some values have to be inverted
        double rayX = 0, rayY = 0, rayAngle, xOff = 0, yOff = 0, finalDistance = 0;
        int mapIndex;
        for (int ray = 0; ray < Window.width; ray++) { // the visibility cone

            //rayAngle = player.viewAngle -  Math.toRadians(fov) / 2 +  Math.toRadians(fov) * ray / Window.width;
            /*
            spent a whole evening figuring out the line below, gonna leave the line above there and nicely commented for my stupid future self
            https://stackoverflow.com/questions/24173966/raycasting-engine-rendering-creating-slight-distortion-increasing-towards-edges
            */
            rayAngle = Math.atan((ray - Window.width / 2f) / (1 / Math.tan(Math.toRadians(fov / 2f)) * Window.width / 2)) + player.getViewAngle();

            if (rayAngle < 0) rayAngle += 2 * Math.PI;
            if (rayAngle > 2 * Math.PI) rayAngle -= 2 * Math.PI;

            // VERTICAL GRID LINES
            double rayTan = Math.tan(-rayAngle), vertical, verticalX, verticalY;

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

            vertical = CameraUtil.rayLength(player.posX, player.posY, rayX, rayY);
            verticalX = rayX;
            verticalY = rayY;

            // HORIZONTAL GRID LINES
            double rayCot = (-1 / Math.tan(rayAngle)), horizontal, horizontalX, horizontalY;

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

            horizontal = CameraUtil.rayLength(player.posX, player.posY, rayX, rayY);
            horizontalX = rayX;
            horizontalY = rayY;

            // check which is shorter
            if (vertical < horizontal) {
                rayX = verticalX;
                rayY = verticalY;
                finalDistance = vertical;
            }

            if (horizontal < vertical) {
                rayX = horizontalX;
                rayY = horizontalY;
                finalDistance = horizontal;
            }

            // add to depth buffer
            depthBuffer[ray] = (int) finalDistance;

            // final calculation of mapIndex
            try {
                mapIndex = Map.map[((int) rayY / Map.wallSize) * Map.mapX + ((int) rayX / Map.wallSize)];
            } catch (ArrayIndexOutOfBoundsException ignored) {
                mapIndex = 0;
            }

            // DRAW (PSEUDO) 3D!1!1!1!1!1!1!
            double lineHeight = (Map.wallSize * Window.height) / (finalDistance * Math.cos(rayAngle - player.getViewAngle())); // line height with fisheye correction
            double lineOff = (Window.height / 2f) - (lineHeight / 2);

            // calculate column and flip texture
            int xTexture;
            if (horizontal < vertical) {
                xTexture = (int) (rayX % Map.wallSize);
                if (rayAngle > Math.PI) xTexture = Map.wallSize - 1 - xTexture;
            } else {
                xTexture = (int) (rayY % Map.wallSize);
                if (rayAngle > Math.PI / 2 && rayAngle < 3 * Math.PI / 2) xTexture = Map.wallSize - 1 - xTexture;
            }

            GL11.glColor3f(1, 1, 1);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, Map.tileset.textureID);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(mapIndex / (float) (Map.tileset.textureWidth / Map.wallSize) + xTexture / (float) Map.tileset.textureWidth, 0);
            GL11.glVertex2d(ray, lineOff);

            GL11.glTexCoord2f(mapIndex / (float) (Map.tileset.textureWidth / Map.wallSize) + 1f / Map.tileset.textureWidth + xTexture / (float) Map.tileset.textureWidth, 0);
            GL11.glVertex2d(ray + 1, lineOff);

            GL11.glTexCoord2f(mapIndex / (float) (Map.tileset.textureWidth / Map.wallSize) + 1f / Map.tileset.textureWidth + xTexture / (float) Map.tileset.textureWidth, 1);
            GL11.glVertex2d(ray + 1, lineHeight + lineOff);

            GL11.glTexCoord2f(mapIndex / (float) (Map.tileset.textureWidth / Map.wallSize) + xTexture / (float) Map.tileset.textureWidth, 1);
            GL11.glVertex2d(ray, lineHeight + lineOff);
            GL11.glEnd();

            //  draw visibility cone
            /*GL11.glColor3f(1, 1, 1);
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2d(player.posX, player.posY);
            GL11.glVertex2d(rayX, rayY);
            GL11.glEnd();*/
        }
    }

    public void drawSprites() {
        CameraUtil.sortElements(player);
        for (Element element : Map.elements) {
            double spriteDirection = Math.atan2(element.posY - player.posY, element.posX - player.posX);

            while (spriteDirection - player.getViewAngle() > Math.PI) spriteDirection -= 2 * Math.PI;
            while (spriteDirection - player.getViewAngle() < -Math.PI) spriteDirection += 2 * Math.PI;

            double spriteDistance = CameraUtil.rayLength(player.posX, player.posY, element.posX, element.posY);
            double spriteSize = Window.height / spriteDistance;

            double xOff = (spriteDirection - player.getViewAngle()) * Window.width / Math.toRadians(fov) + Window.width / 2.0 - spriteSize * element.sprite.textureWidth * element.size / 2.0;
            double yOff = Window.height / 2.0 - spriteSize * element.sprite.textureHeight * element.size + spriteSize * Map.wallSize / 2.0;

            for (int spriteColumn = 0; spriteColumn < spriteSize * element.sprite.textureWidth * element.size; spriteColumn++) {
                try {
                    if (spriteDistance < depthBuffer[(int) (xOff + spriteColumn)]) { // this column is behind a wall
                        GL11.glColor3f(1, 1, 1);
                        GL11.glBindTexture(GL11.GL_TEXTURE_2D, element.sprite.textureID);
                        GL11.glBegin(GL11.GL_QUADS);

                        GL11.glTexCoord2f((float) (1f / (element.sprite.textureWidth * spriteSize * element.size) * spriteColumn), 0);
                        GL11.glVertex2d(xOff + spriteColumn, yOff);

                        GL11.glTexCoord2f((float) (1f / (element.sprite.textureWidth * spriteSize * element.size + 1) * spriteColumn), 0);
                        GL11.glVertex2d(xOff + spriteColumn + 1, yOff);

                        GL11.glTexCoord2f((float) (1f / (element.sprite.textureWidth * spriteSize * element.size + 1) * spriteColumn), 1);
                        GL11.glVertex2d(xOff + spriteColumn + 1, yOff + spriteSize * element.sprite.textureHeight * element.size);

                        GL11.glTexCoord2f((float) (1f / (element.sprite.textureWidth * spriteSize * element.size) * spriteColumn), 1);
                        GL11.glVertex2d(xOff + spriteColumn, yOff + spriteSize * element.sprite.textureHeight * element.size);

                        GL11.glEnd();
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
        }
    }

    public void render() {
        castRays();
        drawSprites();
    }

    public Camera(Player player) {
        this.player = player;
        this.depthBuffer = new int[Window.width];
    }
}
