package ru.nsu.icg.lab2.gui.controller.tools;

import ru.nsu.icg.lab2.gui.common.Context;
import ru.nsu.icg.lab2.gui.common.DrawingAreaAction;
import ru.nsu.icg.lab2.gui.common.ToolController;
import ru.nsu.icg.lab2.gui.common.View;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.dto.Tool;

import java.awt.event.ActionEvent;

public class HandController extends ToolController {
    public HandController(Context context,
                          View view,
                          ImageFactory imageFactory,
                          Tool tool) {
        super(context, view, imageFactory, tool);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (getContext().getDrawingAreaAction() == DrawingAreaAction.SWAP_IMAGE) {
            getContext().setDrawingAreaAction(DrawingAreaAction.MOVE_SCROLLS);
        } else {
            getContext().setDrawingAreaAction(DrawingAreaAction.SWAP_IMAGE);
        }
    }
}
