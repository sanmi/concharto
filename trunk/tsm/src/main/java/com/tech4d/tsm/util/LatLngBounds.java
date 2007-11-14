package com.tech4d.tsm.util;

import com.vividsolutions.jts.geom.Point;

public class LatLngBounds {
	private Point southWest;
	private Point northEast;

	public LatLngBounds(Point southWest, Point northEast) {
		super();
		this.southWest = southWest;
		this.northEast = northEast;
	}
	public LatLngBounds() {
		super();
	}
	public Point getSouthWest() {
		return southWest;
	}
	public void setSouthWest(Point southWest) {
		this.southWest = southWest;
	}
	public Point getNorthEast() {
		return northEast;
	}
	public void setNorthEast(Point northEast) {
		this.northEast = northEast;
	}
}
