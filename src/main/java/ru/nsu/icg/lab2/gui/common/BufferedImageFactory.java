package ru.nsu.icg.lab2.gui.common;

import lombok.Data;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.ImageInterface;

import java.awt.image.BufferedImage;

@Data
public class BufferedImageFactory implements ImageFactory {
    private int type;

    @Override
    public ImageInterface createImage(int width, int height, int[] grid) {
        final ImageInterface result = createImage(width, height);
        result.setGrid(grid);
        return result;
    }

    @Override
    public ImageInterface createImage(ImageInterface baseImage, int[] grid) {
        final ImageInterface result = createImage(baseImage.getWidth(), baseImage.getHeight(), grid);
        result.setGrid(grid);
        return result;
    }

    private ImageInterface createImage(int width, int height) {
        return new BufferedImageImpl(new BufferedImage(width, height, type));
    }
}
