import javafx.util.Pair;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.PriorityQueue;
import java.util.Comparator;

public class MonotonePartition {
	private TreeMap<Integer,DoublyConnectedEdgeList> Partition;
	private ArrayList<Edge> Trapezoidalization;

	public MonotonePartition() {
		this.Partition = new TreeMap<Integer,DoublyConnectedEdgeList>();
		this.Trapezoidalization = new ArrayList<Edge>();
	}

	public MonotonePartition(DoublyConnectedEdgeList polygon)
	{
		//Default PQ capacity is 11
		PriorityQueue<DoublyConnectedEdgeList.Node> Q = new PriorityQueue<DoublyConnectedEdgeList.Node>(11, new MonotoneVertexComparator());

		DoublyConnectedEdgeList.DCEL_Edge cur_edge = polygon.rep_edge();
		do
		{
			Q.add(cur_edge.origin());
			cur_edge = cur_edge.next();
		}while(cur_edge.id() != polygon.rep_edge().id());
		
		// Initializing the binary tree
		this.Partition = new TreeMap<Integer,DoublyConnectedEdgeList>();
		Partition.put(polygon.id(),polygon);
		this.Trapezoidalization = new ArrayList<Edge>();
		
		TreeSet<DoublyConnectedEdgeList.DCEL_Edge> T = new TreeSet<DoublyConnectedEdgeList.DCEL_Edge>(new MonotoneEdgeComparator());

		TreeMap<Integer, Pair<DoublyConnectedEdgeList.Node,VertexType> > Helper = new TreeMap<Integer, Pair<DoublyConnectedEdgeList.Node,VertexType> >();

		while (! Q.isEmpty()) {
			try {
				handleVertex(Q.poll(), T, Helper);
			} catch(Exception e) {
				System.out.print("!");
			}
		}
	}
	
	private void handleVertex(DoublyConnectedEdgeList.Node v_i, TreeSet<DoublyConnectedEdgeList.DCEL_Edge> T, TreeMap<Integer, Pair<DoublyConnectedEdgeList.Node,VertexType> > Helper) {
		

		DoublyConnectedEdgeList.DCEL_Edge e_prev = v_i.IncidentEdge().prev();
		DoublyConnectedEdgeList.DCEL_Edge e_next = v_i.IncidentEdge();

		DoublyConnectedEdgeList.DCEL_Edge e_i, e_i_1, e_j, e_j_1;
		double x1,x2;
		DoublyConnectedEdgeList newDCEL;
		switch (getVertexType(v_i)) {
			case START:
				e_i = v_i.IncidentEdge();
				e_i_1 = v_i.IncidentEdge().prev();
				
				T.add(e_i);
				T.add(e_i_1);

				Helper.put(e_i.id(),new Pair<DoublyConnectedEdgeList.Node,VertexType>(v_i,VertexType.START));

				break;
			
			case END:
				e_i = v_i.IncidentEdge();
				e_i_1 = v_i.IncidentEdge().prev();
				if(Helper.get(e_i_1.id()).getValue() == VertexType.MERGE)
				{
					newDCEL = Partition.get(e_i_1.DCEL_id()).connect(v_i,Helper.get(e_i_1.id()).getKey(),e_i_1,e_i_1.next());
					Partition.put(newDCEL.id(), newDCEL);
				}
				T.remove(e_i);
				T.remove(e_i_1);
				
				break;

			case SPLIT:
				e_j = T_Query(T,v_i,false);
				e_j_1 = T_Query(T,v_i,true);

				x1 = xQuery(e_j.getNode(true),e_j.getNode(false),v_i);
				x2 = xQuery(e_j_1.getNode(true),e_j_1.getNode(false),v_i);
				Trapezoidalization.add(new Edge(new Vertex(x1,v_i.y()),new Vertex(x2,v_i.y())));

				newDCEL = Partition.get(e_j.DCEL_id()).connect(v_i,Helper.get(e_j.id()).getKey(),e_j,e_j.next());
				Partition.put(newDCEL.id(), newDCEL);
				Helper.put(e_j.id(),new Pair<DoublyConnectedEdgeList.Node,VertexType>(v_i,VertexType.SPLIT));
				
				e_i = v_i.IncidentEdge();
				e_i_1 = v_i.IncidentEdge().prev();
				
				T.add(e_i);
				T.add(e_i_1);
				
				Helper.put(e_i.id(),new Pair<DoublyConnectedEdgeList.Node,VertexType>(v_i,VertexType.SPLIT));
				break;

			case MERGE:
				e_i = v_i.IncidentEdge();
				e_i_1 = v_i.IncidentEdge().prev();
				
				if(Helper.get(e_i_1.id()).getValue() == VertexType.MERGE)
				{
					newDCEL = Partition.get(e_i_1.DCEL_id()).connect(v_i,Helper.get(e_i_1.id()).getKey(),e_i_1,e_i_1.next());
					Partition.put(newDCEL.id(),newDCEL);
				}
				T.remove(e_i);
				T.remove(e_i_1);

				e_j = T_Query(T,v_i,false);
				e_j_1 = T_Query(T,v_i,true);

				x1 = xQuery(e_j.getNode(true),e_j.getNode(false),v_i);
				x2 = xQuery(e_j_1.getNode(true),e_j_1.getNode(false),v_i);
				Trapezoidalization.add(new Edge(new Vertex(x1,v_i.y()),new Vertex(x2,v_i.y())));

				if(Helper.get(e_j.id()).getValue() == VertexType.MERGE)
				{
					newDCEL = Partition.get(e_j.DCEL_id()).connect(v_i,Helper.get(e_j.id()).getKey(),e_j,e_j.next());
					Partition.put(newDCEL.id(), newDCEL);
				}
				Helper.put(e_j.id(),new Pair<DoublyConnectedEdgeList.Node,VertexType>(v_i,VertexType.MERGE));
				break;
			
			case REGULAR:
				e_i = v_i.IncidentEdge();
				if(Math.atan2(e_i.next().origin().y()-e_i.origin().y(),e_i.next().origin().x()-e_i.origin().x()) < 0)
				{
					e_i_1 = v_i.IncidentEdge().prev();
					if(Helper.get(e_i_1.id()).getValue() == VertexType.MERGE)
					{
						newDCEL = Partition.get(e_i_1.DCEL_id()).connect(v_i,Helper.get(e_i_1.id()).getKey(),e_i_1,e_i_1.next());
						Partition.put(newDCEL.id(), newDCEL);
					}
					T.remove(e_i_1);

					e_j_1 = T_Query(T,v_i,true);
					x2 = xQuery(e_j_1.getNode(true),e_j_1.getNode(false),v_i);
					Trapezoidalization.add(new Edge(new Vertex(v_i.x(),v_i.y()),new Vertex(x2,v_i.y())));

					e_i = v_i.IncidentEdge();
					T.add(e_i);
					Helper.put(e_i.id(),new Pair<DoublyConnectedEdgeList.Node,VertexType>(v_i,VertexType.REGULAR));
				}
				else
				{
					T.remove(e_i);

					e_j = T_Query(T,v_i,false);
					x1 = xQuery(e_j.getNode(true),e_j.getNode(false),v_i);
					Trapezoidalization.add(new Edge(new Vertex(x1,v_i.y()),new Vertex(v_i.x(),v_i.y())));

					e_j = T_Query(T,v_i,false);
					if(Helper.get(e_j.id()).getValue() == VertexType.MERGE)
					{
						newDCEL = Partition.get(e_j.DCEL_id()).connect(v_i,Helper.get(e_j.id()).getKey(),e_j,e_j.next());
						Partition.put(newDCEL.id(), newDCEL);
					}
					Helper.put(e_j.id(),new Pair<DoublyConnectedEdgeList.Node,VertexType>(v_i,VertexType.REGULAR));			

					e_i = v_i.IncidentEdge();
					e_i_1 = v_i.IncidentEdge().prev();
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
		
		DoublyConnectedEdgeList.Node prev = cur.IncidentEdge().prev().origin();
		DoublyConnectedEdgeList.Node next = cur.IncidentEdge().next().origin();

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

	private DoublyConnectedEdgeList.DCEL_Edge T_Query(TreeSet<DoublyConnectedEdgeList.DCEL_Edge> T, DoublyConnectedEdgeList.Node v, boolean direction)
	{
		DoublyConnectedEdgeList.DCEL_Edge e = new DoublyConnectedEdgeList.DCEL_Edge();
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

		@Override
		public int compare(DoublyConnectedEdgeList.Node v1, DoublyConnectedEdgeList.Node v2) {
			if (v1.y() > v2.y())
				return -1;
			else 
				return 1;
		}
	}
	static class MonotoneEdgeComparator implements Comparator<DoublyConnectedEdgeList.DCEL_Edge> {

		@Override
		public int compare(DoublyConnectedEdgeList.DCEL_Edge e1, DoublyConnectedEdgeList.DCEL_Edge e2) {
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
				if(bot1.x() > bot2.x())	return mul;
				return -1*mul;
			}

			if (midx > top1.x())
				return mul;
			else 
				return -1*mul;
		}
	}
}