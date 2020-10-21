import UTXOSet.ElementProof;

import java.util.ArrayList;

public class InvalidBlock extends Block {
    /**
     * Constructs Block by array of deleted coins with their
     * proofs and array new coins.
     *
     * @param coinsNew - {@link ArrayList} new coins.
     * @param proofs   - {@link ArrayList} of deleted coins
     */
    public InvalidBlock(ArrayList<String> coinsNew, ArrayList<ElementProof> proofs) {
        super(coinsNew, proofs);
    }
}