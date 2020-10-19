package UTXOSet;

/**
 * Implementation of {@link StepProof}
 *
 * @author Geny200
 * @see StepProof
 */
class StepProofImpl implements StepProof {
    final byte[] hash;
    final boolean isLeft;

    /**
     * Constructs StepProofImpl by hash and boolean flag.
     *
     * @param hash   - hash
     * @param isLeft - True if the element's subtree is
     *               on the left, false otherwise.
     */
    StepProofImpl(byte[] hash, boolean isLeft) {
        this.hash = hash;
        this.isLeft = isLeft;
    }

    /**
     * Get the hash of the current proof step.
     *
     * @return {@link byte[]} hash of the current neighbor.
     */
    @Override
    public byte[] hash() {
        return hash;
    }

    /**
     * Get the position of the current proof step.
     *
     * @return {@link boolean} True if the element's subtree
     * is on the left, false otherwise.
     */
    @Override
    public boolean isLeft() {
        return isLeft;
    }
}
