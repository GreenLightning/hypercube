package eu.greenlightning.hypercubepdf.layout;

class HCPSplitLayoutResults extends HCPSpacedLayoutResults {

	private final int count;
	private final float size;

	private float start;

	public HCPSplitLayoutResults(HCPLayoutSpace space, int count, float size, float spacing) {
		super(space, spacing);
		this.count = count;
		this.size = size;
	}

	@Override
	protected boolean hasNext(int index) {
		return index + 1 < count;
	}

	@Override
	protected void beforeNext(int index) {
		start = getSpace().getStart() + getSpace().getDirection() * index * (size + getSpacing());
	}

	@Override
	protected float getStart(int index) {
		return start;
	}

	@Override
	protected float getEnd(int index) {
		return start + getSpace().getDirection() * size;
	}

}