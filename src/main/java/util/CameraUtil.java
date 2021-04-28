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
        Map.elements.sort(new Comparator<Element>() {
            @Override
            public int compare(Element element1, Element element2) {
                double dist1 = rayLength(player.posX, player.posY, element1.posX, element1.posY);
                double dist2 = rayLength(player.posX, player.posY, element2.posX, element2.posY);
                return Double.compare(dist2, dist1);
            }
        });
    }
}
