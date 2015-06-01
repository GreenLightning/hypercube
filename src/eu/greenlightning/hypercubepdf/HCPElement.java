package eu.greenlightning.hypercubepdf;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

public interface HCPElement {

	float getWidth() throws IOException;
	float getHeight() throws IOException;
	void paint(PDPageContentStream content, PDRectangle shape) throws IOException;

}
