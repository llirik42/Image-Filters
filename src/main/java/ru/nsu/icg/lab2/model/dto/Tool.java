package ru.nsu.icg.lab2.model.dto;

public record Tool(
        String name,

        String description,
        String tip,
        String selectedTip,
        String iconPath,
        String selectedIconPath,
        String controllerClassPath,
        int group,
        boolean isToggle,
        boolean isHand,
        boolean isBack,
        boolean isOneToOne,
        boolean isOnWindowSize) {

    public boolean hasGroup() {
        return group != 0;
    }
}
