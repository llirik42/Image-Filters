package ru.nsu.icg.lab2.gui.controller;

import lombok.Getter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.util.function.Function;

public class TextFieldSliderController implements ChangeListener, DocumentListener {
    private final JTextField textField;
    private final JSlider slider;
    private final Function<Integer, Double> fromSliderValue;
    private final Function<String, Double> fromTextField;
    private final Function<Double, Integer> toSliderValue;
    private final Function<Double, String> toTextFieldValue;

    private final double minValue;
    private final double maxValue;

    private boolean hasError;
    private boolean hasValue;

    @Getter
    private double value;

    public TextFieldSliderController(JTextField textField,
                                     JSlider slider,
                                     double minValue,
                                     double maxValue,
                                     Function<Integer, Double> fromSliderValue,
                                     Function<String, Double> fromTextField,
                                     Function<Double, Integer> toSliderValue,
                                     Function<Double, String> toTextFieldValue) {
        this.textField = textField;
        this.slider = slider;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.fromSliderValue = fromSliderValue;
        this.fromTextField = fromTextField;
        this.toSliderValue = toSliderValue;
        this.toTextFieldValue = toTextFieldValue;
        this.slider.addChangeListener(this);
        this.textField.getDocument().addDocumentListener(this);
    }

    public void reset(double value) {
        this.value = value;
        hasError = false;
        updateValueOfSlider();
        updateValueOfTextArea();
    }

    public void reset(int value) {
        reset((double) value);
    }

    public int getIntValue() {
        return (int) Math.round(value);
    }

    public boolean hasError() {
        return hasError;
    }

    public boolean hasValue() {
        return hasValue;
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        final double oldValue = value;
        final double newValue = fromSliderValue.apply(slider.getValue());
        updateValue(newValue);

        if (oldValue != newValue) {
            updateValueOfTextArea();
        }
    }

    @Override
    public void insertUpdate(DocumentEvent documentEvent) {
        handleDocumentTextChange(documentEvent.getDocument());
    }

    @Override
    public void removeUpdate(DocumentEvent documentEvent) {
        handleDocumentTextChange(documentEvent.getDocument());
    }

    @Override
    public void changedUpdate(DocumentEvent documentEvent) {
        // Method is empty because JTextField doesn't change it's state (only text)
    }

    private void handleDocumentTextChange(Document document) {
        try {
            readValueFromString(document.getText(0, document.getLength()));
        } catch (BadLocationException ignored) {
            // Impossible case
        }
    }

    private void readValueFromString(String string) {
        try {
            final double tmp = fromTextField.apply(string);
            hasError = tmp < minValue || tmp > maxValue;

            if (!hasError) {
                updateValue(tmp);

                // We cannot just updateValueOfSlider() because we get exception "Attempt to mutate in notification:
                SwingUtilities.invokeLater(this::updateValueOfSlider);
            }
        } catch (Exception exception) {
            hasError = true;
        }
    }

    private void updateValueOfSlider() {
        slider.setValue(toSliderValue.apply(value));
    }

    private void updateValueOfTextArea() {
        textField.setText(toTextFieldValue.apply(value));
    }

    private void updateValue(double value) {
        this.value = value;
        hasValue = true;
    }
}
