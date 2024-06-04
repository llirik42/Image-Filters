package ru.nsu.icg.lab2.model.transformations;

import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.ImageInterface;
import ru.nsu.icg.lab2.model.Transformation;

public class Sharpening extends Transformation {
    private static final int WINDOW_SIZE = 3;
    private static final double[][] MATRIX = {
            {0, -1, 0},
            {-1, 5, -1},
            {0, -1, 0}
    };

    private final FilterApplicator filterApplicator;

    public Sharpening(ImageFactory imageFactory) {
        super(imageFactory);

        filterApplicator = new FilterApplicator(imageFactory);
        filterApplicator.setWindowSize(WINDOW_SIZE);
        filterApplicator.setMatrix(MATRIX);
        filterApplicator.setCounter((red, green, blue) -> {
            red = Integer.min(255, Integer.max(red, 0));
            green = Integer.min(255, Integer.max(green, 0));
            blue = Integer.min(255, Integer.max(blue, 0));
            return (red << 16) | (green << 8) | blue;
        });
    }

    public ImageInterface apply(ImageInterface oldImage) {
        return filterApplicator.apply(oldImage);
    }
}
