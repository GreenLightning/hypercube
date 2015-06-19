package eu.greenlightning.hypercubepdf.layout;

public class HCPSplitLayout extends HCPSpacedLayout {

	private static final HCPSplitLayout NO_SPACING = new HCPSplitLayout(0);

	public static HCPSplitLayout getInstance() {
		return NO_SPACING;
	}

	public static HCPSplitLayout getInstance(float spacing) {
		return spacing == 0 ? NO_SPACING : new HCPSplitLayout(spacing);
	}

	private HCPSplitLayout(float spacing) {
		super(spacing);
	}
	
	@Override
	public float getSize(float[] sizes) {
		return sizes.length * getMaxSize(sizes) + getTotalSpacing(sizes);
	}

	@Override
	public HCPLayoutResults apply(HCPLayoutSpace space, float[] sizes) {
		float totalSpacing = getTotalSpacing(sizes);
		if (totalSpacing >= space.getLength())
			return HCPEmptyLayoutResults.INSTANCE;
		float elementSize = (space.getLength() - totalSpacing) / sizes.length;
		return new HCPSplitLayoutResults(space, sizes.length, elementSize, getSpacing());
	}

}
