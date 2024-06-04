package ru.nsu.icg.lab2.gui.common;

import javax.swing.*;

public interface View {
    void show();

    void hide();

    void destroy();

    void resize();

    void showError(String errorMessage);

    void repaint();

    void showHelp();

    void showAbout();

    boolean showConfirmationDialog(String title, JPanel content);

    JFrame getFrame();

    JScrollPane getMainScrollPane();

    void setWaitCursor();

    void setDefaultCursor();
}
