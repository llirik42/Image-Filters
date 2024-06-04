package ru.nsu.icg.lab2.model.transformations;

import lombok.Getter;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.ImageInterface;
import ru.nsu.icg.lab2.model.Transformation;

public class GammaCorrection extends Transformation {
    private final int[] correctedValues = new int[256];

    @Getter
    private double gamma;

    public GammaCorrection(ImageFactory imageFactory) {
        super(imageFactory);
        this.gamma = 1;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;

        for (int i = 0; i < 256; i++) {
            correctedValues[i] = (int) (Math.pow(i / 255.0, gamma) * 255);
        }
    }

    @Override
    public ImageInterface apply(ImageInterface oldImage) {
        final int[] grid = oldImage.getGrid();

        for (int pixel = 0; pixel < grid.length; pixel++) {
            final int oldAlpha = TransformationUtils.getAlpha(grid[pixel]);
            final int oldRed = TransformationUtils.getRed(grid[pixel]);
            final int oldGreen = TransformationUtils.getGreen(grid[pixel]);
            final int oldBlue = TransformationUtils.getBlue(grid[pixel]);

            final int newRed = correctedValues[oldRed];
            final int newGreen = correctedValues[oldGreen];
            final int newBlue = correctedValues[oldBlue];

            grid[pixel] = TransformationUtils.getARGB(
                    oldAlpha,
                    newRed,
                    newGreen,
                    newBlue
            );
        }

        return getImageFactory().createImage(oldImage, grid);
    }
}
