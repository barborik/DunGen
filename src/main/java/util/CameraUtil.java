package util;

import element.Element;
import element.entity.Player;
import map.Map;

import java.util.Comparator;

public class CameraUtil {
    public static double rayLength(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    public static void sortElements(Player player) {
        Map.elements.sort((element1, element2) -> {
            double dist1 = rayLength(player.posX, player.posY, element1.posX, element1.posY);
            double dist2 = rayLength(player.posX, player.posY, element2.posX, element2.posY);
            return Double.compare(dist2, dist1);
        });
    }

    public static double fixAngle(double angle) {
        if (angle > 2 * Math.PI - 0.01) angle -= 2 * Math.PI;
        if (angle < 0) angle += 2 * Math.PI;
        return angle;
    }

    public static int unsignedToByte(byte b) {
        return b & 0xFF;
    }
}
