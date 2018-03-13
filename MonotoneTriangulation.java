import java.util.*;

public class MonotoneTriangulation {

  private ArrayList<DoublyConnectedEdgeList> monotonePolygons;

  public MonotoneTriangulation() {
    monotonePolygons = new ArrayList<DoublyConnectedEdgeList>();
  }

  public MonotoneTriangulation(ArrayList<DoublyConnectedEdgeList> monotonePolygons) {
    this.monotonePolygons = monotonePolygons;
  }

  public boolean CCW(DoublyConnectedEdgeList.Node one, DoublyConnectedEdgeList.Node two, DoublyConnectedEdgeList.Node three) {
    double x1,y1,x2,y2;
    x1 = two.x()-one.x();
    y1 = two.y()-one.y();
    x2 = three.x()-one.x();
    y2 = three.y()-one.y();
    if(x1*y2-x2*y1 < 0.0)
      return true;
    return false;
  }

  public double calculateAngle(DoublyConnectedEdgeList.Node a, DoublyConnectedEdgeList.Node b, DoublyConnectedEdgeList.Node c) {
    
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
        //TODO: is Reflex status will change dynamically with the algorithm
        TreeMap<Integer,Boolean> isReflex = new TreeMap<Integer,Boolean>();
        temp = topEdge;

        TreeMap<Integer,Double> interiorAngle = new TreeMap<Integer,Double>();
        DoublyConnectedEdgeList.Node prev = topEdge.origin(),current;
        isLeft.put(topEdge.origin().id(),true);
        isReflex.put(topEdge.origin().id(),false);
        interiorAngle.put(temp.origin().id(),calculateAngle(temp.prev().origin(),temp.origin(),temp.next().origin()));
        temp = temp.next();
        while(temp.origin().id() != topEdge.origin().id()) {

          if(temp.origin().y() > temp.next().origin().y()) {
            isLeft.put(temp.origin().id(),true);
          }
          else {
            isLeft.put(temp.origin().id(),false);
          }

          //Calculate the interior angle
          interiorAngle.put(temp.origin().id(),calculateAngle(temp.prev().origin(),temp.origin(),temp.next().origin()));
          current = temp.origin();
          temp = temp.next();

          if(CCW(prev,current,temp.origin()))
          isReflex.put(current.id(),false);
          else
          isReflex.put(current.id(),true);

          prev = current;
        }

        PriorityQueue<DoublyConnectedEdgeList.Node> pQueue = new PriorityQueue<DoublyConnectedEdgeList.Node>(1, new MonotoneNodeComparator());
        temp = topEdge;
        pQueue.add(temp.origin());
        temp = temp.next();
        while(temp.origin().id() != topEdge.origin().id()) {
          pQueue.add(temp.origin());
          temp = temp.next();
        }
        Stack<DoublyConnectedEdgeList.Node> stack = new Stack<DoublyConnectedEdgeList.Node>();
        ArrayList<DoublyConnectedEdgeList.Triangle> triangles = new ArrayList<DoublyConnectedEdgeList.Triangle>();
        DoublyConnectedEdgeList.Node ph,pi,tempNode,prevLeft,prevRight; //ph is previous,pi is current
        prevLeft = null;
        prevRight = null;

        ph = pQueue.poll();
        stack.push(ph);

        //Update latest left and right nodes encountered before pi
        if(isLeft.get(ph.id()))
        prevLeft = ph;
        else
        prevRight = ph;

        ph = pQueue.poll();
        stack.push(ph);

        //Update latest left and right nodes encountered before pi
        if(isLeft.get(ph.id()))
        prevLeft = ph;
        else
        prevRight = ph;

        while(!pQueue.isEmpty()) {
          pi = pQueue.poll();
          //if pi is adjacent to previous node
          if(isLeft.get(pi.id()) == isLeft.get(ph.id())) {
            while(!isReflex.get(ph.id())){
              tempNode = stack.peek();
              stack.pop();
              ph = stack.peek();
              // diagonals.add(ph,pi);
              //Maintain counterclockwise nature of nodes
              if(isLeft.get(pi.id()))
              triangles.add(new DoublyConnectedEdgeList.Triangle(pi,ph,tempNode,DoublyConnectedEdgeList.DCEL_count()));
              else
              triangles.add(new DoublyConnectedEdgeList.Triangle(pi,tempNode,ph,DoublyConnectedEdgeList.DCEL_count()));
              DoublyConnectedEdgeList.incrementDCELCount();
            }
            stack.push(pi);
          }
          else {
            //add diagonals to the nodes in other sides
            while(!stack.isEmpty()) {
              // diagonals.add(stack.pop(),pi);
              tempNode = stack.peek();
              stack.pop();
              if(isLeft.get(pi.id())) {
                if(stack.isEmpty() && prevLeft != null)
                triangles.add(new DoublyConnectedEdgeList.Triangle(pi,tempNode,prevLeft,DoublyConnectedEdgeList.DCEL_count()));
                else
                triangles.add(new DoublyConnectedEdgeList.Triangle(pi,tempNode,stack.peek(),DoublyConnectedEdgeList.DCEL_count()));
                DoublyConnectedEdgeList.incrementDCELCount();
              }
              else {
                if(stack.isEmpty() && prevRight != null)
                triangles.add(new DoublyConnectedEdgeList.Triangle(pi,prevRight,tempNode,DoublyConnectedEdgeList.DCEL_count()));
                else
                triangles.add(new DoublyConnectedEdgeList.Triangle(pi,stack.peek(),tempNode,DoublyConnectedEdgeList.DCEL_count()));
                DoublyConnectedEdgeList.incrementDCELCount();
              }
            }
            stack.push(ph);
            stack.push(pi);
            ph = pi;  //Update the previous node
          }

          //Update the latest left and right node encountered before pi
          if(isLeft.get(pi.id()))
          prevLeft = pi;
          else
          prevRight = pi;
        }

        // //TODO: check for cases when stack might not be empty and how to handle that
        // if(!stack.isEmpty() && stack.size() > 1)
        // {
        //   pi = stack.pop();
        //   ph = stack.pop();
        //   //diagonals.add(ph,pi);
        // }
        for(DoublyConnectedEdgeList.Triangle triangle: triangles) {
          listOfTriangles.add(triangle.convertToDCEL());
        }
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
