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
	public HCPLayoutResult[] apply(HCPLayoutSpace space, float[] sizes) {
		int count = sizes.length;
		float totalSpacing = getTotalSpacing(sizes);
		if (totalSpacing >= space.getLength())
			return new HCPLayoutResult[0];
		float size = (space.getLength() - totalSpacing) / count;
		HCPLayoutResult[] results = new HCPLayoutResult[count];
		for (int i = 0; i < count; i++) {
			float start = space.getStart() + space.getDirection() * i * (size + getSpacing());
			results[i] = new HCPLayoutResult(start, start + space.getDirection() * size);
		}
		return results;
	}

}
