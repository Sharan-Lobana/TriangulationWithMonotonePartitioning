// GroupID-21 (14114053_14114071) - Sharanpreet Singh & Vaibhav Gosain
// Date: March 15, 2018
// Edge.java - This file contains the (non-dcel) edge data structure used in simple polygon

public class Edge {
	private Vertex start_vertex;
	private Vertex end_vertex;

	public Edge() {
		start_vertex = null;
		end_vertex = null;
	}

	public Edge(Vertex start_vertex, Vertex end_vertex) {
		this.start_vertex = start_vertex;
		this.end_vertex = end_vertex;
	}

	public Vertex start_vertex() {
		return this.start_vertex;
	}

	public Vertex end_vertex() {
		return this.end_vertex;
	}

	public void setStartVertex(Vertex start_vertex) {
		this.start_vertex = start_vertex;
	}

	public void setEndVertex(Vertex end_vertex) {
		this.end_vertex = end_vertex;
	}
}
