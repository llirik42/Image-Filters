package ru.nsu.icg.lab2.model.transformations;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.ImageInterface;
import ru.nsu.icg.lab2.model.Transformation;

@Setter
@Getter
public abstract class AbstractDithering extends Transformation {
    public enum FilterCreator{
        SIROTKIN,
        VOROBEV,
        KONDRENKO
    }

    private int redK;
    private int greenK;
    private int blueK;
    private FilterCreator creator;

    protected AbstractDithering(ImageFactory imageFactory) {
        super(imageFactory);
        redK = 2;
        greenK = 2;
        blueK = 2;
        creator = FilterCreator.VOROBEV;
    }

    @Override
    public abstract ImageInterface apply(ImageInterface oldImage);
}
