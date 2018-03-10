/**
 * Doubly connected edge list representation of polygon
 */
import java.util.ArrayList;

public class DoublyConnectedEdgeList {

    private DCEL_Edge rep_edge; //representative edge
    private int id;  //assigned number to DCEL
    private int node_count; //number of nodes
    private static int edge_count = 0; //number of edges

    public DoublyConnectedEdgeList() {
        this.rep_edge = null;
        this.id = 0;
        this.node_count = 0;
    }

    public DoublyConnectedEdgeList(DCEL_Edge rep_edge, int id) {
      this.rep_edge = rep_edge;
      this.id = id;

      //find the node count
      DCEL_Edge temp = rep_edge;
      Node temp_node = temp.origin();
      int count = 1;
      temp = temp.next();
      while(temp.origin().id() != temp_node.id()) {
        count++;
        temp = temp.next();
      }

      this.incrementNodeCount(count);
    }

    public DoublyConnectedEdgeList(ArrayList<Integer> nodes, int face_id, int id) {
      if(nodes.size() >= 3) {
        Node one = new Node(nodes.get(0));
        Node two = new Node(nodes.get(1));
        Node three = new Node(nodes.get(2));
        DCEL_Face interior = new DCEL_Face(face_id);
        DCEL_Face exterior = new DCEL_Face(DCEL_Face.INF);

        DoublyConnectedEdgeList.incrementEdgeCount();
        DCEL_Edge hone1 = new DCEL_Edge(DoublyConnectedEdgeList.edge_count());
        DoublyConnectedEdgeList.incrementEdgeCount();
        DCEL_Edge hone2 = new DCEL_Edge(DoublyConnectedEdgeList.edge_count());
        DoublyConnectedEdgeList.incrementEdgeCount();
        DCEL_Edge htwo1 = new DCEL_Edge(DoublyConnectedEdgeList.edge_count());
        DoublyConnectedEdgeList.incrementEdgeCount();
        DCEL_Edge htwo2 = new DCEL_Edge(DoublyConnectedEdgeList.edge_count());
        DoublyConnectedEdgeList.incrementEdgeCount();
        DCEL_Edge hthree1 = new DCEL_Edge(DoublyConnectedEdgeList.edge_count());
        DoublyConnectedEdgeList.incrementEdgeCount();
        DCEL_Edge hthree2 = new DCEL_Edge(DoublyConnectedEdgeList.edge_count());

        hone1.setOrigin(one);
        hone1.setNext(htwo1);
        hone1.setPrev(hthree1);
        hone1.setTwin(hone2);
        hone1.setFace(interior);

        hone2.setOrigin(two);
        hone2.setNext(hthree2);
        hone2.setPrev(hthree1);
        hone2.setTwin(hone2);
        hone2.setFace(exterior);
        hone2.setCounterClock(false);

        htwo1.setOrigin(two);
        htwo1.setNext(hthree1);
        htwo1.setPrev(hone1);
        htwo1.setTwin(htwo2);
        htwo1.setFace(interior);

        htwo2.setOrigin(three);
        htwo2.setNext(hone2);
        htwo2.setPrev(hthree2);
        htwo2.setTwin(htwo1);
        htwo2.setFace(exterior);
        htwo2.setCounterClock(false);

        hthree1.setOrigin(three);
        hthree1.setNext(hone1);
        hthree1.setPrev(htwo1);
        hthree1.setTwin(hthree2);
        hthree1.setFace(interior);

        hthree2.setOrigin(one);
        hthree2.setNext(htwo2);
        hthree2.setPrev(hone2);
        hthree2.setTwin(hthree1);
        hthree2.setFace(exterior);
        hthree2.setCounterClock(false);

        this.setRepEdge(hthree1);
        this.setID(id);
        this.incrementNodeCount(3);

        for(int i = 3; i < nodes.size(); i++)
        {
          this.insert(new Node(nodes.get(i)), 2*i+1, 2*i+2);
        }
        return;
      }
      this.rep_edge = null;
      this.id = 0;
      this.node_count = 0;

    }
    public DCEL_Edge rep_edge() {
      return this.rep_edge;
    }

    public int id() {
      return this.id;
    }

    public int node_count() {
      return this.node_count;
    }

    public static int edge_count() {
      return edge_count;
    }
    public void setRepEdge(DCEL_Edge rep_edge) {
      this.rep_edge = rep_edge;
    }

    public void setID(int id) {
      this.id = id;
    }

    public void incrementNodeCount(int val) {
      this.node_count += val;
    }

    public static void incrementEdgeCount() {
      edge_count++;
    }
    public void insert(Node a, int edge_val_1, int edge_val_2) {

      assert this.node_count() >= 3;  //only works if the DCEL forms a closed polygon
      DCEL_Edge head = this.rep_edge();
      DCEL_Edge half_edge_1 = new DCEL_Edge(edge_val_1);
      DCEL_Edge half_edge_2 = new DCEL_Edge(edge_val_2);

      half_edge_1.setOrigin(a);
      half_edge_1.setPrev(head);
      half_edge_1.setNext(head.next());
      half_edge_1.setTwin(half_edge_2);
      half_edge_1.next().twin().setNext(half_edge_2);
      half_edge_1.setFace(head.face());
      half_edge_1.setCounterClock(half_edge_1.prev().isCounterClock());

      half_edge_2.setOrigin(head.next().origin());
      half_edge_2.setPrev(head.next().twin());
      half_edge_2.setNext(head.twin());
      half_edge_2.setTwin(half_edge_1);
      half_edge_2.setFace(head.twin().face());
      half_edge_2.setCounterClock(half_edge_2.prev().isCounterClock());

      head.setNext(half_edge_1);
      head.twin().setOrigin(a);
      head.twin().setPrev(half_edge_2);
      this.setRepEdge(half_edge_1);
      this.incrementNodeCount(1);
      DoublyConnectedEdgeList.incrementEdgeCount();
      DoublyConnectedEdgeList.incrementEdgeCount();

    }

    public void printInterior() {
      System.out.println("Printing interior cycle of dcel with id: "+Integer.toString(this.id));
      System.out.println("Face id: "+Integer.toString(this.rep_edge().face().id()));
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
    // public ArrayList<DoublyConnectedEdgeList> connect(Node a, Node b, double id1, double id2) {
    //   ArrayList<DoublyConnectedEdgeList> new_DCELs;
    // }

    //TODO: add code for nextInteriorEdge
    public class Node {
        private double x,y;
        private DCEL_Edge nextInteriorEdge;
        private int id;

        public Node() {
          this.id = 0;
        }

        public Node(int id) {
          this.id = id;
        }

        public double x() {
          return this.x;
        }

        public double y() {
          return this.y;
        }

        public DCEL_Edge nextInteriorEdge() {
          return this.nextInteriorEdge;
        }

        public int id() {
          return this.id;
        }

        

        public void setID(int id) {
          this.id = id;
        }

    }

    public class DCEL_Edge {

      private Node origin;
      private DCEL_Edge next;
      private DCEL_Edge prev;
      private DCEL_Edge twin;
      private DCEL_Face face;
      private int id;
      private boolean is_counter_clock;
      private Node helper;

      public DCEL_Edge() {
        this.origin = null;
        this.next = null;
        this.prev = null;
        this.twin = null;
        this.face = null;
        this.id = 0;
        this.is_counter_clock = true;
        this.helper = null;
      }

      public DCEL_Edge(int id) {
        this.id = id;
      }

      public DCEL_Edge(int id, Node origin) {
        this.id = id;
        this.origin = origin;
      }

      public DCEL_Edge(Node origin, DCEL_Edge next, DCEL_Edge prev, DCEL_Edge twin, DCEL_Face face, int id){
        this.origin = origin;
        this.next = next;
        this.prev = prev;
        this.twin = twin;
        this.face = face;
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

      public DCEL_Face face() {
        return this.face;
      }

      public int id() {
        return this.id;
      }

      public boolean isCounterClock() {
        return this.is_counter_clock;
      }
      public Node helper() {
        return this.helper;
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

      public void setFace(DCEL_Face face) {
        this.face = face;
      }

      public void setID(int id) {
        this.id = id;
      }

      public void setCounterClock(boolean is_counter_clock) {
        this.is_counter_clock = is_counter_clock;
      }

      public void setHelper(Node h) {
        this.helper = h;
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

    }

    public class DCEL_Face {

      private int id;
      public static final int INF = 1000000;

      public DCEL_Face() {
        this.id = 0;  //id of the face
      }

      public DCEL_Face(int id) {
        this.id = id;
      }

      public int id() {
        return this.id;
      }

      public void setID(int id) {
        this.id = id;
      }

    }

}
