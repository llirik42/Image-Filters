package ru.nsu.icg.lab2.gui.common;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageWriter {
    private static final String[] SUPPORTED_FORMATS = {"PNG"};

    public void save(BufferedImage image, File file) throws IOException {
        final String fileName = file.getName();

        for (final var it : SUPPORTED_FORMATS) {
            if (fileName.endsWith(it)) {
                ImageIO.write(image, it, file);
                return;
            }
        }

        throw new RuntimeException(String.format("Saving an image in an unsupported format \"%s\"", fileName));
    }

    public String[] getSupportedFormats() {
        return SUPPORTED_FORMATS.clone();
    }
}
