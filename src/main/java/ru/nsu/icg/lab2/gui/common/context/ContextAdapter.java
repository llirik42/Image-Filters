package ru.nsu.icg.lab2.gui.common.context;

import ru.nsu.icg.lab2.gui.common.Context;

public abstract class ContextAdapter implements ContextImageListener,
        ContextDrawingAreaActionListener,
        ContextTransformationListener,
        ContextViewModeListener {
    @Override
    public void onImageChange(Context context) {

    }

    @Override
    public void onDrawingAreaActionChange(Context context) {

    }

    @Override
    public void onTransformationChange(Context context) {

    }

    @Override
    public void onChangeViewMode(Context context) {

    }
}
