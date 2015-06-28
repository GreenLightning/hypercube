package eu.greenlightning.hypercubepdf.layout;

/**
 * A layout algorithm which splits the available space equally among the elements, regardless of their preferred size.
 * <p>
 * Always paints all elements unless there is not enough space for the spacing alone, in which case nothing is painted.
 * <p>
 * This class is immutable.
 * 
 * @author Green Lightning
 */
public class HCPSplitLayout extends HCPSpacedLayout {

	private static final HCPSplitLayout NO_SPACING = new HCPSplitLayout(0);

	/**
	 * Returns an {@link HCPSplitLayout} instance which places elements next to each other.
	 * 
	 * @return an instance with no spacing
	 */
	public static HCPSplitLayout getInstance() {
		return NO_SPACING;
	}

	/**
	 * Returns an {@link HCPSplitLayout} instance which maintains the specified spacing between two adjacent elements.
	 * 
	 * @param spacing must be {@literal >= 0}
	 * @return an instance with the specified spacing
	 * @throws IllegalArgumentException if spacing is {@literal < 0}
	 */
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
