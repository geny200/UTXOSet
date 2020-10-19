package UTXOSet;

import java.util.ArrayList;

/**
 * Implementation of {@link ElementProof}
 *
 * @author Geny200
 * @see ElementProof
 */
class ElementProofImpl implements ElementProof {
    final ArrayList<StepProof> stepList;
    final String coin;

    /**
     * Creates an empty proof based on the coin.
     *
     * @param coin - {@link String} representation of coin.
     */
    ElementProofImpl(String coin) {
        this.coin = coin;
        this.stepList = new ArrayList<>();
    }

    /**
     * Creates a copy of the proof.
     *
     * @param proof - {@link ElementProof} the original proof.
     */
    ElementProofImpl(ElementProof proof) {
        this.coin = proof.element();
        this.stepList = new ArrayList<>(proof.size());
        for (int i = 0; i != proof.size(); ++i) {
            stepList.add(proof.get(i));
        }
    }

    /**
     * Returns the last step of the proof.
     *
     * @return {@link StepProof} last step of the proof.
     */
    StepProof back() {
        return stepList.get(stepList.size() - 1);
    }

    /**
     * Deletes the last step of the proof.
     */
    void pop() {
        stepList.remove(stepList.size() - 1);
    }

    /**
     * Adds a proof step, with an insert on the right.
     *
     * @param left - hash of the right subtree.
     * @see ElementProofImpl#pushRight(byte[])
     */
    void pushLeft(byte[] left) {
        stepList.add(new StepProofImpl(left, false));
    }

    /**
     * Adds a proof step, with an insert on the left.
     *
     * @param right - hash of the left subtree.
     * @see ElementProofImpl#pushLeft(byte[])
     */
    void pushRight(byte[] right) {
        stepList.add(new StepProofImpl(right, true));
    }

    /**
     * Returns the number of steps in this proof.
     *
     * @return the number of steps in this proof.
     */
    @Override
    public int size() {
        return stepList.size();
    }

    /**
     * Returns the step at the specified position in this proof.
     *
     * @param index - index of the step to return.
     * @return the {@link StepProof} at the specified position
     * in this proof.
     * @throws IndexOutOfBoundsException - if the index is out
     *                                   of range
     */
    @Override
    public StepProof get(int index) {
        return stepList.get(index);
    }

    /**
     * Get the element for which we prove..
     *
     * @return {@link String} representation of wallet data.
     */
    @Override
    public String element() {
        return coin;
    }
}