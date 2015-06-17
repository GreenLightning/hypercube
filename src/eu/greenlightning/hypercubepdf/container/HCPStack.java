package eu.greenlightning.hypercubepdf.container;

import java.io.IOException;
import java.util.Collection;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import eu.greenlightning.hypercubepdf.HCPElement;

/**
 * Paints multiple {@link HCPElement}s on top of each other. The elements are painted in the order they are
 * provided in, i.&nbsp;e. the first element is painted first and the last element is painted last
 * (bottom-to-top ordering).
 * <p>
 * The width of the widest and the height of the highest element are used as the size of the stack.
 * <p>
 * This class is immutable.
 * 
 * @author Green Lightning
 */
public class HCPStack implements HCPElement {

	private final HCPElements elements;

	/**
	 * Creates an {@link HCPStack} from a {@link Collection} of elements.
	 * 
	 * @param elements not {@code null}; must not contain {@code null}
	 * @throws NullPointerException if elements is {@code null}
	 * @throws IllegalArgumentException if elements contains {@code null}
	 */
	public HCPStack(Collection<HCPElement> elements) {
		this.elements = new HCPElements(elements);
	}

	/**
	 * Creates an {@link HCPStack} instance from an array or from a varargs list of elements.
	 * 
	 * @param elements not {@code null}; must not contain {@code null}
	 * @throws NullPointerException if elements is {@code null}
	 * @throws IllegalArgumentException if elements contains {@code null}
	 */
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
