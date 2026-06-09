package frames;

import shapes.GShape;

import javax.swing.*;
import java.awt.*;

public class GAttributePanel extends JPanel {
    private GDrawingPanel drawingPanel;
    private boolean backgroundMode;

    public GAttributePanel() {
        this.backgroundMode = false;

        this.setPreferredSize(new Dimension(180, 0));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(createTitleLabel("속성"));
        this.add(createFillColorPanel());
        this.add(createLineColorPanel());
        this.add(createStrokePanel());
    }

    public void associateWith(GDrawingPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
    }

    public void showShapeMode() {
        this.backgroundMode = false;
    }
    public void showBackgroundMode() {
        this.backgroundMode = true;
    }
    private JPanel createBackgroundPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("배경 색상"));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        buttonPanel.add(createBackgroundColorButton("흰색", Color.WHITE));
        buttonPanel.add(createBackgroundColorButton("회색", Color.LIGHT_GRAY));
        buttonPanel.add(createBackgroundColorButton("검정", Color.BLACK));

        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }
    private JButton createBackgroundColorButton(String text, Color color) {
        JButton button = new JButton();
        button.setToolTipText(text);
        button.setPreferredSize(new Dimension(32, 28));
        button.setBackground(color);
        button.setOpaque(true);
        button.setBorderPainted(true);

        button.addActionListener(e -> {
            if (drawingPanel == null) {
                return;
            }

            drawingPanel.setBackground(color);
            drawingPanel.redraw();
        });

        return button;
    }


    private JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 6, 10));
        return label;
    }

    private JPanel createFillColorPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("채우기 색상"));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        buttonPanel.add(createColorButton("빨강", Color.RED, true));
        buttonPanel.add(createColorButton("파랑", Color.BLUE, true));
        buttonPanel.add(createColorButton("노랑", Color.YELLOW, true));
        buttonPanel.add(createFillNoneButton());

        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createLineColorPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("외곽선 색상"));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        buttonPanel.add(createColorButton("검정", Color.BLACK, false));
        buttonPanel.add(createColorButton("빨강", Color.RED, false));
        buttonPanel.add(createColorButton("파랑", Color.BLUE, false));

        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStrokePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("외곽선 굵기"));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton thinButton = new JButton("얇게");
        thinButton.addActionListener(e -> applyStrokeWidth(1.0f));

        JButton normalButton = new JButton("보통");
        normalButton.addActionListener(e -> applyStrokeWidth(3.0f));

        JButton thickButton = new JButton("굵게");
        thickButton.addActionListener(e -> applyStrokeWidth(6.0f));

        buttonPanel.add(thinButton);
        buttonPanel.add(normalButton);
        buttonPanel.add(thickButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    private JButton createColorButton(String text, Color color, boolean isFillColor) {

        JButton button = new JButton();
        button.setToolTipText(text);
        button.setPreferredSize(new Dimension(32, 28));

        button.setContentAreaFilled(false);
        button.setBackground(color);
        button.setOpaque(true);
        button.setBorderPainted(true);

        button.addActionListener(e -> {
            if (drawingPanel == null) {
                return;
            }

            if (backgroundMode) {
                if (isFillColor) {
                    drawingPanel.setBackground(color);
                    drawingPanel.redraw();
                }
                return;
            }

            GShape selectedShape = drawingPanel.getSelectedShape();

            if (selectedShape == null) {
                return;
            }

            if (isFillColor) {
                selectedShape.setFillColor(color);
            } else {
                selectedShape.setLineColor(color);
            }

            drawingPanel.redraw();
        });

        return button;
    }

    private JButton createFillNoneButton() {
        JButton button = new JButton("없음");

        button.addActionListener(e -> {
            GShape selectedShape = drawingPanel.getSelectedShape();

            if (selectedShape == null) {
                return;
            }

            selectedShape.setFilled(false);
            drawingPanel.redraw();
        });

        return button;
    }

    private void applyStrokeWidth(float width) {
        if (backgroundMode) {
            return;
        }

        GShape selectedShape = drawingPanel.getSelectedShape();

        if (selectedShape == null) {
            return;
        }

        selectedShape.setStrokeWidth(width);
        drawingPanel.redraw();
    }
}