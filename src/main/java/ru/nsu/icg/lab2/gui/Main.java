package ru.nsu.icg.lab2.gui;

import ru.nsu.icg.lab2.gui.common.*;
import ru.nsu.icg.lab2.gui.common.context.ContextImageReader;
import ru.nsu.icg.lab2.gui.view.ViewImpl;
import ru.nsu.icg.lab2.model.ToolsConfigParser;
import ru.nsu.icg.lab2.model.ViewConfigParser;
import ru.nsu.icg.lab2.model.dto.Tool;
import ru.nsu.icg.lab2.model.dto.view.ViewConfig;
import ru.nsu.icg.lab2.model.json.JSONToolsConfigParser;
import ru.nsu.icg.lab2.model.json.JSONViewConfigParser;

import java.util.List;

public class Main {
    private static final ViewMode START_VIEW_MODE = ViewMode.ONE_TO_ONE;
    private static final InterpolationMethod START_INTERPOLATION_METHOD = InterpolationMethod.BILINEAR;

    public static void main(String[] args) {
        try {
            final Context context = new Context(new BufferedImageFactory());

            final ViewConfigParser viewConfigParser = new JSONViewConfigParser("view_config.json");
            final ViewConfig viewConfig = viewConfigParser.parse();

            final ToolsConfigParser toolsConfigParser = new JSONToolsConfigParser("tools_config.json");
            final List<Tool> tools = toolsConfigParser.parse();

            final ContextImageReader imageReader = new ContextImageReader();
            final ImageWriter imageWriter = new ImageWriter();

            final ViewImpl view = new ViewImpl(viewConfig, tools, context, imageReader, imageWriter);
            context.setViewMode(START_VIEW_MODE);
            context.setInterpolationMethod(START_INTERPOLATION_METHOD);
            imageReader.setView(view);
            imageReader.setContext(context);

            view.show();
        } catch (Exception exception) {
            System.err.println(exception.getLocalizedMessage());
        }
    }
}
