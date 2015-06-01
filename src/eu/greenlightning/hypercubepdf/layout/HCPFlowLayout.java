package eu.greenlightning.hypercubepdf.layout;

import java.util.*;

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
	public HCPLayoutResult[] apply(HCPLayoutSpace space, float[] sizes) {
		List<HCPLayoutResult> results = new ArrayList<>(sizes.length);
		float totalSize = 0;
		for (int i = 0; i < sizes.length; i++) {
			float elementSize = sizes[i];
			if (totalSize + elementSize >= space.getLength())
				elementSize = space.getLength() - totalSize;
			float a = space.getStart() + space.getDirection() * totalSize;
			float b = space.getStart() + space.getDirection() * (totalSize + elementSize);
			results.add(new HCPLayoutResult(a, b));
			if (totalSize + elementSize + getSpacing() >= space.getLength())
				break;
			totalSize += elementSize + getSpacing();
		}
		return results.toArray(new HCPLayoutResult[results.size()]);
	}

}
