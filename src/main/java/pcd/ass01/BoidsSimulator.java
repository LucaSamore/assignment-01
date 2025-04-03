package pcd.ass01;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

final class BoidsSimulator {

    private static final int FRAMERATE = 25;

    private final BoidsModel model;
    private Optional<BoidsView> view = Optional.empty();
    private int framerate;

    private final int numThreads;
    private volatile boolean running;
    private volatile boolean paused;
    private Coordinator syncMonitor;
    private WorkerBarrier workerBarrier;

    private List<BoidsWorker> workers;
    private final Object pauseLock = new Object();

    public BoidsSimulator(final BoidsModel model, final int numThreads) {
        this.model = model;
        this.numThreads = numThreads;
    }

    public void runSimulation() {
        if (this.running) {
            return;
        }
        this.running = true;
        this.paused = false;
        this.syncMonitor = new Coordinator(numThreads);
        this.workerBarrier = new WorkerBarrier(numThreads);
        this.workers = new ArrayList<>();
        this.createAndStartWorkers();
        this.runMainSimulationLoop();
        this.syncMonitor.coordinatorDone();
    }

    public void stopSimulation() {
        this.running = false;
        synchronized (this.pauseLock) {
            this.paused = false;
            this.pauseLock.notifyAll();
        }
        if (this.workers != null) {
            for (final var worker : this.workers) {
                worker.terminate();
                try {
                    worker.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            this.workers = null;
        }
        if (this.syncMonitor != null) {
            this.syncMonitor.reset();
        }
        if (this.workerBarrier != null) {
            this.workerBarrier.reset();
        }
        this.model.setCohesionWeight(1.0);
        this.model.setSeparationWeight(1.0);
        this.model.setAlignmentWeight(1.0);
        this.model.resetBoids();
    }

    public void pauseSimulation() {
        synchronized (this.pauseLock) {
            this.paused = true;
        }
    }

    public void resumeSimulation() {
        synchronized (this.pauseLock) {
            this.paused = false;
            this.pauseLock.notifyAll();
        }
    }

    public void attachView(final BoidsView view) {
        this.view = Optional.of(view);
        view.setSimulator(this);
    }

    private void createAndStartWorkers() {
        final var totalBoids = this.model.getBoids().size();
        final var boidsPerThread = totalBoids / this.numThreads;
        final var remainingBoids = totalBoids % this.numThreads;
        var startIndex = 0;
        for (var i = 0; i < this.numThreads; i++) {
            final var boidsForThisThread = boidsPerThread + (i < remainingBoids ? 1 : 0);
            final var endIndex = startIndex + boidsForThisThread;

            final var worker = new BoidsWorker(model, startIndex, endIndex, syncMonitor, workerBarrier);
            this.workers.add(worker);
            worker.start();

            startIndex = endIndex;
        }

    }

    private void runMainSimulationLoop() {
        while (this.running) {
            synchronized (this.pauseLock) {
                while (this.paused && this.running) {
                    try {
                        this.pauseLock.wait();
                    } catch (final InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                if (!this.running) {
                    break;
                }
            }
            final var frameStartTime = System.currentTimeMillis();
            this.syncMonitor.waitWorkers();
            this.updateViewAndManageFramerate(frameStartTime);
            this.syncMonitor.coordinatorDone();
        }
    }

    private void updateViewAndManageFramerate(final long t0) {
        // Update view now that all position updates are complete
        if (this.view.isPresent()) {
            this.view.get().update(framerate);
            final var t1 = System.currentTimeMillis();
            final var dtElapsed = t1 - t0;
            final var frameratePeriod = 1000/FRAMERATE;
            if (dtElapsed < frameratePeriod) {
                try {
                    Thread.sleep(frameratePeriod - dtElapsed);
                } catch (Exception ex) {
                    Thread.currentThread().interrupt();
                }
                this.framerate = FRAMERATE;
            } else {
                framerate = (int)(1000 / dtElapsed);
            }
        }
    }
}
