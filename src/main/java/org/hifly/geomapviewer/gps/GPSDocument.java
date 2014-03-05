package org.hifly.geomapviewer.gps;

import org.hifly.geomapviewer.domain.ProfileSetting;
import org.hifly.geomapviewer.domain.gps.Waypoint;
import org.hifly.geomapviewer.domain.Track;
import org.hifly.geomapviewer.domain.gps.Coordinate;
import org.hifly.geomapviewer.storage.GeoMapStorage;
import org.hifly.geomapviewer.utility.GpsUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @date 26/01/14
 */
//TODO define xmlbean for kml
public abstract class GPSDocument {

    protected Logger log = LoggerFactory.getLogger(GPSDocument.class);
    protected List<Track> result = new ArrayList();
    protected List<Coordinate> coordinates = new ArrayList();
    protected List<Waypoint> waypoints = new ArrayList();
    protected Map<String, Double> elevationMap = GeoMapStorage.gpsElevationMap;
    protected Date startTime, endTime = null;
    protected ProfileSetting profileSetting;

    protected double totalDistance,
           totalDistanceCalculated,
           totalSpeed, totalEffectiveSpeed,
           totalElevation, totalCalculatedElevation,
           totalDescent, totalCalculatedDescent,
           maxSpeed,
           totalTime;
    protected int totalEffectiveSpeedPoints, totalCalculatedSpeedPoints,calories;
    protected long totalTimeDiff = 0;


    public GPSDocument(ProfileSetting profileSetting) {
        totalDistance = totalDistanceCalculated =
        totalSpeed = totalEffectiveSpeed =
        totalElevation = totalCalculatedElevation =
        totalDescent = totalCalculatedDescent =
        maxSpeed =
        totalTime = 0;
        totalEffectiveSpeedPoints = totalCalculatedSpeedPoints = calories = 0;
        this.profileSetting = profileSetting;
    }

    protected void addSpeedElement(double currentLat, double currentLon, double distance, double timeDiffInHour) {
        if (timeDiffInHour != 0) {
            double speed = distance / timeDiffInHour;
            if (speed < 100) {

                if (speed > maxSpeed) {
                    maxSpeed = speed;
                }
                totalEffectiveSpeed += speed;
                totalEffectiveSpeedPoints++;
            } else {
                log.warn("Found a spike in speed for coordinate ["+currentLat+","+currentLon+"]:"+speed);

            }
            totalSpeed += speed;
            totalCalculatedSpeedPoints++;
        }
    }

    protected void addCoordinateElement(double currentLat, double currentLon) {
        Coordinate coordinate = new Coordinate(currentLat, currentLon);
        coordinates.add(coordinate);
    }

    protected void addGPSElement(
            double currentLat,
            double currentLon,
            double lastLat,
            double lastLon,
            double distance,
            BigDecimal currentCalcEle,
            BigDecimal lastCalcEle,
            Date currentTime,
            Date lastTime,
            double totalDistanceFromStartingPoint) {
        long diffMillis = 0;
        if(currentTime!=null && lastTime!=null) {
            diffMillis = currentTime.getTime() - lastTime.getTime();
        }
        Double eleCurrent = elevationMap.get(GpsUtility.getKeyForCoordinatesMap(currentLat + "-" + currentLon));
        if(eleCurrent==null) {
            //TODO change to log4j
            System.out.println("not found elevation for:"+currentLat + "-" + currentLon);
        }
        if (currentLat != lastLat && currentLon != lastLon) {
            totalTimeDiff += diffMillis;
            Double eleLast = elevationMap.get(GpsUtility.getKeyForCoordinatesMap(lastLat + "-" + lastLon));
            if (eleCurrent != null && eleLast != null) {
                Double eleGained = eleCurrent - eleLast;
                if (eleGained > 0) {
                    totalElevation += eleGained;
                }
                if (eleGained < 0) {
                    totalDescent += Math.abs(eleGained);
                }
                addWaypointElement(currentLat, currentLon, distance, eleCurrent, eleGained, currentTime,totalDistanceFromStartingPoint);
            }
            addCalculatedElevationElement(currentCalcEle, lastCalcEle);
        }
        else {
            if (eleCurrent != null) {
                addWaypointElement(currentLat,currentLon,distance,eleCurrent,0.0,currentTime,totalDistanceFromStartingPoint);
            }
            else {
                log.warn("Coordinate not found:"+currentLat + "-" + currentLon);
            }
        }
    }

    protected void addWaypointElement(
            double currentLat,
            double currentLon,
            double distance,
            Double eleCurrent,
            Double eleGained,
            Date currentTime,
            double totalDistanceFromStartingPoint) {
        //TODO totalDistance or totalCalculatedDistance?
        Waypoint waypoint =
                GpsUtility.createWaypointWrapper(
                        currentLat,
                        currentLon,
                        distance,
                        eleCurrent,
                        eleGained,
                        totalDistanceFromStartingPoint,
                        currentTime);
        waypoints.add(waypoint);
    }

    protected void addCalculatedElevationElement(BigDecimal eleCalcCurrent, BigDecimal eleCalcLast) {
        if (eleCalcCurrent != null && eleCalcLast != null) {
            //TODO if
            Double eleGained = eleCalcCurrent.doubleValue() - eleCalcLast.doubleValue();
            if (eleGained > 0) {
                totalCalculatedElevation += eleGained;
            }

            if (eleGained < 0) {
                totalCalculatedDescent += Math.abs(eleGained);
            }
        }
    }

    public abstract List<Track> extractTrack(String gpsFile) throws Exception;
}
