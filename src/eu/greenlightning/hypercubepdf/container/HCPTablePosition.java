package eu.greenlightning.hypercubepdf.container;

import java.io.IOException;
import java.util.Objects;

import eu.greenlightning.hypercubepdf.HCPElement;

public class HCPTablePosition {

	private final HCPElement element;
	private final int x, y;
	private final int horizontalSpan, verticalSpan;

	/**
	 * Creates a new {@link HCPTablePosition} using the specified element and position.
	 * 
	 * @param element not {@code null}
	 * @param x must be {@literal >= 0}
	 * @param y must be {@literal >= 0}
	 * @throws NullPointerException if element is {@code null}
	 * @throws IllegalArgumentException if x or y contains an illegal value
	 */
	public HCPTablePosition(HCPElement element, int x, int y) {
		this(element, x, y, 1, 1);
	}

	/**
	 * Creates a new {@link HCPTablePosition} using the specified element, position and spans.
	 * 
	 * @param element not {@code null}
	 * @param x must be {@literal >= 0}
	 * @param y must be {@literal >= 0}
	 * @param horizontalSpan must be {@literal >= 1}
	 * @param verticalSpan must be {@literal >= 1}
	 * @throws NullPointerException if element is {@code null}
	 * @throws IllegalArgumentException if x, y, horizontalSpan or verticalSpan contains an illegal value
	 */
	public HCPTablePosition(HCPElement element, int x, int y, int horizontalSpan, int verticalSpan) {
		this.element = Objects.requireNonNull(element, "Element must not be null.");
		this.x = checkPosition(x, "X");
		this.y = checkPosition(y, "Y");
		this.horizontalSpan = checkSpan(horizontalSpan, "HorizontalSpan");
		this.verticalSpan = checkSpan(verticalSpan, "VerticalSpan");
	}

	private int checkPosition(int position, String name) {
		if (position < 0)
			throw new IllegalArgumentException(name + " must be equal to or greater than zero.");
		return position;
	}

	private int checkSpan(int span, String name) {
		if (span < 1)
			throw new IllegalArgumentException(name + " must be equal to or greater than one.");
		return span;
	}

	public HCPElement getElement() {
		return element;
	}

	public float getWidth() throws IOException {
		return element.getWidth();
	}

	public float getHeight() throws IOException {
		return element.getHeight();
	}

	public boolean isAt(int x, int y) {
		return this.x == x && this.y == y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean spans() {
		return spansHorizontally() || spansVertically();
	}

	public boolean spansHorizontally() {
		return horizontalSpan > 1;
	}

	public boolean spansVertically() {
		return verticalSpan > 1;
	}

	public int getHorizontalSpan() {
		return horizontalSpan;
	}

	public int getVerticalSpan() {
		return verticalSpan;
	}

}
