package ru.nsu.icg.lab2.gui.common;

import ru.nsu.icg.lab2.model.ImageInterface;

import java.awt.image.BufferedImage;

public record BufferedImageImpl(BufferedImage bufferedImage) implements ImageInterface {
    @Override
    public int getWidth() {
        return bufferedImage.getWidth();
    }

    @Override
    public int getHeight() {
        return bufferedImage.getHeight();
    }

    @Override
    public int getGridSize() {
        return bufferedImage.getWidth() * bufferedImage.getHeight();
    }

    @Override
    public int[] getGrid() {
        return bufferedImage.getRGB(
                0,
                0,
                bufferedImage.getWidth(),
                bufferedImage.getHeight(),
                null,
                0,
                bufferedImage.getWidth()
        );
    }

    @Override
    public void setGrid(int[] grid) {
        bufferedImage.setRGB(
                0,
                0,
                bufferedImage.getWidth(),
                bufferedImage.getHeight(),
                grid,
                0,
                bufferedImage.getWidth()
        );
    }
}
