package ru.nsu.icg.lab2.model.transformations;

import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.ImageInterface;
import ru.nsu.icg.lab2.model.Transformation;

public class Watercoloring extends Transformation {
    private final MedianFilter medianFilter;
    private final Sharpening sharpening;

    public int getWindowSize() {
        return medianFilter.getWindowSize();
    }

    public void setWindowSize(int windowSize) {
        medianFilter.setWindowSize(windowSize);
    }

    public Watercoloring(ImageFactory imageFactory) {
        super(imageFactory);
        medianFilter = new MedianFilter(imageFactory);
        medianFilter.setWindowSize(5);
        sharpening = new Sharpening(imageFactory);
    }

    @Override
    public ImageInterface apply(ImageInterface oldImage) {
        return sharpening.apply(medianFilter.apply(oldImage));
    }
}
