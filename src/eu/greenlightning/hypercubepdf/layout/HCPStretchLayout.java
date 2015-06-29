package eu.greenlightning.hypercubepdf.layout;

/**
 * A layout algorithm which keeps the ratios between the element's sizes the same, but squeezes or stretches them so
 * that all elements together take up all available space.
 * <p>
 * Always paints all elements unless there is not enough space for the spacing alone, in which case nothing is painted.
 * <p>
 * This class is immutable.
 * 
 * @author Green Lightning
 */
public class HCPStretchLayout extends HCPSpacedLayout {

	private static final HCPStretchLayout NO_SPACING = new HCPStretchLayout(0);

	/**
	 * Returns an {@link HCPStretchLayout} instance which places elements next to each other.
	 * 
	 * @return an instance with no spacing
	 */
	public static HCPStretchLayout getInstance() {
		return NO_SPACING;
	}

	/**
	 * Returns an {@link HCPStretchLayout} instance which maintains the specified spacing between two adjacent elements.
	 * 
	 * @param spacing must be {@literal >= 0}
	 * @return an instance with the specified spacing
	 * @throws IllegalArgumentException if spacing is {@literal < 0}
	 */
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
			return HCPEmptyLayoutResults.INSTANCE;
		float stretchFactor = (space.getLength() - totalSpacing) / getTotalSize(sizes);
		return new HCPStretchLayoutResults(space, sizes, stretchFactor, getSpacing());
	}

}
