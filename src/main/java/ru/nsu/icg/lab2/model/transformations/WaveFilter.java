package ru.nsu.icg.lab2.model.transformations;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.ImageInterface;
import ru.nsu.icg.lab2.model.Transformation;

@Getter
@Setter
public class WaveFilter extends Transformation {
    public enum WaveFilterOrder{
        FROM_X_TO_Y,
        FROM_Y_TO_X
    }

    private WaveFilterOrder order;
    private double ampX;
    private double ampY;
    private double freqX;
    private double freqY;

    public WaveFilter(ImageFactory imageFactory) {
        super(imageFactory);
        ampX = 10;
        freqX = 0;
        ampY = 0;
        freqY = 0;
        order = WaveFilterOrder.FROM_X_TO_Y;
    }

    @Override
    public ImageInterface apply(ImageInterface oldImage) {
        final int width = oldImage.getWidth();
        final int height = oldImage.getHeight();
        final int gridSize = width * height;
        final int[] oldGrid = oldImage.getGrid();
        final int[] newGrid = ampX == 0 && ampY == 0 ? oldGrid : new int[gridSize];

        if(order == WaveFilterOrder.FROM_X_TO_Y) {
            waveOnX(oldGrid, width, height, newGrid);
            if (ampX != 0 && ampY != 0) {
                System.arraycopy(newGrid, 0, oldGrid, 0, gridSize);
            }
            waveOnY(oldGrid, width, height, newGrid);
        }
        if(order == WaveFilterOrder.FROM_Y_TO_X) {
            waveOnY(oldGrid, width, height, newGrid);
            if (ampX != 0 && ampY != 0) {
                System.arraycopy(newGrid, 0, oldGrid, 0, gridSize);
            }
            waveOnX(oldGrid, width, height, newGrid);
        }

        return getImageFactory().createImage(oldImage, newGrid);
    }

    private void waveOnX(int[] grid, int width, int height, int[] newGrid) {
        if(ampX == 0){
            return;
        }
        for (int y = 0; y < height; y++) {
            int yDimSize = y * width;
            for (int x = 0; x < width; x++) {
                int newX = x + (int) (ampX * Math.cos(freqX * y));
                if (newX >= 0 && newX < width) {
                    newGrid[yDimSize + newX] = grid[yDimSize + x];
                }
            }
        }
    }

    private void waveOnY(int[] grid, int width, int height, int[] newGrid) {
        if(ampY == 0){
            return;
        }
        for (int y = 0; y < height; y++) {
            int yDimSize = y * width;
            for (int x = 0; x < width; x++) {
                int newY = y + (int) (ampY * Math.sin(freqY * x));
                if (newY >= 0 && newY < height) {
                    newGrid[newY * width + x] = grid[yDimSize + x];
                }
            }
        }
    }
}
