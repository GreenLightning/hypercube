package eu.greenlightning.hypercubepdf.container;

import java.util.Collection;

import eu.greenlightning.hypercubepdf.HCPElement;
import eu.greenlightning.hypercubepdf.layout.*;

/**
 * Utility class for creating standard containers. Standard containers paint a group of {@link HCPElement}s
 * sequentially, applying a {@link HCPLayout} in one direction.
 * <p>
 * The elements are painted in the order in which they were provided while creating the container, as long as enough
 * space is available.
 * <p>
 * Horizontal containers paint their elements from left to right and vertical containers paint from top to bottom.
 * Right-to-left and bottom-to-top painting is not supported at the moment.
 * <p>
 * The width of the elements in horizontal containers depends on the layout, however the elements are always painted
 * using the height of the container. For vertical containers the opposite statement is true, i.&nbsp;e. the height of
 * the elements in vertical containers depends on the layout, however the elements are always painted using the width of
 * the container.
 *
 * @author Green Lightning
 */
public final class HCPContainers {

	public static HCPElement getHorizontalFlow(Collection<? extends HCPElement> elements) {
		return getHorizontalContainer(HCPFlowLayout.getInstance(), elements);
	}

	public static HCPElement getHorizontalFlow(HCPElement... elements) {
		return getHorizontalContainer(HCPFlowLayout.getInstance(), elements);
	}

	public static HCPElement getVerticalFlow(Collection<? extends HCPElement> elements) {
		return getVerticalContainer(HCPFlowLayout.getInstance(), elements);
	}

	public static HCPElement getVerticalFlow(HCPElement... elements) {
		return getVerticalContainer(HCPFlowLayout.getInstance(), elements);
	}

	public static HCPElement getHorizontalFlow(float spacing, Collection<? extends HCPElement> elements) {
		return getHorizontalContainer(HCPFlowLayout.getInstance(spacing), elements);
	}

	public static HCPElement getHorizontalFlow(float spacing, HCPElement... elements) {
		return getHorizontalContainer(HCPFlowLayout.getInstance(spacing), elements);
	}

	public static HCPElement getVerticalFlow(float spacing, Collection<? extends HCPElement> elements) {
		return getVerticalContainer(HCPFlowLayout.getInstance(spacing), elements);
	}

	public static HCPElement getVerticalFlow(float spacing, HCPElement... elements) {
		return getVerticalContainer(HCPFlowLayout.getInstance(spacing), elements);
	}

	public static HCPElement getHorizontalSplit(Collection<? extends HCPElement> elements) {
		return getHorizontalContainer(HCPSplitLayout.getInstance(), elements);
	}

	public static HCPElement getHorizontalSplit(HCPElement... elements) {
		return getHorizontalContainer(HCPSplitLayout.getInstance(), elements);
	}

	public static HCPElement getVerticalSplit(Collection<? extends HCPElement> elements) {
		return getVerticalContainer(HCPSplitLayout.getInstance(), elements);
	}

	public static HCPElement getVerticalSplit(HCPElement... elements) {
		return getVerticalContainer(HCPSplitLayout.getInstance(), elements);
	}

	public static HCPElement getHorizontalSplit(float spacing, Collection<? extends HCPElement> elements) {
		return getHorizontalContainer(HCPSplitLayout.getInstance(spacing), elements);
	}

	public static HCPElement getHorizontalSplit(float spacing, HCPElement... elements) {
		return getHorizontalContainer(HCPSplitLayout.getInstance(spacing), elements);
	}

	public static HCPElement getVerticalSplit(float spacing, Collection<? extends HCPElement> elements) {
		return getVerticalContainer(HCPSplitLayout.getInstance(spacing), elements);
	}

	public static HCPElement getVerticalSplit(float spacing, HCPElement... elements) {
		return getVerticalContainer(HCPSplitLayout.getInstance(spacing), elements);
	}

	public static HCPElement getHorizontalStretch(Collection<? extends HCPElement> elements) {
		return getHorizontalContainer(HCPStretchLayout.getInstance(), elements);
	}

	public static HCPElement getHorizontalStretch(HCPElement... elements) {
		return getHorizontalContainer(HCPStretchLayout.getInstance(), elements);
	}

	public static HCPElement getVerticalStretch(Collection<? extends HCPElement> elements) {
		return getVerticalContainer(HCPStretchLayout.getInstance(), elements);
	}

	public static HCPElement getVerticalStretch(HCPElement... elements) {
		return getVerticalContainer(HCPStretchLayout.getInstance(), elements);
	}

	public static HCPElement getHorizontalStretch(float spacing, Collection<? extends HCPElement> elements) {
		return getHorizontalContainer(HCPStretchLayout.getInstance(spacing), elements);
	}

	public static HCPElement getHorizontalStretch(float spacing, HCPElement... elements) {
		return getHorizontalContainer(HCPStretchLayout.getInstance(spacing), elements);
	}

	public static HCPElement getVerticalStretch(float spacing, Collection<? extends HCPElement> elements) {
		return getVerticalContainer(HCPStretchLayout.getInstance(spacing), elements);
	}

	public static HCPElement getVerticalStretch(float spacing, HCPElement... elements) {
		return getVerticalContainer(HCPStretchLayout.getInstance(spacing), elements);
	}

	public static HCPElement getHorizontalContainer(HCPLayout layout, Collection<? extends HCPElement> elements) {
		return new HCPHorizontalContainer(layout, elements);
	}

	public static HCPElement getHorizontalContainer(HCPLayout layout, HCPElement... elements) {
		return new HCPHorizontalContainer(layout, elements);
	}

	public static HCPElement getVerticalContainer(HCPLayout layout, Collection<? extends HCPElement> elements) {
		return new HCPVerticalContainer(layout, elements);
	}

	public static HCPElement getVerticalContainer(HCPLayout layout, HCPElement... elements) {
		return new HCPVerticalContainer(layout, elements);
	}

	// Prevent instantiation
	private HCPContainers() {
		throw new UnsupportedOperationException();
	}

}
