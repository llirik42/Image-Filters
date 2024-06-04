package ru.nsu.icg.lab2.model.dto.view;

public record ViewConfig(
        DrawingAreaConfig textAreaConfig,
        MainWindowConfig mainWindowConfig,
        ToolsAreaConfig toolsAreaConfig,
        MenuAreaConfig menuAreaConfig) {
}
