import UTXOSet.ElementProof;
import UTXOSet.UTXOSet;
import org.junit.Test;

import UTXOSet.UTXOSetImpl;
import UTXOSet.UTXOSetProofImpl;

import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Base class for testing {@link UTXOSet} realizations.
 *
 * @author olegggatttor
 * @see UTXOSetImpl
 * @see UTXOSetProofImpl
 */
public abstract class SimpleUTXOSetBaseTest {
    protected final int COINS_SIZE = 100;
    protected UTXOSet utxoSet;

    /**
     * Simple test for addition to {@link UTXOSet} implementation.
     *
     * Verifies that the coin presents in set after addition.
     */
    @Test
    public void singleAdd() {
        final String coin = "123456789";
        final ElementProof proof = utxoSet.add(coin);
        assertTrue(utxoSet.verify(proof));
    }

    /**
     * Simple test with addition and deletion to/from {@link UTXOSet} implementation.
     *
     * Verifies that the coin presents in set after addition and
     * does not present after deletion.
     */
    @Test
    public void singleAddThenDelete() {
        final String coin = "123456789";
        final ElementProof proof = utxoSet.add(coin);
        final boolean deletionResult = utxoSet.verifyAndDelete(proof);
        assertTrue(deletionResult);
        assertFalse(utxoSet.verify(proof));
    }

    /**
     * Complex test with addition and deletion of many coins to/from {@link UTXOSet} implementation.
     *
     * Verifies that coin presents in set after addition and
     * does not present after deletion.
     */
    @Test
    public void multipleInteractions() {
        final String[] coins = new String[COINS_SIZE];
        final ElementProof[] proofs = new ElementProof[COINS_SIZE];

        for (int i = 0; i < COINS_SIZE; i++) {
            coins[i] = Node.genCoin();
            proofs[i] = utxoSet.add(coins[i]);
            assertTrue(utxoSet.verify(proofs[i]));
        }
        for (int i = COINS_SIZE - 1; i >= 0; i--) {
            assertTrue(utxoSet.verifyAndDelete(proofs[i]));
            assertFalse(utxoSet.verifyAndDelete(proofs[i]));
        }
    }
}
