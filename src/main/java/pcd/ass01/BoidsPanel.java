package pcd.ass01;

import javax.swing.*;
import java.awt.*;

final class BoidsPanel extends JPanel {

    private final BoidsView view;
    private final BoidsModel model;
    private int framerate;

    public BoidsPanel(final BoidsView view, final BoidsModel model) {
        this.model = model;
        this.view = view;
    }

    public void setFrameRate(final int framerate) {
        this.framerate = framerate;
    }

    @Override
    protected void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);
        setBackground(Color.WHITE);
        final var width = view.getWidth();
        final var height = view.getHeight();
        final var envWidth = model.getWidth();
        final var xScale = width / envWidth;
        final var boids = model.getBoids();
        graphics.setColor(Color.BLUE);
        for (final var boid : boids) {
            final var x = boid.getPosition().x();
            final var y = boid.getPosition().y();
            final var px = (int) ((double) width / 2 + x * xScale);
            final var py = (int) ((double) height / 2 - y * xScale);
            graphics.fillOval(px, py, 5, 5);
        }
        graphics.setColor(Color.BLACK);
        graphics.drawString("Num. Boids: " + boids.size(), 10, 25);
        graphics.drawString("Framerate: " + framerate, 10, 40);
    }
}
