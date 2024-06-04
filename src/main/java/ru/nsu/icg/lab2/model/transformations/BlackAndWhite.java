package ru.nsu.icg.lab2.model.transformations;

import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.ImageInterface;
import ru.nsu.icg.lab2.model.Transformation;

public class BlackAndWhite extends Transformation {
    public BlackAndWhite(ImageFactory imageFactory) {
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

            final int newComponent = getBlackAndWhite(oldRed, oldGreen, oldBlue);

            grid[pixel] = TransformationUtils.getARGB(
                    oldAlpha,
                    newComponent,
                    newComponent,
                    newComponent
            );
        }

        return getImageFactory().createImage(oldImage, grid);
    }

    private static int getBlackAndWhite(int red, int green, int blue) {
        return (int)(0.299 * red + 0.587 * green + 0.114 * blue);
    }
}
