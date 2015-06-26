package eu.greenlightning.hypercubepdf.layout;

import java.util.NoSuchElementException;

abstract class HCPSpacedLayoutResults implements HCPLayoutResults {

	private final HCPLayoutSpace space;
	private final float spacing;

	private int index = -1;
	private float start = 0, end = 0;

	public HCPSpacedLayoutResults(HCPLayoutSpace space, float spacing) {
		this.space = space;
		this.spacing = spacing;
	}

	protected final HCPLayoutSpace getSpace() {
		return space;
	}

	protected final float getSpacing() {
		return spacing;
	}

	protected abstract boolean hasNext(int index);
	protected void beforeNext(int index) {};
	protected abstract float getStart(int index);
	protected abstract float getEnd(int index);
	protected void afterNext(int index) {};
	protected void afterReset() {};

	@Override
	public final boolean hasNext() {
		return hasNext(index);
	}

	@Override
	public final void next() {
		if (!hasNext())
			throw new NoSuchElementException("No more elements.");
		index++;
		beforeNext(index);
		start = getStart(index);
		end = getEnd(index);
		afterNext(index);
	}

	@Override
	public final void reset() {
		index = -1;
		afterReset();
	}

	@Override
	public final int getIndex() {
		checkElement();
		return index;
	}

	@Override
	public final float getLow() {
		checkElement();
		return Math.min(start, end);
	}

	@Override
	public final float getHigh() {
		checkElement();
		return Math.max(start, end);
	}

	private void checkElement() {
		if (index == -1)
			throw new IllegalStateException("No element.");
	}

}
