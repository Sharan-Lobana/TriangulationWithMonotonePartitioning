import java.util.*;
/* Important assertion is that the dcel's in triangulation list
*  are sorted by their ids which are numbers from 1 to n
*  where n is the number of triangles, check this assertion if required
*/
public class ThreeColoring {
  public TreeMap<Integer,Integer> threeColor(ArrayList<DoublyConnectedEdgeList> triangulation) {

    for(DoublyConnectedEdgeList dcel: triangulation) {
      assert dcel.node_count() == 3;
    }

    TreeMap<Integer,Integer> nodeColor = new TreeMap<Integer,Integer>();
    DualGraph dualGraph  = new DualGraph(triangulation);
    dualGraph.construct();
    ArrayList<Boolean> visited = new ArrayList<Boolean>();
    for(int i = 0; i <= triangulation.size(); i++) {
      visited.add(false);
    }
    DFS(dualGraph, triangulation, nodeColor, visited);
    return nodeColor;
  }

  public void DFS(DualGraph dualGraph, ArrayList<DoublyConnectedEdgeList> triangulation, TreeMap<Integer,Integer> nodeColor, ArrayList<Boolean> visited) {
    int src = 1;
    DoublyConnectedEdgeList dcel = triangulation.get(0);
    DoublyConnectedEdgeList.DCEL_Edge tempEdge = dcel.rep_edge();
    for(int i = 0; i < 3; i++) {
      nodeColor.put(tempEdge.origin().id(),i);
      tempEdge = tempEdge.next();
    }
    visited.set(src,true);
    for(int k: dualGraph.getAdjacencyList().get(src)) {
      if(!visited.get(k))
        DFSUtil(dualGraph, triangulation, nodeColor, k, visited);
    }
  }

  public void DFSUtil(DualGraph dualGraph, ArrayList<DoublyConnectedEdgeList> triangulation, TreeMap<Integer,Integer> nodeColor, int src, ArrayList<Boolean> visited) {
    DoublyConnectedEdgeList dcel = triangulation.get(src-1);
    DoublyConnectedEdgeList.DCEL_Edge tempEdge = dcel.rep_edge();
    int color = 0;
    for(int i = 0; i < 3; i++) {
      if(nodeColor.containsKey(tempEdge.origin().id()))
      color += nodeColor.get(tempEdge.origin().id());
      tempEdge = tempEdge.next();
    }

    for(int i = 0; i < 3; i++) {
      if(!nodeColor.containsKey(tempEdge.origin().id())) {
        nodeColor.put(tempEdge.origin().id(),color%3);
      }
    }
    visited.set(src,true);
    for(int k: dualGraph.getAdjacencyList().get(src)) {
      if(!visited.get(k))
        DFSUtil(dualGraph, triangulation, nodeColor, k, visited);
    }
  }
}
