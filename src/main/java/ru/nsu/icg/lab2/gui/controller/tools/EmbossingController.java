package ru.nsu.icg.lab2.gui.controller.tools;

import ru.nsu.icg.lab2.gui.common.Context;
import ru.nsu.icg.lab2.gui.common.ToolController;
import ru.nsu.icg.lab2.gui.common.Utils;
import ru.nsu.icg.lab2.gui.common.View;
import ru.nsu.icg.lab2.gui.controller.TextFieldSliderController;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.dto.Tool;
import ru.nsu.icg.lab2.model.transformations.Embossing;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class EmbossingController extends ToolController {
    private static final int SLIDER_MIN_VALUE = 0;
    private static final int SLIDER_MAX_VALUE = 255;
    private static final int BRIGHTNESS_MIN_VALUE = 0;
    private static final int BRIGHTNESS_MAX_VALUE = 255;
    private final JPanel optionsSetWindow;
    private final TextFieldSliderController textFieldSliderController;
    private final Embossing embossing;

    public EmbossingController(Context context,
                               View view,
                               ImageFactory imageFactory,
                               Tool tool) {
        super(context, view, imageFactory, tool);
        embossing = new Embossing(imageFactory);
        JSlider slider = new JSlider(SLIDER_MIN_VALUE, SLIDER_MAX_VALUE);
        JTextField textField = new JTextField();
        this.optionsSetWindow = Utils.createSimpleSliderDialogInputPanel(
                textField,
                slider,
                "Brightness:",
                1
        );
        textFieldSliderController = new TextFieldSliderController(
                textField,
                slider,
                BRIGHTNESS_MIN_VALUE,
                BRIGHTNESS_MAX_VALUE,
                Double::valueOf,
                Double::parseDouble,
                Double::intValue,
                aDouble -> String.valueOf(Math.round(aDouble)));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        final View view = getView();

        while (true) {
            textFieldSliderController.reset(embossing.getBrightness());

            final boolean ok = view.showConfirmationDialog("Embossing", optionsSetWindow);
            if (!ok) {
                return;
            }

            if (textFieldSliderController.hasValue()) {
                embossing.setBrightness(textFieldSliderController.getIntValue());
            }
            if (!textFieldSliderController.hasError()) {
                break;
            }
            view.showError(String.format(
                    "Incorrect brightness number! It must be between %d and %d",
                    BRIGHTNESS_MIN_VALUE,
                    BRIGHTNESS_MAX_VALUE
            ));
        }

        getContext().setTransformation(embossing);
    }
}
