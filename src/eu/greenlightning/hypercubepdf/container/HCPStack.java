package eu.greenlightning.hypercubepdf.container;

import java.io.IOException;
import java.util.Collection;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import eu.greenlightning.hypercubepdf.HCPElement;

public class HCPStack implements HCPElement {

	private final HCPElements elements;

	public HCPStack(Collection<HCPElement> elements) {
		this.elements = new HCPElements(elements);
	}

	public HCPStack(HCPElement... elements) {
		this.elements = new HCPElements(elements);
	}

	@Override
	public float getWidth() throws IOException {
		return elements.getMaxWidth();
	}

	@Override
	public float getHeight() throws IOException {
		return elements.getMaxHeight();
	}

	@Override
	public void paint(PDPageContentStream content, PDRectangle shape) throws IOException {
		for (HCPElement element : elements) {
			element.paint(content, shape);
		}
	}

}
