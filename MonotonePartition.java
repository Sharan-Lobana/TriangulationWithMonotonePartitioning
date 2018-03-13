import javafx.util.Pair;
import java.util.ArrayList;

public class MonotonePartition {
	private TreeMap<Integer,DoublyConnectedEdgeList> Partition;
	private ArrayList<Edge> Trapezoidalization;

	public MonotonePartition() {
		this.Partition = new ArrayList<DoublyConnectedEdgeList>();
		this.Trapezoidalization = new ArrayList<Edge>();
	}

	public MonotonePartition(DoublyConnectedEdgeList polygon)
	{
		//Default PQ capacity is 11
		PriorityQueue<DoublyConnectedEdgeList.Node> Q = new PriorityQueue<DoublyConnectedEdgeList.Node>(11, new MonotoneVertexComparator());

		DCEL_Edge cur_edge = polygon.rep_edge();
		do
		{
			Q.add(cur_edge.origin());
			cur_edge = cur_edge.next();
		}while(cur_edge.id() != polygon.rep_edge().id());
		
		// Initializing the binary tree
		this.Partition = new TreeMap<Integer,DoublyConnectedEdgeList>();
		Partition.put(polygon.id(),polygon);
		this.Trapezoidalization = new ArrayList<Edge>();
		
		TreeSet<DCEL_Edge> T = new TreeSet<DCEL_Edge>(new MonotoneEdgeComparator());

		TreeMap<Integer, Pair<DoublyConnectedEdgeList.Node,VertexType> > Helper = new TreeMap<Integer, Pair<DoublyConnectedEdgeList.Node,VertexType> >();

		// Partitioning as set of diagonals
		// ArrayList<Diagonal> D = new ArrayList<Diagonal>();
		
		// Handling vertices
		while (! Q.isEmpty()) {
			try {
				handleVertex(Q.poll(), T, Helper);
			} catch(Exception e) {
				System.out.print("!");
			}
		}
	}
	
	/*private static ArrayList<Triangle> triangulateMonotonePolygon(Polygon p) {
		
	}*/
	
	private static void handleVertex(DoublyConnectedEdgeList.Node v_i, TreeSet<DCEL_Edge> T, TreeMap<Integer, DoublyConnectedEdgeList.Node> Helper) {
		
		// DoublyConnectedEdgeList.Node prev_node = v_i.IncidentEdge().prev().origin();

		// Edge e_i, e_j, e_i_1, temp;
		// DoublyConnectedEdgeList.Node helper_e_i_1, helper_e_j;
		
		// switch (v_i.getVertexType()) {
			
		// 	case VertexType.START:
		// 		e_i = new Edge(v_i.getPolygon(), i);
		// 		e_i.setHelper(v_i);
		// 		T.put(i, e_i);
				
		// 		break;
				
		// 	case VertexType.END:
				
		// 		e_i_1 = T.get(i_1);
				
		// 		helper_e_i_1 = e_i_1.getHelper();
		// 		if (helper_e_i_1.getVertexType() == VertexType.MERGE) {
		// 			D.add(new Diagonal(i, helper_e_i_1.getIndex()));
		// 		}
				
		// 		//T.remove(i_1);
				
		// 		break;
				
		// 	case VertexType.SPLIT:
		// 		e_j = null;
		// 		// TODO: Improve to O(log(n))!
		// 		for (int j : T.keySet()) {
		// 			temp = T.get(j);
		// 			if (temp.intersectsWithSweepLine(v_i.getY()) &&
		// 					temp.liesOnTheLeftSideof(v_i)) {
		// 				if (e_j == null) {
		// 					e_j = temp;
		// 				} else if (temp.liesOnTheRightSideof(e_j, v_i.getY())) {
		// 					e_j = temp;
		// 				}
		// 			}
		// 		}
				
		// 		D.add(new Diagonal(i, e_j.getHelper().getIndex()));
				
		// 		e_j.setHelper(v_i);
				
		// 		e_i = new Edge(v_i.getPolygon(), i);
		// 		T.put(i, e_i);
		// 		e_i.setHelper(v_i);
				
		// 		break;
				
		// 	case VertexType.MERGE:
				
		// 		e_i_1 = T.get(i_1);
		// 		helper_e_i_1 = e_i_1.getHelper();
				
		// 		if (helper_e_i_1.getVertexType() == VertexType.MERGE) {
		// 			D.add(new Diagonal(i, helper_e_i_1.getIndex()));
		// 		}
				
		// 		//T.remove(i_1);
				
		// 		e_j = null;
		// 		// TODO: Improve to O(log(n))!
		// 		for (int j : T.keySet()) {
		// 			temp = T.get(j);
		// 			if (temp.intersectsWithSweepLine(v_i.getY()) &&
		// 					temp.liesOnTheLeftSideof(v_i)) {
		// 				if (e_j == null) {
		// 					e_j = temp;
		// 				} else if (temp.liesOnTheRightSideof(e_j, v_i.getY())) {
		// 					e_j = temp;
		// 				}
		// 			}
		// 		}
				
		// 		helper_e_j = e_j.getHelper();
				
		// 		if (helper_e_j.getVertexType() == VertexType.MERGE) {
		// 			D.add(new Diagonal(i, helper_e_j.getIndex()));
		// 		}
				
		// 		e_j.setHelper(v_i);				
				
		// 		break;
				
		// 	case VertexType.REGULAR:
				
		// 		if (v_i.polygonInteriorLiesToTheRight()) {
					
		// 			e_i_1 = T.get(i_1);
		// 			helper_e_i_1 = e_i_1.getHelper();

		// 			if (helper_e_i_1.getVertexType() == VertexType.MERGE) {
		// 				D.add(new Diagonal(i, helper_e_i_1.getIndex()));
		// 			}
					
		// 			//T.remove(i_1);
					
		// 			e_i = new Edge(v_i.getPolygon(), i);
		// 			T.put(i, e_i);
		// 			e_i.setHelper(v_i);
					
		// 		} else {
					
		// 			e_j = null;
		// 			// TODO: Improve to O(log(n))!
		// 			for (int j : T.keySet()) {
		// 				temp = T.get(j);
		// 				if (temp.intersectsWithSweepLine(v_i.getY()) &&
		// 					temp.liesOnTheLeftSideof(v_i)) {
		// 					if (e_j == null) {
		// 						e_j = temp;
		// 					} else if (temp.liesOnTheRightSideof(e_j, v_i.getY())) {
		// 						e_j = temp;
		// 					}
		// 				}
		// 			}
					
		// 			helper_e_j = e_j.getHelper();
				
		// 			if (helper_e_j.getVertexType() == VertexType.MERGE) {
		// 				D.add(new Diagonal(i, helper_e_j.getIndex()));
		// 			}

		// 			e_j.setHelper(v_i); 
		// 		}
				
		// 		break;
		// }

		DCEL_Edge e_prev = v_i.IncidentEdge().prev();
		DCEL_Edge e_next = v_i.IncidentEdge();

		switch (getVertexType(v_i)) {
			case VertexType.START:
				DCEL_Edge e_i = v_i.IncidentEdge();
				DCEL_Edge e_i_1 = v_i.IncidentEdge().prev();
				
				T.add(e_i);
				T.add(e_i_1);

				Helper.put(e_i.id(),new Pair<DoublyConnectedEdgeList.Node,VertexType>(v_i,START));

				break;
			
			case VertexType.END:
				DCEL_Edge e_i = v_i.IncidentEdge();
				DCEL_Edge e_i_1 = v_i.IncidentEdge().prev();
				if(Helper.get(e_i_1.id()).getValue() == VertexType.MERGE)
					Partition.put(Partition.get(e_i_1.DCEL_id()).connect(v_i,Helper.get(e_i_1.id()).getKey(),e_i_1,e_i_1.next()));
				T.remove(e_i);
				T.remove(e_i_1);
				
				break;

			case VertexType.SPLIT:
				DCEL_Edge e_j = T_Query(T,v_i,false);
				DCEL_Edge e_j_1 = T_Query(T,v_i,true);

				double x1 = xQuery(e_j.getNode(true),e_j.getNode(false),v_i);
				double x2 = xQuery(e_j_1.getNode(true),e_j_1.getNode(false),v_i);
				Trapezoidalization.add(new Edge(new Vertex(x1,v_i.y()),new Vertex(x2,v_i.y())));

				Partition.put(Partition.get(e_j.DCEL_id()).connect(v_i,Helper.get(e_j.id()).getKey(),e_j,e_j.next()));
				Helper.put(e_j.id(),new Pair<DoublyConnectedEdgeList.Node,VertexType>(v_i,SPLIT));
				
				DCEL_Edge e_i = v_i.IncidentEdge();
				DCEL_Edge e_i_1 = v_i.IncidentEdge().prev();
				
				T.add(e_i);
				T.add(e_i_1);
				
				Helper.put(e_i.id(),new Pair<DoublyConnectedEdgeList.Node,VertexType>(v_i,SPLIT));
				break;

			case VertexType.MERGE:
				DCEL_Edge e_i = v_i.IncidentEdge();
				DCEL_Edge e_i_1 = v_i.IncidentEdge().prev();
				
				if(Helper.get(e_i_1.id()).getValue() == VertexType.MERGE)
					Partition.put(Partition.get(e_i_1.DCEL_id()).connect(v_i,Helper.get(e_i_1.id()).getKey(),e_i_1,e_i_1.next()));
				T.remove(e_i);
				T.remove(e_i_1);

				DCEL_Edge e_j = T_Query(T,v_i,false);
				DCEL_Edge e_j_1 = T_Query(T,v_i,true);

				double x1 = xQuery(e_j.getNode(true),e_j.getNode(false),v_i);
				double x2 = xQuery(e_j_1.getNode(true),e_j_1.getNode(false),v_i);
				Trapezoidalization.add(new Edge(new Vertex(x1,v_i.y()),new Vertex(x2,v_i.y())));

				if(Helper.get(e_j.id()).getValue() == VertexType.MERGE)
					Partition.put(Partition.get(e_j.DCEL_id()).connect(v_i,Helper.get(e_j.id()).getKey(),e_j,e_j.next()));
				Helper.put(e_j.id(),new Pair<DoublyConnectedEdgeList.Node,VertexType>(v_i,MERGE));
				break;
			
			case VertexType.REGULAR:
				DCEL_Edge e_i = v_i.IncidentEdge();
				if(Math.atan2(e_i.next().origin().y()-e_i.origin().y(),e_i.next().origin().x()-e_i.origin().x()) < 0)
				{
					DCEL_Edge e_i_1 = v_i.IncidentEdge().prev();
					if(Helper.get(e_i_1.id()).getValue() == VertexType.MERGE)
						Partition.put(Partition.get(e_i_1.DCEL_id()).connect(v_i,Helper.get(e_i_1.id()).getKey(),e_i_1,e_i_1.next()));
					T.remove(e_i_1);

					DCEL_Edge e_j_1 = T_Query(T,v_i,true);
					double x2 = xQuery(e_j_1.getNode(true),e_j_1.getNode(false),v_i);
					Trapezoidalization.add(new Edge(new Vertex(v_i.x(),v_i.y()),new Vertex(x2,v_i.y())));

					DCEL_Edge e_i = v_i.IncidentEdge();
					T.add(e_i);
					Helper.put(e_i.id(),new Pair<DoublyConnectedEdgeList.Node,VertexType>(v_i,REGULAR));
				}
				else
				{
					T.remove(e_i);

					DCEL_Edge e_j = T_Query(T,v_i,false);
					double x1 = xQuery(e_j.getNode(true),e_j.getNode(false),v_i);
					Trapezoidalization.add(new Edge(new Vertex(x1,v_i.y()),new Vertex(v_i.x(),v_i.y())));

					DCEL_Edge e_j = T_Query(T,v_i,false);
					if(Helper.get(e_j.id()).getValue() == VertexType.MERGE)
						Partition.put(Partition.get(e_j.DCEL_id()).connect(v_i,Helper.get(e_j.id()).getKey(),e_j,e_j.next()));
					Helper.put(e_j.id(),new Pair<DoublyConnectedEdgeList.Node,VertexType>(v_i,REGULAR));			

					DCEL_Edge e_i = v_i.IncidentEdge();
					DCEL_Edge e_i_1 = v_i.IncidentEdge().prev();
					T.add(e_i_1);
				}
				break;
			}
		}

	static private enum VertexType {
		START,
		END,
		REGULAR,
		SPLIT,
		MERGE
	}

	private VertexType getVertexType(DoublyConnectedEdgeList.Node cur) {
		
		Point prev = cur.IncidentEdge().prev().origin();
		Point next = cur.IncidentEdge().next().origin();

		if (prev.y() < cur.y() &&
			 next.y() < cur.y()) {
			if (isConvex(cur)) {
				return VertexType.START;
			} else {
				return VertexType.SPLIT;
			}
		} else if (prev.y() > cur.y() &&
					next.y() > cur.y()) {
			if (isConvex(cur)) {
				return VertexType.END;
			} else {
				return VertexType.MERGE;
			}
		} else {
			return VertexType.REGULAR;
		}
	}

	private boolean isConvex(DoublyConnectedEdgeList.Node b)
	{
		DoublyConnectedEdgeList.Node a = b.IncidentEdge().prev().origin();
		DoublyConnectedEdgeList.Node c = b.IncidentEdge().next().origin();
		double th1 = Math.atan2(b.y()-a.y(),b.x()-a.x());
		double th2 = Math.atan2(c.y()-b.y(),c.x()-b.x());
		double diff = th2 - th1;
		if(diff > Math.PI)
			diff -= 2*Math.PI;
		if(diff < -Math.PI)
			diff += 2*Math.PI;

		if(diff > 0)	return true;
		return false;
	}

	private DCEL_Edge T_Query(TreeSet<DCEL_Edge> T, DoublyConnectedEdgeList.Node v, boolean direction)
	{
		DCEL_Edge e = new DCEL_Edge();
		e.setOrigin(v);
		e.setNext(e);
		if(direction)
			return T.higher(e);
		return T.lower(e);
	}

	private double xQuery(DoublyConnectedEdgeList.Node a, DoublyConnectedEdgeList.Node b, DoublyConnectedEdgeList.Node c)
	{
		return a.x() + (b.x()-a.x())*(a.y()-c.y())/(a.y()-b.y());
	}

	// static class Node {
		
	// 	public Node(Polygon p, int i) {
	// 		polygon = p;
	// 		index = i;
	// 	}
		
	// 	public int getIndex() {
	// 		return index;
	// 	}
		
	// 	public double getX() {
	// 		return polygon.get(index).getX();
	// 	}
		
	// 	public double getY() {
	// 		return polygon.get(index).getY();
	// 	}
		
	// 	public Polygon getPolygon() {
	// 		return polygon;
	// 	}
		
	// 	public VertexType getVertexType() {
			
	// 		Point pCur = polygon.get(index);
	// 		Point pPrev = polygon.get(index == 0 ? polygon.size()-1 : index-1);
	// 		Point pNext = polygon.get((index+1) % polygon.size());
			
	// 		if (pPrev.getY() < pCur.getY() &&
	// 			 pNext.getY() < pCur.getY()) {
	// 			if (polygon.isConvex(index)) {
	// 				return VertexType.START;
	// 			} else {
	// 				return VertexType.SPLIT;
	// 			}
	// 		} else if (pPrev.getY() > pCur.getY() &&
	// 					pNext.getY() > pCur.getY()) {
	// 			if (polygon.isConvex(index)) {
	// 				return VertexType.END;
	// 			} else {
	// 				return VertexType.MERGE;
	// 			}
	// 		} else {
	// 			return VertexType.REGULAR;
	// 		}
	// 	}
		
	// 	public boolean polygonInteriorLiesToTheRight() {
			
	// 		Point pCur = polygon.get(index);
	// 		Point pPrev = polygon.get(index == 0 ? polygon.size()-1 : index-1);
	// 		Point pNext = polygon.get((index+1) % polygon.size());
			
	// 		if (pPrev.getY() > pCur.getY() &&
	// 			 pNext.getY() < pCur.getY()) {
	// 			return true;
	// 		} else {
	// 			return false;
	// 		}
	// 	}
		
	// 	private Polygon polygon;
	// 	private int index;
	// }
	
	// static class Edge {
		
	// 	public Edge(Polygon p, int i) {
	// 		polygon = p;
	// 		index = i;
	// 	}
		
	// 	public void setHelper(Node v) {
	// 		helper = v;
	// 	}
		
	// 	public Node getHelper() {
	// 		return helper;
	// 	}
		
	// 	public int getIndex() {
	// 		return index;
	// 	}
		
	// 	public Node getA() {
	// 		return new Node(polygon, index);
	// 	}
		
	// 	public Node getB() {
	// 		return new Node(polygon, (index+1)%polygon.size());
	// 	}
		
	// 	public boolean intersectsWithSweepLine(double sweepY) {
	// 		return (sweepY >= getA().getY() && sweepY <= getB().getY()) ||
	// 				(sweepY >= getB().getY() && sweepY <= getA().getY());
	// 	}
		
	// 	public Line getLine() {
	// 		return new Line(polygon.get(index),
	// 				polygon.get((index+1)%polygon.size()));
	// 	}
		
	// 	public boolean liesOnTheRightSideof(Edge e, double Y) {
	// 		return this.getLine().XforY(Y) > e.getLine().XforY(Y);
	// 	}
		
	// 	public boolean liesOnTheLeftSideof(Node v) {
	// 		return this.getLine().XforY(v.getY()) < v.getX();
	// 	}
		
	// 	private Polygon polygon;		
	// 	private Node helper;
	// 	private int index;
	// }

	static class Diagonal {
		
		public Diagonal(int i1, int i2) {
			index1 = i1;
			index2 = i2;
		}
		
		public int getA() {
			return index1;
		}
		
		public int getB() {
			return index2;
		}
		
		int index1, index2;
	}
	static class MonotoneVertexComparator implements Comparator<DoublyConnectedEdgeList.Node> {

		public int compare(DoublyConnectedEdgeList.Node v1, DoublyConnectedEdgeList.Node v2) {
			if (v1.y() > v2.y() || v1.y() == v2.y() && v1.x() > v2.x())
				return -1;
			else 
				return 1;
		}
	}
	static class MonotoneEdgeComparator implements Comparator<DCEL_Edge> {

		public int compare(DCEL_Edge e1, DCEL_Edge e2) {
			DoublyConnectedEdgeList.Node top1 = e1.getNode(true), bot1 = e1.getNode(false);
			DoublyConnectedEdgeList.Node top2 = e2.getNode(true), bot2 = e2.getNode(false);
			int mul = 1;
			if(top1.y()>top2.y())
			{
				mul = -1;

				DoublyConnectedEdgeList.Node t = top1;
				top1 = top2;
				top2 = t;

				t = bot1;
				bot1 = bot2;
				bot2 = t;
			}

			double midx = top2.x() + (bot2.x()-top2.x())*(top2.y()-top1.y())/(top2.y()-bot2.y());

			if(Math.abs(midx - top1.x()) < 0.00001)
			{
				if(bot1.x() > bot2.x())	return -1*mul;
				return mul;
			}

			if (midx > top1.x())
				return -1*mul;
			else 
				return mul;
		}
	}
}