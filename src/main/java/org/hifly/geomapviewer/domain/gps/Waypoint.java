package org.hifly.geomapviewer.domain.gps;

import java.io.Serializable;
import java.util.Date;

/**
 * @author
 * @date 02/02/14
 */
public class Waypoint implements Serializable {

    private double distance = 0;
    private double gradient = 0;
    private double lat;
    private double lon;
    private double ele;
    private double distanceFromStartingPoint;
    private Date dateRelevation;

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public Date getDateRelevation() {
        return dateRelevation;
    }

    public void setDateRelevation(Date dateRelevation) {
        this.dateRelevation = dateRelevation;
    }

    public double getEle() {
        return ele;
    }

    public void setEle(double ele) {
        this.ele = ele;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getGradient() {
        return gradient;
    }

    public void setGradient(double gradient) {
        this.gradient = gradient;
    }

    public double getDistanceFromStartingPoint() {
        return distanceFromStartingPoint;
    }

    public void setDistanceFromStartingPoint(double distanceFromStartingPoint) {
        this.distanceFromStartingPoint = distanceFromStartingPoint;
    }
}
