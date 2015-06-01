package eu.greenlightning.hypercubepdf.container;

import java.io.IOException;
import java.util.*;

import eu.greenlightning.hypercubepdf.HCPElement;

class HCPElements implements Iterable<HCPElement> {

	private final HCPElement[] elements;

	public HCPElements(HCPElement... elements) {
		this.elements = Objects.requireNonNull(elements, "Elements must not be null.").clone();
		checkElements();
	}

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

	public float getMaxWidth() throws IOException {
		float width = 0;
		for (HCPElement element : elements)
			width = Math.max(width, element.getWidth());
		return width;
	}

	public float getMaxHeight() throws IOException {
		float height = 0;
		for (HCPElement element : elements)
			height = Math.max(height, element.getHeight());
		return height;
	}

	public float getTotalWidth() throws IOException {
		float width = 0;
		for (HCPElement element : elements)
			width += element.getWidth();
		return width;
	}

	public float getTotalHeight() throws IOException {
		float height = 0;
		for (HCPElement element : elements)
			height += element.getHeight();
		return height;
	}

	public float[] getWidths() throws IOException {
		int count = elements.length;
		float[] widths = new float[count];
		for (int index = 0; index < count; index++)
			widths[index] = elements[index].getWidth();
		return widths;
	}

	public float[] getHeights() throws IOException {
		int count = elements.length;
		float[] heights = new float[count];
		for (int index = 0; index < count; index++)
			heights[index] = elements[index].getHeight();
		return heights;
	}

	public int size() {
		return elements.length;
	}

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
