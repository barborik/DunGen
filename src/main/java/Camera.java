import element.entity.Player;
import map.Map;
import org.lwjgl.opengl.GL11;

public class Camera {
    private final Player player;
    static int fov = 70;

    public void debugDrawing() {
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);

        // draw ray
        GL11.glColor3f(1.5f, 0, 0);
        GL11.glLineWidth(3);

        GL11.glBegin(GL11.GL_LINES);
        GL11.glColor3f(1.5f, 0, 0);
        GL11.glVertex2f(player.getPosX(), player.getPosY());
        GL11.glVertex2f(50 * (float) Math.cos(player.getViewAngle()) + player.getPosX(), 50 * (float) Math.sin(player.getViewAngle()) + player.getPosY());
        GL11.glEnd();

        // draw player
        GL11.glColor3f(1.5f, 1.5f, 0);
        GL11.glPointSize(8);

        GL11.glBegin(GL11.GL_POINTS);
        GL11.glColor3f(1.5f, 1.5f, 0);
        GL11.glVertex2f(player.getPosX(), player.getPosY());
        GL11.glEnd();

        // draw map
        int xOff;
        int yOff;
        for (int y = 0; y < Map.mapY; y++) {
            for (int x = 0; x < Map.mapX; x++) {
                xOff = x * Map.wallSize;
                yOff = y * Map.wallSize;

                if (Map.map[y * Map.mapX + x] != 0) {
                    GL11.glColor3f(1, 1, 1);

                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

                    GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_PIXELS, Map.wallSize * Map.map[y * Map.mapX + x]);
                    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, Map.wallSize, Map.wallSize, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, Map.tileset.tileSet);

                    GL11.glBegin(GL11.GL_QUADS);

                    GL11.glTexCoord2f(0, 0);
                    GL11.glVertex2i(xOff, yOff);

                    GL11.glTexCoord2f(1, 0);
                    GL11.glVertex2i(xOff + Map.wallSize, yOff);

                    GL11.glTexCoord2f(1, 1);
                    GL11.glVertex2i(xOff + Map.wallSize, yOff + Map.wallSize);

                    GL11.glTexCoord2f(0, 1);
                    GL11.glVertex2i(xOff, yOff + Map.wallSize);

                    GL11.glEnd();
                }
            }
        }
    }

    public float rayLength(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    public void castRays() {
        // by default, java (java Math lib) handles angles clockwise (or I may just be stupid and made a mistake along the way)
        // that means that some values have to be inverted
        float rayX = 0, rayY = 0, rayAngle, xOff = 0, yOff = 0, finalDistance = 0;
        int mapIndex = 0;
        for (int ray = 0; ray < Window.width; ray++) { // the visibility cone

            //rayAngle = player.getViewAngle() - (float) Math.toRadians(fov) / 2 + (float) Math.toRadians(fov) * ray / Window.width;
            /*
            spent a whole evening figuring out the line below, gonna leave the line above there and nicely commented for my stupid future self
            https://stackoverflow.com/questions/24173966/raycasting-engine-rendering-creating-slight-distortion-increasing-towards-edges
            */
            rayAngle = (float) Math.atan((ray - Window.width / 2f) / (1 / Math.tan(Math.toRadians(fov / 2f)) * Window.width / 2)) + player.getViewAngle();

            if (rayAngle < 0) rayAngle += 2 * Math.PI;
            if (rayAngle > 2 * Math.PI) rayAngle -= 2 * Math.PI;

            // VERTICAL GRID LINES
            float rayTan = (float) Math.tan(-rayAngle), vertical, verticalX, verticalY;

            if (rayAngle > Math.PI / 2 && rayAngle < 3 * Math.PI / 2) { // looking left
                rayX = player.getPosX() - (player.getPosX() % Map.wallSize) - 0.01f;
                // we need to subtract that small number there so the ray isn't directly at the cross section
                rayY = rayTan * (player.getPosX() % Map.wallSize) + player.getPosY();
                xOff = -Map.wallSize;
                yOff = -xOff * rayTan;
            }

            if (rayAngle > 3 * Math.PI / 2 || rayAngle < Math.PI / 2) { // looking right
                rayX = player.getPosX() + (Map.wallSize - player.getPosX() % Map.wallSize);
                rayY = -rayTan * (Map.wallSize - player.getPosX() % Map.wallSize) + player.getPosY();
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

            vertical = rayLength(player.getPosX(), player.getPosY(), rayX, rayY);
            verticalX = rayX;
            verticalY = rayY;

            // HORIZONTAL GRID LINES
            float rayCot = (float) (-1 / Math.tan(rayAngle)), horizontal, horizontalX, horizontalY;

            if (rayAngle > Math.PI) { // looking up
                rayX = rayCot * (player.getPosY() % Map.wallSize) + player.getPosX();
                rayY = player.getPosY() - (player.getPosY() % Map.wallSize) - 0.01f;
                // we need to subtract that small number there so the ray isn't directly at the cross section
                yOff = -Map.wallSize;
                xOff = -yOff * rayCot;
            }

            if (rayAngle < Math.PI) { // looking down
                rayX = -rayCot * (Map.wallSize - player.getPosY() % Map.wallSize) + player.getPosX();
                rayY = player.getPosY() + (Map.wallSize - player.getPosY() % Map.wallSize);
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

            horizontal = rayLength(player.getPosX(), player.getPosY(), rayX, rayY);
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

            // final calculation of mapIndex
            try {
                mapIndex = Map.map[((int) rayY / Map.wallSize) * Map.mapX + ((int) rayX / Map.wallSize)];
            } catch (ArrayIndexOutOfBoundsException ignored) {
                mapIndex = 0;
            }

            // DRAW (PSEUDO) 3D!1!1!1!1!1!1!
            double lineHeight = (Map.wallSize * Window.height) / (finalDistance * (float) Math.cos(rayAngle - player.getViewAngle())); // line height with fisheye correction
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
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(mapIndex / (float) (Map.tileset.tileSetWidth / Map.wallSize) + xTexture / (float) Map.tileset.tileSetWidth, 0);
            GL11.glVertex2d(ray, lineOff);

            GL11.glTexCoord2f(mapIndex / (float) (Map.tileset.tileSetWidth / Map.wallSize) + 1 / (float) Map.tileset.tileSetWidth + xTexture / (float) Map.tileset.tileSetWidth, 0);
            GL11.glVertex2d(ray + 1, lineOff);

            GL11.glTexCoord2f(mapIndex / (float) (Map.tileset.tileSetWidth / Map.wallSize) + 1 / (float) Map.tileset.tileSetWidth + xTexture / (float) Map.tileset.tileSetWidth, 1);
            GL11.glVertex2d(ray + 1, lineHeight + lineOff);

            GL11.glTexCoord2f(mapIndex / (float) (Map.tileset.tileSetWidth / Map.wallSize) + xTexture / (float) Map.tileset.tileSetWidth, 1);
            GL11.glVertex2d(ray, lineHeight + lineOff);
            GL11.glEnd();

            //  draw visibility cone
            /*GL11.glColor3f(1, 1, 1);
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2d(player.getPosX(), player.getPosY());
            GL11.glVertex2d(rayX, rayY);
            GL11.glEnd();*/
        }
    }

    public Camera(Player player) {
        this.player = player;
    }
}
