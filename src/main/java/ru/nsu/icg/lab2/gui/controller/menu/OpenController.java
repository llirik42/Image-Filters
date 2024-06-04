package ru.nsu.icg.lab2.gui.controller.menu;

import ru.nsu.icg.lab2.gui.common.View;
import ru.nsu.icg.lab2.gui.common.context.ContextImageReader;
import ru.nsu.icg.lab2.gui.controller.files.ImageOpeningChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpenController implements ActionListener {
    private final View view;
    private final ContextImageReader imageReader;
    private final JFileChooser fileChooser;

    public OpenController(View view, ContextImageReader imageReader) {
        this.view = view;
        this.imageReader = imageReader;
        fileChooser = new ImageOpeningChooser(imageReader.getSupportedFormats());
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        final int code = fileChooser.showOpenDialog(view.getFrame());

        if (code == JFileChooser.CANCEL_OPTION) {
            return;
        }

        if (code == JFileChooser.ERROR_OPTION) {
            view.showError("Cannot open file");
            return;
        }

        imageReader.read(fileChooser.getSelectedFile());
    }
}
