import UTXOSet.ElementProof;
import UTXOSet.UTXOSetImpl;
import UTXOSet.UTXOSetProof;
import UTXOSet.UTXOSetProofImpl;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimpleUTXOSetProofImplTest extends SimpleUTXOSetBaseTest {
    private UTXOSetProof utxoSetProof;
    private static String[] coins;
    private static final int AMOUNT_OF_COINS = 100;
    private final int AMOUNT_OF_TEST = 100;

    @BeforeClass
    public static void generateCoins() {
        coins = new String[AMOUNT_OF_COINS];
        Random coinGen = new Random();
        for (int i = 0; i < AMOUNT_OF_COINS; i++) {
            coins[i] = (String.valueOf(coinGen.nextInt()));
        }
    }

    @Before
    public void init() {
        try {
            utxoSet = new UTXOSetProofImpl();
            utxoSetProof = new UTXOSetProofImpl();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saveAndGetProof() {
        final List<Integer> order = IntStream
                .range(0, AMOUNT_OF_COINS)
                .boxed()
                .collect(Collectors.toList());
        for (int i = 0; i < AMOUNT_OF_TEST; i++) {
            try {
                utxoSet = new UTXOSetImpl();
                utxoSetProof = new UTXOSetProofImpl();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            Collections.shuffle(order);
            for(int coinPos : order) {
                final String coin = coins[coinPos];
                final ElementProof proof = utxoSetProof.add(coin);
                utxoSet.add(coin);
                utxoSetProof.saveProof(proof);
            }
            Collections.shuffle(order);
            for(int coinPos : order) {
                final String coin = coins[coinPos];
                final ElementProof proof = utxoSetProof.getProof(coin);

                assertTrue(utxoSetProof.verify(proof));
                assertTrue(utxoSetProof.verifyAndDelete(proof));
                assertFalse(utxoSetProof.verifyAndDelete(proof));

                assertTrue(utxoSet.verify(proof));
                assertTrue(utxoSet.verifyAndDelete(proof));
                assertFalse(utxoSet.verifyAndDelete(proof));
            }
        }
    }
}
