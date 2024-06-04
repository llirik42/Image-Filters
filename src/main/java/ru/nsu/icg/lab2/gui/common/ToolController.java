package ru.nsu.icg.lab2.gui.common;

import lombok.Getter;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.dto.Tool;

import java.awt.event.ActionListener;

@Getter
public abstract class ToolController implements ActionListener {
    private final Context context;
    private final View view;
    private final ImageFactory imageFactory;
    private final Tool tool;

    protected ToolController(Context context, View view, ImageFactory imageFactory, Tool tool) {
        this.context = context;
        this.view = view;
        this.imageFactory = imageFactory;
        this.tool = tool;
    }
}
