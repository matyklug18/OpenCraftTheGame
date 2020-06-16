package matyk.engine.utils;

import matyk.engine.data.Texture;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.BufferUtils.createByteBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class TextureLoader {


    /**
     * load a texture
     * @param name the filename of the texture
     * @return the texture
     */
    public static Texture load(String name) {
        ByteBuffer image;

        ByteBuffer imageBuffer;
        try {
            imageBuffer = ioResourceToByteBuffer("textures/" + name, 8 * 1024);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (MemoryStack stack = stackPush()) {
            IntBuffer w    = stack.mallocInt(1);
            IntBuffer h    = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            if (!stbi_info_from_memory(imageBuffer, w, h, comp)) {
                throw new RuntimeException("Failed to read image information: " + stbi_failure_reason());
            } else {
                System.out.println("OK with reason: " + stbi_failure_reason());
            }

            System.out.println("Image width: " + w.get(0));
            System.out.println("Image height: " + h.get(0));
            System.out.println("Image components: " + comp.get(0));
            System.out.println("Image HDR: " + stbi_is_hdr_from_memory(imageBuffer));

            // Decode the image
            image = stbi_load_from_memory(imageBuffer, w, h, comp, 0);
            if (image == null) {
                throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
            }

            Texture tex = new Texture();

            tex.w = w.get(0);
            tex.h = h.get(0);
            tex.c = comp.get(0);

            int texID = glGenTextures();

            glBindTexture(GL_TEXTURE_2D, texID);

            int format = 0;

            if(comp.get(0) == 3)
                format = GL_RGB;
            else if(comp.get(0) == 4)
                format = GL_RGBA;

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            glTexImage2D(GL_TEXTURE_2D, 0, format, w.get(0), h.get(0), 0, format, GL_UNSIGNED_BYTE, image);

            glBindTexture(GL_TEXTURE_2D, 0);

            stbi_image_free(image);

            tex.id = texID;

            return tex;
        }
    }

    /**
     * load a resource into a buffer
     * @param resource the path to the resource
     * @param bufferSize the size of the buffer
     * @return the buffer
     */
    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer = createByteBuffer(bufferSize);
        buffer.put(StringLoader.loadResourceAsStream(resource).readAllBytes());
        buffer.flip();
        return buffer;
    }
}
