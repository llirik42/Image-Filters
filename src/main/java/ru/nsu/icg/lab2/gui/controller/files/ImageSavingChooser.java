package ru.nsu.icg.lab2.gui.controller.files;

import javax.swing.filechooser.FileFilter;

public class ImageSavingChooser extends ImageChooser {
    public ImageSavingChooser(String[] supportedFormats) {
        for (final var it : supportedFormats) {
            final FileFilter fileFilter = new FileSavingFilter(it);
            addChoosableFileFilter(fileFilter);
        }
    }
}
