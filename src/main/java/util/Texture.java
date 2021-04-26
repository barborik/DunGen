package util;

import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

public class Texture {
    public int tileSetWidth, tileSetHeight;
    public ByteBuffer tileSet;

    public Texture(String filename) {
        IntBuffer bmpWidth = BufferUtils.createIntBuffer(1);
        IntBuffer bmpHeight = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);
        try {
            byte[] bytes = IOUtils.toByteArray(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(filename)));
            ByteBuffer textures = ByteBuffer.allocateDirect(bytes.length);
            for (int i = 0; i < bytes.length; i++) {
                textures.put(bytes[i]);
            }
            textures.flip();
            tileSet = STBImage.stbi_load_from_memory(textures, bmpWidth, bmpHeight, comp, 4);
        } catch (Exception ignored) {
        }
        tileSetWidth = bmpWidth.get(0);
        tileSetHeight = bmpHeight.get(0);

        GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, bmpWidth.get());
        bmpWidth.rewind();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, GL11.glGenTextures());

        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, tileSetWidth, tileSetHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, tileSet);
    }
}
