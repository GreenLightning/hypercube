package eu.greenlightning.hypercubepdf.layout;

class HCPFlowLayoutResults extends HCPSpacedLayoutResults {

	private final float[] sizes;

	private float totalSize = 0;
	private float elementSize;

	public HCPFlowLayoutResults(HCPLayoutSpace space, float[] sizes, float spacing) {
		super(space, spacing);
		this.sizes = sizes;
	}

	@Override
	protected boolean hasNext(int index) {
		return index + 1 < sizes.length && totalSize < getSpace().getLength();
	}

	@Override
	protected void beforeNext(int index) {
		elementSize = sizes[index];
		if (totalSize + elementSize >= getSpace().getLength())
			elementSize = getSpace().getLength() - totalSize;
	}

	@Override
	protected float getStart(int index) {
		return getSpace().getStart() + getSpace().getDirection() * totalSize;
	}

	@Override
	protected float getEnd(int index) {
		return getSpace().getStart() + getSpace().getDirection() * (totalSize + elementSize);
	}

	@Override
	protected void afterNext(int index) {
		totalSize += elementSize + getSpacing();
	}

	@Override
	protected void afterReset() {
		totalSize = 0;
	}

}