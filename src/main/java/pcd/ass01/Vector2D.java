/*
 *   V2d.java
 *
 * Copyright 2000-2001-2002  aliCE team at deis.unibo.it
 *
 * This software is the proprietary information of deis.unibo.it
 * Use is subject to license terms.
 *
 */
package pcd.ass01;

record Vector2D(double x, double y) {

    public Vector2D sum(final Vector2D v) {
        return new Vector2D(this.x + v.x, this.y + v.y);
    }

    public double abs() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public Vector2D getNormalized() {
        final var module = Math.sqrt(this.x * this.x + this.y * this.y);
        return new Vector2D(this.x / module, this.y / module);
    }

    public Vector2D mul(final double factor) {
        return new Vector2D(this.x * factor, this.y * factor);
    }

    public String toString() {
        return "Vector2D(" + this.x + "," + this.y + ")";
    }
}
