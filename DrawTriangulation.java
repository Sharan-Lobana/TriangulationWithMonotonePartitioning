// GroupID-21 (14114053_14114071) - Sharanpreet Singh & Vaibhav Gosain
// Date: March 15, 2018
// DrawTriangulation.java - This file is utility for rendering triangulation of monotone polygon

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.HashMap;
import java.util.TreeMap;
import javax.swing.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class DrawTriangulation extends JPanel {

  private static final double PREF_W = 800; //Preferred width
  private static final double PREF_H = 650; //Preferred height
  private static final int BORDER_GAP = 30;  //Gap from screen border
  private static final Color GRAPH_COLOR = Color.gray; //Color for graph edge
  private static final Color TRAPEZOIDAL_COLOR = Color.green; //Color for trapezoidalization edges
  private static final Color GRAPH_POINT_COLOR = new Color(150, 50, 50, 180); //Color of graph node
  private static final Stroke GRAPH_STROKE = new BasicStroke(1f);
  private static final double GRAPH_POINT_WIDTH = 12;
  private ArrayList<DoublyConnectedEdgeList> listOfTriangles;
  private int n;
  private static double hor_mul = 1;
  private static double ver_mul = 1;

  public DrawTriangulation(ArrayList<DoublyConnectedEdgeList> listOfTriangles, int n) {
    this.n = n;
    this.listOfTriangles = listOfTriangles;
    this.hor_mul= Math.min(70,(1000/n));
    this.ver_mul=Math.min(70,(700/n));
  }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      Stroke oldStroke = g2.getStroke();

      //Draw the edges on the graph;
      g2.setStroke(GRAPH_STROKE);

      for(DoublyConnectedEdgeList triangle: listOfTriangles)
      {
        DoublyConnectedEdgeList.DCEL_Edge current = triangle.rep_edge();
        do {
          double x1 = current.origin().x();
          double y1 = current.origin().y()*(-1);
          double x2 = current.next().origin().x();
          double y2 = current.next().origin().y()*(-1);

          double dx = hor_mul*(x2 - x1);
          double dy = ver_mul*(y2 - y1);
          double dtemp = dx;
          dx = dy;
          dy = dtemp;
          dx *= -1.0;
          double len = Math.sqrt(dx * dx + dy * dy);
          dx /= len;
          dy /= len;

          double mx = (int)(hor_mul*(((x1 + x2)/2) + (0.15*dx)));
          double my = (int)(ver_mul*(((y1 + y2)/2) + (0.15*dy)));

          String label = Integer.toString(current.id());
          g2.setColor(GRAPH_COLOR);
          g2.drawLine((int)Math.round(hor_mul*x1), (int)Math.round(ver_mul*y1), (int)Math.round(hor_mul*x2),(int)Math.round(ver_mul*y2));

          current = current.next();
        } while(current != triangle.rep_edge());
      }

      //Draw the vertices on the graph
      g2.setStroke(oldStroke);
      for(DoublyConnectedEdgeList triangle: listOfTriangles)
      {
        DoublyConnectedEdgeList.DCEL_Edge current = triangle.rep_edge();
        do {
          g2.setColor(GRAPH_POINT_COLOR);
          double x = hor_mul*current.origin().x() - GRAPH_POINT_WIDTH/2;
          double y = -1*ver_mul*current.origin().y() - GRAPH_POINT_WIDTH/2;
          double ovalW = GRAPH_POINT_WIDTH;
          double ovalH = GRAPH_POINT_WIDTH;
          g2.fillOval((int)Math.round(x), (int)Math.round(y), (int)Math.round(ovalW), (int)Math.round(ovalH));
          String label = Integer.toString(current.origin().id());
          g2.drawString(label, (int)(x), (int)(y));
          g2.setColor(Color.blue);
          g2.drawString("( "+Double.toString(current.origin().x())+","+Double.toString(current.origin().y())+" )", (int)(x), (int)(y+20));

          current = current.next();
        } while(current != triangle.rep_edge());
      }
   }

   @Override
   public Dimension getPreferredSize() {
      return new Dimension((int)Math.round(PREF_W), (int)Math.round(PREF_H));
   }
}
