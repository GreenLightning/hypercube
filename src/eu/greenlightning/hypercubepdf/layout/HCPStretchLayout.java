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
	public HCPLayoutResult[] apply(HCPLayoutSpace space, float[] sizes) {
		int count = sizes.length;
		float totalSpacing = getTotalSpacing(sizes);
		if (totalSpacing >= space.getLength())
			return new HCPLayoutResult[0];
		float factor = (space.getLength() - totalSpacing) / getTotalSize(sizes);
		HCPLayoutResult[] results = new HCPLayoutResult[count];
		float position = space.getStart();
		for (int i = 0; i < count; i++) {
			float size = sizes[i] * space.getDirection() * factor;
			results[i] = new HCPLayoutResult(position, position + size);
			position += size + space.getDirection() * getSpacing();
		}
		return results;
	}

}
