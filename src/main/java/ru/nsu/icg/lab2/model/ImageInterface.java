package ru.nsu.icg.lab2.model;

public interface ImageInterface {
    int getWidth();

    int getHeight();

    int getGridSize();

    int[] getGrid();

    void setGrid(int[] grid);
}
