package pcd.ass01;

final class WorkerBarrier {

    private final int totalWorkers;
    private int count;
    private int phase;

    WorkerBarrier(final int totalWorkers) {
        this.totalWorkers = totalWorkers;
        this.count = totalWorkers;
    }

    public synchronized void await() {
        final var workerPhase = this.phase;
        this.count--;
        if (this.count == 0) {
            this.count = this.totalWorkers;
            this.phase++;
            this.notifyAll();
        } else {
            while (this.phase == workerPhase) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    public synchronized void reset() {
        this.count = this.totalWorkers;
        this.phase++;
        this.notifyAll();
    }
}
