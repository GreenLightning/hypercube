package eu.greenlightning.hypercubepdf.text;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

/**
 * A simple text element, which paints a single line of text.
 * <p>
 * This class is immutable.
 * 
 * @author Green Lightning
 */
public class HCPNormalText extends HCPAbstractText {
	
	/**
	 * Constructs a {@link HCPNormalText} from a {@link String} and an {@link HCPStyle}.
	 * 
	 * @param text not {@code null}; line breaks are ignored
	 * @param style not {@code null}
	 * @throws NullPointerException if text or style is {@code null}
	 */
	public HCPNormalText(String text, HCPStyle style) {
		super(text, style);
	}

	@Override
	public float getWidth() throws IOException {
		return style.getStringWidth(text);
	}

	@Override
	public float getHeight() throws IOException {
		return style.getHeight();
	}

	@Override
	protected void paintText(PDPageContentStream content, float x, float y) throws IOException {
		content.beginText();
		style.apply(content);
		content.setTextTranslation(x, y - style.getDescent());
		content.drawString(text);
		content.endText();
	}

}
