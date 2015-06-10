package eu.greenlightning.hypercubepdf.layout;

public interface HCPLayout {

	float getSize(float[] sizes);
	HCPLayoutResults apply(HCPLayoutSpace space, float[] sizes);

}
