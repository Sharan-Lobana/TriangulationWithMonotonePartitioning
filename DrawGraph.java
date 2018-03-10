import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.HashMap;
import javax.swing.*;

@SuppressWarnings("serial")
public class DrawGraph extends JPanel {

  private static final int PREF_W = 800; //Preferred width
  private static final int PREF_H = 650; //Preferred height
  private static final int BORDER_GAP = 30;  //Gap from screen border
  private static final Color GRAPH_COLOR = Color.gray; //Color for graph edge
  private static final Color GRAPH_POINT_COLOR = new Color(150, 50, 50, 180); //Color of graph node
  private static final Stroke GRAPH_STROKE = new BasicStroke(3f);
  private static final int GRAPH_POINT_WIDTH = 12;
  private Vertex vertices[];
  private int n;
  private static int hor_mul = 1;
  private static int ver_mul = 1;

  public DrawGraph(Vertex vertices[], int n) {
    this.n = n;
    this.vertices = vertices;
    this.hor_mul=(1000/n);
    this.ver_mul=(700/n);
  }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      Stroke oldStroke = g2.getStroke();

      //Draw the edges on the graph;
      g2.setStroke(GRAPH_STROKE);
      for (int i=1; i<=n; i++) {
          Vertex current_vertex = vertices[i];
          Vertex next_vertex = vertices[i+1];

          int x1 = current_vertex.x;
          int y1 = current_vertex.y*(-1);
          int x2 = next_vertex.x;
          int y2 = next_vertex.y*(-1);

          int dx = hor_mul*(x2 - x1);
          int dy = ver_mul*(y2 - y1);
          double slope = dx == 0 ? dy * 100000 : dy/dx;

          int mx = (hor_mul*(x1 + x2)/2) + (int)(-slope * 20 * dx/Math.sqrt(dx * dx + dy * dy));
          int my = (ver_mul*(y1 + y2)/2) + (int)(-slope * 20 * dy/Math.sqrt(dx * dx + dy * dy));

          String label = Long.toString(i);
          g2.setColor(Color.red);
          g2.drawString(label, mx, my);
          g2.setColor(GRAPH_COLOR);
          g2.drawLine(hor_mul*x1, ver_mul*y1, hor_mul*x2,ver_mul*y2);
      }

      //Draw the vertices on the graph
      g2.setStroke(oldStroke);
      for (int i = 1; i <=n; i++) {
         g2.setColor(GRAPH_POINT_COLOR);
         int x = hor_mul*vertices[i].x - GRAPH_POINT_WIDTH/2;
         int y = -1*ver_mul*vertices[i].y - GRAPH_POINT_WIDTH/2;
         int ovalW = GRAPH_POINT_WIDTH;
         int ovalH = GRAPH_POINT_WIDTH;
         g2.fillOval(x, y, ovalW, ovalH);
         String label = Integer.toString(vertices[i].index);
         g2.drawString(label, (int)(x), (int)(y));
         g2.setColor(Color.blue);
         g2.drawString("( "+Integer.toString(vertices[i].x)+","+Integer.toString(vertices[i].y)+" )", (int)(x), (int)(y+20));
      }
   }

   @Override
   public Dimension getPreferredSize() {
      return new Dimension(PREF_W, PREF_H);
   }
}
