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
        double moveX = 0, moveY = 0;

        if (GLFW.glfwGetKey(Window.window, GLFW.GLFW_KEY_W) == GLFW.GLFW_TRUE) {
            moveX += dX * Window.frameTime * player.speed;
            moveY += dY * Window.frameTime * player.speed;
        }
        if (GLFW.glfwGetKey(Window.window, GLFW.GLFW_KEY_S) == GLFW.GLFW_TRUE) {
            moveX -= dX * Window.frameTime * player.speed;
            moveY -= dY * Window.frameTime * player.speed;
        }
        if (GLFW.glfwGetKey(Window.window, GLFW.GLFW_KEY_A) == GLFW.GLFW_TRUE) {
            moveX -= Math.cos(player.getViewAngle() + Math.PI / 2) * Window.frameTime * player.speed;
            moveY -= Math.sin(player.getViewAngle() + Math.PI / 2) * Window.frameTime * player.speed;
        }
        if (GLFW.glfwGetKey(Window.window, GLFW.GLFW_KEY_D) == GLFW.GLFW_TRUE) {
            moveX += Math.cos(player.getViewAngle() + Math.PI / 2) * Window.frameTime * player.speed;
            moveY += Math.sin(player.getViewAngle() + Math.PI / 2) * Window.frameTime * player.speed;
        }

        player.collider.checkCollision(moveX, moveY);

        // mouse
        GLFW.glfwGetCursorPos(Window.window, mouseX, mouseY);
        mouseX.rewind();
        mouseY.rewind();
        player.setViewAngle((player.getViewAngle() + (mouseX.get(0) - (Window.width / 2.0)) / Window.width));
    }

    public Input(Player player) {
        this.player = player;
    }
}
