package eu.greenlightning.hypercubepdf.layout;

/**
 * Represents a one-dimensional layout algorithm which is independent of the axis on which it is applied.
 *
 * @author Green Lightning
 */
public interface HCPLayout {

	/**
	 * Returns the total size that this layout would need to optimally lay out the specified elements. This method takes
	 * shrinking and stretching as well as gaps introduced by the layout into account.
	 * 
	 * @param sizes the sizes of the individual elements
	 * @return the total size needed to lay out these elements
	 */
	float getSize(float[] sizes);

	/**
	 * Applies this layout algorithm to the specified elements and places them in the specified layout space.
	 * 
	 * @param space the space in which the elements should be placed
	 * @param sizes the sizes of the individual elements
	 * @return an {@link HCPLayoutResults} instance containing the results of the layout process
	 */
	HCPLayoutResults apply(HCPLayoutSpace space, float[] sizes);

}
