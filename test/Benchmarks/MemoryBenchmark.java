package Benchmarks;

import UTXOSet.*;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class MemoryBenchmark {
    private static int COIN_ID = 0;
    private static final Random random = new Random();
    private static final int SET_SIZE = 2_000_000;

    public static void main(String[] args) {
        NaiveUTXOSet naiveset = new NaiveUTXOSet();
        for (int i = 0; i < SET_SIZE; i++) {
            naiveset.add(genCoin());
        }
        printResult("Naive memory in bytes: ");
        naiveset = null;
        UTXOSet utxoSet;
        try {
            utxoSet = new UTXOSetImpl();
        } catch (NoSuchAlgorithmException ignored) {
            return;
        }
        for (int i = 0; i < SET_SIZE; i++) {
            utxoSet.add(genCoin());
        }
        printResult("Light UTXOSet memory in bytes: ");
        utxoSet = null;
        UTXOSetProof utxoSetProof;
        try {
            utxoSetProof = new UTXOSetProofImpl();
        } catch (NoSuchAlgorithmException ignored) {
            return;
        }
        for (int i = 0; i < SET_SIZE; i++) {
            utxoSetProof.add(genCoin());
        }
        printResult("Proof UTXOSet memory in bytes: ");
        utxoSetProof = null;
    }

    public static void printResult(final String message) {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println(message + memory);
    }

    public static String genCoin() {
        return ++COIN_ID + " " + random.nextInt();
    }

}
