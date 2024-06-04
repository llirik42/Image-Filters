package ru.nsu.icg.lab2.gui.controller.tools;

import ru.nsu.icg.lab2.gui.common.BufferedImageImpl;
import ru.nsu.icg.lab2.gui.common.Context;
import ru.nsu.icg.lab2.gui.common.ToolController;
import ru.nsu.icg.lab2.gui.common.View;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.dto.Tool;

import java.awt.event.ActionEvent;

public class BackController extends ToolController {
    public BackController(Context context,
                          View view,
                          ImageFactory imageFactory,
                          Tool tool) {
        super(context, view, imageFactory, tool);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        final Context context = getContext();
        final BufferedImageImpl processedImage = context.getProcessedImage();

        if (processedImage == null) {
            return;
        }

        final BufferedImageImpl newCurrentImage = context.getImage() == context.getProcessedImage()
                ? context.getOriginalImage()
                : processedImage;

        context.setImage(newCurrentImage);
    }
}
