package ru.nsu.icg.lab2.model.transformations;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.ImageInterface;
import ru.nsu.icg.lab2.model.Transformation;

import java.util.Arrays;

@Setter
@Getter
public class EdgeDetection extends Transformation {
    public enum EdgeDetectionType{
        ROBERTS, SOBEL
    }

    private EdgeDetectionType type;
    private int binarization;

    public EdgeDetection(ImageFactory imageFactory) {
        super(imageFactory);
        this.binarization = 128;
        this.type = EdgeDetectionType.ROBERTS;
    }

    private ImageInterface applyRoberts(ImageInterface oldImage){
        final int borderStep = 1;
        final int width = oldImage.getWidth();
        final int height = oldImage.getHeight();
        final int[] oldGrid = oldImage.getGrid();
        final int[] newGrid = new int[oldGrid.length];
        Arrays.fill(newGrid, 0xFF000000);

        //without borders
        for (int y = 0; y < height - borderStep; y++){
            for (int x = 0; x < width - borderStep; x++){
                int index = y * width + x;
                int colorTopLeft = oldGrid[index];
                int colorTopRight = oldGrid[index + 1];
                int colorBottomLeft = oldGrid[index + width];
                int colorBottomRight = oldGrid[index + width + 1];
                int red = 0;
                int green = 0;
                int blue = 0;

                int colorXRed = ((colorBottomRight & 0x00FF0000) >> 16) - ((colorTopLeft & 0x00FF0000) >> 16);
                int colorYRed = ((colorBottomLeft & 0x00FF0000) >> 16) - ((colorTopRight & 0x00FF0000) >> 16);
                int colorXGreen = ((colorBottomRight & 0x0000FF00) >> 8) - ((colorTopLeft & 0x0000FF00) >> 8);
                int colorYGreen = ((colorBottomLeft & 0x0000FF00) >> 8) - ((colorTopRight & 0x0000FF00) >> 8);
                int colorXBlue = ((colorBottomRight & 0x000000FF)) - ((colorTopLeft & 0x000000FF));
                int colorYBlue = ((colorBottomLeft & 0x000000FF)) - ((colorTopRight & 0x000000FF));
                int deltaRed = (int) Math.sqrt(colorXRed * colorXRed + colorYRed * colorYRed);
                int deltaGreen = (int) Math.sqrt(colorXGreen * colorXGreen + colorYGreen * colorYGreen);
                int deltaBlue = (int) Math.sqrt(colorXBlue * colorXBlue + colorYBlue * colorYBlue);

                if (deltaRed > binarization || deltaGreen > binarization || deltaBlue > binarization){
                    red = 255;
                    green = 255;
                    blue = 255;
                }

                newGrid[index] |= (red << 16) | (green << 8) | blue;
            }
        }

        return getImageFactory().createImage(oldImage, newGrid);
    }


    private ImageInterface applySobel(ImageInterface oldImage){
        final int borderStep = 1;
        final int width = oldImage.getWidth();
        final int height = oldImage.getHeight();
        final int gridSize = oldImage.getGridSize();
        final int[] grid = oldImage.getGrid();
        final int[] newGrid = new int[gridSize];
        Arrays.fill(newGrid, 0xFF000000);

        int[][] matrixX = {
                {-1, -2 , -1},
                {0, 0, 0},
                {1, 2, 1}
        };

        int[][] matrixY = {
                {-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1}
        };

        //without borders
        for (int y = borderStep; y < height - borderStep; y++){
            for (int x = borderStep; x < width - borderStep; x++){
                int index = y * width + x;
                int redX = 0;
                int greenX = 0;
                int blueX = 0;
                int redY = 0;
                int greenY = 0;
                int blueY = 0;
                for (int inX = - borderStep; inX <= borderStep; inX++){
                    for (int inY = -borderStep; inY <= borderStep; inY++){
                        int inXIndex = inX + borderStep;
                        int inYIndex = inY + borderStep;
                        int gridIndex = index + inY * width + inX;
                        int pixelColor = grid[gridIndex];
                        int colorRed = (pixelColor & 0x00FF0000) >> 16;
                        int colorGreen = (pixelColor & 0x0000FF00) >> 8;
                        int colorBlue = pixelColor & 0x000000FF;
                        redX += colorRed * matrixX[inYIndex][inXIndex];
                        greenX += colorGreen * matrixX[inYIndex][inXIndex];
                        blueX += colorBlue * matrixX[inYIndex][inXIndex];
                        redY += colorRed * matrixY[inYIndex][inXIndex];
                        greenY +=  colorGreen * matrixY[inYIndex][inXIndex];
                        blueY +=  colorBlue * matrixY[inYIndex][inXIndex];
                    }
                }
                int deltaRed = (int) Math.sqrt(redX * redX + redY * redY);
                int deltaBlue = (int) Math.sqrt(blueX * blueX + blueY * blueY);
                int deltaGreen = (int) Math.sqrt(greenX * greenX + greenY * greenY);
                int red = 0;
                int green = 0;
                int blue = 0;

                if (deltaRed > binarization || deltaGreen > binarization || deltaBlue > binarization){
                    red = 255;
                    green = 255;
                    blue = 255;
                }

                newGrid[index] |= (red << 16) | (green << 8) | blue;
            }
        }

        return getImageFactory().createImage(oldImage, newGrid);
    }


    @Override
    public ImageInterface apply(ImageInterface oldImage) {
        if (type == EdgeDetectionType.SOBEL){
            return applySobel(oldImage);
        }
        return applyRoberts(oldImage);
    }
}
