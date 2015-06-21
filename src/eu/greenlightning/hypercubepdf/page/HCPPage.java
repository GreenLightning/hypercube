package eu.greenlightning.hypercubepdf.page;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPage;

import eu.greenlightning.hypercubepdf.HCPElement;

/**
 * Wrapper class for a {@link PDPage}.
 * <p>
 * {@link HCPPage}s are the connection between PDFBox's {@link PDPage}s and Hypercube's {@link HCPElement}s as they
 * allow you to paint elements (including element hierarchies stored in containers).
 *
 * @author Green Lightning
 * @see HCPPages
 */
public interface HCPPage {

	/**
	 * Returns the underlying {@link PDPage}.
	 * 
	 * @return the underlying {@link PDPage}
	 */
	PDPage asPDPage();

	/**
	 * Paints an {@link HCPElement} on this page. The element will cover the whole page. This method takes into account
	 * if the page is rotated.
	 * 
	 * @param element not {@code null}
	 * @throws NullPointerException if element is {@code null}
	 * @throws IOException if there is an error while painting
	 */
	void paint(HCPElement element) throws IOException;

}
