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
            player.collider.checkCollision((float) (dX * Window.frameTime * player.getSpeed()), (float) (dY * Window.frameTime * player.getSpeed()));
        }
        if (GLFW.glfwGetKey(Window.window, GLFW.GLFW_KEY_S) == GLFW.GLFW_TRUE) {
            player.collider.checkCollision((float) (-dX * Window.frameTime * player.getSpeed()), (float) (-dY * Window.frameTime * player.getSpeed()));
        }
        if (GLFW.glfwGetKey(Window.window, GLFW.GLFW_KEY_A) == GLFW.GLFW_TRUE) {
            player.collider.checkCollision((float) (-Math.cos(player.getViewAngle() + Math.PI / 2) * Window.frameTime * player.getSpeed()), (float) (-Math.sin(player.getViewAngle() + Math.PI / 2) * Window.frameTime * player.getSpeed()));
        }
        if (GLFW.glfwGetKey(Window.window, GLFW.GLFW_KEY_D) == GLFW.GLFW_TRUE) {
            player.collider.checkCollision((float) (Math.cos(player.getViewAngle() + Math.PI / 2) * Window.frameTime * player.getSpeed()), (float) (Math.sin(player.getViewAngle() + Math.PI / 2) * Window.frameTime * player.getSpeed()));
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
