package ru.nsu.icg.lab2.model.transformations;

import lombok.Getter;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.ImageInterface;
import ru.nsu.icg.lab2.model.Transformation;

public class Blur extends Transformation {
    private final FilterApplicator filterApplicator;

    @Getter
    private int windowSize;

    @Getter
    private double sigma;

    public Blur(ImageFactory imageFactory) {
        super(imageFactory);
        windowSize = 5;
        sigma = 1;
        filterApplicator = new FilterApplicator(imageFactory);
        filterApplicator.setCounter(((red, green, blue) -> (red << 16) | (green << 8) | blue));
    }

    public void setParameters(double sigma, int windowSize) {
        this.sigma = sigma;
        this.windowSize = windowSize;
        final double[][] matrix = new double[windowSize][windowSize];

        final int borderStep = (windowSize - 1) / 2;
        final double squared_doubled_sigma = 2 * sigma * sigma;
        final double k = 1.0 / (Math.PI * squared_doubled_sigma);
        double elemSum = 0.0;
        for (int y = 0; y < windowSize; y++){
            for (int x = 0; x < windowSize; x++){
                final int xMoved = x - borderStep;
                final int yMoved = y - borderStep;
                final double numerator = xMoved * xMoved + yMoved * yMoved;
                matrix[y][x] = k * Math.exp(-numerator / squared_doubled_sigma);
                elemSum += matrix[y][x];
            }
        }
        for (int y = 0; y < windowSize; y++){
            for (int x = 0; x < windowSize; x++){
                matrix[y][x] = matrix[y][x] / elemSum;
            }
        }
        filterApplicator.setMatrix(matrix);
        filterApplicator.setWindowSize(windowSize);
    }

    @Override
    public ImageInterface apply(ImageInterface oldImage) {
        return filterApplicator.apply(oldImage);
    }
}
