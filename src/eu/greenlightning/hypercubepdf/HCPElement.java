package eu.greenlightning.hypercubepdf;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

/**
 * General purpose element which has a size and can paint itself onto a page.
 *
 * @author Green Lightning
 */
public interface HCPElement {

	/**
	 * Returns the width of this element.
	 * <p>
	 * In general, this should be the minimal / optimal / preferred width of this element.
	 * 
	 * @return the width of this element
	 * @throws IOException if an error occurs
	 */
	float getWidth() throws IOException;

	/**
	 * Returns the height of this element.
	 * <p>
	 * In general, this should be the minimal / optimal / preferred height of this element.
	 * 
	 * @return the height of this element
	 * @throws IOException if an error occurs
	 */
	float getHeight() throws IOException;

	/**
	 * Paints this element to the specified {@link PDPageContentStream}.
	 * <p>
	 * Shape defines the position and size which should be used for painting. If the size is less than specified by
	 * {@link #getWidth()} and {@link #getHeight()} then this method does not guarantee that (a) the requested size is
	 * used or (b) the element is painted correctly or (c) the element is painted at all.
	 * 
	 * @param content not {@code null}
	 * @param shape not {@code null}
	 * @throws NullPointerException if content or shape is {@code null}
	 * @throws IOException if there is an error writing to the stream
	 */
	void paint(PDPageContentStream content, PDRectangle shape) throws IOException;

}
