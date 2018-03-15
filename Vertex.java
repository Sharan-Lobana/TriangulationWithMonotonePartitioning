public class Vertex {

	private double x;
	private double y;
	private int index;
	private double angle;

	public Vertex() {
		this.x = 0;
		this.y = 0;
	}
	public Vertex(double x, double y){
		this.x = x;
		this.y = y;
	}
	public Vertex(double x, double y, double angle){
		this.x = x;
		this.y = y;
		this.angle = angle;
	}

	public Vertex(DoublyConnectedEdgeList.Node a) {
		this.x = a.x();
		this.y = a.y();
		this.index = a.id();
	}

	public double x() {
		return this.x;
	}

	public double y() {
		return this.y;
	}

	public int index() {
		return this.index;
	}

	public double angle() {
		return this.angle;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public String toString() {
		return new String("x: "+Double.toString(x)+", y: "+Double.toString(y)+";");
	}
}
