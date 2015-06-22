package eu.greenlightning.hypercubepdf.page;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

/**
 * Utility class used to create {@link HCPPage}s.
 * 
 * @author Green Lightning
 */
public final class HCPPages {

	/**
	 * Adds a new letter-sized (8.5 x 11 inch) portrait page to the document.
	 * 
	 * @param document not {@code null}
	 * @return the new page
	 * @throws NullPointerException if document is {@code null}
	 */
	public static HCPPage addPortraitPage(PDDocument document) {
		return new HCPPortraitPage(document);
	}

	/**
	 * Adds a new letter-sized (8.5 x 11 inch) landscape page to the document.
	 * 
	 * @param document not {@code null}
	 * @return the new page
	 * @throws NullPointerException if document is {@code null}
	 */
	public static HCPPage addLandscapePage(PDDocument document) {
		return new HCPLandscapePage(document);
	}


	/**
	 * Adds a new portrait page with the specified size to the document.
	 * 
	 * @param document not {@code null}
	 * @param size not {@code null}
	 * @return the new page
	 * @throws NullPointerException if document or size is {@code null}
	 */
	public static HCPPage addPortraitPage(PDDocument document, PDRectangle size) {
		return new HCPPortraitPage(document, size);
	}

	/**
	 * Adds a new landscape page with the specified size to the document.
	 * 
	 * @param document not {@code null}
	 * @param size not {@code null}
	 * @return the new page
	 * @throws NullPointerException if document or size is {@code null}
	 */
	public static HCPPage addLandscapePage(PDDocument document, PDRectangle size) {
		return new HCPLandscapePage(document, size);
	}

	// Prevent instantiation
	private HCPPages() {
		throw new UnsupportedOperationException();
	}

}
