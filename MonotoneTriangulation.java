// GroupID-21 (14114053_14114071) - Sharanpreet Singh & Vaibhav Gosain
// Date: March 15, 2018
// MonotoneTriangulation.java - This file contains algorithm for triangulation
// of a monotone polygon in linear time.

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
    if((x1*y2-x2*y1) < 0.0)
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
    if(Math.abs(x1*y2 - x2*y1) <= EPSILON)
    angle = Math.PI;
    else if(x1*y2-y1*x2 < 0.0)
    angle += Math.PI;
    else
    angle = Math.PI - angle;
    return angle;
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
          temp = temp.next();
        }
        TreeMap<Integer,Boolean> isLeft = new TreeMap<Integer,Boolean>();
        TreeMap<Integer,Boolean> isReflex = new TreeMap<Integer,Boolean>();
        temp = topEdge;

        TreeMap<Integer,Double> interiorAngle = new TreeMap<Integer,Double>();
        DoublyConnectedEdgeList.Node prev = topEdge.origin(),current, prevAngleNode;
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

          if(interiorAngle.get(current.id()) >= (Math.PI-EPSILON))
          isReflex.put(current.id(),true);
          else
          isReflex.put(current.id(),false);

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
        DoublyConnectedEdgeList.Node ph,pi,tempNode; //ph is previous,pi is current

        ph = pQueue.poll();
        stack.push(ph);
        ph = pQueue.poll();
        stack.push(ph);
        while(!pQueue.isEmpty()) {
          pi = pQueue.poll();
          ph = stack.peek();

          //if pi is adjacent to previous node
          if((isLeft.get(pi.id()) && isLeft.get(ph.id())) || (!isLeft.get(ph.id()) && !isLeft.get(pi.id()))) {
            while(!isReflex.get(ph.id()) && stack.size() >= 2){
              tempNode = stack.peek();
              stack.pop();
              ph = stack.peek();

              //Maintain counterclockwise nature of nodes
              if(isLeft.get(pi.id())){
                triangles.add(new DoublyConnectedEdgeList.Triangle(pi,ph,tempNode,DoublyConnectedEdgeList.DCEL_count()));
              }
              else {
                triangles.add(new DoublyConnectedEdgeList.Triangle(pi,tempNode,ph,DoublyConnectedEdgeList.DCEL_count()));
              }

              //Update the interior angle of the vertex ph, works for both left and right vertices
              if(isLeft.get(pi.id())){
                double deltaAnglePh = Math.PI -calculateAngleUtil(pi,ph,tempNode);
                interiorAngle.put(ph.id(),interiorAngle.get(ph.id())-deltaAnglePh);
                double deltaAnglePi = Math.PI - deltaAnglePh - interiorAngle.get(tempNode.id());
                interiorAngle.put(pi.id(),interiorAngle.get(pi.id())-deltaAnglePi);
              }else {
                double deltaAnglePh = Math.PI -calculateAngleUtil(tempNode,ph,pi);
                interiorAngle.put(ph.id(),interiorAngle.get(ph.id())-deltaAnglePh);
                double deltaAnglePi = Math.PI - deltaAnglePh - interiorAngle.get(tempNode.id());
                interiorAngle.put(pi.id(),interiorAngle.get(pi.id())-deltaAnglePi);
              }

              if(interiorAngle.get(ph.id()) <= (Math.PI - EPSILON)) {
                isReflex.put(ph.id(),false);
              }

              if(interiorAngle.get(pi.id()) <= (Math.PI - EPSILON)) {
                isReflex.put(pi.id(),false);
              }

              DoublyConnectedEdgeList.incrementDCELCount();
            }

            stack.push(pi);
          }
          else {
            boolean angleChangeFlag = true;
            DoublyConnectedEdgeList.Node nodeToBePushed = null;

            //add diagonals to the nodes in other sides
            while(stack.size() >= 2) {
              tempNode = stack.peek();

              if(angleChangeFlag == true)
                nodeToBePushed = tempNode;
              stack.pop();
              if(isLeft.get(pi.id())) {
                triangles.add(new DoublyConnectedEdgeList.Triangle(pi,tempNode,stack.peek(),DoublyConnectedEdgeList.DCEL_count()));
              }
              else {
                triangles.add(new DoublyConnectedEdgeList.Triangle(pi,stack.peek(),tempNode,DoublyConnectedEdgeList.DCEL_count()));
              }

              //angle change needs to be computed only once
              if(angleChangeFlag) {

                if(isLeft.get(pi.id())) {
                  double deltaAngleTemp = Math.PI - calculateAngleUtil(pi,tempNode,stack.peek());
                  interiorAngle.put(tempNode.id(),interiorAngle.get(tempNode.id())-deltaAngleTemp);
                  double deltaAnglePi = Math.PI - calculateAngleUtil(stack.peek(),pi,tempNode);
                  interiorAngle.put(pi.id(),interiorAngle.get(pi.id())-deltaAnglePi);
                }
                else {
                  double deltaAngleTemp = Math.PI - calculateAngleUtil(stack.peek(), tempNode, pi);
                  interiorAngle.put(tempNode.id(),interiorAngle.get(tempNode.id())-deltaAngleTemp);
                  double deltaAnglePi = Math.PI - calculateAngleUtil(tempNode, pi, stack.peek());
                  interiorAngle.put(pi.id(),interiorAngle.get(pi.id())-deltaAnglePi);
                }

                if(interiorAngle.get(tempNode.id()) <= (Math.PI-EPSILON)) {
                  isReflex.put(tempNode.id(),false);
                }

                if(interiorAngle.get(pi.id()) <= (Math.PI-EPSILON)) {
                  isReflex.put(pi.id(),false);
                }

                angleChangeFlag = false;
              }

              DoublyConnectedEdgeList.incrementDCELCount();
            }
            stack.pop();

            if(nodeToBePushed != null) {
              stack.push(nodeToBePushed);
            }

            stack.push(pi);
            ph = pi;  //Update the previous node
          }
        }

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
