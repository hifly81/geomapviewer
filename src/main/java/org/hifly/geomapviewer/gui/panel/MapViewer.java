package org.hifly.geomapviewer.gui.panel;

import org.hifly.geomapviewer.domain.gps.WaypointSegment;
import org.hifly.geomapviewer.gui.marker.CircleMarker;
import org.hifly.geomapviewer.gui.marker.LineMarker;
import org.hifly.geomapviewer.gui.marker.TooltipMarker;
import org.hifly.geomapviewer.storage.GeoMapStorage;
import org.hifly.geomapviewer.utility.GpsUtility;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.util.List;

/**
 * @author
 * @date 03/02/14
 */
public class MapViewer extends JMapViewer implements MouseListener, MouseMotionListener {
    protected Logger log = LoggerFactory.getLogger(MapViewer.class);

    public Map<String,WaypointSegment>  mapCircleCoordinates = new Hashtable<>();
    public TooltipMarker lastOpenedMarker = null;


    public MapViewer(
            List<List<ICoordinate>> coordinates,
            List<Map<String, WaypointSegment>> coordinatesNewKm,
            int zoomLevel) {
        super();

        addMouseListener(this);

        double limitFlat = 200;
        double limitHill = 600;
        double limitMountain = 800;
        double currentLimit = 0;

        int indexMap = 0;
        if (coordinates != null) {
            for (List<ICoordinate> listCoordinates : coordinates) {
                List<ICoordinate> listTemp = new ArrayList<>();
                Map<String, WaypointSegment> tempMap = coordinatesNewKm.get(indexMap);
                for (int i = 0; i < listCoordinates.size(); i++) {
                    String key = GpsUtility.getKeyForCoordinatesMap(
                            String.valueOf(listCoordinates.get(i).getLat()) + "-" + String.valueOf(listCoordinates.get(i).getLon()));
                    Double obj = GeoMapStorage.gpsElevationMap.get(key);
                    double ele = 0;
                    if (obj == null) {
                        //skip element
                        log.warn("Elevation not found for:" + key);
                    } else {
                        ele = GeoMapStorage.gpsElevationMap.get(key);
                    }
                    if (listTemp.size() == 0) {
                        if (ele < limitFlat) {
                            currentLimit = limitFlat;
                        } else if (ele >= limitFlat && ele < limitHill) {
                            currentLimit = limitHill;
                        } else {
                            currentLimit = limitMountain;
                        }
                    }

                    if (currentLimit == limitFlat) {
                        if (ele > currentLimit) {
                            MapPolygonImpl poly = new LineMarker(listTemp, Color.GREEN);
                            addMapPolygon(poly);
                            listTemp = new ArrayList<>();
                            i--;
                        } else {
                            listTemp.add(listCoordinates.get(i));
                        }
                    } else if (currentLimit == limitHill) {
                        if (ele < limitFlat || ele > limitHill) {
                            MapPolygonImpl poly = new LineMarker(listTemp, Color.YELLOW);
                            addMapPolygon(poly);
                            listTemp = new ArrayList<>();
                            i--;
                        } else {
                            listTemp.add(listCoordinates.get(i));
                        }
                    } else if (currentLimit == limitMountain) {
                        if (ele < limitHill) {
                            MapPolygonImpl poly = new LineMarker(listTemp, Color.RED);
                            addMapPolygon(poly);
                            listTemp = new ArrayList<>();
                            i--;
                        } else {
                            listTemp.add(listCoordinates.get(i));
                        }
                    }
                }


                if (listTemp.size() > 0) {
                    Color color = null;
                    if (currentLimit <= limitFlat) {
                        color = Color.GREEN;
                    } else if (currentLimit > limitFlat && currentLimit <= limitMountain) {
                        color = Color.YELLOW;
                    } else {
                        color = Color.RED;
                    }

                    MapPolygonImpl poly = new LineMarker(listTemp, color);
                    addMapPolygon(poly);
                }

                for (int i = 0; i < listCoordinates.size(); i++) {
                    String key = String.valueOf(listCoordinates.get(i).getLat())
                            + "-"
                            +
                            String.valueOf(listCoordinates.get(i).getLon());
                    WaypointSegment waypoint = tempMap.get(key);
                    if (waypoint != null) {
                        CircleMarker c = new CircleMarker(
                                listCoordinates.get(i).getLat(),
                                listCoordinates.get(i).getLon(),
                                waypoint,
                                this);
                        addMapMarker(c);
                        mapCircleCoordinates.put(listCoordinates.get(i).getLat()+"-"+listCoordinates.get(i).getLon(),waypoint);
                    }
                }
                indexMap++;

            }
        }

        //final settings for mapviewer

        if (coordinates != null && !coordinates.isEmpty()) {
            setDisplayPositionByLatLon(
                    coordinates.get(0).get(0).getLat(),
                    coordinates.get(0).get(0).getLon(), zoomLevel);
        }
        setZoom(zoomLevel);
        setDisplayToFitMapMarkers();
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        Coordinate position = getPosition(me.getPoint());

        WaypointSegment waypoint = this.mapCircleCoordinates.get(position.getLat() + "-" + position.getLon());
        if(waypoint == null) {
            double distance = 1111111111;
            for (Map.Entry<String, WaypointSegment> entry : mapCircleCoordinates.entrySet()) {
                    double lat = Math.abs(position.getLat()-Double.valueOf(entry.getKey().split("-")[0]));
                    double lon = Math.abs(position.getLon()-Double.valueOf(entry.getKey().split("-")[1]));
                    double tot = lat+lon;
                    if(tot<distance) {
                        distance = tot;
                        waypoint = entry.getValue();
                    }
            }
        }

        System.out.println("found marker:" + waypoint.getKm());

        if(lastOpenedMarker!=null) {
           removeMapMarker(lastOpenedMarker);
        }

        TooltipMarker t = new TooltipMarker(
                position.getLat(),
                position.getLon(),
                me.getPoint().getX(),
                me.getPoint().getY(),
                waypoint,
                this);
        addMapMarker(t);
        lastOpenedMarker = t;


    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(lastOpenedMarker!=null) {
            removeMapMarker(lastOpenedMarker);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(lastOpenedMarker!=null) {
            removeMapMarker(lastOpenedMarker);
        }
    }
}