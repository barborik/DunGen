import element.entity.Player;
import map.Map;

public class Main {
    public static void main(String[] args) {
        Settings settings = new Settings();
        Window window = new Window();
        Map map = new Map();

        int[] emptyCell = map.firstRoom.getEmptyCell();
        Player player = new Player(emptyCell[0] * Map.wallSize + Map.wallSize / 2.0, emptyCell[1] * Map.wallSize + Map.wallSize / 2.0, 0);

        Camera camera = new Camera(player);
        Input input = new Input(player);

        while (window.windowUpdate()) {
            //System.out.println(1 / Window.frameTime);
            input.move();
            camera.render();
        }
    }
}
