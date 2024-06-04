package ru.nsu.icg.lab2.gui.controller.menu;

import ru.nsu.icg.lab2.gui.common.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ExitController extends WindowAdapter implements ActionListener {
    private final View view;

    public ExitController(View view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        doReaction();
    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        doReaction();
    }

    private void doReaction() {
        view.hide();
        view.destroy();
    }
}
