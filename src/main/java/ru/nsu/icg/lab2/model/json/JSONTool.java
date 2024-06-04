package ru.nsu.icg.lab2.model.json;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class JSONTool {
    private String name;

    private String tip;

    @SerializedName("icon")
    private String iconPath;

    @SerializedName("selectedIcon")
    private String selectedIconPath;

    @SerializedName("controller")
    private String controllerClassPath;

    private int group;

    @SerializedName("isToggle")
    private boolean isToggle;

    @SerializedName("hand")
    private boolean isHand;

    @SerializedName("back")
    private boolean isBack;

    @SerializedName("1:1")
    private boolean isOneToOne;

    @SerializedName("onWindowSize")
    private boolean isOnWindowSize;

    @SerializedName("selectedTip")
    private String selectedTip;

    @SerializedName("description")
    private String description;
}
