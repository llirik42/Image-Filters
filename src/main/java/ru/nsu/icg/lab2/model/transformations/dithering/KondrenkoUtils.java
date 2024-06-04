package ru.nsu.icg.lab2.model.transformations.dithering;

public final class KondrenkoUtils {
    private KondrenkoUtils() {
    }

    public static int findNearestColor(int value, double delta) {
        if (value >= 255) {
            return 255;
        } else if (value < 0) {
            return 0;
        } else {
            return (int) Math.round(Math.round((float) value / delta) * delta);
        }
    }

    public static double calculateDelta(int paletteSize) {
        return 255.0 / (paletteSize - 1);
    }
}
