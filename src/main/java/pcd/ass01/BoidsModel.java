package pcd.ass01;

import java.util.ArrayList;
import java.util.List;

final class BoidsModel {

    private final List<Boid> boids = new ArrayList<>();
    private double separationWeight;
    private double alignmentWeight;
    private double cohesionWeight;
    private final double width;
    private final double height;
    private final double maxSpeed;
    private final double perceptionRadius;
    private final double avoidRadius;

    public BoidsModel(final int numBoids,
                      final double initialSeparationWeight,
                      final double initialAlignmentWeight,
                      final double initialCohesionWeight,
                      final double width,
                      final double height,
                      final double maxSpeed,
                      final double perceptionRadius,
                      final double avoidRadius) {
        this.separationWeight = initialSeparationWeight;
        this.alignmentWeight = initialAlignmentWeight;
        this.cohesionWeight = initialCohesionWeight;
        this.width = width;
        this.height = height;
        this.maxSpeed = maxSpeed;
        this.perceptionRadius = perceptionRadius;
        this.avoidRadius = avoidRadius;
        for (var i = 0; i < numBoids; i++) {
            final var position = new Point2D(-width / 2 + Math.random() * width,
                    -height / 2 + Math.random() * height);
            final var velocity = new Vector2D(Math.random() * maxSpeed / 2 - maxSpeed / 4,
                    Math.random() * maxSpeed / 2 - maxSpeed / 4);
            this.boids.add(new Boid(position, velocity));
        }
    }

    public List<Boid> getBoids() {
        return this.boids;
    }

    public double getMinX() {
        return -this.width / 2;
    }

    public double getMaxX() {
        return this.width / 2;
    }

    public double getMinY() {
        return -this.height / 2;
    }

    public double getMaxY() {
        return this.height / 2;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public void setSeparationWeight(final double value) {
        this.separationWeight = value;
    }

    public void setAlignmentWeight(final double value) {
        this.alignmentWeight = value;
    }

    public void setCohesionWeight(final double value) {
        this.cohesionWeight = value;
    }

    public double getSeparationWeight() {
        return this.separationWeight;
    }

    public double getCohesionWeight() {
        return this.cohesionWeight;
    }

    public double getAlignmentWeight() {
        return this.alignmentWeight;
    }

    public double getMaxSpeed() {
        return this.maxSpeed;
    }

    public double getAvoidRadius() {
        return this.avoidRadius;
    }

    public double getPerceptionRadius() {
        return this.perceptionRadius;
    }
}
