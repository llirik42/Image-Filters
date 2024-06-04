package ru.nsu.icg.lab2.model.transformations.dithering;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.icg.lab2.model.ImageFactory;
import ru.nsu.icg.lab2.model.ImageInterface;
import ru.nsu.icg.lab2.model.transformations.AbstractDithering;
import ru.nsu.icg.lab2.model.transformations.TransformationUtils;

import java.util.Arrays;
import java.util.function.BiFunction;

@Getter
@Setter
public class FloydSteinbergDithering extends AbstractDithering {
    public FloydSteinbergDithering(ImageFactory imageFactory) {
        super(imageFactory);
    }

    @Override
    public ImageInterface apply(ImageInterface oldImage) {
        final FilterCreator creator = getCreator();
        final int redK = getRedK();
        final int greenK = getGreenK();
        final int blueK = getBlueK();

        switch (creator) {
            case SIROTKIN -> {
                return SirotkinVariant.apply(oldImage, redK, blueK, greenK, getImageFactory());
            }
            case VOROBEV -> {
                return VorobevVariant.apply(oldImage, redK, blueK, greenK, getImageFactory());
            }
            case KONDRENKO -> {
                return KondrenkoVariant.apply(oldImage, redK, blueK, greenK, getImageFactory());
            }
            default -> throw new IllegalArgumentException("No such creator of Floyd-Steinberg dithering");
        }
    }

    private static class SirotkinVariant {
        public static ImageInterface apply(ImageInterface oldImage, int redK, int blueK, int greenK, ImageFactory imageFactory) {
            int width = oldImage.getWidth();
            int height = oldImage.getHeight();
            int gridSize = height * width;
            int errorGridSize = (height + 1) * (width + 2);
            int[] grid = oldImage.getGrid();
            int[] newGrid = new int[gridSize];
            int[] errorRed = new int[errorGridSize];
            int[] errorGreen = new int[errorGridSize];
            int[] errorBlue = new int[errorGridSize];
            Arrays.fill(errorRed, 0);
            Arrays.fill(errorGreen, 0);
            Arrays.fill(errorBlue, 0);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int index = y * width + x;
                    int errorIndex = y * (width + 2) + x + 1;

                    //red
                    int oldRedColor = ((grid[index] & 0x00FF0000) >> 16) + (int) Math.round(errorRed[errorIndex] / 16.0);
                    int redColor = getClosedPalletColor(oldRedColor, redK);
                    int error = oldRedColor - redColor;
                    errorRed[errorIndex + 1] += error * 7;
                    errorRed[errorIndex + (width + 2) - 1] += error * 3;
                    errorRed[errorIndex + (width + 2)] += error * 5;
                    errorRed[errorIndex + (width + 2) + 1] += error;

                    //green
                    int oldGreenColor = ((grid[index] & 0x0000FF00) >> 8) + (int) Math.round(errorGreen[errorIndex] / 16.0);
                    int greenColor = getClosedPalletColor(oldGreenColor, greenK);
                    error = oldGreenColor - greenColor;
                    errorGreen[errorIndex + 1] += error * 7;
                    errorGreen[errorIndex + (width + 2) - 1] += error * 3;
                    errorGreen[errorIndex + (width + 2)] += error * 5;
                    errorGreen[errorIndex + (width + 2) + 1] += error;

                    //blue
                    int oldBlueColor = (grid[index] & 0x000000FF) + (int) Math.round(errorBlue[errorIndex] / 16.0);
                    int blueColor = getClosedPalletColor(oldBlueColor, blueK);
                    error = oldBlueColor - blueColor;
                    errorBlue[errorIndex + 1] += error * 7;
                    errorBlue[errorIndex + (width + 2) - 1] += error * 3;
                    errorBlue[errorIndex + (width + 2)] += error * 5;
                    errorBlue[errorIndex + (width + 2) + 1] += error;

                    newGrid[index] = (grid[index] & 0xFF000000) | (redColor << 16) | (greenColor << 8) | blueColor;
                }
            }
            return imageFactory.createImage(oldImage, newGrid);
        }

        private static int getClosedPalletColor(int color, int palletSize) {
            if (color >= 255) {
                return 255;
            } else if (color < 0) {
                return 0;
            }
            double palletStep = 255.0 / (palletSize - 1);

            return (int) ((color * palletSize / 255) * palletStep);


        }


    }

    // НЕ РЕФАКТОРИТЬ
    private static class VorobevVariant {
        public static ImageInterface apply(ImageInterface oldImage, int redK, int blueK, int greenK, ImageFactory imageFactory) {

            int width = oldImage.getWidth();
            int height = oldImage.getHeight();
            int[] redErrors = new int[(width + 2) << 1];
            int[] greenErrors = new int[(width + 2) << 1];
            int[] blueErrors = new int[(width + 2) << 1];
            Arrays.fill(redErrors, 0);
            Arrays.fill(greenErrors, 0);
            Arrays.fill(blueErrors, 0);

            int[] grid = oldImage.getGrid();
            int[] newGrid = new int[width * height];
            Arrays.fill(newGrid, 0);

            BiFunction<Integer, Integer, Integer> findNearestNeighbor = (color, paletteSize) -> {
                if (color < 0) {
                    return 0;
                }
                if (color >= 255) {
                    return 255;
                }
                return (int) ((color * paletteSize / 255) * (255f / (paletteSize - 1)));
            };

            int currErrorShift = 1;
            int nextErrorShift = width + 3;

            for (int y = 0; y < height; y++) {
                int dimYShift = y * width;
                int leftRedError = 0;
                int leftGreenError = 0;
                int leftBlueError = 0;
                for (int x = 0; x < width; x++) {
                    int currPixel;
                    int newPixel;
                    int quantError;
                    int currPixelPos = dimYShift + x;

                    //**********Alpha**********\\

                    newGrid[currPixelPos] |= (TransformationUtils.getAlpha(grid[currPixelPos]) << 24);

                    //**********RED**********\\

                    currPixel = TransformationUtils.getRed(grid[currPixelPos]) + (int) Math.round((redErrors[currErrorShift + x] + leftRedError) / 16.0);
                    redErrors[currErrorShift + x] = 0;
                    newPixel = findNearestNeighbor.apply(currPixel, redK);
                    newGrid[currPixelPos] |= (newPixel << 16);
                    quantError = currPixel - newPixel;
                    leftRedError = quantError * 7;
                    redErrors[nextErrorShift + x] += quantError * 5;
                    redErrors[nextErrorShift + x - 1] += quantError * 3;
                    redErrors[nextErrorShift + x + 1] += quantError;

                    //**********GREEN**********\\

                    currPixel = TransformationUtils.getGreen(grid[currPixelPos]) + (int) Math.round((greenErrors[currErrorShift + x] + leftGreenError) / 16.0);
                    greenErrors[currErrorShift + x] = 0;
                    newPixel = findNearestNeighbor.apply(currPixel, greenK);
                    newGrid[currPixelPos] |= (newPixel << 8);
                    quantError = currPixel - newPixel;
                    leftGreenError = quantError * 7;
                    greenErrors[nextErrorShift + x] += quantError * 5;
                    greenErrors[nextErrorShift + x - 1] += quantError * 3;
                    greenErrors[nextErrorShift + x + 1] += quantError;

                    //**********BLUE**********\\

                    currPixel = TransformationUtils.getBlue(grid[currPixelPos]) + (int) Math.round((blueErrors[currErrorShift + x] + leftBlueError) / 16.0);
                    blueErrors[currErrorShift + x] = 0;
                    newPixel = findNearestNeighbor.apply(currPixel, blueK);
                    quantError = currPixel - newPixel;
                    newGrid[currPixelPos] |= (newPixel);
                    leftBlueError = quantError * 7;
                    blueErrors[nextErrorShift + x] += quantError * 5;
                    blueErrors[nextErrorShift + x - 1] += quantError * 3;
                    blueErrors[nextErrorShift + x + 1] += quantError;


                }
                currErrorShift ^= nextErrorShift;
                nextErrorShift ^= currErrorShift;
                currErrorShift ^= nextErrorShift;
            }
            return imageFactory.createImage(oldImage, newGrid);
        }
    }

    private static class KondrenkoVariant {
        public static class ErrorDiffusionDithering {
            public static void apply(int[] input,
                                     int redK,
                                     int greenK,
                                     int blueK,
                                     int imageWidth,
                                     int imageHeight,
                                     double[][] matrix,
                                     int matrixWidth,
                                     int matrixHeight,
                                     int currentElementMatrixXPosition,
                                     int[] output) {
                final double redDelta = KondrenkoUtils.calculateDelta(redK);
                final double greenDelta = KondrenkoUtils.calculateDelta(greenK);
                final double blueDelta = KondrenkoUtils.calculateDelta(blueK);

                // + (matrixWidth - 1) - to avoid invalid index error while processing border pixels naively
                final double[][] redErrors = new double[matrixHeight][imageWidth + matrixWidth - 1];
                final double[][] greenErrors = new double[matrixHeight][imageWidth + matrixWidth - 1];
                final double[][] blueErrors = new double[matrixHeight][imageWidth + matrixWidth - 1];

                for (int i = 0; i < matrixHeight; i++) {
                    Arrays.fill(redErrors[i], 0);
                    Arrays.fill(greenErrors[i], 0);
                    Arrays.fill(blueErrors[i], 0);
                }

                for (int y = 0; y < imageHeight; y++) {
                    for (int x = 0; x < imageWidth; x++) {
                        final int index = x + y * imageWidth;

                        final int oldPixel = input[index];
                        final int oldAlpha = TransformationUtils.getAlpha(oldPixel);
                        final int oldRed = TransformationUtils.getRed(oldPixel);
                        final int oldGreen = TransformationUtils.getGreen(oldPixel);
                        final int oldBlue = TransformationUtils.getBlue(oldPixel);

                        final double accumulatedRedError = redErrors[0][x + currentElementMatrixXPosition];
                        final double accumulatedGreenError = greenErrors[0][x + currentElementMatrixXPosition];
                        final double accumulatedBlueError = blueErrors[0][x + currentElementMatrixXPosition];

                        final int oldRedWithError = oldRed + (int) Math.round(accumulatedRedError);
                        final int oldGreenWithError = oldGreen + (int) Math.round(accumulatedGreenError);
                        final int oldBlueWithError = oldBlue + (int) Math.round(accumulatedBlueError);

                        final int newRed = KondrenkoUtils.findNearestColor(oldRedWithError, redDelta);
                        final int newGreen = KondrenkoUtils.findNearestColor(oldGreenWithError, greenDelta);
                        final int newBlue = KondrenkoUtils.findNearestColor(oldBlueWithError, blueDelta);

                        final int redError = oldRedWithError - newRed;
                        final int greenError = oldGreenWithError - newGreen;
                        final int blueError = oldBlueWithError - newBlue;

                        for (int xx = 0; xx < matrixWidth; xx++) {
                            for (int yy = 0; yy < matrixHeight; yy++) {
                                redErrors[yy][x - currentElementMatrixXPosition + xx + 1] += redError * matrix[yy][xx];
                                greenErrors[yy][x - currentElementMatrixXPosition + xx + 1] += greenError * matrix[yy][xx];
                                blueErrors[yy][x - currentElementMatrixXPosition + xx + 1] += blueError * matrix[yy][xx];
                            }
                        }

                        output[index] = TransformationUtils.getARGB(
                                oldAlpha,
                                newRed,
                                newGreen,
                                newBlue
                        );
                    }

                    shiftErrorsArray(redErrors);
                    shiftErrorsArray(greenErrors);
                    shiftErrorsArray(blueErrors);
                }
            }

            private static void shiftErrorsArray(double[][] errors) {
                for (int y = 0; y < errors.length - 1; y++) {
                    errors[y] = errors[y + 1].clone();
                }
                Arrays.fill(errors[errors.length - 1], 0);
            }
        }

        private static final double[][] FLOYD_MATRIX = {
                {0.0, 0.0, 7.0 / 16.0},
                {3.0 / 16.0, 5.0 / 16.0, 1.0 / 16.0},
        };

        public static ImageInterface apply(ImageInterface oldImage,
                                           int redK,
                                           int blueK,
                                           int greenK,
                                           ImageFactory imageFactory) {
            final int[] oldGrid = oldImage.getGrid();
            final int[] newGrid = new int[oldGrid.length];

            ErrorDiffusionDithering.apply(
                    oldGrid,
                    redK,
                    greenK,
                    blueK,
                    oldImage.getWidth(),
                    oldImage.getHeight(),
                    FLOYD_MATRIX,
                    3,
                    2,
                    1,
                    newGrid
            );

            return imageFactory.createImage(oldImage, newGrid);
        }
    }
}
