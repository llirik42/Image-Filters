package ru.nsu.icg.lab2.gui.controller.menu;

import ru.nsu.icg.lab2.gui.common.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HelpController implements ActionListener {
    private final View view;

    public HelpController(View view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        view.showHelp();
    }
}
