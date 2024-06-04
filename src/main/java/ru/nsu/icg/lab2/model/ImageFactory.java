package ru.nsu.icg.lab2.model;

public interface ImageFactory {
    ImageInterface createImage(int width, int height, int[] grid);

    ImageInterface createImage(ImageInterface baseImage, int[] grid);
}
