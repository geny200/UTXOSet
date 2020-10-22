package base.network;

import UTXOSet.ElementProof;

import java.util.ArrayList;

/**
 * Simulates block behavior.
 *
 * @author Geny200
 * @see Node
 * @see Node#generate()
 */
public class Block {
    public final static int MAX_SIZE = 20;
    private final ArrayList<String> coinsNew;
    private final ArrayList<ElementProof> proofs;

    /**
     * Constructs Block by array of deleted coins with their
     * proofs and array new coins.
     *
     * @param coinsNew - {@link ArrayList} new coins.
     * @param proofs   - {@link ArrayList} of deleted coins
     *                 with their proofs.
     */
    public Block(ArrayList<String> coinsNew, ArrayList<ElementProof> proofs) {
        this.coinsNew = coinsNew;
        this.proofs = proofs;
    }

    /**
     * Returns an array of deleted coins with their proofs.
     *
     * @return {@link ArrayList} of deleted coins with their
     * proofs.
     */
    public ArrayList<ElementProof> getProofs() {
        return proofs;
    }

    /**
     * Returns an array of new coins.
     *
     * @return {@link ArrayList} of new coins.
     */
    public ArrayList<String> getCoinsNew() {
        return coinsNew;
    }
}
