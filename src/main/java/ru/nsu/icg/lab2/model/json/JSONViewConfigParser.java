package ru.nsu.icg.lab2.model.json;

import com.google.gson.Gson;
import ru.nsu.icg.lab2.model.ViewConfigParser;
import ru.nsu.icg.lab2.model.dto.view.ViewConfig;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class JSONViewConfigParser implements ViewConfigParser {
    private final String fileName;

    public JSONViewConfigParser(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public ViewConfig parse() {
        try {
            final String content = new String(
                    Objects.requireNonNull(this.getClass().getResourceAsStream("/" + fileName))
                            .readAllBytes(),
                    StandardCharsets.UTF_8);


            return new Gson().fromJson(content, ViewConfig.class);
        } catch (Exception exception) {
            throw new RuntimeException(String.format("Invalid view config \"%s\"", fileName), exception);
        }
    }
}
