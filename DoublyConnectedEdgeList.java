// GroupID-21 (14114053_14114071) - Sharanpreet Singh & Vaibhav Gosain
// Date: March 15, 2018
// DoublyConnectedEdgeList.java - This file contains the definition of DCEL
// data structure and its member variables, it also contains the member functions
// like edge insertion.

import java.util.*;
import javafx.util.Pair;

public class DoublyConnectedEdgeList {

    private DCEL_Edge rep_edge; //representative edge
    private int id;  //assigned number to DCEL
    private static int DCEL_count = 0;  //number of DCELs
    private static int edge_count = 0;  //number of edges
    private static int node_count = 0;  //number of nodes
    private static final double eps = 1e-4; //epsilon for floating point comparisons

    public DoublyConnectedEdgeList() {
        this.rep_edge = null;
        this.id = 0;
    }

    public DoublyConnectedEdgeList(DCEL_Edge rep_edge, int id) {
      this.rep_edge = rep_edge;
      this.id = id;
    }

    public DoublyConnectedEdgeList(DCEL_Edge rep_edge) {
      this.rep_edge = rep_edge;
      incrementDCELCount();
      this.id = DCEL_count();
    }

    public DoublyConnectedEdgeList(ArrayList<Vertex> nodes) {
      if(nodes.size() >= 3) {
        incrementDCELCount();
        this.id = DCEL_count();
        Node one = new Node(nodes.get(0));
        Node two = new Node(nodes.get(1));
        Node three = new Node(nodes.get(2));

        DCEL_Edge hone1 = new DCEL_Edge(DCEL_count());
        DCEL_Edge hone2 = new DCEL_Edge(-1);
        DCEL_Edge htwo1 = new DCEL_Edge(DCEL_count());
        DCEL_Edge htwo2 = new DCEL_Edge(-1);
        DCEL_Edge hthree1 = new DCEL_Edge(DCEL_count());
        DCEL_Edge hthree2 = new DCEL_Edge(-1);

        one.insertIncidentEdge(new Pair<Double, DCEL_Edge>(Math.atan2(two.y()-one.y(),two.x()-one.x()),hone1));
        two.insertIncidentEdge(new Pair<Double, DCEL_Edge>(Math.atan2(three.y()-two.y(),three.x()-two.x()),htwo1));
        three.insertIncidentEdge(new Pair<Double, DCEL_Edge>(Math.atan2(one.y()-three.y(),one.x()-three.x()),hthree1));

        hone1.setOrigin(one);
        hone1.setNext(htwo1);
        hone1.setPrev(hthree1);
        hone1.setTwin(hone2);

        hone2.setOrigin(two);
        hone2.setNext(hthree2);
        hone2.setPrev(hthree1);
        hone2.setTwin(hone2);
        hone2.setCounterClock(false);

        htwo1.setOrigin(two);
        htwo1.setNext(hthree1);
        htwo1.setPrev(hone1);
        htwo1.setTwin(htwo2);

        htwo2.setOrigin(three);
        htwo2.setNext(hone2);
        htwo2.setPrev(hthree2);
        htwo2.setTwin(htwo1);
        htwo2.setCounterClock(false);

        hthree1.setOrigin(three);
        hthree1.setNext(hone1);
        hthree1.setPrev(htwo1);
        hthree1.setTwin(hthree2);

        hthree2.setOrigin(one);
        hthree2.setNext(htwo2);
        hthree2.setPrev(hone2);
        hthree2.setTwin(hthree1);
        hthree2.setCounterClock(false);

        this.setRepEdge(hthree1);
        this.setID(id);

        for(int i = 3; i < nodes.size(); i++) {
          this.insert(new Node(nodes.get(i)));
        }

        return;
      }
      this.rep_edge = null;
      this.id = 0;

    }

    public DoublyConnectedEdgeList(ArrayList<Vertex> nodes, int id) {
      if(nodes.size() >= 3) {
        this.id = id;
        Node one = new Node(nodes.get(0));
        Node two = new Node(nodes.get(1));
        Node three = new Node(nodes.get(2));

        DCEL_Edge hone1 = new DCEL_Edge(id);
        DCEL_Edge hone2 = new DCEL_Edge(-1);
        DCEL_Edge htwo1 = new DCEL_Edge(id);
        DCEL_Edge htwo2 = new DCEL_Edge(-1);
        DCEL_Edge hthree1 = new DCEL_Edge(id);
        DCEL_Edge hthree2 = new DCEL_Edge(-1);

        one.insertIncidentEdge(new Pair<Double, DCEL_Edge>(Math.atan2(two.y()-one.y(),two.x()-one.x()),hone1));
        two.insertIncidentEdge(new Pair<Double, DCEL_Edge>(Math.atan2(three.y()-two.y(),three.x()-two.x()),htwo1));
        three.insertIncidentEdge(new Pair<Double, DCEL_Edge>(Math.atan2(one.y()-three.y(),one.x()-three.x()),hthree1));

        hone1.setOrigin(one);
        hone1.setNext(htwo1);
        hone1.setPrev(hthree1);
        hone1.setTwin(hone2);

        hone2.setOrigin(two);
        hone2.setNext(hthree2);
        hone2.setPrev(hthree1);
        hone2.setTwin(hone2);
        hone2.setCounterClock(false);

        htwo1.setOrigin(two);
        htwo1.setNext(hthree1);
        htwo1.setPrev(hone1);
        htwo1.setTwin(htwo2);

        htwo2.setOrigin(three);
        htwo2.setNext(hone2);
        htwo2.setPrev(hthree2);
        htwo2.setTwin(htwo1);
        htwo2.setCounterClock(false);

        hthree1.setOrigin(three);
        hthree1.setNext(hone1);
        hthree1.setPrev(htwo1);
        hthree1.setTwin(hthree2);

        hthree2.setOrigin(one);
        hthree2.setNext(htwo2);
        hthree2.setPrev(hone2);
        hthree2.setTwin(hthree1);
        hthree2.setCounterClock(false);

        this.setRepEdge(hthree1);
        this.setID(id);

        for(int i = 3; i < nodes.size(); i++) {
          this.insert(new Node(nodes.get(i)));
        }

        return;
      }
      this.rep_edge = null;
      this.id = 0;

    }

    public DCEL_Edge rep_edge() {
      return this.rep_edge;
    }

    public int id() {
      return this.id;
    }

    public static int DCEL_count() {
      return DCEL_count;
    }

    public static int edge_count() {
      return edge_count;
    }

    public static int node_count() {
      return node_count;
    }

    public void setRepEdge(DCEL_Edge rep_edge) {
      this.rep_edge = rep_edge;
    }

    public void setID(int id) {
      this.id = id;
    }

    public static void incrementDCELCount() {
      DCEL_count++;
    }

    public static void incrementEdgeCount() {
      edge_count++;
    }

    public static void incrementNodeCount() {
      node_count++;
    }

    public void insert(Node a) {

      DCEL_Edge head = this.rep_edge();
      DCEL_Edge half_edge_1 = new DCEL_Edge(this.id);
      DCEL_Edge half_edge_2 = new DCEL_Edge(-1);

      Node b = head.origin(), c = head.next().origin();

      b.removeIncidentEdge(new Pair<Double, DCEL_Edge>(Math.atan2(c.y()-b.y(),c.x()-b.x()),head));

      half_edge_1.setOrigin(a);
      half_edge_1.setPrev(head);
      half_edge_1.setNext(head.next());
      half_edge_1.setTwin(half_edge_2);
      half_edge_1.setCounterClock(half_edge_1.prev().isCounterClock());

      half_edge_2.setOrigin(head.next().origin());
      half_edge_2.setPrev(head.next().twin());
      half_edge_2.setNext(head.twin());
      half_edge_2.setTwin(half_edge_1);
      half_edge_2.setCounterClock(half_edge_2.prev().isCounterClock());

      head.next().setPrev(half_edge_1);
      head.setNext(half_edge_1);

      head.twin().prev().setNext(half_edge_2);
      head.twin().setPrev(half_edge_2);

      head.setNext(half_edge_1);
      head.twin().setOrigin(a);
      head.twin().setPrev(half_edge_2);

      b.insertIncidentEdge(new Pair<Double, DCEL_Edge>(Math.atan2(a.y()-b.y(),a.x()-b.x()),head));
      a.insertIncidentEdge(new Pair<Double, DCEL_Edge>(Math.atan2(c.y()-a.y(),c.x()-a.x()),half_edge_1));
      this.setRepEdge(half_edge_1);

    }

    public void printInterior() {
      System.out.println("Printing interior cycle of dcel with id: "+Integer.toString(this.id));
      DCEL_Edge temp = this.rep_edge();
      DCEL_Edge temp2 = temp;
      System.out.print("Edge Cycle is: "+Integer.toString(temp2.origin.id())+"--("+Integer.toString(temp2.id())+")-->");
      temp2 = temp2.next();
      while(temp2.id() != temp.id()) {
        System.out.print(Integer.toString(temp2.origin.id())+"--("+Integer.toString(temp2.id)+")-->");
        temp2 = temp2.next();
      }
      System.out.println(Integer.toString(temp.origin.id()));
    }

    public void printVertices() {
      System.out.println("Printing vertices of dcel with id: "+Integer.toString(this.id));
      DCEL_Edge temp = this.rep_edge();
      DCEL_Edge temp2 = temp;
      System.out.print("Vertices are: ("+Double.toString(temp2.origin().x())+","+Double.toString(temp2.origin().y())+")-->");
      temp2 = temp2.next();
      while(temp2.id() != temp.id()) {
        System.out.print("("+Double.toString(temp2.origin().x())+","+Double.toString(temp2.origin().y())+")-->");
        temp2 = temp2.next();
      }
      System.out.println("End");
    }

    public DoublyConnectedEdgeList connect(Node a, Node b, DCEL_Edge e_prev, DCEL_Edge e_next) {
      DCEL_Edge e_a_b = new DCEL_Edge(this.id());
      DCEL_Edge e_b_a = new DCEL_Edge(this.id());

      e_a_b.setOrigin(a);
      e_b_a.setOrigin(b);

      e_a_b.setNext(b.IncidentEdge(Math.atan2(a.y()-b.y(),a.x()-b.x())));
      e_a_b.setPrev(e_prev);

      e_b_a.setNext(e_next);
      e_b_a.setPrev(e_a_b.next().prev());

      e_a_b.setTwin(e_b_a);
      e_b_a.setTwin(e_a_b);

      e_a_b.setCounterClock(true);
      e_b_a.setCounterClock(true);

      e_a_b.next().setPrev(e_a_b);
      e_a_b.prev().setNext(e_a_b);

      e_b_a.next().setPrev(e_b_a);
      e_b_a.prev().setNext(e_b_a);

      this.setRepEdge(e_a_b);
      DoublyConnectedEdgeList newDCEL = new DoublyConnectedEdgeList(e_b_a);

      e_b_a.setDCELID(newDCEL.id());
      DCEL_Edge cur = e_b_a.next();

      while(cur != e_b_a)
      {
        cur.setDCELID(newDCEL.id());
        cur = cur.next();
      }

      a.insertIncidentEdge(new Pair<Double, DCEL_Edge>(Math.atan2(b.y()-a.y(),b.x()-a.x()),e_a_b));
      b.insertIncidentEdge(new Pair<Double, DCEL_Edge>(Math.atan2(a.y()-b.y(),a.x()-b.x()),e_b_a));

      return newDCEL;
    }

    //TODO: add code for IncidentEdges
    public static class Node {
        private double x,y;
        private TreeSet<Pair<Double,DCEL_Edge> > IncidentEdges;
        private int id;

        public Node() {
          DoublyConnectedEdgeList.incrementNodeCount();
          this.id = DoublyConnectedEdgeList.node_count();
          this.IncidentEdges = new TreeSet<Pair<Double,DCEL_Edge> >(new IncidentEdgeComparator());
        }

        public Node(double x, double y) {
          DoublyConnectedEdgeList.incrementNodeCount();
          this.id = DoublyConnectedEdgeList.node_count();
          this.x = x;
          this.y = y;
          this.IncidentEdges = new TreeSet<Pair<Double,DCEL_Edge> >(new IncidentEdgeComparator());
        }

        public Node(int id) {
          this.id = id;
          this.IncidentEdges = new TreeSet<Pair<Double,DCEL_Edge> >(new IncidentEdgeComparator());
        }

        public Node(Vertex v) {
          this.id = v.index();
          this.x = v.x();
          this.y = v.y();
          this.IncidentEdges = new TreeSet<Pair<Double,DCEL_Edge> >(new IncidentEdgeComparator());
        }

        public double x() {
          return this.x;
        }

        public double y() {
          return this.y;
        }

        public DCEL_Edge IncidentEdge() {
          return this.IncidentEdge(3*Math.PI);
        }

        public DCEL_Edge IncidentEdge(Double d) {
          Pair<Double,DCEL_Edge> p = this.IncidentEdges.lower(new Pair<Double,DCEL_Edge>(d,new DCEL_Edge()));
          if(p == null && d <= 2*Math.PI)
            return this.IncidentEdge();
          if(p == null)
            return null;
          return p.getValue();
        }

        public int id() {
          return this.id;
        }

        public void setID(int id) {
          this.id = id;
        }

        public void insertIncidentEdge(Pair<Double,DCEL_Edge> p)
        {
          this.IncidentEdges.add(p);
        }

        public void removeIncidentEdge(Pair<Double,DCEL_Edge> p)
        {
          this.IncidentEdges.remove(p);
        }

        public class IncidentEdgeComparator implements Comparator<Pair<Double,DCEL_Edge> > {

          @Override
          public int compare(Pair<Double,DCEL_Edge> p1, Pair<Double,DCEL_Edge> p2) {
            if(Math.abs(p1.getKey() - p2.getKey()) < eps) return 0;
            if(p1.getKey() < p2.getKey()) return -1;
            return 1;
          }
        }

    }

    //TODO: check static
    public static class DCEL_Edge {

      private Node origin;
      private DCEL_Edge next;
      private DCEL_Edge prev;
      private DCEL_Edge twin;
      private int id, DCEL_id;
      private boolean is_counter_clock;

      public DCEL_Edge() {
        this.origin = null;
        this.next = null;
        this.prev = null;
        this.twin = null;
        DoublyConnectedEdgeList.incrementEdgeCount();
        this.id = DoublyConnectedEdgeList.edge_count();
        this.DCEL_id = 0;
        this.is_counter_clock = true;
      }

      public DCEL_Edge(int DCEL_id) {
        DoublyConnectedEdgeList.incrementEdgeCount();
        this.id = DoublyConnectedEdgeList.edge_count();
        this.DCEL_id = DCEL_id;
      }

      public DCEL_Edge(int id, Node origin) {
        this.id = id;
        this.origin = origin;
      }

      public DCEL_Edge(Node origin, DCEL_Edge next, DCEL_Edge prev, DCEL_Edge twin, int id){
        this.origin = origin;
        this.next = next;
        this.prev = prev;
        this.twin = twin;
        this.id = id;
        this.is_counter_clock = true;
      }

      public Node origin() {
        return this.origin;
      }

      public DCEL_Edge next() {
        return this.next;
      }

      public DCEL_Edge prev() {
        return this.prev;
      }

      public DCEL_Edge twin() {
        return this.twin;
      }

      public int id() {
        return this.id;
      }
      public int DCEL_id() {
        return this.DCEL_id;
      }

      public boolean isCounterClock() {
        return this.is_counter_clock;
      }

      public void setOrigin(Node origin) {
        this.origin = origin;
      }

      public void setNext(DCEL_Edge next) {
        this.next = next;
      }

      public void setPrev(DCEL_Edge prev) {
        this.prev = prev;
      }

      public void setTwin(DCEL_Edge twin) {
        this.twin = twin;
      }

      public void setID(int id) {
        this.id = id;
      }
      public void setDCELID(int DCEL_id)
      {
        this.DCEL_id = DCEL_id;
      }
      public void setCounterClock(boolean is_counter_clock) {
        this.is_counter_clock = is_counter_clock;
      }

      public Node getNode(boolean isTop)
      {
        if(this.origin().y() > this.next().origin().y())
        {
          if(isTop) return this.origin();
          return this.next().origin();
        }
        if(isTop) return this.next().origin();
        return this.origin();
      }

      public boolean isEqualto(DCEL_Edge b) {
        boolean flag1 = ((Math.abs(this.origin().x()-b.origin().x()) <= eps) && (Math.abs(this.origin().y()-b.origin().y()) <= eps));
        boolean flag2 = ((Math.abs(this.next().origin().x()-b.next().origin().x()) <= eps) && (Math.abs(this.next().origin().y()-b.next().origin().y()) <= eps));
        boolean flag3 = ((Math.abs(this.origin().x()-b.next().origin().x()) <= eps) && (Math.abs(this.origin().y()-b.next().origin().y()) <= eps));
        boolean flag4 = ((Math.abs(this.next().origin().x()-b.origin().x()) <= eps) && (Math.abs(this.next().origin().y()-b.origin().y()) <= eps));

        return (flag1 && flag2) || (flag3 && flag4);
      }
    }

    public static class Triangle {
      
      //nodes are in counterclockwise order
      private int id;
      private Vertex first;
      private Vertex second;
      private Vertex third;

      public Triangle() {
        this.id = 0;
        this.first = null;
        this.second = null;
        this.third = null;
      }

      public Triangle(Vertex first, Vertex second, Vertex third, int id) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.id = id;
      }

      public Triangle(Node a, Node b, Node c, int id) {
        this.first = new Vertex(a);
        this.second = new Vertex(b);
        this.third = new Vertex(c);
        this.id = id;
      }

      public Vertex first() {
        return this.first;
      }

      public Vertex second() {
        return this.second;
      }

      public Vertex thrid() {
        return this.third;
      }

      public int id() {
        return this.id;
      }

      public void setFirst(Vertex first) {
        this.first = first;
      }

      public void setSecond(Vertex second) {
        this.second = second;
      }

      public void setThird(Vertex third) {
        this.third = third;
      }

      public void setID(int id) {
        this.id = id;
      }

      public DoublyConnectedEdgeList convertToDCEL() {
        ArrayList<Vertex> nodes = new ArrayList<Vertex>();
        nodes.add(first);
        nodes.add(second);
        nodes.add(third);
        return new DoublyConnectedEdgeList(nodes,this.id());
      }

      public void printTriangle() {
        System.out.printf("The triangle formed is %s, %s, %s\n",first.toString(), second.toString(), third.toString());
      }
    }

}
