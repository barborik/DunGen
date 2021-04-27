import element.entity.Player;
import map.Map;

public class Main {
    public static void main(String[] args) {
        Window window = new Window();

        Player player = new Player(300, 300, 0);
        Map map = new Map();
        Camera camera = new Camera(player);
        Input input = new Input(player);

        while (window.windowUpdate()) {
            System.out.println(1 / Window.frameTime);

            //camera.debugDrawing();
            camera.castRays();
            input.move();

        }
    }
}
