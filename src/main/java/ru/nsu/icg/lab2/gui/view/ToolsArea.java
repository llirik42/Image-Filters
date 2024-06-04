package ru.nsu.icg.lab2.gui.view;

import ru.nsu.icg.lab2.gui.common.Context;
import ru.nsu.icg.lab2.gui.common.DrawingAreaAction;
import ru.nsu.icg.lab2.gui.common.ToolController;
import ru.nsu.icg.lab2.gui.common.ViewMode;
import ru.nsu.icg.lab2.gui.common.context.ContextDrawingAreaActionListener;
import ru.nsu.icg.lab2.gui.common.context.ContextImageListener;
import ru.nsu.icg.lab2.gui.common.context.ContextViewModeListener;
import ru.nsu.icg.lab2.model.dto.Tool;
import ru.nsu.icg.lab2.model.dto.view.ToolsAreaConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolsArea extends JPanel implements ContextViewModeListener,
        ContextDrawingAreaActionListener,
        ContextImageListener {
    private class ToolButton {
        private final Tool tool;
        private final AbstractButton button;
        private final Icon defaultIcon;
        private final Icon selectedIcon;
        private final String defaultTip;
        private final String selectedTip;

        private ToolButton(Tool tool, AbstractButton button) {
            this.tool = tool;
            this.button = button;
            defaultTip = tool.tip();
            selectedTip = tool.selectedTip();
            defaultIcon = button.getIcon();

            final String selectedIconPath = tool.selectedIconPath();
            selectedIcon = selectedIconPath != null ? loadIcon(selectedIconPath) : null;
        }

        public void setSelected(boolean b) {
            button.setSelected(b);
            if (b) {
                lastSelectedButtons.put(tool.group(), button);
            }
        }

        public void setDefaultIcon() {
            button.setIcon(defaultIcon);
        }

        public void setSelectedIcon() {
            button.setIcon(selectedIcon);
        }

        public void setDefaultTip() {
            button.setToolTipText(defaultTip);
        }

        public void setSelectedTip() {
            button.setToolTipText(selectedTip);
        }
    }

    private final Map<Integer, AbstractButton> lastSelectedButtons = new HashMap<>();

    private ToolButton handButton;
    private ToolButton backButton;
    private ToolButton onWindowSizeButton;
    private ToolButton oneToOneButton;

    public ToolsArea(List<ToolController> toolControllers,
                     ToolsAreaConfig toolsAreaConfig,
                     KeyListener keyListener) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        Color areaBackgroundColor = Color.decode(toolsAreaConfig.areaBackgroundColor());
        Color buttonsBackgroundColor = Color.decode(toolsAreaConfig.buttonsBackgroundColor());
        int toolSize = toolsAreaConfig.toolSize();
        setBackground(areaBackgroundColor);

        final Map<Integer, ButtonGroup> toolGroups = new HashMap<>();

        for (final var it : toolControllers) {
            final Tool tool = it.getTool();

            AbstractButton newToolButton;

            // Creating tool-button of appropriate type
            if (tool.isToggle()) {
                newToolButton = createToolToggleButton(it, toolSize, buttonsBackgroundColor);
            } else if (tool.hasGroup()) {
                final int group = tool.group();
                newToolButton = createToolToggleButton(it, toolSize, buttonsBackgroundColor);
                if (!toolGroups.containsKey(group)) {
                    toolGroups.put(group, new ButtonGroup());
                }
                toolGroups.get(group).add(newToolButton);
                newToolButton.addActionListener(actionEvent -> {
                    final AbstractButton lastSelectedButton = lastSelectedButtons.get(group);
                    if (lastSelectedButton != null) {
                        lastSelectedButton.setSelected(true);
                    }
                    lastSelectedButtons.put(group, newToolButton);
                });
            } else {
                newToolButton = createToolButton(it, toolSize, buttonsBackgroundColor);
            }

            add(newToolButton);
            newToolButton.addKeyListener(keyListener);

            if (tool.isHand()) {
                handButton = new ToolButton(tool, newToolButton);
            }
            if (tool.isBack()) {
                backButton = new ToolButton(tool, newToolButton);
            }
            if (tool.isOneToOne()) {
                oneToOneButton = new ToolButton(tool, newToolButton);
            }
            if (tool.isOnWindowSize()) {
                onWindowSizeButton = new ToolButton(tool, newToolButton);
            }
        }
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
            backButton.setDefaultIcon();
            backButton.setDefaultTip();
        } else {
            backButton.setSelectedIcon();
            backButton.setSelectedTip();
        }
    }

    private static JButton createToolButton(ToolController toolController,
                                            int toolSize,
                                            Color backgroundColor) {
        final Tool tool = toolController.getTool();
        final JButton result = new JButton(loadIcon(tool.iconPath()));
        initButton(result, tool.tip(), toolController, toolSize, backgroundColor);
        return result;
    }

    private static JToggleButton createToolToggleButton(ToolController toolController,
                                                        int toolSize,
                                                        Color backgroundColor) {
        final Tool tool = toolController.getTool();
        final JToggleButton result = new JToggleButton(loadIcon(tool.iconPath()));
        initButton(result, tool.tip(), toolController, toolSize, backgroundColor);
        return result;
    }

    private static void initButton(AbstractButton button,
                                   String tip,
                                   ActionListener actionListener,
                                   int toolSize,
                                   Color backgroundColor) {
        button.setFocusPainted(false);
        button.setToolTipText(tip);
        button.addActionListener(actionListener);
        button.setPreferredSize(new Dimension(toolSize, toolSize));
        button.setMinimumSize(new Dimension(toolSize, toolSize));
        button.setBackground(backgroundColor);
        button.setBorderPainted(false);
    }

    private static ImageIcon loadIcon(String path) {
        final URL url = ToolsArea.class.getResource(path);

        if (url == null) {
            throw new RuntimeException("Icon not found: " + path);
        }

        return new ImageIcon(url);
    }
}
