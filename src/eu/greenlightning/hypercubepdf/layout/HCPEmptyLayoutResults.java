package eu.greenlightning.hypercubepdf.layout;

import java.util.NoSuchElementException;

/**
 * An empty {@link HCPLayoutResults}. Can be returned by {@link HCPLayout} if there are no results.
 * 
 * @author Green Lightning
 */
public enum HCPEmptyLayoutResults implements HCPLayoutResults {

	/** The only instance of this class. Avoids unnecessary object creation. */
	INSTANCE;

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public void next() {
		throw new NoSuchElementException("No more elements.");
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
