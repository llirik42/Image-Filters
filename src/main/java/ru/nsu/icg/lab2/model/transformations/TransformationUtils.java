package ru.nsu.icg.lab2.model.transformations;

public class TransformationUtils {
    public static int getAlpha(int argb) {
        return (argb & 0xFF000000) >> 24;
    }

    public static int getRed(int argb) {
        return (argb & 0x00FF0000) >> 16;
    }

    public static int getGreen(int argb) {
        return (argb & 0x0000FF00) >> 8;
    }

    public static int getBlue(int argb) {
        return argb & 0x000000FF;
    }

    public static int getARGB(int alpha, int red, int green, int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public static void copyBorderPixels(int[] src, int[] dest, int width, int height, int offset) {
        for (int y = 0; y < offset; y++) {
            System.arraycopy(src, y * width, dest, y * width, width);
        }
        for (int y = height - offset; y < height; y++) {
            System.arraycopy(src, y * width, dest, y * width, width);
        }
        for (int x = 0; x < offset; x++) {
            for (int y = offset; y < height - offset; y++) {
                dest[y * width + x] = src[y * width + x];
            }
        }
        for (int x = width - offset; x < width; x++) {
            for (int y = offset; y < height - offset; y++) {
                dest[y * width + x] = src[y * width + x];
            }
        }
    }
}
