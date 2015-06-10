package eu.greenlightning.hypercubepdf.layout;

public class HCPStretchLayout extends HCPSpacedLayout {

	private static final HCPStretchLayout NO_SPACING = new HCPStretchLayout(0);

	public static HCPStretchLayout getInstance() {
		return NO_SPACING;
	}

	public static HCPStretchLayout getInstance(float spacing) {
		return spacing == 0 ? NO_SPACING : new HCPStretchLayout(spacing);
	}

	private HCPStretchLayout(float spacing) {
		super(spacing);
	}
	
	@Override
	public float getSize(float[] sizes) {
		return getTotalSize(sizes) + getTotalSpacing(sizes);
	}

	@Override
	public HCPLayoutResults apply(HCPLayoutSpace space, float[] sizes) {
		float totalSpacing = getTotalSpacing(sizes);
		if (totalSpacing >= space.getLength())
			return new HCPEmptyLayoutResults();
		float stretchFactor = (space.getLength() - totalSpacing) / getTotalSize(sizes);
		return new HCPStretchLayoutResults(space, sizes, stretchFactor, getSpacing());
	}

}
