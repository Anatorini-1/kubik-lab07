package Util;

import model.Artifact;
import model.Category;


import java.awt.*;

public class Util {
    public static String coordsToLabel(int x, int y, int xMax, int yMax) {
        return (char) ('A' + x) + String.valueOf(yMax - y);
    }

    public static String coordsToLabel(int x, int y, int size) {
        return coordsToLabel(x, y, size, size);
    }

    public static int[] labelToCoords(String label, int xMax, int yMax) {
        int[] toReturn = new int[2];
        toReturn[0] = label.charAt(0) - 'A';
        toReturn[1] = yMax - Integer.parseInt(label.substring(1));
        return toReturn;
    }

    public static int[] labelToCoords(String label, int size) {
        return labelToCoords(label, size, size);
    }

    public static Color getArtifactColor(Artifact a) {
        Category c = a.getCategory();
        return switch (c) {
            case EMPTY -> Color.red;
            case OTHER -> Color.GREEN;
            case GOLD -> Color.YELLOW;
            case SILVER -> Color.LIGHT_GRAY;
            case BRONZE -> Color.ORANGE;
            default -> Color.MAGENTA;
        };
    }
}
