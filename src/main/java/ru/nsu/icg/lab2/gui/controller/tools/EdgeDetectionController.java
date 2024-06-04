package ru.nsu.icg.lab2.gui.controller.tools;

import ru.nsu.icg.lab2.gui.common.Context;
import ru.nsu.icg.lab2.gui.common.ToolController;
import ru.nsu.icg.lab2.gui.common.Utils;
import ru.nsu.icg.lab2.gui.common.View;
import ru.nsu.icg.lab2.gui.controller.TextFieldSliderController;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.dto.Tool;
import ru.nsu.icg.lab2.model.transformations.EdgeDetection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class EdgeDetectionController extends ToolController {
    private static final int SLIDER_MIN_VALUE = 0;
    private static final int SLIDER_MAX_VALUE = 1000;
    private static final String INCORRECT_VALUE_MESSAGE = String.format(
            "Incorrect binarization! It must be between %d and %d",
            SLIDER_MIN_VALUE,
            SLIDER_MAX_VALUE
    );

    private final EdgeDetection edgeDetection;
    private final JPanel optionsSetWindow;
    private final TextFieldSliderController textFieldSliderController;
    private final JComboBox<String> algComboBox;
    private final HashMap<String, EdgeDetection.EdgeDetectionType> edgeDetectionTypeHashMap;

    public EdgeDetectionController(Context context,
                                   View view,
                                   ImageFactory imageFactory,
                                   Tool tool) {
        super(context, view, imageFactory, tool);
        edgeDetection = new EdgeDetection(imageFactory);
        edgeDetectionTypeHashMap = new HashMap<>(Map.of(
                "Sobel", EdgeDetection.EdgeDetectionType.SOBEL,
                "Roberts", EdgeDetection.EdgeDetectionType.ROBERTS
        ));
        algComboBox = new JComboBox<>(edgeDetectionTypeHashMap.keySet().toArray(new String[0]));
        JTextField textField = new JTextField();
        JSlider slider = new JSlider(SLIDER_MIN_VALUE, SLIDER_MAX_VALUE);
        optionsSetWindow = Utils.createSimpleSliderDialogInputPanel(
                textField,
                slider,
                "Binarization:",
                1
        );
        Utils.addComboBoxTo3ColsPanel(
                optionsSetWindow,
                algComboBox,
                "Algorithm:",
                1
        );
        textFieldSliderController = new TextFieldSliderController(
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
            String edgeDetectionType = null;
            for (Map.Entry<String, EdgeDetection.EdgeDetectionType> entry : edgeDetectionTypeHashMap.entrySet()) {
                if (entry.getValue() == edgeDetection.getType()) {
                    edgeDetectionType = entry.getKey();
                }
            }

            if (edgeDetectionType == null) {
                throw new IllegalArgumentException("No such edge detection type: " + edgeDetection.getType());
            }

            algComboBox.setSelectedItem(edgeDetectionType);
            textFieldSliderController.reset(edgeDetection.getBinarization());

            final boolean ok = view.showConfirmationDialog("Edge Detection", optionsSetWindow);
            if (!ok) {
                return;
            }

            if (textFieldSliderController.hasValue()) {
                edgeDetection.setBinarization(textFieldSliderController.getIntValue());
            }
            if (!textFieldSliderController.hasError()) {
                edgeDetection.setType(edgeDetectionTypeHashMap.get((String) algComboBox.getSelectedItem()));
                break;
            }
            view.showError(INCORRECT_VALUE_MESSAGE);
        }

        getContext().setTransformation(edgeDetection);
    }
}
