package ru.nsu.icg.lab2.gui.view;

import ru.nsu.icg.lab2.model.dto.Tool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;

public class HelpWindow extends JDialog {
    private static final Dimension prefSize = new Dimension(1000,800);

    private final JPanel mainPanel;

    public HelpWindow(JFrame parentFrame, String name, List<Tool> tools){
        super(parentFrame, name, true);
        this.setLayout(new BorderLayout());
        this.setPreferredSize(prefSize);
        this.setResizable(false);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        for (Tool tool : tools){
            setToolDescription(tool);
        }

        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));


        JButton closeBtn = new JButton("Close");
        ActionListener actionListener = e -> this.dispose();
        closeBtn.addActionListener(actionListener);
        closeBtn.setPreferredSize(new Dimension(100,50));
        JPanel buttonHolder = new JPanel(new BorderLayout());
        buttonHolder.add(closeBtn, BorderLayout.CENTER);
        mainPanel.add(buttonHolder);


        this.add(scrollPane, BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
    }

    private void setToolDescription(Tool tool){

        JTextArea desc = new JTextArea();
        desc.setText(tool.name() + " - " + tool.description());
        desc.setEditable(false);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        ImageIcon image = new ImageIcon(Objects.requireNonNull(HelpWindow.class.getResource(tool.iconPath())));
        JLabel imageHolder = new JLabel(image);
        imageHolder.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));

        JPanel holder = new JPanel(new BorderLayout());
        holder.add(imageHolder, BorderLayout.WEST);
        holder.add(desc, BorderLayout.CENTER);
        holder.setBorder(BorderFactory.createEmptyBorder(5,0,10,10));
        mainPanel.add(holder);
    }

}
