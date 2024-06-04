package ru.nsu.icg.lab2.gui.controller.tools;

import org.decimal4j.util.DoubleRounder;
import ru.nsu.icg.lab2.gui.common.Context;
import ru.nsu.icg.lab2.gui.common.ToolController;
import ru.nsu.icg.lab2.gui.common.Utils;
import ru.nsu.icg.lab2.gui.common.View;
import ru.nsu.icg.lab2.gui.controller.TextFieldSliderController;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.dto.Tool;
import ru.nsu.icg.lab2.model.transformations.GammaCorrection;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class GammaCorrectionController extends ToolController {
    private static final int SLIDER_MIN_VALUE = 0;
    private static final int SLIDER_MAX_VALUE = 1000;
    private static final int SLIDER_MAX_VALUE_HALF = SLIDER_MAX_VALUE / 2;
    private static final double GAMMA_MIN_VALUE = 0.1;
    private static final double GAMMA_MAX_VALUE = 10;
    private static final double K = Math.log(GAMMA_MAX_VALUE) / SLIDER_MAX_VALUE_HALF;
    private static final String INCORRECT_VALUE_MESSAGE = String.format(
            "Incorrect gamma! It must be between %.1f and %.1f",
            GAMMA_MIN_VALUE,
            GAMMA_MAX_VALUE
    );

    private final GammaCorrection gammaCorrection;
    private final DoubleRounder rounder;
    private final TextFieldSliderController dialogWindowController;
    private final JPanel panel;

    public GammaCorrectionController(Context context,
                                     View view,
                                     ImageFactory imageFactory,
                                     Tool tool) {
        super(context, view, imageFactory, tool);
        gammaCorrection = new GammaCorrection(imageFactory);
        rounder = new DoubleRounder(1);
        final JTextField textField = new JTextField();
        final JSlider slider = new JSlider(SLIDER_MIN_VALUE, SLIDER_MAX_VALUE);
        panel = Utils.createSimpleSliderDialogInputPanel(
                textField,
                slider,
                "Gamma:",
                1
        );
        dialogWindowController = new TextFieldSliderController(
                textField,
                slider,
                GAMMA_MIN_VALUE,
                GAMMA_MAX_VALUE,
                this::getGammaFromSliderValue,
                this::getGammaFromTextField,
                this::getSliderValueFromGamma,
                Object::toString
        );
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        final View view = getView();

        while (true) {
            dialogWindowController.reset(gammaCorrection.getGamma());

            final boolean ok = view.showConfirmationDialog("Gamma", panel);
            if (!ok) {
                return;
            }

            if (dialogWindowController.hasValue()) {
                gammaCorrection.setGamma(dialogWindowController.getValue());
            }
            if (!dialogWindowController.hasError()) {
                break;
            }
            view.showError(INCORRECT_VALUE_MESSAGE);
        }

        getContext().setTransformation(gammaCorrection);
    }

    private double getGammaFromSliderValue(int sliderValue) {
        return rounder.round(Math.exp(K * (sliderValue - SLIDER_MAX_VALUE_HALF)));
    }

    private int getSliderValueFromGamma(double gamma) {
        return (int) Math.round(Math.log(gamma) / K + SLIDER_MAX_VALUE_HALF);
    }

    private double getGammaFromTextField(String text) {
        return rounder.round(Double.parseDouble(text));
    }
}
