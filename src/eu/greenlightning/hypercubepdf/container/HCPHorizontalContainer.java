package eu.greenlightning.hypercubepdf.container;

import java.io.IOException;
import java.util.*;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import eu.greenlightning.hypercubepdf.HCPElement;
import eu.greenlightning.hypercubepdf.layout.*;

class HCPHorizontalContainer implements HCPElement {

	private final HCPLayout layout;
	private final HCPElements elements;

	private HCPHorizontalContainer(HCPLayout layout, HCPElements elements){
		this.layout = Objects.requireNonNull(layout, "Layout must not be null.");
		this.elements = Objects.requireNonNull(elements, "Elements must not be null.");
	}
	
	public HCPHorizontalContainer(HCPLayout layout, Collection<? extends HCPElement> elements) {
		this(layout, new HCPElements(elements));
	}
	
	public HCPHorizontalContainer(HCPLayout layout, HCPElement... elements) {
		this(layout, new HCPElements(elements));
	}

	@Override
	public float getWidth() throws IOException {
		return layout.getSize(elements.getWidths());
	}

	@Override
	public float getHeight() throws IOException {
		return elements.getMaxHeight();
	}

	@Override
	public void paint(PDPageContentStream content, PDRectangle shape) throws IOException {
		HCPLayoutSpace space = new HCPLayoutSpace(shape.getLowerLeftX(), shape.getUpperRightX());
		HCPLayoutResult[] results = layout.apply(space, elements.getWidths());

		PDRectangle elementShape = new PDRectangle();
		elementShape.setLowerLeftY(shape.getLowerLeftY());
		elementShape.setUpperRightY(shape.getUpperRightY());

		for (int index = 0; index < results.length && index < elements.size(); index++) {
			elementShape.setLowerLeftX(results[index].getLow());
			elementShape.setUpperRightX(results[index].getHigh());
			elements.get(index).paint(content, elementShape);
		}
	}

}
