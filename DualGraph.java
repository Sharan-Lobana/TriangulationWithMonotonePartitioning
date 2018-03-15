import java.util.ArrayList;
import java.util.TreeMap;

public class DualGraph {
  private ArrayList<DoublyConnectedEdgeList> listOfTriangles;
  private TreeMap<Integer, ArrayList<Integer>> adjacencyList;
  private ArrayList<Vertex> vertices;
  private int n;
  public DualGraph() {
    this.listOfTriangles = new ArrayList<DoublyConnectedEdgeList>();
  }

  public DualGraph(ArrayList<DoublyConnectedEdgeList> listOfTriangles, ArrayList<Vertex> vertices) {
    this.listOfTriangles = listOfTriangles;
    this.vertices = vertices;
    n = vertices.size();
  }

  public ArrayList<DoublyConnectedEdgeList> getListOfTriangles() {
    return this.listOfTriangles;
  }

  public TreeMap<Integer,ArrayList<Integer>> getAdjacencyList() {
    return this.adjacencyList;
  }

  public ArrayList<Vertex> vertices()
  {
    return this.vertices;
  }

  public void construct() {

    int numberOfTriangles = listOfTriangles.size();
    DoublyConnectedEdgeList.DCEL_Edge temp;
    ArrayList<Integer> tempList;
    this.adjacencyList = new TreeMap<Integer,ArrayList<Integer>>();


    for(int ind1 = 0; ind1 < listOfTriangles.size(); ind1++) {
      DoublyConnectedEdgeList d = listOfTriangles.get(ind1);
      DoublyConnectedEdgeList d2;
      DoublyConnectedEdgeList.DCEL_Edge temp2;
      temp = d.rep_edge();
      int temporary_id = temp.id();

      assert(temp.DCEL_id() == d.id());
      //Debug
      //System.out.printf("Currently finding neighbours for triangle with id: %d\n", temp.DCEL_id());
      //d.printInterior();
      //d.printVertices();

      do {
        for(int i=0;i<n;i++)
        {
          if(Math.abs(temp.origin().x() - vertices.get(i).x()) < 0.001 && Math.abs(temp.origin().y() - vertices.get(i).y()) < 0.001)
            temp.origin().setID(i+1);
        }
        for(int ind2 = 0; ind2 < listOfTriangles.size(); ind2++) {
          if(ind1 == ind2)  continue;
          d2 = listOfTriangles.get(ind2);
          temp2 = d2.rep_edge();

          //Debug
          //System.out.printf("Current triangle for comparison is: %d\n", d2.id());
          //d2.printInterior();
          //d2.printVertices();

          if(temp.isEqualto(temp2) || temp.isEqualto(temp2.next()) || temp.isEqualto(temp2.prev())) {
            if(adjacencyList.containsKey(temp.DCEL_id()))
              tempList = adjacencyList.get(temp.DCEL_id());
            else
              tempList = new ArrayList<Integer>();

            // //Debug
            //System.out.printf("Added an edge between triangles with ids %d and %d\n",temp.DCEL_id(),temp2.DCEL_id());
            tempList.add(temp2.DCEL_id());
            adjacencyList.put(temp.DCEL_id(),tempList);

          }
        }
        temp = temp.next();
        assert(temp.DCEL_id() == d.id());

      } while(temp.id() != temporary_id);
    }
  }

}
