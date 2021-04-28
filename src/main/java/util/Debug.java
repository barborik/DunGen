package util;

import element.entity.Player;
import map.Map;
import org.lwjgl.opengl.GL11;

public class Debug {
    public static void debugDrawing(Player player) {
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);

        // draw ray
        GL11.glColor3f(1.5f, 0, 0);
        GL11.glLineWidth(3);

        GL11.glBegin(GL11.GL_LINES);
        GL11.glColor3f(1.5f, 0, 0);
        GL11.glVertex2d(player.posX, player.posY);
        GL11.glVertex2d(50 * Math.cos(player.getViewAngle()) + player.posX, 50 * Math.sin(player.getViewAngle()) + player.posY);
        GL11.glEnd();

        // draw player
        GL11.glColor3f(1.5f, 1.5f, 0);
        GL11.glPointSize(8);

        GL11.glBegin(GL11.GL_POINTS);
        GL11.glColor3f(1.5f, 1.5f, 0);
        GL11.glVertex2d(player.posX, player.posY);
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
}
