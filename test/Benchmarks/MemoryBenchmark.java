package Benchmarks;

import UTXOSet.*;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class MemoryBenchmark {
    private static int COIN_ID = 0;
    private static final Random random = new Random();
    private static final int SET_SIZE = 2_000_000;

    public static void main(String[] args) {
        printResult("Naive memory in Kb before init: ");
        NaiveUTXOSet naiveset = new NaiveUTXOSet();
        for (int i = 0; i < SET_SIZE; i++) {
            naiveset.add(genCoin());
        }
        printResult("Naive memory in Kb after init: ");
        naiveset = null;
        printResult("Light memory in Kb before init: ");
        UTXOSet utxoSet;
        try {
            utxoSet = new UTXOSetImpl();
        } catch (NoSuchAlgorithmException ignored) {
            return;
        }
        for (int i = 0; i < SET_SIZE; i++) {
            utxoSet.add(genCoin());
        }
        printResult("Light UTXOSet memory in Kb after init: ");
        utxoSet = null;
        System.gc();
        printResult("Proof memory in Kb before init: ");
        UTXOSetProof utxoSetProof;
        try {
            utxoSetProof = new UTXOSetProofImpl();
        } catch (NoSuchAlgorithmException ignored) {
            return;
        }
        for (int i = 0; i < SET_SIZE; i++) {
            utxoSetProof.add(genCoin());
        }
        printResult("Proof UTXOSet memory in Kb after init: ");
        utxoSetProof = null;
    }

    public static void printResult(final String message) {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println(message + (memory/(1024)));
    }

    public static String genCoin() {
        return ++COIN_ID + " " + random.nextInt();
    }

}
