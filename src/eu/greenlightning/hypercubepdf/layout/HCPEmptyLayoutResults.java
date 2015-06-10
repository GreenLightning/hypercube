package eu.greenlightning.hypercubepdf.layout;

public class HCPEmptyLayoutResults implements HCPLayoutResults {

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public void next() {
		throw new IllegalStateException("No more elements.");
	}

	@Override
	public void reset() {}

	@Override
	public int getIndex() {
		throw new IllegalStateException("No element.");
	}

	@Override
	public float getLow() {
		throw new IllegalStateException("No element.");
	}

	@Override
	public float getHigh() {
		throw new IllegalStateException("No element.");
	}

}
