import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

public class Map {
    public int tileSetWidth, tileSetHeight, mapX = 8, mapY = 8, wallSize = 64;
    public ByteBuffer tileSet;
    public int[] map = {
            4, 4, 4, 4, 2, 3, 4, 4,
            4, 0, 0, 4, 0, 0, 0, 4,
            4, 0, 0, 4, 0, 0, 0, 5,
            4, 0, 0, 0, 0, 0, 0, 5,
            4, 4, 4, 4, 0, 0, 0, 5,
            2, 0, 0, 0, 0, 0, 0, 3,
            2, 0, 0, 0, 0, 0, 0, 3,
            4, 5, 4, 1, 1, 1, 1, 4,
    };

    public Map() {
        IntBuffer bmpWidth = BufferUtils.createIntBuffer(1);
        IntBuffer bmpHeight = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);
        try {
            byte[] bytes = IOUtils.toByteArray(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("tile_set.bmp")));
            ByteBuffer textures = ByteBuffer.allocateDirect(bytes.length);
            for (int i = 0; i < bytes.length; i++) {
                textures.put(bytes[i]);
            }
            textures.flip();
            tileSet = STBImage.stbi_load_from_memory(textures, bmpWidth, bmpHeight, comp, 4);
        } catch (Exception ignored) {
        }
        this.tileSetWidth = bmpWidth.get(0);
        this.tileSetHeight = bmpHeight.get(0);

        GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, bmpWidth.get());
        bmpWidth.rewind();

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, tileSetWidth, tileSetHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, tileSet);
    }
}
