package ru.nsu.icg.lab2.gui.controller.tools;

import ru.nsu.icg.lab2.gui.common.Context;
import ru.nsu.icg.lab2.gui.common.ToolController;
import ru.nsu.icg.lab2.gui.common.Utils;
import ru.nsu.icg.lab2.gui.common.View;
import ru.nsu.icg.lab2.gui.controller.TextFieldSliderController;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.dto.Tool;
import ru.nsu.icg.lab2.model.transformations.GlassEffect;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class GlassEffectController extends ToolController {
    private static final int SPREAD_MIN_VALUE = 0;
    private static final int SPREAD_MAX_VALUE = 50;

    private final GlassEffect glassEffect;
    private final JPanel optionsSetWindow;
    private final TextFieldSliderController textFieldSliderController;

    public GlassEffectController(Context context,
                                 View view,
                                 ImageFactory imageFactory,
                                 Tool tool) {
        super(context, view, imageFactory, tool);
        glassEffect = new GlassEffect(imageFactory);
        final JSlider slider = new JSlider(SPREAD_MIN_VALUE, SPREAD_MAX_VALUE);
        final JTextField textField = new JTextField();
        optionsSetWindow = Utils.createSimpleSliderDialogInputPanel(
                textField,
                slider,
                "Spread:",
                1
        );
        textFieldSliderController = new TextFieldSliderController(
                textField,
                slider,
                SPREAD_MIN_VALUE,
                SPREAD_MAX_VALUE,
                Double::valueOf,
                Double::parseDouble,
                Double::intValue,
                aDouble -> String.valueOf(Math.round(aDouble)));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        final View view = getView();

        while (true) {
            textFieldSliderController.reset(glassEffect.getSpread());

            final boolean ok = view.showConfirmationDialog("Glass effect", optionsSetWindow);
            if (!ok) {
                return;
            }

            if (textFieldSliderController.hasValue()) {
                glassEffect.setSpread(textFieldSliderController.getIntValue());
            }
            if (!textFieldSliderController.hasError()) {
                break;
            }
            view.showError(String.format(
                    "Incorrect spread number! It must be between %d and %d",
                    SPREAD_MIN_VALUE,
                    SPREAD_MAX_VALUE
            ));
        }

        getContext().setTransformation(glassEffect);
    }
}
