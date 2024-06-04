package ru.nsu.icg.lab2.gui.view;

import lombok.Getter;
import ru.nsu.icg.lab2.gui.common.Context;
import ru.nsu.icg.lab2.gui.common.DrawingAreaAction;
import ru.nsu.icg.lab2.gui.common.ToolController;
import ru.nsu.icg.lab2.gui.common.ViewMode;
import ru.nsu.icg.lab2.gui.common.context.ContextDrawingAreaActionListener;
import ru.nsu.icg.lab2.gui.common.context.ContextImageListener;
import ru.nsu.icg.lab2.gui.common.context.ContextViewModeListener;
import ru.nsu.icg.lab2.model.dto.Tool;
import ru.nsu.icg.lab2.model.dto.view.MenuAreaConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuArea extends JPanel implements ContextViewModeListener,
        ContextDrawingAreaActionListener,
        ContextImageListener {
    private JMenuItem handButton;
    private JMenuItem backButton;
    private JMenuItem onWindowSizeButton;
    private JMenuItem oneToOneButton;

    private final String undoLabel;
    private final String redoLabel;

    private final Font font;
    private final Color menuBackgroundColor;
    private final Color buttonsFontColor;

    @Getter
    private final JMenuBar menuBar;

    public MenuArea(ActionListener openListener,
                    ActionListener saveListener,
                    ActionListener exitListener,
                    ActionListener helpListener,
                    ActionListener aboutListener,
                    List<ToolController> toolControllers,
                    MenuAreaConfig menuAreaConfig) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        menuBackgroundColor = Color.decode(menuAreaConfig.menuBackgroundColor());
        setBackground(menuBackgroundColor);

        redoLabel = menuAreaConfig.redoLabel();
        undoLabel = menuAreaConfig.undoLabel();

        int fontType;
        switch (menuAreaConfig.fontType()) {
            case "Bold" -> fontType = Font.BOLD;
            case "Italic" -> fontType = Font.ITALIC;
            case "Plain" -> fontType = Font.PLAIN;
            default -> throw new IllegalArgumentException("No such font type");
        }

        font = new Font(menuAreaConfig.fontName(), fontType, menuAreaConfig.fontSize());
        buttonsFontColor = Color.decode(menuAreaConfig.buttonsFontColor());

        menuBar = new JMenuBar();
        menuBar.setBackground(menuBackgroundColor);

        menuBar.add(createFileMenu(
                openListener,
                saveListener,
                exitListener,
                menuBackgroundColor,
                buttonsFontColor,
                font
        ));
        menuBar.add(createEditMenu(toolControllers));
        menuBar.add(createInfoMenu(
                helpListener,
                aboutListener,
                menuBackgroundColor,
                buttonsFontColor,
                font
        ));
    }

    private static JMenu createFileMenu(ActionListener openListener,
                                        ActionListener saveListener,
                                        ActionListener exitListener,
                                        Color menuBackgroundColor,
                                        Color buttonsFontColor,
                                        Font font) {
        final JMenu result = createMenu("File", menuBackgroundColor, buttonsFontColor, font);
        result.add(createItem("Open", openListener, font, menuBackgroundColor));
        result.add(createItem("Save", saveListener, font, menuBackgroundColor));
        result.add(createItem("Exit", exitListener, font, menuBackgroundColor));
        return result;
    }

    private JMenu createEditMenu(List<ToolController> toolControllers) {
        final JMenu result = createMenu("Edit", menuBackgroundColor, buttonsFontColor, font);

        final Map<Integer, ButtonGroup> toolGroups = new HashMap<>();

        for (final var it : toolControllers) {
            final Tool tool = it.getTool();
            final String toolName = tool.name();

            JMenuItem newIMenuItem;

            // Creating item of appropriate type
            if (tool.isToggle()) {
                newIMenuItem = createCheckboxItem(toolName, it, font, menuBackgroundColor);
            } else if (tool.hasGroup()) {
                newIMenuItem = createRadioItem(toolName, it, font, menuBackgroundColor);
                final int group = tool.group();
                if (!toolGroups.containsKey(group)) {
                    toolGroups.put(group, new ButtonGroup());
                }
                toolGroups.get(group).add(newIMenuItem);
            } else {
                newIMenuItem = createItem(toolName, it, font, menuBackgroundColor);
            }

            result.add(newIMenuItem);

            if (tool.isHand()) {
                handButton = newIMenuItem;
            }
            if (tool.isBack()) {
                backButton = newIMenuItem;
                backButton.setText(undoLabel);
            }
            if (tool.isOneToOne()) {
                oneToOneButton = newIMenuItem;
            }
            if (tool.isOnWindowSize()) {
                onWindowSizeButton = newIMenuItem;
            }
        }

        return result;
    }

    private static JMenu createInfoMenu(ActionListener helpListener,
                                        ActionListener aboutListener,
                                        Color menuBackgroundColor,
                                        Color buttonsFontColor,
                                        Font font) {
        final JMenu result = createMenu("Info", menuBackgroundColor, buttonsFontColor, font);
        result.add(createItem("Help", helpListener, font, menuBackgroundColor));
        result.add(createItem("About", aboutListener, font, menuBackgroundColor));
        return result;
    }

    private static JMenu createMenu(String label,
                                    Color menuBackgroundColor,
                                    Color buttonsFontColor,
                                    Font font) {
        JMenu result = new JMenu(label);
        result.setBackground(menuBackgroundColor);
        result.setForeground(buttonsFontColor);
        result.setFont(font);
        return result;
    }

    private static JMenuItem createItem(String label,
                                        ActionListener actionListener,
                                        Font font,
                                        Color menuBackgroundColor) {
        final JMenuItem result = new JMenuItem(label);
        initButton(result, actionListener, font, menuBackgroundColor);
        return result;
    }

    private static JRadioButtonMenuItem createRadioItem(String label,
                                                        ActionListener actionListener,
                                                        Font font,
                                                        Color menuBackgroundColor) {
        final JRadioButtonMenuItem result = new JRadioButtonMenuItem(label);
        initButton(result, actionListener, font, menuBackgroundColor);
        return result;
    }

    private static JCheckBoxMenuItem createCheckboxItem(String label,
                                                        ActionListener actionListener,
                                                        Font font,
                                                        Color menuBackgroundColor) {
        final JCheckBoxMenuItem result = new JCheckBoxMenuItem(label);
        initButton(result, actionListener, font, menuBackgroundColor);
        return result;
    }

    private static void initButton(AbstractButton button,
                                   ActionListener actionListener,
                                   Font font,
                                   Color menuBackgroundColor) {
        button.setBorderPainted(false);
        button.setFont(font);
        button.setForeground(menuBackgroundColor);
        button.addActionListener(actionListener);
    }

    @Override
    public void onDrawingAreaActionChange(Context context) {
        if (handButton != null) {
            handButton.setSelected(context.getDrawingAreaAction() == DrawingAreaAction.MOVE_SCROLLS);
        }
    }

    @Override
    public void onChangeViewMode(Context context) {
        final ViewMode viewMode = context.getViewMode();

        if (oneToOneButton != null) {
            oneToOneButton.setSelected(viewMode == ViewMode.ONE_TO_ONE);
        }

        if (onWindowSizeButton != null) {
            onWindowSizeButton.setSelected(viewMode == ViewMode.ON_WINDOW_SIZE);
        }
    }

    @Override
    public void onImageChange(Context context) {
        if (context.getProcessedImage() == null || context.getImage() != context.getOriginalImage()) {
            backButton.setText(undoLabel);
        } else {
            backButton.setText(redoLabel);
        }
    }
}
