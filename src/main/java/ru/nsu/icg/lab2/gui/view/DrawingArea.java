package ru.nsu.icg.lab2.gui.view;

import lombok.Setter;
import ru.nsu.icg.lab2.model.dto.view.DrawingAreaConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;

@Setter
public class DrawingArea extends JPanel {
    private BufferedImage image;

    public DrawingArea(MouseAdapter mouseAdapter, DrawingAreaConfig drawingAreaConfig) {
        setBorder(BorderFactory.createDashedBorder(
                Color.GREEN,
                drawingAreaConfig.borderThickness(),
                drawingAreaConfig.borderLength(),
                drawingAreaConfig.borderSpacing(),
                false
        ));
        setBackground(Color.DARK_GRAY);
        setDoubleBuffered(true);
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    public void resizeSoftly(int width, int height) {
        setPreferredSize(new Dimension(
                width + getInsets().left + getInsets().right,
                height + getInsets().top + getInsets().bottom
        ));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, getInsets().top, getInsets().left, null);
    }
}
