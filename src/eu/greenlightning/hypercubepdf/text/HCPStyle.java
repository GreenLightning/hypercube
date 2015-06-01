package eu.greenlightning.hypercubepdf.text;

import java.awt.Color;
import java.io.IOException;
import java.util.Objects;

import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

public class HCPStyle {

	private final PDFont font;
	private final float size;
	private final Color color;

	public HCPStyle(PDFont font, float size) {
		this(font, size, Color.BLACK);
	}

	public HCPStyle(PDFont font, float size, Color color) {
		this.font = Objects.requireNonNull(font, "Font must not be null.");
		if (size < 1)
			throw new IllegalArgumentException("Size must be greater or equal to one, but was " + size + ".");
		this.size = size;
		this.color = Objects.requireNonNull(color, "Color must not be null.");
	}

	public PDFont getFont() {
		return font;
	}

	public float getSize() {
		return size;
	}

	public Color getColor() {
		return color;
	}

	public HCPStyle withFont(PDFont font) {
		return font == this.font ? this : new HCPStyle(font, size, color);
	}

	public HCPStyle withSize(float size) {
		return size == this.size ? this : new HCPStyle(font, size, color);
	}

	public HCPStyle withColor(Color color) {
		return color == this.color ? this : new HCPStyle(font, size, color);
	}

	public void apply(PDPageContentStream content) throws IOException {
		content.setNonStrokingColor(color);
		content.setFont(font, size);
	}

	public float getAscent() {
		return font.getFontDescriptor().getAscent() * size / 1000;
	}

	public float getDescent() {
		return font.getFontDescriptor().getDescent() * size / 1000;
	}

	public float getStringWidth(String text) throws IOException {
		return font.getStringWidth(text) * size / 1000;
	}

	public float getHeight() throws IOException {
		return font.getFontBoundingBox().getHeight() * size / 1000;
	}

}
