package eu.greenlightning.hypercubepdf.text;

import java.awt.Color;
import java.io.IOException;
import java.util.Objects;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import eu.greenlightning.hypercubepdf.HCPElement;

public abstract class HCPAbstractText implements HCPElement {

	private static final boolean DEBUG = false;

	protected final String text;
	protected final HCPStyle style;

	public HCPAbstractText(String text, HCPStyle style) {
		this.text = Objects.requireNonNull(text, "Text must not be null.");
		this.style = Objects.requireNonNull(style, "Style must not be null.");
	}

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
		return inRange(color.getRed(), start, end) && inRange(color.getGreen(), start, end)
				&& inRange(color.getBlue(), start, end);
	}

	private boolean inRange(int value, int start, int end) {
		return value >= start && value < end;
	}

	protected abstract void paintText(PDPageContentStream content, float x, float y) throws IOException;

}
