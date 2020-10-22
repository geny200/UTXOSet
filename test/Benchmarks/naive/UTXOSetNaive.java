package Benchmarks.naive;

import UTXOSet.*;

import java.util.HashSet;

public class UTXOSetNaive implements UTXOSet {
    private final HashSet<String> coins;

    public UTXOSetNaive() {
        this.coins = new HashSet<>();
    }

    /**
     * Verifies proof of the element's existence and then
     * deletes it if it is validated.
     *
     * @param proof - {@link ElementProof} the proof of the
     *              existence of the wallet.
     * @return True if the verified element is deleted,
     * False otherwise.
     */
    @Override
    public boolean verifyAndDelete(ElementProof proof) {
        coins.remove(proof.element());
        return false;
    }

    /**
     * Adds wallet to UTXO set.
     *
     * @param coin - {@link String} representation of wallet
     *             data.
     * @return {@link ElementProof} proof of coin.
     */
    @Override
    public ElementProof add(String coin) {
        coins.add(coin);
        return null;
    }

    /**
     * Verifies proof of the element's existence.
     *
     * @param proof - {@link ElementProof} the proof of the
     *              existence of the wallet.
     */
    @Override
    public boolean verify(ElementProof proof) {
        return false;
    }

}
