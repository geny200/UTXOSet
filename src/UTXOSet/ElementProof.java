package UTXOSet;

/**
 * Stores the presentation of proof.
 *
 * @author Geny200
 */
public interface ElementProof {
    /**
     * Returns the number of steps in this proof.
     *
     * @return the number of steps in this proof.
     */
    int size();

    /**
     * Returns the step at the specified position in this proof.
     *
     * @param index - index of the step to return.
     * @return the {@link StepProof} at the specified position in this proof.
     * @throws IndexOutOfBoundsException - if the index is out of range
     */
    StepProof get(int index);

    /**
     * Get the element for which we prove..
     *
     * @return {@link String} representation of wallet data.
     */
    String element();
}
