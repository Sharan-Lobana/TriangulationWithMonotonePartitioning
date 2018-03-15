// GroupID-21 (14114053_14114071) - Sharanpreet Singh & Vaibhav Gosain
// Date: March 15, 2018
// ArtGalleryProblem.java - This file contains the main code for the project.
// The entire flow of code can be determined from this file.

import java.util.*;
import javax.swing.JFrame;

public class ArtGalleryProblem {

	public static final int MAX = 1000;
	public static final double EPSILON = 1e-4;
	public static ArrayList<Vertex> vertices = new ArrayList<Vertex>();

	public static void main(String[] args) {

		int n,i;
		//Random point generation from user input
		System.out.println("Please enter the number of points:\n");
		Scanner input = new Scanner(System.in);
		Random rand = new Random();
		n = input.nextInt();
		n = Math.min(n,MAX);
		System.out.println("The value of n entered is: "+Integer.toString(n));
		System.out.println("The vertices initialized are: ");
		TreeSet<Integer> tSet = new TreeSet<Integer>();
		for(i=0; i<n; i++){
			vertices.add(new Vertex());
			vertices.get(i).setX(rand.nextInt(n)+1);
			int temp_y = rand.nextInt(n)+1;
			while(tSet.contains(temp_y))
			temp_y = rand.nextInt(n)+1;
			tSet.add(temp_y);
			vertices.get(i).setY(-1*(temp_y));	// operating in fourth quadrant for computation purposes
			vertices.get(i).setIndex(i);
			System.out.println("( "+Double.toString(vertices.get(i).x())+","+Double.toString(vertices.get(i).y())+" )");
		}

		//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

		// ##############################################
		// //Test case 1: A non monotone polygon
		// Vertex[] arr = {
		// 		new Vertex( 5.0,-3.0 ),
		// 		new Vertex( 2.0,-6.0 ),
		// 		new Vertex( 10.0,-2.0 ),
		// 		new Vertex( 5.0,-8.0 ),
		// 		new Vertex( 6.0,-1.0 ),
		// 		new Vertex( 10.0,-5.0 ),
		// 		new Vertex( 9.0,-9.0 ),
		// 		new Vertex( 1.0,-4.0 ),
		// 		new Vertex( 2.0,-10.0 ),
		// 		new Vertex( 4.0,-7.0 )
		// 	};
		// n = 10;

		// //Test case 2: A monotone polygon
		// Vertex[] arr = {
		// 	new Vertex( 2.0, -1.0 ),
		// 	new Vertex( 1.0, -2.0 ),
		// 	new Vertex( 1.0, -6.0 ),
		// 	new Vertex( 2.0, -7.0 ),
		// 	new Vertex( 5.0, -5.0 ),
		// 	new Vertex( 4.0, -4.0 ),
		// 	new Vertex( 3.0, -3.0 )
		// };
		// n = 7;

		// //Test case 3: A diamond
		// Vertex[] arr = {
		// 	new Vertex(3.0,-1.0),
		// 	new Vertex(1.0,-2.0),
		// 	new Vertex(3.0,-5.0),
		// 	new Vertex(4.0,-4.0)
		// };
		// n=4;

		//Important: Uncomment the underlying code along with a single test case
		// to avoid compilation errors

		// for(i = 0; i < n; i++)
		// arr[i].setIndex(i+1);
		// vertices = new ArrayList<Vertex>(Arrays.asList(arr));
		//################################################

		int lowest_index = 0;
		for(i=0; i<n; i++){
			if(vertices.get(i).y() < vertices.get(lowest_index).y()){
				lowest_index = i;
			}
			else if(vertices.get(i).y() == vertices.get(lowest_index).y() && vertices.get(i).x() < vertices.get(lowest_index).x()){
				lowest_index = i;
			}
		}

		double tempy,tempx,length;
		for(i=0; i<n; i++){
			tempx = vertices.get(i).x() - vertices.get(lowest_index).x();
			tempy = vertices.get(i).y() - vertices.get(lowest_index).y();
			length = Math.pow(Math.pow(tempx,2)+Math.pow(tempy,2),0.5);
			vertices.get(i).setAngle(Math.acos(tempx/length));
			vertices.get(i).setAngle(vertices.get(i).angle()*(-1.0));
		}

		long start_time = System.currentTimeMillis();

		// Sort according to edge angles
		Collections.sort(vertices,new Comparator<Vertex>(){
			public int compare(Vertex a,Vertex b){
				if(Math.abs(a.angle()-b.angle()) < EPSILON)
					return Double.compare(a.y(),b.y()) < 0.0 ? 1: -1;
				return Double.compare(a.angle(), b.angle()) < 0.0 ? 1:-1;
			}
		});

		for(i=0;i<n;i++)
		{
			vertices.get(i).setIndex(i+1);
		}

		DoublyConnectedEdgeList dcel = new DoublyConnectedEdgeList(vertices);
		MonotonePartition monPart = new MonotonePartition(dcel);
		ArrayList<DoublyConnectedEdgeList> monPolygons = new ArrayList<DoublyConnectedEdgeList>();

		for(Integer k: monPart.partition().keySet()) {
			monPolygons.add(monPart.partition().get(k));
		}

		MonotoneTriangulation monTriangulation = new MonotoneTriangulation(monPolygons);
		ArrayList<DoublyConnectedEdgeList> triangulation = monTriangulation.triangulateMonotonePolygon();
		DualGraph dualGraph = new DualGraph(triangulation,vertices);
		dualGraph.construct();
		TreeMap<Integer,ArrayList<Integer>> adjacencyList = dualGraph.getAdjacencyList();
		ThreeColoring threeColoring = new ThreeColoring();
		TreeMap<Integer,Integer> nodeColor = threeColoring.threeColor(triangulation,vertices);
		TreeMap<Integer,Integer> colorFreq = new TreeMap<Integer,Integer>();

		for(Integer k: nodeColor.keySet()) {
			if(colorFreq.containsKey(nodeColor.get(k)))
				colorFreq.put(nodeColor.get(k),colorFreq.get(nodeColor.get(k))+1);
			else
				colorFreq.put(nodeColor.get(k),1);
		}

		Integer minFreq = n;
		Integer minColor = 0;
		for(Integer col: colorFreq.keySet()) {
			minFreq = Math.min(minFreq,colorFreq.get(col));
		}
		for(Integer col: colorFreq.keySet()) {
			if(colorFreq.get(col) == minFreq) {
				minColor = col;
				break;
			}
		}

		long end_time=System.currentTimeMillis();
		System.out.println("Time taken = "+(end_time-start_time)+" ms");

		//Draw the simple polygon
		DrawGraph mainPanel = new DrawGraph(monPolygons,n);
		JFrame frame = new JFrame("DrawGraph");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(mainPanel);
    frame.pack();
    frame.setLocationByPlatform(true);
    frame.setVisible(true);

		//Draw the trapezoidal lines
		DrawTrapezoidalization trapezoidalPanel = new DrawTrapezoidalization(monPart.partition(),monPart.trapezoidalization(),n);
		JFrame frameT = new JFrame("DrawTrapezoidalization");
    frameT.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frameT.getContentPane().add(trapezoidalPanel);
    frameT.pack();
    frameT.setLocationByPlatform(true);
    frameT.setVisible(true);

		DrawMonotonePartition monPanel = new DrawMonotonePartition(monPart.partition(),monPart.trapezoidalization(),n);
		JFrame frameM = new JFrame("DrawMonotonePartition");
    frameM.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frameM.getContentPane().add(monPanel);
    frameM.pack();
    frameM.setLocationByPlatform(true);
    frameM.setVisible(true);

		//Draw the traingulation of polygon obtained from triangulating monotone polygons
		DrawTriangulation triangulationPanel = new DrawTriangulation(triangulation,n);
		JFrame frameTri = new JFrame("DrawTriangulation");
		frameTri.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameTri.getContentPane().add(triangulationPanel);
		frameTri.pack();
		frameTri.setLocationByPlatform(true);
		frameTri.setVisible(true);

		//Draw the dual graph of triangulation
		DrawDualGraph dualPanel = new DrawDualGraph(triangulation,adjacencyList,n);
		JFrame frameDual = new JFrame("DrawDualGraph");
		frameDual.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameDual.getContentPane().add(dualPanel);
		frameDual.pack();
		frameDual.setLocationByPlatform(true);
		frameDual.setVisible(true);

		//Draw the three coloring of triangulation obtained from dfs on dual graph
		DrawThreeColoring threeColoringPanel = new DrawThreeColoring(triangulation,adjacencyList,n,nodeColor);
		JFrame frameThreeColoring = new JFrame("DrawThreeColoring");
		frameThreeColoring.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameThreeColoring.getContentPane().add(threeColoringPanel);
		frameThreeColoring.pack();
		frameThreeColoring.setLocationByPlatform(true);
		frameThreeColoring.setVisible(true);

		//Draw the vertex gaurds as obtained from Fisk's theorem
		DrawVertexGaurd vertexGaurdPanel = new DrawVertexGaurd(triangulation,adjacencyList,n,nodeColor,minColor);
		JFrame frameVertexGaurd = new JFrame("DrawVertexGaurd");
		frameVertexGaurd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameVertexGaurd.getContentPane().add(vertexGaurdPanel);
		frameVertexGaurd.pack();
		frameVertexGaurd.setLocationByPlatform(true);
		frameVertexGaurd.setVisible(true);

	}
}
