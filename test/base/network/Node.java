package base.network;

import UTXOSet.ElementProof;
import UTXOSet.UTXOSetProof;
import UTXOSet.UTXOSetProofImpl;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;

/**
 * Simulates the behavior of a node on the network.
 *
 * @author Geny200
 * @see Block
 */
public class Node {
    private static int COIN_ID = 0;
    private static final Random random = new Random();
    private final UTXOSetProof utxoSet;
    private final Queue<String> myCoins;

    /**
     * Creates a unique coin string.
     *
     * @return {@link String} unique coin.
     */
    public static String genCoin() {
        return ++COIN_ID + " " + random.nextInt();
    }

    /**
     * Constructs Node.
     *
     * @throws NoSuchAlgorithmException - If SHA256 was not found.
     */
    public Node() throws NoSuchAlgorithmException {
        this.utxoSet = new UTXOSetProofImpl();
        this.myCoins = new ArrayDeque<>();
    }

    /**
     * Simulates the "create block" action of the network node.
     *
     * @return {@link Block} generated data block.
     * @see Block
     */
    public Block generate() {
        ArrayList<ElementProof> blockProofs = new ArrayList<>();
        ArrayList<String> coinsAdd = new ArrayList<>();

        if (!myCoins.isEmpty()) {
            int size = Math.abs(random.nextInt()) % myCoins.size();

            for (int i = 0; i != size; ++i) {
                String curCoin = myCoins.poll();
                blockProofs.add(utxoSet.getProof(curCoin));

                utxoSet.verifyAndDelete(utxoSet.getProof(curCoin));
            }
        }

        int size = Math.abs(random.nextInt()) % Block.MAX_SIZE;

        for (int i = 0; i != size; ++i) {
            String curCoin = genCoin();

            coinsAdd.add(curCoin);
            utxoSet.addAndSave(curCoin);
            myCoins.add(curCoin);
        }

        return new Block(coinsAdd, blockProofs);
    }

    /**
     * Simulates the "verify block" action of the network node.
     *
     * @param block - the {@link Block} to be verified.
     * @return True if the block was verified, false otherwise.
     */
    public boolean add(Block block) {
        for (ElementProof item : block.getProofs()) {
            if (!utxoSet.verifyAndDelete(item))
                return false;
        }

        for (String coin : block.getCoinsNew()) {
            utxoSet.add(coin);
        }
        return true;
    }
}
