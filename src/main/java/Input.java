import element.entity.Player;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.DoubleBuffer;

public class Input {
    Player player;
    static DoubleBuffer mouseX = BufferUtils.createDoubleBuffer(1);
    static DoubleBuffer mouseY = BufferUtils.createDoubleBuffer(1);
    static double dX, dY;

    public void move() {
        dX = Math.cos(player.getViewAngle());
        dY = Math.sin(player.getViewAngle());

        if (GLFW.glfwGetKey(Window.window, GLFW.GLFW_KEY_W) == GLFW.GLFW_TRUE) {
            player.setPosX((float) (player.getPosX() + (dX * Window.frameTime * 200)));
            player.setPosY((float) (player.getPosY() + (dY * Window.frameTime * 200)));
        }
        if (GLFW.glfwGetKey(Window.window, GLFW.GLFW_KEY_S) == GLFW.GLFW_TRUE) {
            player.setPosX((float) (player.getPosX() - (dX * Window.frameTime * 200)));
            player.setPosY((float) (player.getPosY() - (dY * Window.frameTime * 200)));
        }
        if (GLFW.glfwGetKey(Window.window, GLFW.GLFW_KEY_A) == GLFW.GLFW_TRUE) {
            player.setPosX((float) (player.getPosX() - Math.cos(player.getViewAngle() + Math.PI / 2) * Window.frameTime * 200));
            player.setPosY((float) (player.getPosY() - Math.sin(player.getViewAngle() + Math.PI / 2) * Window.frameTime * 200));
        }
        if (GLFW.glfwGetKey(Window.window, GLFW.GLFW_KEY_D) == GLFW.GLFW_TRUE) {
            player.setPosX((float) (player.getPosX() + Math.cos(player.getViewAngle() + Math.PI / 2) * Window.frameTime * 200));
            player.setPosY((float) (player.getPosY() + Math.sin(player.getViewAngle() + Math.PI / 2) * Window.frameTime * 200));
        }

        // mouse
        GLFW.glfwGetCursorPos(Window.window, mouseX, mouseY);
        mouseX.rewind();
        mouseY.rewind();
        player.setViewAngle((float) (player.getViewAngle() + (mouseX.get(0) - (Window.width / 2.0)) / Window.width));
        if (player.getViewAngle() > 2 * Math.PI) player.setViewAngle((float) (player.getViewAngle() - 2 * Math.PI));
        if (player.getViewAngle() < 0) player.setViewAngle((float) (player.getViewAngle() + 2 * Math.PI));
    }

    public Input(Player player) {
        this.player = player;
    }
}
