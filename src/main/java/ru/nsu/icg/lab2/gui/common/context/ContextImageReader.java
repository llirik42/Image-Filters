package ru.nsu.icg.lab2.gui.common.context;

import lombok.Setter;
import ru.nsu.icg.lab2.gui.common.BufferedImageImpl;
import ru.nsu.icg.lab2.gui.common.Context;
import ru.nsu.icg.lab2.gui.common.View;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Setter
public class ContextImageReader {
    private static final String[] SUPPORTED_FORMATS = {"GIF", "PNG", "JPEG", "JPG", "BMP"};

    private Context context;
    private View view;

    public void read(File file) {
        try {
            final BufferedImageImpl image = new BufferedImageImpl(ImageIO.read(file));
            final int imageType = image.bufferedImage().getType();
            final int imageFactoryType = imageType == BufferedImage.TYPE_CUSTOM
                    ? BufferedImage.TYPE_INT_ARGB
                    : imageType;

            context.setCurrentFile(file);
            context.setWorkingDirectory(file.getParentFile());
            context.getBufferedImageFactory().setType(imageFactoryType);
            context.setOriginalImage(image);
            context.setProcessedImage(null);
            context.setImage(image);
        } catch (IOException exception) {
            view.showError(exception.getLocalizedMessage());
        }
    }

    public String[] getSupportedFormats() {
        return SUPPORTED_FORMATS.clone();
    }
}
