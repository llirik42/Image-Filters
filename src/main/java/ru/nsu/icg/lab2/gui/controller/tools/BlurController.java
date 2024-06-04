package ru.nsu.icg.lab2.gui.controller.tools;

import org.decimal4j.util.DoubleRounder;
import ru.nsu.icg.lab2.gui.common.Context;
import ru.nsu.icg.lab2.gui.common.ToolController;
import ru.nsu.icg.lab2.gui.common.Utils;
import ru.nsu.icg.lab2.gui.common.View;
import ru.nsu.icg.lab2.gui.controller.TextFieldSliderController;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.dto.Tool;
import ru.nsu.icg.lab2.model.transformations.Blur;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class BlurController extends ToolController {
    private static final int MATRIX_SLIDER_MIN_VALUE = 3;
    private static final int MATRIX_SLIDER_MAX_VALUE = 11;
    private static final int MATRIX_MIN_VALUE = 3;
    private static final int MATRIX_MAX_VALUE = 11;
    private static final int SIGMA_SLIDER_MIN_VALUE = 1;
    private static final int SIGMA_SLIDER_MAX_VALUE = 50;
    private static final double SIGMA_MIN_VALUE = 0.1;
    private static final double SIGMA_MAX_VALUE = 5;

    private final Blur blur;
    private final JPanel optionsPanel;
    private final TextFieldSliderController matrixSizeController;
    private final TextFieldSliderController sigmaController;
    private final DoubleRounder doubleRounder;

    public BlurController(Context context,
                          View view,
                          ImageFactory imageFactory,
                          Tool tool) {
        super(context, view, imageFactory, tool);
        blur = new Blur(imageFactory);
        doubleRounder = new DoubleRounder(1);
        final JSlider matrixSlider = new JSlider(MATRIX_SLIDER_MIN_VALUE, MATRIX_SLIDER_MAX_VALUE);
        final JTextField matrixTextField = new JTextField();
        optionsPanel = Utils.createSimpleSliderDialogInputPanel(
                matrixTextField,
                matrixSlider,
                "Matrix size:",
                1
        );
        final JSlider sigmaSlider = new JSlider(SIGMA_SLIDER_MIN_VALUE, SIGMA_SLIDER_MAX_VALUE);
        final JTextField sigmaTextField = new JTextField();
        Utils.addSyncSliderTo3ColsPanel(
                optionsPanel,
                sigmaTextField,
                sigmaSlider,
                "Sigma:",
                1
        );
        matrixSizeController = new TextFieldSliderController(
                matrixTextField,
                matrixSlider,
                MATRIX_MIN_VALUE,
                MATRIX_MAX_VALUE,
                integer -> (double) ((integer / 2) * 2 + 1),
                s -> (double) (Integer.parseInt(s)),
                Double::intValue,
                aDouble -> aDouble.intValue() + ""
        );
        sigmaController = new TextFieldSliderController(
                sigmaTextField,
                sigmaSlider,
                SIGMA_MIN_VALUE,
                SIGMA_MAX_VALUE,
                integer -> (integer * 1.0) / 10,
                s -> doubleRounder.round(Double.parseDouble(s)),
                aDouble -> (int) (doubleRounder.round(aDouble) * 10),
                aDouble -> doubleRounder.round(aDouble) + ""
        );
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        final View view = getView();

        while (true) {
            matrixSizeController.reset(blur.getWindowSize());
            sigmaController.reset(blur.getSigma());
            final boolean ok = view.showConfirmationDialog("Blur", optionsPanel);
            if (!ok) {
                return;
            }

            if (matrixSizeController.hasError()) {
                view.showError(String.format(
                        "Incorrect matrix size! It must be between %d and %d and odd",
                        MATRIX_MIN_VALUE,
                        MATRIX_MAX_VALUE
                ));
                continue;
            }
            if (sigmaController.hasError()) {
                view.showError(String.format(
                        "Incorrect sigma! It must be between %.1f and %.1f",
                        SIGMA_MIN_VALUE,
                        SIGMA_MAX_VALUE
                ));
                continue;
            }
            blur.setParameters(sigmaController.getValue(), matrixSizeController.getIntValue());
            break;
        }

        getContext().setTransformation(blur);
    }
}
