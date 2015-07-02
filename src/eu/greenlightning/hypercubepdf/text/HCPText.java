package eu.greenlightning.hypercubepdf.text;

import java.awt.Color;
import java.io.IOException;
import java.util.Objects;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import eu.greenlightning.hypercubepdf.HCPElement;

/**
 * Base class for text elements. The text is always painted in the center of the element.
 * <p>
 * This class is immutable.
 *
 * @author Green Lightning
 */
public abstract class HCPText implements HCPElement {

	private static final boolean DEBUG = false;

	/**
	 * The text that this text element should paint. May include line breaks. This field is never {@code null}.
	 */
	protected final String text;

	/**
	 * The style to use for painting. This field is never {@code null}.
	 */
	protected final HCPStyle style;

	/**
	 * Creates a new {@link HCPText} instance using the specified text and style.
	 * 
	 * @param text not {@code null}
	 * @param style not {@code null}
	 * @throws NullPointerException if text or style is {@code null}
	 */
	public HCPText(String text, HCPStyle style) {
		this.text = Objects.requireNonNull(text, "Text must not be null.");
		this.style = Objects.requireNonNull(style, "Style must not be null.");
	}

	/**
	 * Returns an {@link HCPText} instance that paints the specified text, but has all other properties in common with
	 * this instance. This method may return {@code this} instance if it already uses the specified text.
	 * 
	 * @param newText not {@code null}
	 * @return an {@link HCPText} instance that paints the specified text
	 * @throws NullPointerException if newText is {@code null}
	 */
	public HCPText withText(String newText) {
		Objects.requireNonNull(newText, "NewText must not be null.");
		return this.text.equals(newText) ? this : createInstance(newText, style);
	}

	/**
	 * Returns an {@link HCPText} instance that uses the specified style, but has all other properties in common with
	 * this instance. This method may return {@code this} instance if it already uses the specified style.
	 * 
	 * @param newStyle not {@code null}
	 * @return an {@link HCPText} instance that uses the specified style
	 * @throws NullPointerException if newStyle is {@code null}
	 */
	public HCPText withStyle(HCPStyle newStyle) {
		Objects.requireNonNull(newStyle, "NewStyle must not be null.");
		return this.style.equals(newStyle) ? this : createInstance(text, newStyle);
	}

	/**
	 * Creates a new object of the same class as this object, using the same properties as this object but the specified
	 * text and style.
	 * 
	 * @param text not {@code null}
	 * @param style not {@code null}
	 * @return a new object of this class that uses the specified text and style
	 * @throws NullPointerException if text or style is {@code null}
	 */
	protected abstract HCPText createInstance(String text, HCPStyle style);

	@Override
	public final void paint(PDPageContentStream content, PDRectangle shape) throws IOException {
		float x = shape.getLowerLeftX() + (shape.getWidth() - getWidth()) / 2;
		float y = shape.getLowerLeftY() + (shape.getHeight() - getHeight()) / 2;
		paintDebug(content, x, y);
		paintText(content, x, y);
	}

	private void paintDebug(PDPageContentStream content, float x, float y) throws IOException {
		if (DEBUG) {
			content.setNonStrokingColor(invert(style.getColor()));
			content.fillRect(x, y, getWidth(), getHeight());
		}
	}

	private Color invert(Color color) {
		Color result = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
		if (inRange(result, 120, 128))
			return result.darker();
		if (inRange(result, 128, 136))
			return result.brighter();
		return result;
	}

	private boolean inRange(Color color, int start, int end) {
		return inRange(color.getRed(), start, end) && inRange(color.getGreen(), start, end) && inRange(color.getBlue(),
			start, end);
	}

	private boolean inRange(int value, int start, int end) {
		return value >= start && value < end;
	}

	/**
	 * Paints the text of this element at the specified position.
	 * 
	 * @param content not {@code null}
	 * @param x the x position to use
	 * @param y the y position to use
	 * @throws IOException if there is an error writing to the stream
	 */
	protected abstract void paintText(PDPageContentStream content, float x, float y) throws IOException;

}
