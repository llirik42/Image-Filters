package ru.nsu.icg.lab2.gui.controller.tools;

import ru.nsu.icg.lab2.gui.common.Context;
import ru.nsu.icg.lab2.gui.common.ToolController;
import ru.nsu.icg.lab2.gui.common.Utils;
import ru.nsu.icg.lab2.gui.common.View;
import ru.nsu.icg.lab2.gui.controller.TextFieldSliderController;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.dto.Tool;
import ru.nsu.icg.lab2.model.transformations.Watercoloring;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class WatercoloringController extends ToolController {
    private static final int WATERCOLORING_MIN_VALUE = 3;
    private static final int WATERCOLORING_MAX_VALUE = 15;

    private final Watercoloring watercoloring;
    private final JPanel optionsSetWindow;
    private final TextFieldSliderController textFieldSliderController;

    public WatercoloringController(Context context,
                                   View view,
                                   ImageFactory imageFactory,
                                   Tool tool) {
        super(context, view, imageFactory, tool);
        watercoloring = new Watercoloring(imageFactory);
        final JSlider slider = new JSlider(WATERCOLORING_MIN_VALUE, WATERCOLORING_MAX_VALUE);
        final JTextField textField = new JTextField();
        optionsSetWindow = Utils.createSimpleSliderDialogInputPanel(
                textField,
                slider,
                "Window size:",
                1
        );
        textFieldSliderController = new TextFieldSliderController(
                textField,
                slider,
                WATERCOLORING_MIN_VALUE,
                WATERCOLORING_MAX_VALUE,
                Double::valueOf,
                Double::parseDouble,
                Double::intValue,
                aDouble -> String.valueOf(Math.round(aDouble)));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        final View view = getView();

        while (true) {
            textFieldSliderController.reset(watercoloring.getWindowSize());

            final boolean ok = view.showConfirmationDialog("Watercoloring", optionsSetWindow);
            if (!ok) {
                return;
            }

            if (textFieldSliderController.hasValue()) {
                watercoloring.setWindowSize(textFieldSliderController.getIntValue());
            }
            if (!textFieldSliderController.hasError()) {
                break;
            }
            view.showError(String.format(
                    "Incorrect window size number! It must be between %d and %d",
                    WATERCOLORING_MIN_VALUE,
                    WATERCOLORING_MAX_VALUE
            ));
        }

        getContext().setTransformation(watercoloring);
    }
}
