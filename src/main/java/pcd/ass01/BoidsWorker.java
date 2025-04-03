package pcd.ass01;

final class BoidsWorker extends Thread {

    private final BoidsModel model;
    private final int startIndex;
    private final int endIndex;
    private final Coordinator syncMonitor;
    private final WorkerBarrier workerBarrier;
    private volatile boolean running = true;

    BoidsWorker(final BoidsModel model,
                final int startIndex,
                final int endIndex,
                final Coordinator syncMonitor,
                final WorkerBarrier workerBarrier) {
        this.model = model;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.syncMonitor = syncMonitor;
        this.workerBarrier = workerBarrier;
    }

    @Override
    public void run() {
        while (this.running) {
            final var boids = this.model.getBoids();
            this.workerBarrier.await();
            if (!this.running) {
                break;
            }
            for (var i = startIndex; i < endIndex && i < boids.size(); i++) {
                boids.get(i).updateVelocity(this.model);
            }
            this.workerBarrier.await();
            if (!this.running) {
                break;
            }
            for (var i = startIndex; i < endIndex && i < boids.size(); i++) {
                boids.get(i).updatePosition(model);
            }
            this.syncMonitor.workDoneWaitCoordinator();
            if (!running) {
                break;
            }
        }
    }

    public void terminate() {
        this.running = false;
        this.interrupt(); // Interrupt if waiting at a barrier or monitor
    }
}
