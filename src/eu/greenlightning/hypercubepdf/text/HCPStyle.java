package eu.greenlightning.hypercubepdf.text;

import java.awt.Color;
import java.io.IOException;
import java.util.Objects;

import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

import eu.greenlightning.hypercubepdf.HCPUnits;

/**
 * A style for text consisting of the font, size and color to use when painting the text.
 * <p>
 * This class is immutable.
 *
 * @author Green Lightning
 */
public class HCPStyle {

	private final PDFont font;
	private final float size;
	private final Color color;

	/**
	 * Constructs a style with black color and the specified font and size.
	 * 
	 * @param font not {@code null}
	 * @param size must be {@literal >= 1}
	 * @throws NullPointerException if font is {@code null}
	 * @throws IllegalArgumentException if size {@code < 1}
	 */
	public HCPStyle(PDFont font, float size) {
		this(font, size, Color.BLACK);
	}

	/**
	 * Constructs a style with the specified font, size and color.
	 * 
	 * @param font not {@code null}
	 * @param size must be {@literal >= 1}
	 * @param color not {@code null}
	 * @throws NullPointerException if font or color is {@code null}
	 * @throws IllegalArgumentException if size {@literal < 1}
	 */
	public HCPStyle(PDFont font, float size, Color color) {
		this.font = Objects.requireNonNull(font, "Font must not be null.");
		if (size < 1)
			throw new IllegalArgumentException("Size must be greater or equal to one, but was " + size + ".");
		this.size = size;
		this.color = Objects.requireNonNull(color, "Color must not be null.");
	}

	/**
	 * Returns the font of this style.
	 * 
	 * @return the font of this style
	 */
	public PDFont getFont() {
		return font;
	}

	/**
	 * Returns the size of this style.
	 * 
	 * @return the size of this style
	 */
	public float getSize() {
		return size;
	}

	/**
	 * Returns the color of this style.
	 * 
	 * @return the color of this style
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Returns an {@link HCPStyle} instance that uses the specified font, but has all other properties in common
	 * with this instance. This method may return {@code this} instance if it already uses the specified font.
	 * 
	 * @param font not {@code null}
	 * @return an {@link HCPStyle} instance that uses the specified font
	 * @throws NullPointerException if font is {@code null}
	 */
	public HCPStyle withFont(PDFont font) {
		return this.font.equals(font) ? this : new HCPStyle(font, size, color);
	}

	/**
	 * Returns an {@link HCPStyle} instance that uses the specified size, but has all other properties in common
	 * with this instance. This method may return {@code this} instance if it already uses the specified size.
	 * 
	 * @param size must be {@literal >= 1}
	 * @return an {@link HCPStyle} instance that uses the specified size
	 * @throws IllegalArgumentException if size {@literal < 1}
	 */
	public HCPStyle withSize(float size) {
		return this.size == size ? this : new HCPStyle(font, size, color);
	}

	/**
	 * Returns an {@link HCPStyle} instance that uses the specified color, but has all other properties in common
	 * with this instance. This method may return {@code this} instance if it already uses the specified color.
	 * 
	 * @param color not {@code null}
	 * @return an {@link HCPStyle} instance that uses the specified color
	 * @throws NullPointerException if color is {@code null}
	 */
	public HCPStyle withColor(Color color) {
		return this.color.equals(color) ? this : new HCPStyle(font, size, color);
	}

	/**
	 * Configures the specified {@link PDPageContentStream} so that any subsequent text operations will be performed
	 * using the font, size and color defined by this style.
	 * 
	 * @param content the stream to set up; not {@code null}
	 * @throws IOException if an IO error occurs while writing to the stream
	 */
	public void apply(PDPageContentStream content) throws IOException {
		content.setNonStrokingColor(color);
		content.setFont(font, size);
	}

	/**
	 * Calculates the ascent of this style in default user space units.
	 * 
	 * @return the ascent in default user space units
	 * @see HCPUnits Default User Space
	 */
	public float getAscent() {
		return font.getFontDescriptor().getAscent() * size / 1000;
	}

	/**
	 * Calculates the descent of this style in default user space units.
	 * 
	 * @return the descent in default user space units
	 * @see HCPUnits Default User Space
	 */
	public float getDescent() {
		return font.getFontDescriptor().getDescent() * size / 1000;
	}

	/**
	 * Calculates the width of the specified text in default user space units.
	 * 
	 * @param text not {@code null}
	 * @return the width in default user space units
	 * @throws NullPointerException if text is {@code null}
	 * @throws IOException if there is an error getting the width information
	 * @see HCPUnits Default User Space
	 */
	public float getStringWidth(String text) throws IOException {
		return font.getStringWidth(text) * size / 1000;
	}

	/**
	 * Calculates the height of this style in default user space units.
	 * 
	 * @return the height in default user space units
	 * @throws IOException if there is an error calculating the height
	 */
	public float getHeight() throws IOException {
		return font.getFontBoundingBox().getHeight() * size / 1000;
	}

	@Override
	public boolean equals(Object object) {
		if (object == this)
			return true;
		if (!(object instanceof HCPStyle))
			return false;
		HCPStyle style = (HCPStyle) object;
		if (Float.compare(size, style.getSize()) != 0)
			return false;
		if (!color.equals(style.getColor()))
			return false;
		if (!font.equals(style.getFont()))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + Float.floatToIntBits(size);
		result = 31 * result + color.hashCode();
		result = 31 * result + font.hashCode();
		return result;
	}

}
