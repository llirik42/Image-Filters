package ru.nsu.icg.lab2.model.dto.view;

public record MainWindowConfig(
        String name,
        int windowPrefWidth,
        int windowPrefHeight,
        int windowMinWidth,
        int windowMinHeight,
        int scrollPaneBorderThickness) {
}
