import java.util.*;

import javax.swing.JFrame;

public class SimplePolygon {

	public static final int MAX = 105;
	public static ArrayList<Vertex> vertices = new ArrayList<Vertex>();

	public static void main(String[] args) {
		int n,i;
		System.out.println("Please enter the number of points:\n");
		Scanner input = new Scanner(System.in);
		Random rand = new Random();
		n = input.nextInt();
		n = Math.min(n,MAX-1);
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

		System.out.println("\n Finding lowermost point");
		int lowest_index = 0;
		for(i=0; i<n; i++){
			if(vertices.get(i).y() < vertices.get(lowest_index).y()){
				lowest_index = i;
				System.out.println("lowest_index updated to: "+Integer.toString(lowest_index));
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
			System.out.println("Assigned angle for index i: "+Integer.toString(i)+" is: "+Double.toString(vertices.get(i).angle()));
			vertices.get(i).setAngle(vertices.get(i).angle()*(-1.0));
		}

		long start_time = System.currentTimeMillis();

		// Sort according to edge angles
		Collections.sort(vertices,new Comparator<Vertex>(){
			public int compare(Vertex a,Vertex b){
				return Double.compare(a.angle(), b.angle()) < 0 ? 1:-1;
			}
		});

		System.out.println("The vertices after sorting w.r.t angle are: ");
		for(i=0; i<n; i++){
			System.out.println("( "+Double.toString(vertices.get(i).x())+","+Double.toString(vertices.get(i).y())+" ), angle: "+Double.toString(vertices.get(i).angle()));
		}

		//vertices[n+1] = new Vertex(vertices[1].x(),vertices[1].y(),vertices[1].angle());
		long end_time=System.currentTimeMillis();
		System.out.println("Time taken = "+(end_time-start_time));


		DoublyConnectedEdgeList dcel = new DoublyConnectedEdgeList(vertices);
		dcel.printInterior();
		DrawGraph mainPanel = new DrawGraph(dcel,n);
		JFrame frame = new JFrame("DrawGraph");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(mainPanel);
    frame.pack();
    frame.setLocationByPlatform(true);
    frame.setVisible(true);

		System.out.println("Reached line 82");
		MonotonePartition monPart = new MonotonePartition(dcel);
		System.out.println("Reached line 84");

	}
}
