package ru.nsu.icg.lab2.gui.controller.tools;

import ru.nsu.icg.lab2.gui.common.Context;
import ru.nsu.icg.lab2.gui.common.View;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.dto.Tool;
import ru.nsu.icg.lab2.model.transformations.AbstractDithering;
import ru.nsu.icg.lab2.model.transformations.dithering.OrderedDithering;

public class OrderedDitheringController extends AbstractDitheringController {
    private final OrderedDithering orderedDithering;

    public OrderedDitheringController(Context context,
                                      View view,
                                      ImageFactory imageFactory,
                                      Tool tool) {
        super(context, view, imageFactory, tool);
        orderedDithering = new OrderedDithering(imageFactory);
    }

    @Override
    protected AbstractDithering getDithering() {
        return orderedDithering;
    }

    @Override
    protected String getDitheringName() {
        return "Ordered dithering";
    }
}
