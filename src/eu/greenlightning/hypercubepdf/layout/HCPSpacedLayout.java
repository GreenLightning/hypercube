package eu.greenlightning.hypercubepdf.layout;

public abstract class HCPSpacedLayout implements HCPLayout {

	private final float spacing;

	public HCPSpacedLayout(float spacing) {
		if (spacing < 0)
			throw new IllegalArgumentException("Spacing must be equal to or greater than zero, but was "
					+ spacing + ".");
		this.spacing = spacing;
	}

	public float getSpacing() {
		return spacing;
	}

	protected float getTotalSpacing(float[] sizes) {
		if (sizes.length == 0)
			return 0;
		return (sizes.length - 1) * spacing;
	}
	
	protected float getMaxSize(float[] sizes){
		float maxSize = 0;
		for(float elementSize : sizes)
			maxSize = Math.max(maxSize, elementSize);
		return maxSize;
	}

	protected float getTotalSize(float[] sizes) {
		float totalSize = 0;
		for (float elementSize : sizes)
			totalSize += elementSize;
		return totalSize;
	}

}
