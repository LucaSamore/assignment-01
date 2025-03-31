package pcd.ass01;

import java.util.ArrayList;
import java.util.List;

final class Boid {

    private Point2D position;
    private Vector2D velocity;

    public Boid(final Point2D position, final Vector2D velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public void updateVelocity(final BoidsModel model) {
        final var nearbyBoids = getNearbyBoids(model);
        final var separation = calculateSeparation(nearbyBoids, model);
        final var alignment = calculateAlignment(nearbyBoids);
        final var cohesion = calculateCohesion(nearbyBoids);
        this.velocity = this.velocity
                .sum(alignment.mul(model.getAlignmentWeight()))
                .sum(separation.mul(model.getSeparationWeight()))
                .sum(cohesion.mul(model.getCohesionWeight()));
        final var speed = velocity.abs();
        if (speed > model.getMaxSpeed()) {
            this.velocity = this.velocity.getNormalized().mul(model.getMaxSpeed());
        }
    }

    public void updatePosition(final BoidsModel model) {
        this.position = this.position.sum(this.velocity);
        if (position.x() < model.getMinX()) {
            this.position = this.position.sum(new Vector2D(model.getWidth(), 0));
        }
        if (position.x() >= model.getMaxX()) {
            this.position = this.position.sum(new Vector2D(-model.getWidth(), 0));
        }
        if (position.y() < model.getMinY()) {
            this.position = this.position.sum(new Vector2D(0, model.getHeight()));
        }
        if (position.y() >= model.getMaxY()) {
            this.position = this.position.sum(new Vector2D(0, -model.getHeight()));
        }
    }

    public Point2D getPosition() {
        return this.position;
    }

    public Vector2D getVelocity() {
        return this.velocity;
    }

    private List<Boid> getNearbyBoids(final BoidsModel model) {
        final var list = new ArrayList<Boid>();
        for (final var other : model.getBoids()) {
            if (other != this) {
                final var otherPosition = other.getPosition();
                final var distance = this.position.distance(otherPosition);
                if (distance < model.getPerceptionRadius()) {
                    list.add(other);
                }
            }
        }
        return list;
    }

    private Vector2D calculateAlignment(final List<Boid> nearbyBoids) {
        double avgVx = 0;
        double avgVy = 0;
        if (nearbyBoids.isEmpty()) {
            return new Vector2D(0, 0);
        }
        for (final var other : nearbyBoids) {
            Vector2D otherVel = other.getVelocity();
            avgVx += otherVel.x();
            avgVy += otherVel.y();
        }
        avgVx /= nearbyBoids.size();
        avgVy /= nearbyBoids.size();
        return new Vector2D(avgVx - this.velocity.x(), avgVy - this.velocity.y()).getNormalized();
    }

    private Vector2D calculateCohesion(final List<Boid> nearbyBoids) {
        double centerX = 0;
        double centerY = 0;
        if (nearbyBoids.isEmpty()) {
            return new Vector2D(0, 0);
        }
        for (final var other : nearbyBoids) {
            final var otherPos = other.getPosition();
            centerX += otherPos.x();
            centerY += otherPos.y();
        }
        centerX /= nearbyBoids.size();
        centerY /= nearbyBoids.size();
        return new Vector2D(centerX - this.position.x(), centerY - this.position.y()).getNormalized();
    }

    private Vector2D calculateSeparation(final List<Boid> nearbyBoids, final BoidsModel model) {
        double dx = 0;
        double dy = 0;
        int count = 0;
        for (final var other : nearbyBoids) {
            final var otherPos = other.getPosition();
            final var distance = this.position.distance(otherPos);
            if (distance < model.getAvoidRadius()) {
                dx += this.position.x() - otherPos.x();
                dy += this.position.y() - otherPos.y();
                count++;
            }
        }
        if (count <= 0) {
            return new Vector2D(0, 0);
        }
        dx /= count;
        dy /= count;
        return new Vector2D(dx, dy).getNormalized();
    }
}
