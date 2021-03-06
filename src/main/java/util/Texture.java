package util;

import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

public class Texture {
    public int textureWidth, textureHeight;
    public ByteBuffer tileSet;
    public int textureID;

    public Texture(String filename) {
        IntBuffer bmpWidth = BufferUtils.createIntBuffer(1);
        IntBuffer bmpHeight = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);
        try {
            byte[] data = IOUtils.toByteArray(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(filename)));
            ByteBuffer textures = ByteBuffer.allocateDirect(data.length);
            for (int i = 0; i < data.length; i++) {
                textures.put(data[i]);
            }
            textures.flip();
            tileSet = STBImage.stbi_load_from_memory(textures, bmpWidth, bmpHeight, comp, 4);
        } catch (Exception ignored) {
        }
        textureWidth = bmpWidth.get(0);
        textureHeight = bmpHeight.get(0);

        GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, bmpWidth.get());
        bmpWidth.rewind();

        textureID = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, textureWidth, textureHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, tileSet);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }
}
