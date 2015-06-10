package eu.greenlightning.hypercubepdf.layout;

public class HCPFlowLayout extends HCPSpacedLayout {

	private final static HCPFlowLayout NO_SPACING = new HCPFlowLayout(0);

	public static HCPFlowLayout getInstance() {
		return NO_SPACING;
	}

	public static HCPFlowLayout getInstance(float spacing) {
		return spacing == 0 ? NO_SPACING : new HCPFlowLayout(spacing);
	}

	private HCPFlowLayout(float spacing) {
		super(spacing);
	}

	@Override
	public float getSize(float[] sizes) {
		return getTotalSize(sizes) + getTotalSpacing(sizes);
	}

	@Override
	public HCPLayoutResults apply(HCPLayoutSpace space, float[] sizes) {
		return new HCPFlowLayoutResults(space, sizes, getSpacing());
	}

}
