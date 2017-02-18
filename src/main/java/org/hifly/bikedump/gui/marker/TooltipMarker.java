package org.hifly.bikedump.gui.marker;

import org.hifly.bikedump.domain.gps.WaypointSegment;
import org.hifly.bikedump.gui.panel.MapViewer;
import org.hifly.bikedump.utility.GPSUtility;
import org.hifly.bikedump.utility.TimeUtility;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class TooltipMarker extends MapMarkerDot  {
    protected String text,text2,text3,text4,text5=null;
    protected WaypointSegment waypoint;
    protected MapViewer mapViewer;
    private double x,y;

    public TooltipMarker(double lat, double lon, double x, double y,WaypointSegment waypoint, MapViewer viewer) {
        super(lat,lon);

        text =  "Lap: "+waypoint.getUnit();
        text2 = TimeUtility.convertToString("dd/MM/yyyy HH:mm:ss", waypoint.getTimeSpent());
        text3 = TimeUtility.toStringFromTimeDiff(waypoint.getTimeIncrement());
        text4 = GPSUtility.roundDoubleStat(waypoint.getAvgSpeed()) + " km/h";
        text5 = GPSUtility.roundDoubleStat(waypoint.getEleGained())+ " m";

        this.waypoint = waypoint;
        this.mapViewer = viewer;
        this.x = x;
        this.y = y;
    }

    public TooltipMarker(Color color, double lat, double lon) {
        super(color,lat,lon);
    }

    @Override
    public void paint(Graphics g, Point position, int radio) {
        if (g instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D) g;
            Composite oldComposite = g2.getComposite();
            g2.draw(new Rectangle2D.Double(x, y, 105, 105));

            g.setFont(new Font("Arial", Font.BOLD, 10));
            g.setColor(Color.BLUE);

            g.drawString(text,(int)x+5,(int)y+15);
            g.drawString(text2,(int)x+5,(int)y+30);
            g.drawString(text3,(int)x+5,(int)y+45);
            g.drawString(text4,(int)x+5,(int)y+60);
            g.drawString(text5,(int)x+5,(int)y+75);
            g2.setComposite(oldComposite);

        }
    }


}