package UTXOSet;

/**
 * Modified UTXO set with a Merkle tree-based cryptographic accumulator.
 * Maintains the retention of correct proofs. (All changes made will not
 * affect the correctness of the proof, only if it is not the removal
 * of the element that is proved by the proof.)
 *
 * @author Geny200
 * @see UTXOSet
 */
public interface UTXOSetProof extends UTXOSet {
    /**
     * Every time the tree changes, it is guaranteed that the
     * proofs added by this function remain correct. To get a
     * correct proof, use {@link UTXOSetProof#getProof(String)}.
     *
     * @param proof - {@link ElementProof} current correct
     *              proof.
     * @return - True if the item will be maintained,
     * false otherwise
     * @see UTXOSetProof#getProof(String)
     * @see UTXOSetProof#deleteProof(String)
     */
    boolean saveProof(ElementProof proof);

    /**
     * Adds an element to the tree and starts maintaining its
     * proof correct. Works as {@code saveProof(add(coin))}
     *
     * @param coin - {@link String} representation of wallet
     *             data.
     * @return {@link ElementProof} proof of coin.
     * @see UTXOSetProof#saveProof(ElementProof)
     * @see UTXOSet#add(String)
     */
    ElementProof addAndSave(String coin);

    /**
     * After calling this function, the structure ceases to
     * maintain the proof for this wallet.
     *
     * @param coin - {@link String} representation of wallet
     *             data.
     * @see UTXOSetProof#saveProof(ElementProof)
     */
    void deleteProof(String coin);

    /**
     * Returns the correct proof of the item if it is maintain;
     * otherwise, it returns an invalid proof.
     *
     * @param coin - {@link String} representation of wallet
     *             data.
     * @return {@link ElementProof} proof of coin.
     * @see UTXOSetProof#saveProof(ElementProof)
     */
    ElementProof getProof(String coin);
}
