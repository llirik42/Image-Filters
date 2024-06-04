package ru.nsu.icg.lab2.gui.controller;

import ru.nsu.icg.lab2.gui.common.Context;
import ru.nsu.icg.lab2.gui.common.DrawingAreaAction;
import ru.nsu.icg.lab2.gui.common.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DrawingAreaController extends MouseAdapter {
    private final Context context;
    private final View view;

    private int prevX, prevY;

    public DrawingAreaController(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        final boolean swapMode = context.getDrawingAreaAction() == DrawingAreaAction.SWAP_IMAGE;
        final boolean hasImagesToSwap = context.getProcessedImage() != null && context.getOriginalImage() != null;

        if (swapMode && hasImagesToSwap) {
            context.swapImage();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (context.getDrawingAreaAction() == DrawingAreaAction.MOVE_SCROLLS) {
            prevX = e.getX();
            prevY = e.getY();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        final boolean isScrollMode = context.getDrawingAreaAction() == DrawingAreaAction.MOVE_SCROLLS;
        final boolean invalidMagic = e.getModifiersEx() == InputEvent.BUTTON3_DOWN_MASK;

        if (!isScrollMode || invalidMagic) {
            return;
        }

        final JScrollPane spIm = view.getMainScrollPane();
        final Point scroll = spIm.getViewport().getViewPosition();
        scroll.x += (prevX - e.getX());
        scroll.y += (prevY - e.getY());
        spIm.getHorizontalScrollBar().setValue(scroll.x);
        spIm.getVerticalScrollBar().setValue(scroll.y);
        spIm.repaint();
    }
}
