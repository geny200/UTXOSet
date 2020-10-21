package Benchmarks;

import UTXOSet.ElementProof;
import UTXOSet.UTXOSet;
import UTXOSet.UTXOSetImpl;
import UTXOSet.UTXOSetProof;
import UTXOSet.UTXOSetProofImpl;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class UTXOSetTimeBenchmarkTest {
    private static int COIN_ID = 0;
    private static final Random random = new Random();
    private static final int SET_SIZE = 2_000_000;

    final ArrayList<ElementProof> proofs = new ArrayList<>();
    final ArrayList<String> coins = new ArrayList<>();
    private UTXOSet utxoSet;
    private UTXOSetProof utxoSetProof;
    private NaiveUTXOSet naiveSet;

    public UTXOSetTimeBenchmarkTest() {
    }

    public static String genCoin() {
        return ++COIN_ID + " " + random.nextInt();
    }


    @Setup
    public void setupCollections() {
        try {
            utxoSet = new UTXOSetImpl();
            utxoSetProof = new UTXOSetProofImpl();
        } catch (NoSuchAlgorithmException ignored) {
            System.out.println("ERROR");
            return;
        }
        naiveSet = new NaiveUTXOSet();
        for (int i = 0; i < SET_SIZE; i++) {
            final String value = genCoin();
            coins.add(value);
            naiveSet.add(value);
            utxoSet.add(value);
            utxoSetProof.addAndSave(value);
        }
        for (String coin : coins) {
            proofs.add(utxoSetProof.getProof(coin));
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void testUTXOSetImpl(Blackhole blackhole) {
        for(ElementProof proof : proofs) {
            blackhole.consume(utxoSet.verifyAndDelete(proof));
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void testUTXOSetProofImpl(Blackhole blackhole) {
        for(ElementProof proof : proofs) {
            blackhole.consume(utxoSetProof.verifyAndDelete(proof));
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void testNaiveUTXOSet(Blackhole blackhole) {
        for(ElementProof proof : proofs) {
            blackhole.consume(naiveSet.verifyAndDelete(proof.element()));
        }
    }
}
