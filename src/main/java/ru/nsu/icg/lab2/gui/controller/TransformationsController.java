package ru.nsu.icg.lab2.gui.controller;

import ru.nsu.icg.lab2.gui.common.BufferedImageImpl;
import ru.nsu.icg.lab2.gui.common.Context;
import ru.nsu.icg.lab2.gui.common.View;
import ru.nsu.icg.lab2.gui.common.context.ContextTransformationListener;
import ru.nsu.icg.lab2.model.Transformation;

public class TransformationsController implements ContextTransformationListener {
    private final View view;

    public TransformationsController(View view) {
        this.view = view;
    }

    @Override
    public void onTransformationChange(Context context) {
        if (context.getImage() == null) {
            view.showError("No image to process!");
            return;
        }

        view.setWaitCursor();

        final Transformation transformation = context.getTransformation();

        // We know exactly what type of image we use, so we can safely cast
        final BufferedImageImpl image = (BufferedImageImpl) transformation.apply(context.getOriginalImage());

        context.setProcessedImage(image);
        context.setImage(image);

        view.repaint();
        view.setDefaultCursor();
    }
}
