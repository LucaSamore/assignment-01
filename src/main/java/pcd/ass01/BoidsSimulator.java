package pcd.ass01;

import java.util.Optional;

final class BoidsSimulator {

    private static final int FRAMERATE = 25;

    private final BoidsModel model;
    private Optional<BoidsView> view = Optional.empty();
    private int framerate;

    public BoidsSimulator(final BoidsModel model) {
        this.model = model;
    }

    public void runSimulation() {
        while (true) {
            final var t0 = System.currentTimeMillis();
            final var boids = model.getBoids();
            for (final var boid : boids) {
                boid.updateVelocity(model);
            }
            for (final var boid : boids) {
                boid.updatePosition(model);
            }
            if (this.view.isPresent()) {
                this.view.get().update(framerate);
                final var t1 = System.currentTimeMillis();
                final var dtElapsed = t1 - t0;
                final var frameratePeriod = 1000 / FRAMERATE;
                if (dtElapsed < frameratePeriod) {
                    try {
                        Thread.sleep(frameratePeriod - dtElapsed);
                    } catch (Exception ignored) {
                    }
                    this.framerate = FRAMERATE;
                } else {
                    this.framerate = (int) (1000 / dtElapsed);
                }
            }
        }
    }

    public void attachView(final BoidsView view) {
        this.view = Optional.of(view);
    }
}
