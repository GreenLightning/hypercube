package eu.greenlightning.hypercubepdf.text;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

/**
 * A text element which paints a single line of text rotated by 90° counterclockwise.
 * <p>
 * This class is immutable.
 * 
 * @author Green Lightning
 */
public class HCPSideText extends HCPText {

	/**
	 * Constructs a {@link HCPSideText} from a {@link String} and an {@link HCPStyle}.
	 * 
	 * @param text not {@code null}; line breaks are ignored
	 * @param style not {@code null}
	 * @throws NullPointerException if text or style is {@code null}
	 */
	public HCPSideText(String text, HCPStyle style) {
		super(text, style);
	}

	@Override
	protected HCPText createInstance(String text, HCPStyle style) {
		return new HCPSideText(text, style);
	}

	@Override
	public float getWidth() throws IOException {
		return style.getHeight();
	}

	@Override
	public float getHeight() throws IOException {
		return style.getStringWidth(text);
	}

	@Override
	protected void paintText(PDPageContentStream content, float x, float y) throws IOException {
		content.beginText();
		style.apply(content);
		content.setTextRotation(Math.PI / 2, x + getWidth() + style.getDescent(), y);
		content.drawString(text);
		content.endText();
	}

}
