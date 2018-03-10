import java.util.ArrayList;
public class DualGraph {
  private ArrayList<DoublyConnectedEdgeList> listOfTriangles;
  private ArrayList<ArrayList<Integer>> adjacencyList;

  public DualGraph() {
    this.listOfTriangles = new ArrayList<DoublyConnectedEdgeList>();
  }

  public DualGraph(ArrayList<DoublyConnectedEdgeList> listOfTriangles) {
    this.listOfTriangles = listOfTriangles;
  }

  public ArrayList<DoublyConnectedEdgeList> getListOfTriangles() {
    return this.listOfTriangles;
  }

  public ArrayList<ArrayList<Integer>> getAdjacencyList() {
    return this.adjacencyList;
  }

  public void construct() {

    int numberOfTriangles = listOfTriangles.size();
    DoublyConnectedEdgeList.DCEL_Edge temp;
    ArrayList<Integer> tempList;
    this.adjacencyList = new ArrayList<ArrayList<Integer>>();

    for(int i = 0; i <= numberOfTriangles; i++) {
      this.adjacencyList.add(new ArrayList<Integer>());
    }

    for(DoublyConnectedEdgeList d: listOfTriangles) {
      temp = d.rep_edge();
      int temporary_id = temp.id();

      do {
        if(temp.face().id() != DoublyConnectedEdgeList.DCEL_Face.INF && temp.twin().face().id() != DoublyConnectedEdgeList.DCEL_Face.INF) {
          tempList = adjacencyList.get(temp.face().id());
          tempList.add(temp.twin().face().id());
          adjacencyList.set(temp.face().id(),tempList);
        }
        temp = temp.next();
      } while(temp.id() != temporary_id);
    }

  }

}
