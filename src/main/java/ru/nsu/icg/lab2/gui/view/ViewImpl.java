package ru.nsu.icg.lab2.gui.view;

import com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme;
import ru.nsu.icg.lab2.gui.common.*;
import ru.nsu.icg.lab2.gui.common.context.ContextAdapter;
import ru.nsu.icg.lab2.gui.common.context.ContextImageReader;
import ru.nsu.icg.lab2.gui.controller.DrawingAreaController;
import ru.nsu.icg.lab2.gui.controller.FolderImagesScrollingController;
import ru.nsu.icg.lab2.gui.controller.TransformationsController;
import ru.nsu.icg.lab2.gui.controller.WindowResizeController;
import ru.nsu.icg.lab2.gui.controller.menu.*;
import ru.nsu.icg.lab2.model.dto.Tool;
import ru.nsu.icg.lab2.model.dto.view.ViewConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.List;

public class ViewImpl extends ContextAdapter implements View {
    private final Context context;
    private final DrawingArea drawingArea;
    private final MainWindow mainWindow;
    private final JTextPane aboutTextPane;

    private final List<Tool> tools;

    public ViewImpl(ViewConfig viewConfig,
                    List<Tool> tools,
                    Context context,
                    ContextImageReader imageReader,
                    ImageWriter imageWriter) {
        FlatArcDarkOrangeIJTheme.setup();
        this.tools = tools;
        this.context = context;
        context.addImageListener(this);
        context.addViewModeListener(this);

        final KeyListener keyListener = new FolderImagesScrollingController(context, this, imageReader);

        final TransformationsController transformationsController = new TransformationsController(this);
        context.addTransformationListener(transformationsController);

        final OpenController openController = new OpenController(this, imageReader);
        final SaveController saveController = new SaveController(context, this, imageWriter);
        final ExitController exitController = new ExitController(this);

        final ToolControllersFactory toolControllersFactory = new ToolControllersFactory(
                context,
                this,
                context.getBufferedImageFactory(),
                tools
        );

        final HelpController helpController = new HelpController(this);
        final AboutController aboutController = new AboutController(this);
        final WindowResizeController windowResizeController = new WindowResizeController(context, this);
        final DrawingAreaController drawingAreaController = new DrawingAreaController(context, this);

        drawingArea = new DrawingArea(drawingAreaController, viewConfig.textAreaConfig());

        final MenuArea menuArea = new MenuArea(
                openController,
                saveController,
                exitController,
                helpController,
                aboutController,
                toolControllersFactory.getToolControllers(),
                viewConfig.menuAreaConfig()
        );

        final ToolsArea toolsArea = new ToolsArea(
                toolControllersFactory.getToolControllers(),
                viewConfig.toolsAreaConfig(),
                keyListener
        );

        mainWindow = new MainWindow(
                viewConfig.mainWindowConfig(),
                exitController,
                menuArea.getMenuBar(),
                toolsArea,
                drawingArea,
                context,
                windowResizeController
        );

        context.addViewModeListener(menuArea);
        context.addViewModeListener(toolsArea);
        context.addDrawingAreaActionListener(menuArea);
        context.addDrawingAreaActionListener(toolsArea);
        context.addImageListener(toolsArea);
        context.addImageListener(menuArea);
        aboutTextPane = createAboutTextArea();
    }

    @Override
    public void show() {
        mainWindow.setVisible(true);
    }

    @Override
    public void hide() {
        mainWindow.setVisible(false);
    }

    @Override
    public void destroy() {
        mainWindow.dispose();
    }

    @Override
    public void resize() {
        final BufferedImageImpl image = context.getImage();

        if (image == null) {
            return;
        }

        final double k = image.getWidth() * 1.0 / image.getHeight();

        int newHeight;
        int newWidth;
        if (context.getDrawingAreaWidth() * 1.0 / context.getDrawingAreaHeight() >= k) {
            newHeight = context.getDrawingAreaHeight();
            newWidth = (int) (newHeight * k);
        } else {
            newWidth = context.getDrawingAreaWidth();
            newHeight = (int) (newWidth / k);
        }

        final BufferedImage resizedImage = new BufferedImage(
                newWidth,
                newHeight,
                BufferedImage.TYPE_INT_ARGB
        );

        final Object hintValue;
        switch (context.getInterpolationMethod()) {
            case BICUBIC -> hintValue = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
            case BILINEAR -> hintValue = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
            case NEAREST_NEIGHBOR -> hintValue = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
            default -> throw new IllegalArgumentException("Unexpected interpolation method");
        }

        final Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hintValue);
        graphics2D.drawImage(image.bufferedImage(), 0, 0, newWidth, newHeight, null);
        graphics2D.dispose();
        drawingArea.resizeSoftly(newWidth, newHeight);
        drawingArea.setImage(resizedImage);
        drawingArea.repaint();
        drawingArea.revalidate();
    }

    @Override
    public void showError(String errorMessage) {
        JOptionPane.showMessageDialog(
                mainWindow,
                errorMessage,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    @Override
    public void repaint() {
        final BufferedImageImpl image = context.getImage();
        if (image == null) {
            return;
        }
        if (context.getViewMode() == ViewMode.ONE_TO_ONE) {
            drawingArea.resizeSoftly(image.getWidth(), image.getHeight());
            drawingArea.setImage(image.bufferedImage());
            drawingArea.repaint();
            drawingArea.revalidate();
        } else {
            resize();
        }
    }

    @Override
    public void showHelp() {
        new HelpWindow(mainWindow, "help", tools);
    }

    @Override
    public void showAbout() {
        JOptionPane.showMessageDialog(
                null,
                aboutTextPane,
                "About",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public boolean showConfirmationDialog(String title, JPanel content) {
        return JOptionPane.showConfirmDialog(
                mainWindow,
                content,
                title,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION;
    }

    @Override
    public JFrame getFrame() {
        return mainWindow;
    }

    @Override
    public JScrollPane getMainScrollPane() {
        return mainWindow.getScrollPane();
    }

    @Override
    public void setWaitCursor() {
        mainWindow.setWaitCursor();
    }

    @Override
    public void setDefaultCursor() {
        mainWindow.setDefaultCursor();
    }

    @Override
    public void onImageChange(Context context) {
        repaint();
    }

    @Override
    public void onChangeViewMode(Context context) {
        if (context.getViewMode() == ViewMode.ON_WINDOW_SIZE) {
            context.setDrawingAreaHeight(mainWindow.getWindowedDrawingAreaHeight());
            context.setDrawingAreaWidth(mainWindow.getWindowedDrawingAreaWidth());
            resize();
            mainWindow.disableScrolls();
        } else {
            mainWindow.enableScrolls();
            repaint();
        }
    }

    private static JTextPane createAboutTextArea() {
        final JTextPane result = createHelpAboutTextPane();
        result.setText(getAboutText());
        return result;
    }

    private static String getAboutText() {
        return """
               <html>
                    <p><b><i>Photoshop</i></b> represents a program for opening images and apply to it filters and transformations</p>
                    <b>Created by:</b>
                    <ul>
                        <li>Vorobyov Andrew</li>
                        <li>Kondrenko Kirill</li>
                        <li>Sirotkin Michail</li>
                    </ul>
                    <p>students of group 21203 in NSU in March 2024 as task of the course "engineering and computer graphics" </p>
                    <p>link to github of project <u>https://github.com/Wooshey1411/ICGFilters</u>
               </html>
               """;
    }

    private static JTextPane createHelpAboutTextPane() {
        final JTextPane result = new JTextPane();
        result.setEditable(false);
        result.setBackground(null);
        result.setContentType("text/html");
        return result;
    }
}
