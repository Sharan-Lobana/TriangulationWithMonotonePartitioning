import java.util.ArrayList;
import java.util.TreeMap;

public class DualGraph {
  private ArrayList<DoublyConnectedEdgeList> listOfTriangles;
  private TreeMap<Integer, ArrayList<Integer>> adjacencyList;

  public DualGraph() {
    this.listOfTriangles = new ArrayList<DoublyConnectedEdgeList>();
  }

  public DualGraph(ArrayList<DoublyConnectedEdgeList> listOfTriangles) {
    this.listOfTriangles = listOfTriangles;
  }

  public ArrayList<DoublyConnectedEdgeList> getListOfTriangles() {
    return this.listOfTriangles;
  }

  public TreeMap<Integer,ArrayList<Integer>> getAdjacencyList() {
    return this.adjacencyList;
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
      System.out.printf("Currently finding neighbours for triangle with id: %d\n", temp.DCEL_id());
      do {
        for(int ind2 = ind1+1; ind2 < listOfTriangles.size(); ind2++) {
          d2 = listOfTriangles.get(ind2);
          temp2 = d2.rep_edge();
          if(temp.isEqualto(temp2) || temp.isEqualto(temp2.next()) || temp2.isEqualto(temp2.prev())) {
            if(adjacencyList.containsKey(temp.DCEL_id()))
              tempList = adjacencyList.get(temp.DCEL_id());
            else
              tempList = new ArrayList<Integer>();

            System.out.printf("Added and edge between triangles with ids %d and %d\n",temp.DCEL_id(),temp2.DCEL_id());
            tempList.add(temp2.DCEL_id());
            adjacencyList.put(temp.DCEL_id(),tempList);

          }
        }
        temp = temp.next();
      } while(temp.id() != temporary_id);
    }
  }

}
