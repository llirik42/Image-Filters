package ru.nsu.icg.lab2.model.transformations;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.ImageInterface;
import ru.nsu.icg.lab2.model.Transformation;

import java.util.Arrays;

@Setter
@Getter
public class FilterApplicator extends Transformation {
    public interface RGBCounter{
        int color(int red, int green, int blue);
    }

    private double[][] matrix;
    private int windowSize;
    private RGBCounter counter;

    public FilterApplicator(ImageFactory imageFactory) {
        super(imageFactory);
    }

    @Override
    public ImageInterface apply(ImageInterface oldImage){
        final int borderStep = (windowSize - 1) / 2;
        final int width = oldImage.getWidth();
        final int height = oldImage.getHeight();
        final int[] oldGrid = oldImage.getGrid();
        final int[] newGrid = new int[oldGrid.length];
        Arrays.fill(newGrid, 0xFF000000);

        //without borders
        for (int y = borderStep; y < height - borderStep; y++){
            for (int x = borderStep; x < width - borderStep; x++){
                int index = y * width + x;
                double red = 0;
                double green = 0;
                double blue = 0;
                for (int inX = - borderStep; inX <= borderStep; inX++){
                    for (int inY = -borderStep; inY <= borderStep; inY++){
                        int inXIndex = inX + borderStep;
                        int inYIndex = inY + borderStep;
                        int gridIndex = index + inY * width + inX;
                        int pixelColor = oldGrid[gridIndex];
                        red += ((((pixelColor & 0x00FF0000) >> 16) * matrix[inYIndex][inXIndex]));
                        green += ((((pixelColor & 0x0000FF00) >> 8) * matrix[inYIndex][inXIndex]));
                        blue += ((((pixelColor & 0x000000FF)) * matrix[inYIndex][inXIndex]));
                    }
                }
                newGrid[index] |= counter.color((int) red,(int) green,(int) blue);
            }
        }

        //process borders
        //top left corner
        for (int y = 0; y < borderStep; y++){
            for (int x = 0; x < borderStep; x++){
                int index = y * width + x;
                double red = 0;
                double green = 0;
                double blue = 0;
                for (int inX = - borderStep; inX <= borderStep; inX++){
                    for (int inY = -borderStep; inY <= borderStep; inY++){
                        int inXIndex = inX + borderStep;
                        int inYIndex = inY + borderStep;
                        int gridIndex = Math.abs(inY + y) * width + Math.abs(inX + x);
                        int pixelColor = oldGrid[gridIndex];
                        red += ((((pixelColor & 0x00FF0000) >> 16) * matrix[inYIndex][inXIndex]));
                        green += ((((pixelColor & 0x0000FF00) >> 8) * matrix[inYIndex][inXIndex]));
                        blue += ((((pixelColor & 0x000000FF)) * matrix[inYIndex][inXIndex]));
                    }
                }
                newGrid[index] |= counter.color((int) red,(int) green,(int) blue);
            }
        }

        //between top left corner and top right corner
        for (int y = 0; y < borderStep; y++){
            for (int x = borderStep; x < width - borderStep; x++){
                int index = y * width + x;
                double red = 0;
                double green = 0;
                double blue = 0;
                for (int inX = - borderStep; inX <= borderStep; inX++){
                    for (int inY = -borderStep; inY <= borderStep; inY++){
                        int inXIndex = inX + borderStep;
                        int inYIndex = inY + borderStep;
                        int gridIndex = Math.abs(inY + y) * width + inX + x;
                        int pixelColor = oldGrid[gridIndex];
                        red += ((((pixelColor & 0x00FF0000) >> 16) * matrix[inYIndex][inXIndex]));
                        green += ((((pixelColor & 0x0000FF00) >> 8) * matrix[inYIndex][inXIndex]));
                        blue += ((((pixelColor & 0x000000FF)) * matrix[inYIndex][inXIndex]));
                    }
                }
                newGrid[index] |= counter.color((int) red,(int) green,(int) blue);
            }
        }

        //top right corner
        for (int y = 0; y < borderStep; y++){
            for (int x = width - borderStep; x < width; x++){
                int index = y * width + x;
                double red = 0;
                double green = 0;
                double blue = 0;
                for (int inX = - borderStep; inX <= borderStep; inX++){
                    for (int inY = -borderStep; inY <= borderStep; inY++){
                        int inXIndex = inX + borderStep;
                        int inYIndex = inY + borderStep;
                        int gridIndex = Math.abs(inY + y) * width + ((inX + x) % width);
                        int pixelColor = oldGrid[gridIndex];
                        red += ((((pixelColor & 0x00FF0000) >> 16) * matrix[inYIndex][inXIndex]));
                        green += ((((pixelColor & 0x0000FF00) >> 8) * matrix[inYIndex][inXIndex]));
                        blue += ((((pixelColor & 0x000000FF)) * matrix[inYIndex][inXIndex]));
                    }
                }
                newGrid[index] |= counter.color((int) red,(int) green,(int) blue);
            }
        }

        //between top right corner and bottom right corner
        for (int y = borderStep; y < height - borderStep; y++){
            for (int x = width - borderStep; x < width; x++){
                int index = y * width + x;
                double red = 0;
                double green = 0;
                double blue = 0;
                for (int inX = - borderStep; inX <= borderStep; inX++){
                    for (int inY = -borderStep; inY <= borderStep; inY++){
                        int inXIndex = inX + borderStep;
                        int inYIndex = inY + borderStep;
                        int gridIndex = (y + inY) * width + ((inX + x) % width);
                        int pixelColor = oldGrid[gridIndex];
                        red += ((((pixelColor & 0x00FF0000) >> 16) * matrix[inYIndex][inXIndex]));
                        green += ((((pixelColor & 0x0000FF00) >> 8) * matrix[inYIndex][inXIndex]));
                        blue += ((((pixelColor & 0x000000FF)) * matrix[inYIndex][inXIndex]));
                    }
                }
                newGrid[index] |= counter.color((int) red,(int) green,(int) blue);
            }
        }

        //bottom right corner
        for (int y = height - borderStep; y < height; y++){
            for (int x = width - borderStep; x < width; x++){
                int index = y * width + x;
                double red = 0;
                double green = 0;
                double blue = 0;
                for (int inX = - borderStep; inX <= borderStep; inX++){
                    for (int inY = -borderStep; inY <= borderStep; inY++){
                        int inXIndex = inX + borderStep;
                        int inYIndex = inY + borderStep;
                        int gridIndex = ((y + inY) % height)  * width + ((inX + x) % width);
                        int pixelColor = oldGrid[gridIndex];
                        red += ((((pixelColor & 0x00FF0000) >> 16) * matrix[inYIndex][inXIndex]));
                        green += ((((pixelColor & 0x0000FF00) >> 8) * matrix[inYIndex][inXIndex]));
                        blue += ((((pixelColor & 0x000000FF)) * matrix[inYIndex][inXIndex]));
                    }
                }
                newGrid[index] |= counter.color((int) red,(int) green,(int) blue);
            }
        }

        //between bottom left corner and bottom right corner
        for (int y = height - borderStep; y < height; y++){
            for (int x = borderStep; x < width - borderStep; x++){
                int index = y * width + x;
                double red = 0;
                double green = 0;
                double blue = 0;
                for (int inX = - borderStep; inX <= borderStep; inX++){
                    for (int inY = -borderStep; inY <= borderStep; inY++){
                        int inXIndex = inX + borderStep;
                        int inYIndex = inY + borderStep;
                        int gridIndex = ((y + inY) % height)  * width + inX + x;
                        int pixelColor = oldGrid[gridIndex];
                        red += ((((pixelColor & 0x00FF0000) >> 16) * matrix[inYIndex][inXIndex]));
                        green += ((((pixelColor & 0x0000FF00) >> 8) * matrix[inYIndex][inXIndex]));
                        blue += ((((pixelColor & 0x000000FF)) * matrix[inYIndex][inXIndex]));
                    }
                }
                newGrid[index] |= counter.color((int) red,(int) green,(int) blue);
            }
        }

        //bottom left corner
        for (int y = height - borderStep; y < height; y++){
            for (int x = 0; x < borderStep; x++){
                int index = y * width + x;
                double red = 0;
                double green = 0;
                double blue = 0;
                for (int inX = - borderStep; inX <= borderStep; inX++){
                    for (int inY = -borderStep; inY <= borderStep; inY++){
                        int inXIndex = inX + borderStep;
                        int inYIndex = inY + borderStep;
                        int gridIndex = ((y + inY) % height)  * width + Math.abs(inX + x);
                        int pixelColor = oldGrid[gridIndex];
                        red += ((((pixelColor & 0x00FF0000) >> 16) * matrix[inYIndex][inXIndex]));
                        green += ((((pixelColor & 0x0000FF00) >> 8) * matrix[inYIndex][inXIndex]));
                        blue += ((((pixelColor & 0x000000FF)) * matrix[inYIndex][inXIndex]));
                    }
                }
                newGrid[index] |= counter.color((int) red,(int) green,(int) blue);
            }
        }

        //between bottom left corner and top left corner
        for (int y = borderStep; y < height - borderStep; y++){
            for (int x = 0; x < borderStep; x++){
                int index = y * width + x;
                double red = 0;
                double green = 0;
                double blue = 0;
                for (int inX = - borderStep; inX <= borderStep; inX++){
                    for (int inY = -borderStep; inY <= borderStep; inY++){
                        int inXIndex = inX + borderStep;
                        int inYIndex = inY + borderStep;
                        int gridIndex = (y + inY) * width + Math.abs(inX + x);
                        int pixelColor = oldGrid[gridIndex];
                        red += ((((pixelColor & 0x00FF0000) >> 16) * matrix[inYIndex][inXIndex]));
                        green += ((((pixelColor & 0x0000FF00) >> 8) * matrix[inYIndex][inXIndex]));
                        blue += ((((pixelColor & 0x000000FF)) * matrix[inYIndex][inXIndex]));
                    }
                }
                newGrid[index] |= counter.color((int) red,(int) green,(int) blue);
            }
        }

        return getImageFactory().createImage(oldImage, newGrid);
    }
}
