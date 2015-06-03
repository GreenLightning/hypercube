package eu.greenlightning.hypercubepdf.page;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPage;

import eu.greenlightning.hypercubepdf.HCPElement;

public interface HCPPage {

	PDPage asPDPage();
	void paint(HCPElement element) throws IOException;

}
