import UTXOSet.ElementProof;
import UTXOSet.UTXOSet;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class SimpleUTXOSetBaseTest {
    protected final int COINS_SIZE = 100;
    protected UTXOSet utxoSet;

    @Test
    public void singleAdd() {
        final String coin = "123456789";
        final ElementProof proof = utxoSet.add(coin);
        assertTrue(utxoSet.verify(proof));
    }

    @Test
    public void singleAddThenDelete() {
        final String coin = "123456789";
        final ElementProof proof = utxoSet.add(coin);
        final boolean deletionResult = utxoSet.verifyAndDelete(proof);
        assertTrue(deletionResult);
        assertFalse(utxoSet.verify(proof));
    }

    @Test
    public void multipleInteractions() {
        final String[] coins = new String[COINS_SIZE];
        final ElementProof[] proofs = new ElementProof[COINS_SIZE];
        final Random coinGen = new Random();

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
