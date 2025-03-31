package pcd.ass01;

record Point2D(double x, double y) {

    public Point2D sum(final Vector2D v) {
        return new Point2D(this.x + v.x(), this.y + v.y());
    }

    public double distance(final Point2D point) {
        final var dx = point.x - this.x;
        final var dy = point.y - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public String toString() {
        return "Point2D(" + this.x + "," + this.y + ")";
    }
}
