import UTXOSet.ElementProof;
import UTXOSet.UTXOSetProofImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

public class NodeCommunicationTest {
    private final static int AMOUNT_OF_NODES = 10;
    private final static UTXOSetProofImpl[] nodes = new UTXOSetProofImpl[AMOUNT_OF_NODES];
    private final static Set<Integer> utxos = new HashSet<>();

    @BeforeClass
    public static void init() {
        final String[] initCoins = genCoins();
        for(int i = 0; i < nodes.length; i++) {
            try {
                nodes[i] = new UTXOSetProofImpl();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return;
            }
        }
        for(UTXOSetProofImpl node : nodes) {
            for(String coin : initCoins) {
                node.add(coin);
            }
        }
    }

    public static String[] genCoins() {
        final int amountOfCoins = 100;
        final String[] initCoins = new String[amountOfCoins];
        Random coinGen = new Random();
        for (int i = 0; i < amountOfCoins; i++) {
            int utxo = coinGen.nextInt();
            while (utxos.contains(utxo)) {
                utxo = coinGen.nextInt();
            }
            utxos.add(utxo);
            initCoins[i] = (String.valueOf(coinGen.nextInt()));
        }
        return initCoins;
    }

    public void sendBlock(final int from, final String[] coins, final ElementProof[] proofs) {
        for(int i = 0; i < AMOUNT_OF_NODES; i++) {
            if(i == from) {
                continue;
            }
            for (final String coin : coins) {
                nodes[i].add(coin);
            }
            for (final ElementProof proof : proofs) {
                assertTrue(nodes[i].verify(proof));
            }
        }
    }

    @Test
    public void testCommunication() {
        final List<Integer> order = IntStream
                .range(0, AMOUNT_OF_NODES)
                .boxed()
                .collect(Collectors.toList());
        for(int nodePos : order) {
            final String[] newCoins = genCoins();
            for(String coin : newCoins) {
                final ElementProof proof = nodes[nodePos].add(coin);
                nodes[nodePos].saveProof(proof);
            }
            final ElementProof[] proofs = new ElementProof[newCoins.length];
            for(int i = 0; i < proofs.length; i++) {
                proofs[i] = nodes[nodePos].getProof(newCoins[i]);
            }
            sendBlock(nodePos, newCoins, proofs);
        }
    }
}
