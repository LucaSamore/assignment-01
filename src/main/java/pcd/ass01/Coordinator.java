package pcd.ass01;

final class Coordinator {

    private final int totalThreads;
    private int finishedCount = 0;

    Coordinator(final int totalThreads) {
        this.totalThreads = totalThreads;
    }

    // Called by the worker threads when they finish their last work (updatePosition)
    public synchronized void workDoneWaitCoordinator() {
        try {
            this.finishedCount++;
            if (this.finishedCount == this.totalThreads) {
                this.notifyAll();
            }
            while (this.finishedCount != 0) {
                this.wait();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Before update the view, the coordinator must wait for all threads to finish
    public synchronized void waitWorkers() {
        try {
            while (this.finishedCount < this.totalThreads) {
                this.wait();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Called when view is updated
    public synchronized void coordinatorDone() {
        this.finishedCount = 0;
        this.notifyAll();
    }

    public synchronized void reset() {
        this.finishedCount = 0;
        this.notifyAll();
    }
}
