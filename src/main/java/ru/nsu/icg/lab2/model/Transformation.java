package ru.nsu.icg.lab2.model;

public abstract class Transformation {
    private final ImageFactory imageFactory;

    protected Transformation(ImageFactory imageFactory) {
        this.imageFactory = imageFactory;
    }

    protected ImageFactory getImageFactory() {
        return imageFactory;
    }

    public abstract ImageInterface apply(ImageInterface oldImage);
}
