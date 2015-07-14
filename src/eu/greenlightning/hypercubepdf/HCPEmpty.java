package eu.greenlightning.hypercubepdf;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

/**
 * An empty, zero width and zero height {@link HCPElement} which does nothing when painted. Only useful as a
 * placeholder.
 * <p>
 * This class is immutable.
 * 
 * @author Green Lightning
 */
public enum HCPEmpty implements HCPElement {

	/** The only instance of this class. Avoids unnecessary object creation. */
	INSTANCE;

	@Override
	public float getWidth() {
		return 0;
	}

	@Override
	public float getHeight() {
		return 0;
	}

	@Override
	public void paint(PDPageContentStream content, PDRectangle shape) {}
	
	@Override
	public String toString() {
		return "[HCPEmpty]";
	}

}
