package UTXOSet;

/**
 * UTXO set with a Merkle tree-based cryptographic accumulator.
 *
 * @author Geny200
 */
public interface UTXOSet {

    /**
     * Adds wallet to UTXO set.
     *
     * @param coin - {@link String} representation of wallet
     *             data.
     * @return {@link ElementProof} proof of coin.
     */
    ElementProof add(String coin);

    /**
     * Verifies proof of the element's existence.
     *
     * @param proof - {@link ElementProof} the proof of the
     *              existence of the wallet.
     */
    boolean verify(ElementProof proof);

    /**
     * Verifies proof of the element's existence and then
     * deletes it if it is validated.
     *
     * @param proof - {@link ElementProof} the proof of the
     *              existence of the wallet.
     * @return True if the verified element is deleted,
     * False otherwise.
     */
    boolean verifyAndDelete(ElementProof proof);
}
