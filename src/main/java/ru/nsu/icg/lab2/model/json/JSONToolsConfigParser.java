package ru.nsu.icg.lab2.model.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.nsu.icg.lab2.model.ToolsConfigParser;
import ru.nsu.icg.lab2.model.dto.Tool;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class JSONToolsConfigParser implements ToolsConfigParser {
    private final String fileName;

    public JSONToolsConfigParser(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Tool> parse() {
        final Type listOfTools = new TypeToken<ArrayList<JSONTool>>() {
        }.getType();

        try {
            final List<JSONTool> jsonTools = new Gson().fromJson(
                    new String(
                            Objects.requireNonNull(this.getClass().getResourceAsStream("/" + fileName)).readAllBytes(),
                            StandardCharsets.UTF_8
                    ),
                    listOfTools
            );

            return jsonTools.stream().map(JSONToolsConfigParser::convertJSONTool).collect(Collectors.toList());
        } catch (Exception exception) {
            throw new RuntimeException(String.format("Invalid tools config \"%s\"", fileName), exception);
        }
    }

    private static Tool convertJSONTool(JSONTool jsonTool) {
        return new Tool(
                jsonTool.getName(),
                jsonTool.getDescription(),
                jsonTool.getTip(),
                jsonTool.getSelectedTip(),
                jsonTool.getIconPath(),
                jsonTool.getSelectedIconPath(),
                jsonTool.getControllerClassPath(),
                jsonTool.getGroup(),
                jsonTool.isToggle(),
                jsonTool.isHand(),
                jsonTool.isBack(),
                jsonTool.isOneToOne(),
                jsonTool.isOnWindowSize()
        );
    }
}
