package ru.nsu.icg.lab2.gui.controller.tools;

import org.decimal4j.util.DoubleRounder;
import ru.nsu.icg.lab2.gui.common.Context;
import ru.nsu.icg.lab2.gui.common.ToolController;
import ru.nsu.icg.lab2.gui.common.Utils;
import ru.nsu.icg.lab2.gui.common.View;
import ru.nsu.icg.lab2.gui.controller.TextFieldSliderController;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.dto.Tool;
import ru.nsu.icg.lab2.model.transformations.FisheyeEffect;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class FisheyeEffectController extends ToolController {
    private final static int SLIDER_MIN_VALUE = 5;
    private final static int SLIDER_MAX_VALUE = 120;
    private final static double AMP_MIN_VALUE = 0.5;
    private final static double AMP_MAX_VALUE = 12;

    private final FisheyeEffect fisheyeEffect;
    private final JPanel optionsSetWindow;
    private final TextFieldSliderController textFieldSliderController;

    public FisheyeEffectController(Context context,
                                   View view,
                                   ImageFactory imageFactory,
                                   Tool tool) {
        super(context, view, imageFactory, tool);
        fisheyeEffect = new FisheyeEffect(imageFactory);
        JSlider slider = new JSlider(SLIDER_MIN_VALUE, SLIDER_MAX_VALUE);
        JTextField textField = new JTextField();
        this.optionsSetWindow = Utils.createSimpleSliderDialogInputPanel(
                textField,
                slider,
                "Amplitude:",
                1
        );
        DoubleRounder doubleRounder = new DoubleRounder(1);
        textFieldSliderController = new TextFieldSliderController(
                textField,
                slider,
                AMP_MIN_VALUE,
                AMP_MAX_VALUE,
                integer -> (integer * 1.0) / 10,
                s -> doubleRounder.round(Double.parseDouble(s)),
                aDouble -> (int) (doubleRounder.round(aDouble) * 10),
                aDouble -> doubleRounder.round(aDouble) + "");
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        final View view = getView();

        while (true) {
            textFieldSliderController.reset(fisheyeEffect.getAmplitude());

            final boolean ok = view.showConfirmationDialog("Fish eye", optionsSetWindow);
            if (!ok) {
                return;
            }

            if (textFieldSliderController.hasValue()) {
                fisheyeEffect.setAmplitude(textFieldSliderController.getValue());
            }
            if (!textFieldSliderController.hasError()) {
                break;
            }
            view.showError(String.format(
                    "Incorrect amplitude number! It must be between %.1f and %.1f",
                    AMP_MIN_VALUE,
                    AMP_MAX_VALUE
            ));
        }

        getContext().setTransformation(fisheyeEffect);
    }
}
