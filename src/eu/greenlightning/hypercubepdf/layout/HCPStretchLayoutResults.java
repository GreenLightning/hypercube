package eu.greenlightning.hypercubepdf.layout;

class HCPStretchLayoutResults extends HCPSpacedLayoutResults {

	private final float[] sizes;
	private final float stretchFactor;

	private float position;
	private float size;

	public HCPStretchLayoutResults(HCPLayoutSpace space, float[] sizes, float stretchFactor, float spacing) {
		super(space, spacing);
		this.sizes = sizes;
		this.stretchFactor = stretchFactor;
		this.position = getSpace().getStart();
	}

	@Override
	protected boolean hasNext(int index) {
		return index + 1 < sizes.length;
	}

	@Override
	protected void beforeNext(int index) {
		size = stretchFactor * sizes[index];
	}

	@Override
	protected float getStart(int index) {
		return position;
	}

	@Override
	protected float getEnd(int index) {
		return position + getSpace().getDirection() * size;
	}

	@Override
	protected void afterNext(int index) {
		position += getSpace().getDirection() * (size + getSpacing());
	}

	@Override
	protected void afterReset() {
		position = getSpace().getStart();
	}

}