package eu.greenlightning.hypercubepdf.layout;

/**
 * A layout algorithm which always uses the elements preferred size. If an element's size is larger than the remaining
 * space, the element is painted inside the remaining space and all further elements are ignored.
 * <p>
 * This class is immutable.
 * 
 * @author Green Lightning
 */
public class HCPFlowLayout extends HCPSpacedLayout {

	private final static HCPFlowLayout NO_SPACING = new HCPFlowLayout(0);

	/**
	 * Returns an {@link HCPFlowLayout} instance which places elements next to each other.
	 * 
	 * @return an instance with no spacing
	 */
	public static HCPFlowLayout getInstance() {
		return NO_SPACING;
	}

	/**
	 * Returns an {@link HCPFlowLayout} instance which maintains the specified spacing between two adjacent elements.
	 * 
	 * @param spacing must be {@literal >= 0}
	 * @return an instance with the specified spacing
	 * @throws IllegalArgumentException if spacing is {@literal < 0}
	 */
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
