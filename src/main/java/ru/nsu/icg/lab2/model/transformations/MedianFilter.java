package ru.nsu.icg.lab2.model.transformations;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.ImageInterface;
import ru.nsu.icg.lab2.model.Transformation;

import java.util.Arrays;

@Setter
@Getter
public class MedianFilter extends Transformation {
    private int windowSize = 5;

    public MedianFilter(ImageFactory imageFactory) {
        super(imageFactory);
    }

    @Override
    public ImageInterface apply(ImageInterface oldImage) {
        final int width = oldImage.getWidth();
        final int height = oldImage.getHeight();
        final int[] oldGrid = oldImage.getGrid();
        final int[] newGrid = new int[oldImage.getGridSize()];

        processCenter(windowSize, width, height, oldGrid, newGrid);
        processBorders(windowSize, width, height, oldGrid, newGrid);

        return getImageFactory().createImage(oldImage, newGrid);
    }

    private static void processCenter(int windowSize, int width, int height, int[] grid, int[] output) {
        final int radius = (windowSize - 1) / 2;

        for (int y = radius; y < height - radius; y++) {
            for (int x = radius; x < width - radius; x++) {
                final int index = y * width + x;
                final int oldAlpha = TransformationUtils.getAlpha(grid[index]);

                output[index] = processPixel(
                        radius,
                        oldAlpha,
                        index,
                        width,
                        grid
                );
            }
        }
    }

    private static void processBorders(int windowSize, int width, int height, int[] grid, int[] output) {
        final int maxRadius = (windowSize - 1) / 2;

        for (int y = 0; y < maxRadius; y++) {
            for (int x = 0; x < width; x++) {
                final int index = y * width + x;
                final int oldAlpha = TransformationUtils.getAlpha(grid[index]);
                final int currentRadius = Integer.min(
                        Integer.min(x, width - x - 1),
                        y
                );

                if (currentRadius == 0) {
                    output[index] = grid[index];
                } else {
                    output[index] = processPixel(
                            currentRadius,
                            oldAlpha,
                            index,
                            width,
                            grid
                    );
                }
            }
        }

        for (int y = height - maxRadius; y < height; y++) {
            for (int x = 0; x < width; x++) {
                final int index = y * width + x;
                final int oldAlpha = TransformationUtils.getAlpha(grid[index]);
                final int currentRadius = Integer.min(
                        Integer.min(x, width - x - 1),
                        height - y - 1
                );

                if (currentRadius == 0) {
                    output[index] = grid[index];
                } else {
                    output[index] = processPixel(
                            currentRadius,
                            oldAlpha,
                            index,
                            width,
                            grid
                    );
                }
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < maxRadius; x++) {
                final int index = y * width + x;
                final int oldAlpha = TransformationUtils.getAlpha(grid[index]);
                final int currentRadius = Integer.min(
                        Integer.min(y, height - y - 1),
                        x
                );

                if (currentRadius == 0) {
                    output[index] = grid[index];
                } else {
                    output[index] = processPixel(
                            currentRadius,
                            oldAlpha,
                            index,
                            width,
                            grid
                    );
                }
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = width - maxRadius; x < width; x++) {
                final int index = y * width + x;
                final int oldAlpha = TransformationUtils.getAlpha(grid[index]);
                final int currentRadius = Integer.min(
                        Integer.min(y, height - y - 1),
                        width - x - 1
                );

                if (currentRadius == 0) {
                    output[index] = grid[index];
                } else {
                    output[index] = processPixel(
                            currentRadius,
                            oldAlpha,
                            index,
                            width,
                            grid
                    );
                }
            }
        }
    }

    private static int processPixel(int radius, int alpha, int index, int width, int[] grid) {
        final int windowSize = radius * 2 + 1;
        final int windowVolume = windowSize * windowSize;
        final int middle = (windowVolume + 1) / 2;
        final int[] red = new int[windowVolume];
        final int[] green = new int[windowVolume];
        final int[] blue = new int[windowVolume];

        getSortedNeighbours(
                radius,
                index,
                width,
                grid,
                red,
                green,
                blue
        );

        return TransformationUtils.getARGB(
                alpha,
                red[middle],
                green[middle],
                blue[middle]
        );
    }

    private static void getSortedNeighbours(int radius,
                                            int offset,
                                            int width,
                                            int[] grid,
                                            int[] red,
                                            int[] green,
                                            int[] blue) {
        int componentIndex = 0;
        for (int inX = -radius; inX <= radius; inX++) {
            for (int inY = -radius; inY <= radius; inY++) {
                final int gridIndex = offset + inY * width + inX;
                final int pixelColor = grid[gridIndex];
                red[componentIndex] = TransformationUtils.getRed(pixelColor);
                green[componentIndex] = TransformationUtils.getGreen(pixelColor);
                blue[componentIndex] = TransformationUtils.getBlue(pixelColor);
                componentIndex++;
            }
        }

        Arrays.sort(red);
        Arrays.sort(green);
        Arrays.sort(blue);
    }
}
