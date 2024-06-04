package ru.nsu.icg.lab2.model.transformations;

import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.ImageInterface;
import ru.nsu.icg.lab2.model.Transformation;

public class Inversion extends Transformation {
    public Inversion(ImageFactory imageFactory) {
        super(imageFactory);
    }

    @Override
    public ImageInterface apply(ImageInterface oldImage) {
        final int[] grid = oldImage.getGrid();

        for (int pixel = 0; pixel < grid.length; pixel++) {
            final int oldAlpha = TransformationUtils.getAlpha(grid[pixel]);
            final int oldRed = TransformationUtils.getRed(grid[pixel]);
            final int oldGreen = TransformationUtils.getGreen(grid[pixel]);
            final int oldBlue = TransformationUtils.getBlue(grid[pixel]);

            final int newRed = getInvertedValue(oldRed);
            final int newGreen = getInvertedValue(oldGreen);
            final int newBlue = getInvertedValue(oldBlue);

            grid[pixel] = TransformationUtils.getARGB(
                    oldAlpha,
                    newRed,
                    newGreen,
                    newBlue
            );
        }

        return getImageFactory().createImage(oldImage, grid);
    }

    private static int getInvertedValue(final int value) {
        return 255 - value;
    }
}
