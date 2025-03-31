package pcd.ass01;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Hashtable;

final class BoidsView implements ChangeListener {

    private final BoidsPanel boidsPanel;
    private final JSlider cohesionSlider;
    private final JSlider separationSlider;
    private final JSlider alignmentSlider;
    private final BoidsModel model;
    private final int width;
    private final int height;

    public BoidsView(final BoidsModel model, final int width, final int height) {
        this.model = model;
        this.width = width;
        this.height = height;

        final var frame = new JFrame("Boids Simulation");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final var cp = new JPanel();
        final LayoutManager layout = new BorderLayout();
        cp.setLayout(layout);

        this.boidsPanel = new BoidsPanel(this, model);
        cp.add(BorderLayout.CENTER, boidsPanel);

        final var slidersPanel = new JPanel();

        this.cohesionSlider = makeSlider();
        this.separationSlider = makeSlider();
        this.alignmentSlider = makeSlider();

        slidersPanel.add(new JLabel("Separation"));
        slidersPanel.add(separationSlider);
        slidersPanel.add(new JLabel("Alignment"));
        slidersPanel.add(alignmentSlider);
        slidersPanel.add(new JLabel("Cohesion"));
        slidersPanel.add(cohesionSlider);

        cp.add(BorderLayout.SOUTH, slidersPanel);

        frame.setContentPane(cp);

        frame.setVisible(true);
    }

    private JSlider makeSlider() {
        final var slider = new JSlider(JSlider.HORIZONTAL, 0, 20, 10);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        final var labelTable = new Hashtable<>();
        labelTable.put(0, new JLabel("0"));
        labelTable.put(10, new JLabel("1"));
        labelTable.put(20, new JLabel("2"));
        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true);
        slider.addChangeListener(this);
        return slider;
    }

    public void update(final int frameRate) {
        this.boidsPanel.setFrameRate(frameRate);
        this.boidsPanel.repaint();
    }

    @Override
    public void stateChanged(final ChangeEvent e) {
        if (e.getSource() == this.separationSlider) {
            final var val = this.separationSlider.getValue();
            this.model.setSeparationWeight(0.1 * val);
        } else if (e.getSource() == this.cohesionSlider) {
            final var val = this.cohesionSlider.getValue();
            this.model.setCohesionWeight(0.1 * val);
        } else {
            final var val = this.alignmentSlider.getValue();
            this.model.setAlignmentWeight(0.1 * val);
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
