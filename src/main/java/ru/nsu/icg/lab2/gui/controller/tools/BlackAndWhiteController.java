package ru.nsu.icg.lab2.gui.controller.tools;

import ru.nsu.icg.lab2.gui.common.Context;
import ru.nsu.icg.lab2.gui.common.ToolController;
import ru.nsu.icg.lab2.gui.common.View;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.dto.Tool;
import ru.nsu.icg.lab2.model.transformations.BlackAndWhite;

import java.awt.event.ActionEvent;

public class BlackAndWhiteController extends ToolController {
    private final BlackAndWhite blackAndWhite;

    public BlackAndWhiteController(Context context,
                                   View view,
                                   ImageFactory imageFactory,
                                   Tool tool) {
        super(context, view, imageFactory, tool);
        blackAndWhite = new BlackAndWhite(imageFactory);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        getContext().setTransformation(blackAndWhite);
    }
}
