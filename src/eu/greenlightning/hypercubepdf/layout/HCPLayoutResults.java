package eu.greenlightning.hypercubepdf.layout;

public interface HCPLayoutResults {

	boolean hasNext();
	void next();
	void reset();
	int getIndex();
	float getLow();
	float getHigh();

}
