import java.util.*;

public class MonotoneTriangulation {

  private ArrayList<DoublyConnectedEdgeList> monotonePolygons;

  public MonotoneTriangulation() {
    monotonePolygons = new ArrayList<DoublyConnectedEdgeList>();
  }

  public MonotoneTriangulation(ArrayList<DoublyConnectedEdgeList> monotonePolygons) {
    this.monotonePolygons = monotonePolygons;
  }

  public ArrayList<DoublyConnectedEdgeList> triangulateMonotonePolygon() {
    ArrayList<DoublyConnectedEdgeList> listOfTriangles = new ArrayList<DoublyConnectedEdgeList>();
    if(this.monotonePolygons != null) {
      for(DoublyConnectedEdgeList monotoneDCEL: this.monotonePolygons) {

        //initializations
        DoublyConnectedEdgeList.DCEL_Edge temp = monotoneDCEL.rep_edge();
        DoublyConnectedEdgeList.DCEL_Edge topEdge = monotoneDCEL.rep_edge();
        DoublyConnectedEdgeList.Node top = temp.origin();
        temp = temp.next();

        //Determine the edge starting from top Node
        while(temp.id() != monotoneDCEL.rep_edge().id()) {
          if(temp.origin().y() > top.y()) {
            top = temp.origin();
            topEdge = temp;
          }
        }

        //For storing the information about location of nodes
        //i.e on left or right monotone chain
        TreeMap<Integer,Boolean> isLeft = new TreeMap<Integer,Boolean>();
        temp = topEdge;

        isLeft.put(topEdge.origin().id(),true);
        temp = temp.next();
        while(temp.origin().id() != topEdge.origin().id()) {
          if(temp.origin().y() > temp.next().origin().y())
          isLeft.put(temp.origin().id(),true);
          else
          isLeft.put(temp.origin().id(),false);
          temp = temp.next();
        }

        PriorityQueue<DoublyConnectedEdgeList.Node> pQueue = new PriorityQueue<DoublyConnectedEdgeList.Node>(1, new MonotoneNodeComparator());
        temp = topEdge;
        pQueue.add(temp.origin());
        temp = temp.next();
        while(temp.origin().id() != topEdge.origin().id()) {
          pQueue.add(temp.origin());
          temp = temp.next();
        }
        DoublyConnectedEdgeList.Node previousFirst, previousSecond;


      }
    }
    return listOfTriangles;
  }

  static class MonotoneNodeComparator implements Comparator<DoublyConnectedEdgeList.Node> {

		@Override
		public int compare(DoublyConnectedEdgeList.Node v1, DoublyConnectedEdgeList.Node v2) {
			if ((v1.y() > v2.y()) || (v1.y() == v2.y() && v1.x() > v2.x()))
				return 1;
			else
				return -1;
		}
	}
}
