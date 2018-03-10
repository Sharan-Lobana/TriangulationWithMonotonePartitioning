import java.util.*;

import javax.swing.JFrame;

public class SimplePolygon {

	public static final int MAX = 105;
	public static Vertex vertices[] = new Vertex[MAX];

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
		for(i=1; i<=n; i++){
			vertices[i] = new Vertex();
			vertices[i].x = rand.nextInt(n)+1;
			int temp_y = rand.nextInt(n)+1;
			while(tSet.contains(temp_y))
			temp_y = rand.nextInt(n)+1;
			tSet.add(temp_y);
			vertices[i].y = -1*(temp_y);
			vertices[i].index = i;
			System.out.println("( "+Integer.toString(vertices[i].x)+","+Integer.toString(vertices[i].y)+" )");
		}

		System.out.println("\n Finding lowermost point");
		int lowest_index = 1;
		for(i=1; i<=n; i++){
			if(vertices[i].y < vertices[lowest_index].y){
				lowest_index = i;
				System.out.println("lowest_index updated to: "+Integer.toString(lowest_index));
			}
			else if(vertices[i].y == vertices[lowest_index].y && vertices[i].x < vertices[lowest_index].x){
				lowest_index = i;
			}
		}
		double tempy,tempx,length;
		for(i=1; i<=n; i++){
			tempx = vertices[i].x - vertices[lowest_index].x;
			tempy = vertices[i].y - vertices[lowest_index].y;
			length = Math.pow(Math.pow(tempx,2)+Math.pow(tempy,2),0.5);
			vertices[i].angle = Math.acos(tempx/length);
			System.out.println("Assigned angle for index i: "+Integer.toString(i)+" is: "+Double.toString(vertices[i].angle));
			vertices[i].angle *= -1.0;
		}

		long start_time = System.currentTimeMillis();

		// Sort according to edge angles
		Arrays.sort(vertices,1,n+1,new Comparator<Vertex>(){
			public int compare(Vertex a,Vertex b){
				return Double.compare(a.angle, b.angle) < 0 ? 1:-1;
			}
		});

		System.out.println("The vertices after sorting w.r.t angle are: ");
		for(i=1; i<=n; i++){
			System.out.println("( "+Integer.toString(vertices[i].x)+","+Integer.toString(vertices[i].y)+" ), angle: "+Double.toString(vertices[i].angle));
		}

		vertices[n+1] = new Vertex(vertices[1].x,vertices[1].y,vertices[1].angle);
		long end_time=System.currentTimeMillis();
		System.out.println("Time taken = "+(end_time-start_time));

		DrawGraph mainPanel = new DrawGraph(vertices,n);
		JFrame frame = new JFrame("DrawGraph");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.getContentPane().add(mainPanel);
	    frame.pack();
	    frame.setLocationByPlatform(true);
	    frame.setVisible(true);

	}
}
