package eu.greenlightning.hypercubepdf.layout;

public class HCPLayoutResult {

	private final float low, high;

	public HCPLayoutResult(float a, float b) {
		this.low = Math.min(a, b);
		this.high = Math.max(a, b);
	}

	public float getLow() {
		return low;
	}

	public float getHigh() {
		return high;
	}

}
