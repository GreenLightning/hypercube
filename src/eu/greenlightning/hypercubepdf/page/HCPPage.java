package eu.greenlightning.hypercubepdf.page;

import java.io.IOException;

import eu.greenlightning.hypercubepdf.HCPElement;

public interface HCPPage {

	void paint(HCPElement element) throws IOException;

}
