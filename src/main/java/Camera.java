import element.entity.Player;
import org.lwjgl.opengl.GL11;

public class Camera {
    private final Player player;
    private Map map;
    static int fov = 70;

    public void setMap(Map map) {
        this.map = map;
    }

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
        for (int y = 0; y < map.mapY; y++) {
            for (int x = 0; x < map.mapX; x++) {
                xOff = x * map.wallSize;
                yOff = y * map.wallSize;

                if (map.map[y * map.mapX + x] != 0) {
                    GL11.glColor3f(1, 1, 1);

                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

                    GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_PIXELS, map.wallSize * map.map[y * map.mapX + x]);
                    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, map.wallSize, map.wallSize, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, map.tileSet);

                    GL11.glBegin(GL11.GL_QUADS);

                    GL11.glTexCoord2f(0, 0);
                    GL11.glVertex2i(xOff, yOff);

                    GL11.glTexCoord2f(1, 0);
                    GL11.glVertex2i(xOff + map.wallSize, yOff);

                    GL11.glTexCoord2f(1, 1);
                    GL11.glVertex2i(xOff + map.wallSize, yOff + map.wallSize);

                    GL11.glTexCoord2f(0, 1);
                    GL11.glVertex2i(xOff, yOff + map.wallSize);

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
            rayAngle = player.getViewAngle() - (float) Math.toRadians(fov) / 2 + (float) Math.toRadians(fov) * ray / Window.width;
            if (rayAngle < 0) rayAngle += 2 * Math.PI;
            if (rayAngle > 2 * Math.PI) rayAngle -= 2 * Math.PI;

            // VERTICAL GRID LINES
            float rayTan = (float) Math.tan(-rayAngle), vertical, verticalX, verticalY;

            if (rayAngle > Math.PI / 2 && rayAngle < 3 * Math.PI / 2) { // looking left
                rayX = player.getPosX() - (player.getPosX() % map.wallSize) - 0.01f;
                // we need to subtract that small number there so the ray isn't directly at the cross section
                rayY = rayTan * (player.getPosX() % map.wallSize) + player.getPosY();
                xOff = -64;
                yOff = -xOff * rayTan;
            }

            if (rayAngle > 3 * Math.PI / 2 || rayAngle < Math.PI / 2) { // looking right
                rayX = player.getPosX() + (map.wallSize - player.getPosX() % map.wallSize);
                rayY = -rayTan * (map.wallSize - player.getPosX() % map.wallSize) + player.getPosY();
                xOff = 64;
                yOff = -xOff * rayTan;
            }

            for (int i = 0; i < map.mapX + map.mapY; i++) { // add offsets until the ray hits a wall
                try {
                    mapIndex = map.map[((int) rayY / map.wallSize) * map.mapX + ((int) rayX / map.wallSize)];
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
                rayX = rayCot * (player.getPosY() % map.wallSize) + player.getPosX();
                rayY = player.getPosY() - (player.getPosY() % map.wallSize) - 0.01f;
                // we need to subtract that small number there so the ray isn't directly at the cross section
                yOff = -64;
                xOff = -yOff * rayCot;
            }

            if (rayAngle < Math.PI) { // looking down
                rayX = -rayCot * (map.wallSize - player.getPosY() % map.wallSize) + player.getPosX();
                rayY = player.getPosY() + (map.wallSize - player.getPosY() % map.wallSize);
                yOff = 64;
                xOff = -yOff * rayCot;
            }

            for (int i = 0; i < map.mapX + map.mapY; i++) { // add offsets until the ray hits a wall
                try {
                    mapIndex = map.map[((int) rayY / map.wallSize) * map.mapX + ((int) rayX / map.wallSize)];
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

            // DRAW (PSEUDO) 3D!1!1!1!1!1!1!
            double lineHeight = (map.wallSize * Window.height) / (finalDistance * (float) Math.cos(rayAngle - player.getViewAngle()));
            double lineOff = (Window.height / 2f) - (lineHeight / 2); // fix fisheye effect

            GL11.glColor3f(0.3f, 0.3f, 0.3f);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2d(ray, lineOff);
            GL11.glVertex2d(ray + 1, lineOff);
            GL11.glVertex2d(ray + 1, lineHeight + lineOff);
            GL11.glVertex2d(ray, lineHeight + lineOff);
            GL11.glEnd();
        }
    }

    public Camera(Player player, Map map) {
        this.player = player;
        this.map = map;
    }
}
