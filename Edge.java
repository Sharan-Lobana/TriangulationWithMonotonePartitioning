public class Edge {
	Vertex start_vertex;
	Vertex end_vertex;

	public Edge()
	{
		start_vertex = null;
		end_vertex = null;
	}

	public Edge(Vertex start_vertex, Vertex end_vertex)
	{
		this.start_vertex = start_vertex;
		this.end_vertex = end_vertex;
	}
}
