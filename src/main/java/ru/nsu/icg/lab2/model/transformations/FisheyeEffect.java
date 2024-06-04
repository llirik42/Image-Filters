package ru.nsu.icg.lab2.model.transformations;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.ImageInterface;
import ru.nsu.icg.lab2.model.Transformation;

import java.util.Arrays;

@Getter
public class FisheyeEffect extends Transformation {

    @Setter
    private double amplitude;


    public FisheyeEffect(ImageFactory imageFactory) {
        super(imageFactory);
        amplitude = 1;
    }

    @Override
    public ImageInterface apply(ImageInterface oldImage) {
        int width = oldImage.getWidth();
        int height = oldImage.getHeight();
        int gridSize = height * width;
        int[] grid = oldImage.getGrid();
        int[] newGrid = new int[gridSize];
        Arrays.fill(newGrid, 0xFF000000);
        int centerX = width / 2;
        int centerY = height / 2;
        double center = amplitude * Math.min(centerX, centerY) ;
        int maxNewX = 0;
        int minNewX = width;
        int maxNewY = 0;
        int minNewY = height;



        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double deltaX = x - centerX;
                double deltaY = y - centerY;

                double r = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

                double rNew = (center / (Math.PI * r)) * Math.atan((Math.PI * r ) / center);

                int newX = (int) (centerX + rNew * deltaX);
                int newY = (int) (centerY + rNew * deltaY);


                if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                    newGrid[newY * width + newX] = grid[y * width + x];
                    if (newX == 0 || newY == 0){
                        continue;
                    }
                    if (newX > maxNewX){
                        maxNewX = newX;
                    }
                    if (newX < minNewX){
                        minNewX = newX;
                    }
                    if (newY > maxNewY){
                        maxNewY = newY;
                    }
                    if (newY < minNewY){
                        minNewY = newY;
                    }
                }
            }
        }


        int newWidth = maxNewX - minNewX + 1;
        int newHeight = maxNewY - minNewY + 1;
        int[] gridFinal = new int[newWidth * newHeight];
        for (int y = minNewY; y <= maxNewY; y++){
            if (maxNewX + 1 - minNewX >= 0)
                System.arraycopy(newGrid, y * width + minNewX, gridFinal, (y - minNewY) * newWidth, maxNewX + 1 - minNewX);
        }

        return getImageFactory().createImage(newWidth, newHeight, gridFinal);
    }
}
