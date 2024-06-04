package ru.nsu.icg.lab2.gui.controller.files;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FileSavingFilter extends FileFilter {
    private final String description;

    public FileSavingFilter(String description) {
        this.description = description;
    }

    @Override
    public boolean accept(File file) {
        return true;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
