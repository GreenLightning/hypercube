package eu.greenlightning.hypercubepdf.layout;

public class HCPLayoutSpace {

	private final float start, end;

	public HCPLayoutSpace(float start, float end) {
		this.start = start;
		this.end = end;
	}

	public float getStart() {
		return start;
	}

	public float getEnd() {
		return end;
	}

	public float getDirection() {
		return Math.signum(end - start);
	}
	
	public float getLength() {
		return Math.abs(end - start);
	}

}
