package ru.nsu.icg.lab2.gui.controller.tools;

import ru.nsu.icg.lab2.gui.common.Context;
import ru.nsu.icg.lab2.gui.common.ToolController;
import ru.nsu.icg.lab2.gui.common.Utils;
import ru.nsu.icg.lab2.gui.common.View;
import ru.nsu.icg.lab2.gui.controller.TextFieldSliderController;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.dto.Tool;
import ru.nsu.icg.lab2.model.transformations.Rotation;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RotationController extends ToolController {
    private static final int SLIDER_MIN_VALUE = -180;
    private static final int SLIDER_MAX_VALUE = 180;
    private static final String INCORRECT_VALUE_MESSAGE = String.format(
            "Incorrect rotation! It must be between %d and %d",
            SLIDER_MIN_VALUE,
            SLIDER_MAX_VALUE
    );

    private final Rotation rotation;
    private final TextFieldSliderController dialogWindowController;
    private final JPanel panel;

    public RotationController(Context context,
                              View view,
                              ImageFactory imageFactory,
                              Tool tool) {
        super(context, view, imageFactory, tool);

        rotation = new Rotation(imageFactory);
        final JTextField textField = new JTextField();
        final JSlider slider = new JSlider(SLIDER_MIN_VALUE, SLIDER_MAX_VALUE);
        panel = Utils.createSimpleSliderDialogInputPanel(
                textField,
                slider,
                "Angle:",
                1
        );
        dialogWindowController = new TextFieldSliderController(
                textField,
                slider,
                SLIDER_MIN_VALUE,
                SLIDER_MAX_VALUE,
                Double::valueOf,
                Double::parseDouble,
                Double::intValue,
                (x) -> String.valueOf(Math.round(x))
        );
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        final View view = getView();

        while (true) {
            dialogWindowController.reset(rotation.getDegrees());

            final boolean ok = view.showConfirmationDialog("Rotation", panel);
            if (!ok) {
                return;
            }

            if (dialogWindowController.hasValue()) {
                rotation.setDegrees(dialogWindowController.getIntValue());
            }
            if (!dialogWindowController.hasError()) {
                break;
            }
            view.showError(INCORRECT_VALUE_MESSAGE);
        }

        getContext().setTransformation(rotation);
    }
}
