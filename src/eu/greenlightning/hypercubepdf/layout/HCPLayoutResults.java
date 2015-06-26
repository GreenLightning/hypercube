package eu.greenlightning.hypercubepdf.layout;

import java.util.NoSuchElementException;

/**
 * An iterator over the results of a layout process.
 * <p>
 * Each new instance starts <i>before</i> the first result. The availability of more results can be checked using
 * {@link #hasNext()}. After checking that a next result exists, it can safely be activated using the {@link #next()}
 * method and its properties can then be queried using the {@code getXXX()} methods of this class. This allows to
 * iterate over the results using this simple algorithm:
 * 
 * <pre>
 * {@code
 *     HCPLayoutResults results = ...;
 *     while (results.hasNext()) {
 *         results.next();
 *         ...
 *         result.getIndex();
 *         etc.
 *         ...
 *     }
 * }
 * </pre>
 * 
 * In case the results instance contains no results, the first call to next returns {@code false} and the code inside
 * the while loop is never executed.
 * 
 * @author Green Lightning
 */
public interface HCPLayoutResults {

	/**
	 * Checks whether more results are available.
	 * 
	 * @return {@code true} if at least one more result is available, {@code false} otherwise.
	 */
	boolean hasNext();

	/**
	 * Moves this iterator to the next result.
	 * 
	 * @throws NoSuchElementException if there are no more elements
	 */
	void next();

	/**
	 * Resets this instance.
	 */
	void reset();
	
	/**
	 * Returns the index of the element that the current result deals with.
	 * 
	 * @return the index of the element of the current result
	 * @throws IllegalStateException if no result is active, i.&nbsp;e. after this instance has been created and after a
	 *             call to {@link #reset()}, before a successful call to {@link #next()} has been made.
	 */
	int getIndex();
	
	/**
	 * Returns the smaller end point of the designated space for the element of the current result.
	 * 
	 * @return the smaller end point for the current element
	 * @throws IllegalStateException if no result is active, i.&nbsp;e. after this instance has been created and after a
	 *             call to {@link #reset()}, before a successful call to {@link #next()} has been made.
	 */
	float getLow();
	
	/**
	 * Returns the larger end point of the designated space for the element of the current result.
	 * 
	 * @return the larger end point for the current element
	 * @throws IllegalStateException if no result is active, i.&nbsp;e. after this instance has been created and after a
	 *             call to {@link #reset()}, before a successful call to {@link #next()} has been made.
	 */
	float getHigh();

}