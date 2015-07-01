package eu.greenlightning.hypercubepdf.layout;

/**
 * Base class for layout algorithms which allow to introduce spacing between adjacent elements.
 * <p>
 * This class is immutable.
 * 
 * @author Green Lightning
 */
public abstract class HCPSpacedLayout implements HCPLayout {

	private final float spacing;

	/**
	 * Creates a new {@link HCPSpacedLayout} instance which uses the specified spacing.
	 * 
	 * @param spacing must be {@literal >= 0}
	 * @throws IllegalArgumentException if spacing is {@literal < 0}
	 */
	public HCPSpacedLayout(float spacing) {
		if (spacing < 0)
			throw new IllegalArgumentException("Spacing must be equal to or greater than zero, but was " + spacing
				+ ".");
		this.spacing = spacing;
	}

	/**
	 * Returns the spacing used by this instance.
	 * 
	 * @return the spacing used by this instance
	 */
	public float getSpacing() {
		return spacing;
	}

	/**
	 * Returns the total amount of spacing for the specified list of element sizes.
	 * <p>
	 * This is helper method for subclasses.
	 * 
	 * @param sizes not {@code null}
	 * @return the total amount of spacing for the specified list of element sizes
	 * @throws NullPointerException if sizes is {@code null}
	 */
	protected final float getTotalSpacing(float[] sizes) {
		if (sizes.length == 0)
			return 0;
		return (sizes.length - 1) * spacing;
	}

	/**
	 * Returns the maximum size in the specified list of elements sizes.
	 * <p>
	 * This is a helper method for subclasses.
	 * 
	 * @param sizes not {@code null}
	 * @return the maximum size
	 * @throws NullPointerException if sizes is {@code null}
	 */
	protected final float getMaxSize(float[] sizes) {
		float maxSize = 0;
		for (float elementSize : sizes)
			maxSize = Math.max(maxSize, elementSize);
		return maxSize;
	}

	/**
	 * Returns the total size of all element sizes in the specified list.
	 * <p>
	 * This method does <b>not</b> include spacing between the elements. To include spacing add the result of
	 * {@link #getTotalSpacing(float[])} to the return value of this method.
	 * <p>
	 * This is a helper method for subclasses.
	 * 
	 * @param sizes not {@code null}
	 * @return the total size
	 * @throws NullPointerException if sizes is {@code null}
	 */
	protected final float getTotalSize(float[] sizes) {
		float totalSize = 0;
		for (float elementSize : sizes)
			totalSize += elementSize;
		return totalSize;
	}

}
