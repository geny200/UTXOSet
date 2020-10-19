package UTXOSet;

/**
 * Presentation of the proof step.
 *
 * @author Geny200
 */
public interface StepProof {

    /**
     * Get the hash of the current proof step.
     *
     * @return {@link byte[]} hash of the current neighbor.
     */
    byte[] hash();

    /**
     * Get the position of the current proof step.
     *
     * @return {@link boolean} True if the element's subtree is on the left,
     * False otherwise.
     */
    boolean isLeft();
}
