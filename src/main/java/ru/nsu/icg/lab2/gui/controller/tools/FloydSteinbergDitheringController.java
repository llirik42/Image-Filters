package ru.nsu.icg.lab2.gui.controller.tools;

import ru.nsu.icg.lab2.gui.common.Context;
import ru.nsu.icg.lab2.gui.common.View;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.dto.Tool;
import ru.nsu.icg.lab2.model.transformations.AbstractDithering;
import ru.nsu.icg.lab2.model.transformations.dithering.FloydSteinbergDithering;

public class FloydSteinbergDitheringController extends AbstractDitheringController {
    private final FloydSteinbergDithering floydSteinbergDithering;

    public FloydSteinbergDitheringController(Context context,
                                             View view,
                                             ImageFactory imageFactory,
                                             Tool tool) {
        super(context, view, imageFactory, tool);
        floydSteinbergDithering = new FloydSteinbergDithering(imageFactory);
    }

    @Override
    protected AbstractDithering getDithering() {
        return floydSteinbergDithering;
    }

    @Override
    protected String getDitheringName() {
        return "Floyd-Steinberg dithering";
    }
}
