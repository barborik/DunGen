import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Window {
    static int width = 640;
    static int height = 360;
    static long window;
    static double frameTime;
    private long startFrameTime;
    static GLFWVidMode vidMode;

    public boolean windowUpdate() {
        GLFW.glfwSetCursorPos(Window.window, Window.width / 2.0, Window.height / 2.0);
        Window.frameTime = (System.nanoTime() - startFrameTime) / 1e9;
        startFrameTime = System.nanoTime();
        GLFW.glfwPollEvents();
        GLFW.glfwSwapBuffers(window);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        return !GLFW.glfwWindowShouldClose(window);
    }

    public Window() {
        if (!GLFW.glfwInit()) {
            System.err.println("ERROR: GLFW not initialized");
            System.exit(0);
        }

        vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        window = GLFW.glfwCreateWindow(vidMode.width(), vidMode.height(), "DunGen", 0, 0);
        GLFW.glfwShowWindow(window);

        GLFW.glfwSetWindowMonitor(window, 0, 0, 0, vidMode.width(), vidMode.height(), vidMode.refreshRate());

        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GLFW.glfwSwapInterval(0);

        GL11.glOrtho(0, width, height, 0, 1, -1);

        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
    }
}
