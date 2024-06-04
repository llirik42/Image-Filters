package ru.nsu.icg.lab2.gui.controller.files;

import javax.swing.*;

public abstract class ImageChooser extends JFileChooser {
    public ImageChooser() {
        setAcceptAllFileFilterUsed(false);
    }
}
