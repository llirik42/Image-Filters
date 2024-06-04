package ru.nsu.icg.lab2.model.transformations;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.ImageInterface;
import ru.nsu.icg.lab2.model.Transformation;

import java.util.Arrays;

@Setter
@Getter
public class Rotation extends Transformation {
    private int degrees;

    public Rotation(ImageFactory imageFactory) {
        super(imageFactory);
        degrees = 0;
    }

    @Override
    public ImageInterface apply(ImageInterface oldImage) {
        int width = oldImage.getWidth();
        int height = oldImage.getHeight();
        double centerX = (width - 1) / 2.0;
        double centerY = (height - 1) / 2.0;
        double sin = Math.sin((degrees / 180.0) * Math.PI);
        double cos = Math.cos((degrees / 180.0) * Math.PI);
        if (Math.abs(degrees) == 180){
            sin = 0.0;
        }
        double topLeftX =  (((-centerX) * cos) -  (centerY * sin));
        double topLeftY =  (((-centerX) * sin) + (centerY * cos));

        double  topRightX =  ((centerX * cos) - (centerY * sin));
        double  topRightY =  ((centerX * sin) + (centerY * cos));

        double  botLeftX =  (((-centerX) * cos) + (centerY * sin));
        double  botLeftY =  (((-centerX) * sin) - (centerY * cos));

        double  botRightX =  ((centerX * cos) + (centerY * sin));
        double  botRightY =  ((centerX * sin) - (centerY * cos));

        int newHeight = (int) Math.max(Math.abs(topLeftY - botRightY), Math.abs(topRightY - botLeftY)) + 1;
        int newWidth = (int) Math.max(Math.abs(botRightX - topLeftX), Math.abs(topRightX - botLeftX)) + 1;
        int newGridSize = newHeight * newWidth;
        final int[] oldGrid = oldImage.getGrid();
        final int[] newGrid = new int[newGridSize];
        Arrays.fill(newGrid, 0xFFFFFFFF);
        double newCenterX = (newWidth - 1) / 2.0;
        double newCenterY = (newHeight - 1) / 2.0;
        for (int y = 0; y < newHeight; y++){
            for (int x = 0; x < newWidth ; x++){
                int index = y * newWidth + x;
                double xR = x - newCenterX;
                double yR = -y + newCenterY;
                double newX = ((xR * cos) +  (yR * sin));
                double newY = (-(xR * sin) + (yR * cos));
                int oldY = (int) Math.floor(-(newY - centerY));
                int oldX = (int) Math.floor (newX + centerX);
                if (oldX < 0 || oldX >= width || oldY < 0 || oldY >= height){
                    continue;
                }
                int oldIndex = width * oldY + oldX;
                newGrid[index] = oldGrid[oldIndex];
            }
        }

        return getImageFactory().createImage(newWidth, newHeight, newGrid);
    }
}
