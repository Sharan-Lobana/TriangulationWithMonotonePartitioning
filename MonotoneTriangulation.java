import java.util.*;

public class MonotoneTriangulation {
  private static final double EPSILON = 0.0001;
  private ArrayList<DoublyConnectedEdgeList> monotonePolygons;

  public MonotoneTriangulation() {
    monotonePolygons = new ArrayList<DoublyConnectedEdgeList>();
  }

  public MonotoneTriangulation(ArrayList<DoublyConnectedEdgeList> monotonePolygons) {
    this.monotonePolygons = monotonePolygons;
  }

  //computes ccw between vectors one->two and two->three
  public boolean CCW(DoublyConnectedEdgeList.Node one, DoublyConnectedEdgeList.Node two, DoublyConnectedEdgeList.Node three) {
    double x1,y1,x2,y2; //x1,y1 are the new coordinates of b; x2,y2 are the new coordinates of c
    x1 = two.x()-one.x(); //move a to origin such that we are concerned with coordinates of b only
    y1 = two.y()-one.y(); //move a to origin such that we are concerned with coordinates of b only
    x2 = three.x()-two.x(); //move b to origin such that we are concerned with coordinates of c only
    y2 = three.y()-two.y(); //move b to origin such that we are concerned with coordinates of c only
    if(x1*y2-x2*y1 < 0.0)
      return true;
    return false;
  }

  //computes the smaller angle between the vectors a->b and b->c
  public double calculateAngleUtil(DoublyConnectedEdgeList.Node a, DoublyConnectedEdgeList.Node b, DoublyConnectedEdgeList.Node c) {
    double x1,y1,x2,y2;
    x1 = b.x()-a.x();
    y1 = b.y()-a.y();
    x2 = c.x()-b.x();
    y2 = c.y()-b.y();
    double angle = Math.acos((x1*x2+y1*y2)/(Math.pow(x1*x1+y1*y1,0.5)*Math.pow(x2*x2+y2*y2,0.5)));

    //Debug
    System.out.printf("\nInside calculateAngleUtil\n");
    System.out.printf("Node ids are a: %d, b: %d, c: %d\n", a.id(), b.id(), c.id());
    System.out.printf("The coordinates for calculating angle are x1: %f, y1: %f, x2: %f, y2: %f\n", x1, y1, x2, y2);
    System.out.printf("The angle so calculated is %f\n\n", angle);

    return angle;
  }

  //computes the interior angle of vertex b such the the adjoining edges are a->b and b->c
  public double calculateAngle(DoublyConnectedEdgeList.Node a, DoublyConnectedEdgeList.Node b, DoublyConnectedEdgeList.Node c) {
    double x1,y1,x2,y2;
    x1 = b.x()-a.x();
    y1 = b.y()-a.y();
    x2 = c.x()-b.x();
    y2 = c.y()-b.y();

    double angle = Math.acos((x1*x2+y1*y2)/(Math.pow(x1*x1+y1*y1,0.5)*Math.pow(x2*x2+y2*y2,0.5)));

    //Debug
    System.out.printf("\nInside calculateAngle\n");
    System.out.printf("Node ids are a: %d, b: %d, c: %d\n", a.id(), b.id(), c.id());
    System.out.printf("The coordinates for calculating angle are x1: %f, y1: %f, x2: %f, y2: %f\n", x1, y1, x2, y2);
    System.out.printf("The angle so calculated is %f\n", angle);

    if(Math.abs(x1*y2 - x2*y1) <= EPSILON)
    angle = Math.PI;
    else if(x1*y2-y1*x2 < 0.0)
    angle += Math.PI;
    else
    angle = Math.PI - angle;

    //Debug
    System.out.printf("The angle returned is %f\n\n", angle);

    return angle;
  }

  public ArrayList<DoublyConnectedEdgeList> triangulateMonotonePolygon() {
    ArrayList<DoublyConnectedEdgeList> listOfTriangles = new ArrayList<DoublyConnectedEdgeList>();
    if(this.monotonePolygons != null) {
      for(DoublyConnectedEdgeList monotoneDCEL: this.monotonePolygons) {

        //initializations
        DoublyConnectedEdgeList.DCEL_Edge temp = monotoneDCEL.rep_edge();
        //Debug
        System.out.printf("Montone Polygon ID: %d\n", monotoneDCEL.id());
        System.out.printf("Temp Edge ID: %d\n",temp.id());
        System.out.printf("Temp Edge Origin ID: %d\n", temp.origin().id());
        DoublyConnectedEdgeList.DCEL_Edge topEdge = monotoneDCEL.rep_edge();
        DoublyConnectedEdgeList.Node top = temp.origin();
        temp = temp.next();

        //Determine the edge starting from top Node
        while(temp.id() != monotoneDCEL.rep_edge().id()) {
          if(temp.origin().y() > top.y()) {
            top = temp.origin();
            topEdge = temp;
          }
          temp = temp.next();
        }

        //Debug
        System.out.println("Exited first while loop");
        //For storing the information about location of nodes
        //i.e on left or right monotone chain
        TreeMap<Integer,Boolean> isLeft = new TreeMap<Integer,Boolean>();
        TreeMap<Integer,Boolean> isReflex = new TreeMap<Integer,Boolean>();
        temp = topEdge;

        TreeMap<Integer,Double> interiorAngle = new TreeMap<Integer,Double>();
        DoublyConnectedEdgeList.Node prev = topEdge.origin(),current, prevAngleNode;
        isLeft.put(topEdge.origin().id(),true);
        isReflex.put(topEdge.origin().id(),false);
        interiorAngle.put(temp.origin().id(),calculateAngle(temp.prev().origin(),temp.origin(),temp.next().origin()));
        temp = temp.next();

        //Debug
        System.out.printf("The interior angle of top node with ID: %d is: %f\n", temp.prev().origin().id(), interiorAngle.get(temp.prev().origin().id()));

        while(temp.origin().id() != topEdge.origin().id()) {

          if(temp.origin().y() > temp.next().origin().y()) {
            isLeft.put(temp.origin().id(),true);
          }
          else {
            isLeft.put(temp.origin().id(),false);
          }

          //Debug
          System.out.printf("Computation for DCEL with id: %d", monotoneDCEL.id());

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

              //TODO:check the validity of this statement
              if(stack.isEmpty())
              break;
              ph = stack.peek();
              // diagonals.add(ph,pi);
              //Maintain counterclockwise nature of nodes
              if(isLeft.get(pi.id()))
                triangles.add(new DoublyConnectedEdgeList.Triangle(pi,ph,tempNode,DoublyConnectedEdgeList.DCEL_count()));
              else
                triangles.add(new DoublyConnectedEdgeList.Triangle(pi,tempNode,ph,DoublyConnectedEdgeList.DCEL_count()));

              //Update the interior angle of the vertex ph, works for both left and right vertices
              interiorAngle.put(ph.id(),interiorAngle.get(ph.id())-(Math.PI -calculateAngleUtil(tempNode,ph,pi)));
              if(interiorAngle.get(ph.id()) <= Math.PI)
                isReflex.put(ph.id(),false);
              DoublyConnectedEdgeList.incrementDCELCount();
            }
            stack.push(pi);
          }
          else {
            boolean angleChangeFlag = true;
            //add diagonals to the nodes in other sides
            while(!stack.isEmpty()) {
              tempNode = stack.peek();
              stack.pop();
              if(isLeft.get(pi.id())) {
                if(stack.isEmpty() && prevLeft != null) {
                  triangles.add(new DoublyConnectedEdgeList.Triangle(pi,tempNode,prevLeft,DoublyConnectedEdgeList.DCEL_count()));
                  prevAngleNode = prevLeft;
                }
                else {
                  triangles.add(new DoublyConnectedEdgeList.Triangle(pi,tempNode,stack.peek(),DoublyConnectedEdgeList.DCEL_count()));
                  prevAngleNode = stack.peek();
                }
              }
              else {
                if(stack.isEmpty() && prevRight != null) {
                  triangles.add(new DoublyConnectedEdgeList.Triangle(pi,prevRight,tempNode,DoublyConnectedEdgeList.DCEL_count()));
                  prevAngleNode = prevRight;
                }
                //TODO: Check validity of the following statement
                else if(!stack.isEmpty()){
                  triangles.add(new DoublyConnectedEdgeList.Triangle(pi,stack.peek(),tempNode,DoublyConnectedEdgeList.DCEL_count()));
                  prevAngleNode = stack.peek();
                }
                else
                  prevAngleNode = null;
              }

              //angle change needs to be computed only once
              if(angleChangeFlag) {
                interiorAngle.put(tempNode.id(),interiorAngle.get(tempNode.id())-(Math.PI - calculateAngleUtil(pi,tempNode,prevAngleNode)));
                interiorAngle.put(pi.id(),interiorAngle.get(pi.id())-(Math.PI - calculateAngleUtil(tempNode,pi,prevAngleNode)));
                if(interiorAngle.get(tempNode.id()) <= Math.PI)
                  isReflex.put(tempNode.id(),false);
                if(interiorAngle.get(pi.id()) <= Math.PI)
                  isReflex.put(pi.id(),false);
                angleChangeFlag = false;
              }

              DoublyConnectedEdgeList.incrementDCELCount();
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
				return -1;  //return -1 if v1 needs to come before v2 in the final ordering
			else
				return 1;
		}
	}
}
