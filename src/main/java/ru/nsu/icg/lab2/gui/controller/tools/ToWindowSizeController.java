package ru.nsu.icg.lab2.gui.controller.tools;

import ru.nsu.icg.lab2.gui.common.*;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.dto.Tool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class ToWindowSizeController extends ToolController {
    private final JPanel optionsPanel;
    private final JComboBox<String> comboBox;
    private final HashMap<String, InterpolationMethod> methodHashMap;

    public ToWindowSizeController(Context context, View view, ImageFactory imageFactory, Tool tool) {
        super(context, view, imageFactory, tool);
        methodHashMap = new HashMap<>(Map.of(
                "Bilinear", InterpolationMethod.BILINEAR,
                "Bicubic", InterpolationMethod.BICUBIC,
                "Nearest neighbor", InterpolationMethod.NEAREST_NEIGHBOR
        ));
        comboBox = new JComboBox<>(methodHashMap.keySet().toArray(new String[0]));
        optionsPanel = Utils.createSimpleComboDialogInputPanel(comboBox, "Interpolation method:", 1);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        final Context context = getContext();

        if (context.getViewMode() == ViewMode.ON_WINDOW_SIZE) {
            return;
        }

        comboBox.setSelectedItem(context.getInterpolationMethod());

        final boolean ok = getView().showConfirmationDialog("Interpolation method", optionsPanel);
        if (!ok) {
            context.setViewMode(ViewMode.ONE_TO_ONE);
            return;
        }

        context.setInterpolationMethod(methodHashMap.get((String) comboBox.getSelectedItem()));
        context.setViewMode(ViewMode.ON_WINDOW_SIZE);
    }
}
