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
    private int boidsCount;

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
    }

    public synchronized void createBoids(final int numBoids) {
        this.boidsCount = numBoids;
        this.boids.clear();
        for (var i = 0; i < numBoids; i++) {
            final var position = new Point2D(-this.width / 2 + Math.random() * this.width, -this.height / 2 + Math.random() * this.height);
            final var velocity = new Vector2D(Math.random() * this.maxSpeed / 2 - this.maxSpeed / 4, Math.random() * this.maxSpeed / 2 - this.maxSpeed / 4);
            this.boids.add(new Boid(position, velocity));
        }
    }

    public synchronized List<Boid> getBoids() {
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

    public synchronized void setSeparationWeight(final double value) {
        this.separationWeight = value;
    }

    public synchronized void setAlignmentWeight(final double value) {
        this.alignmentWeight = value;
    }

    public synchronized void setCohesionWeight(final double value) {
        this.cohesionWeight = value;
    }

    public synchronized double getSeparationWeight() {
        return this.separationWeight;
    }

    public synchronized double getCohesionWeight() {
        return this.cohesionWeight;
    }

    public synchronized double getAlignmentWeight() {
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

    public void resetBoids() {
        this.createBoids(this.boidsCount);
    }
}
