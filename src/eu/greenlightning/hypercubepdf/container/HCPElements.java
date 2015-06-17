package eu.greenlightning.hypercubepdf.container;

import java.io.IOException;
import java.util.*;

import eu.greenlightning.hypercubepdf.HCPElement;

/**
 * Wraps an array of {@link HCPElement}s.
 * <p>
 * The constructors of this class copy the provided array or collection and perform {@code null}-checks.
 * <p>
 * Utility methods allow to easily calculate the maximum and total width / height of all the elements.
 * <p>
 * <p>
 * This class is iterable and immutable.
 * 
 * @author Green Lightning
 */
class HCPElements implements Iterable<HCPElement> {

	private final HCPElement[] elements;

	/**
	 * Creates an {@link HCPElements} instance from an array or from a varargs list of elements.
	 * 
	 * @param elements not {@code null}; must not contain {@code null}
	 * @throws NullPointerException if elements is {@code null}
	 * @throws IllegalArgumentException if elements contains {@code null}
	 */
	public HCPElements(HCPElement... elements) {
		this.elements = Objects.requireNonNull(elements, "Elements must not be null.").clone();
		checkElements();
	}

	/**
	 * Creates an {@link HCPElements} instance from a {@link Collection} of elements.
	 * 
	 * @param elements not {@code null}; must not contain {@code null}
	 * @throws NullPointerException if elements is {@code null}
	 * @throws IllegalArgumentException if elements contains {@code null}
	 */
	public HCPElements(Collection<? extends HCPElement> elements) {
		this.elements = convertToArray(elements);
		checkElements();
	}

	private static final HCPElement[] convertToArray(Collection<? extends HCPElement> elements) {
		Objects.requireNonNull(elements, "Elements must not be null.");
		return elements.toArray(new HCPElement[elements.size()]);
	}

	private void checkElements() {
		for (int i = 0; i < elements.length; i++)
			if (elements[i] == null)
				throw new IllegalArgumentException("Elements contained null at index " + i
					+ " (total length: " + elements.length + ").");
	}

	/**
	 * Returns the width of the widest element in this {@link HCPElements} instance. Returns zero if this
	 * instance contains no elements.
	 * 
	 * @return the maximum width; 0 if empty
	 * @throws IOException if the {@link HCPElement#getWidth()} method of an element throws
	 */
	public float getMaxWidth() throws IOException {
		float width = 0;
		for (HCPElement element : elements)
			width = Math.max(width, element.getWidth());
		return width;
	}

	/**
	 * Returns the height of the highest element in this {@link HCPElements} instance. Returns zero if this
	 * instance contains no elements.
	 * 
	 * @return the maximum height; 0 if empty
	 * @throws IOException if the {@link HCPElement#getHeight()} method of an element throws
	 */
	public float getMaxHeight() throws IOException {
		float height = 0;
		for (HCPElement element : elements)
			height = Math.max(height, element.getHeight());
		return height;
	}

	/**
	 * Returns the sum of the widths of all of the elements in this {@link HCPElements} instance. Returns zero
	 * if this instance contains no elements.
	 * 
	 * @return the total width; 0 if empty
	 * @throws IOException if the {@link HCPElement#getWidth()} method of an element throws
	 */
	public float getTotalWidth() throws IOException {
		float width = 0;
		for (HCPElement element : elements)
			width += element.getWidth();
		return width;
	}

	/**
	 * Returns the sum of the heights of all of the elements in this {@link HCPElements} instance. Returns
	 * zero if this instance contains not elements.
	 * 
	 * @return the total height; 0 if empty
	 * @throws IOException if the {@link HCPElement#getHeight()} method of an element throws
	 */
	public float getTotalHeight() throws IOException {
		float height = 0;
		for (HCPElement element : elements)
			height += element.getHeight();
		return height;
	}

	/**
	 * Creates an array of the widths of the elements in this {@link HCPElements} instance. The widths will be
	 * in the same order as the elements (as returned by {@link #get(int)}). Returns an empty array if this
	 * instance contains no elements.
	 * 
	 * @return an array of the widths; not null
	 * @throws IOException if the {@link HCPElement#getWidth()} method of an element throws
	 */
	public float[] getWidths() throws IOException {
		int count = elements.length;
		float[] widths = new float[count];
		for (int index = 0; index < count; index++)
			widths[index] = elements[index].getWidth();
		return widths;
	}

	/**
	 * Creates an array of the heights of the elements in this {@link HCPElements} instance. The heights will
	 * be in the same order as the elements (as returned by {@link #get(int)}). Returns an empty array if this
	 * instance contains no elements.
	 * 
	 * @return an array of the heights; not null
	 * @throws IOException if the {@link HCPElement#getHeight()} method of an element throws
	 */
	public float[] getHeights() throws IOException {
		int count = elements.length;
		float[] heights = new float[count];
		for (int index = 0; index < count; index++)
			heights[index] = elements[index].getHeight();
		return heights;
	}

	/**
	 * Returns the number of elements in this {@link HCPElements} instance.
	 * 
	 * @return the number of elements
	 */
	public int size() {
		return elements.length;
	}

	/**
	 * Returns the element at the given index.
	 * 
	 * @param index must be {@literal >= 0 and <} {@link #size()}
	 * @return the element at {@code index}
	 * @throws ArrayIndexOutOfBoundsException if index is out of bounds
	 */
	public HCPElement get(int index) {
		return elements[index];
	}

	@Override
	public Iterator<HCPElement> iterator() {
		return new Iterator<HCPElement>() {

			private int index = 0;

			@Override
			public HCPElement next() {
				if (index == elements.length)
					throw new NoSuchElementException();
				return elements[index++];
			}

			@Override
			public boolean hasNext() {
				return index < elements.length;
			}

		};
	}

}
