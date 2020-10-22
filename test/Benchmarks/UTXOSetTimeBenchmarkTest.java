package Benchmarks;

import UTXOSet.*;
import base.network.Node;
import Benchmarks.naive.UTXOSetNaive;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

/**
 * Benchmarks' test class for {@link UTXOSetNaive}, {@link UTXOSetImpl}
 * and {@link UTXOSetProofImpl} implementations.
 *
 * @author olegggatttor
 * @author Geny200
 */
@State(Scope.Thread)
public class UTXOSetTimeBenchmarkTest {
    private static final int SET_SIZE = 2_000_000;
    private ArrayList<String> coins;
    private UTXOSet utxoLightSet;
    private UTXOSetProof utxoProofSet;
    private UTXOSetNaive utxoNaiveSet;

    /**
     * Set up method.
     */
    @Setup
    public void setupCollections() {
        coins = new ArrayList<>();

        try {
            utxoNaiveSet = new UTXOSetNaive();
            utxoLightSet = new UTXOSetImpl();
            utxoProofSet = new UTXOSetProofImpl();
        } catch (NoSuchAlgorithmException ignored) {
            fail();
        }

        for (int i = 0; i < SET_SIZE; i++) {
            final String value = Node.genCoin();
            coins.add(value);
        }
    }

    /**
     * Testing method for {@link UTXOSetImpl}.
     *
     * @param blackhole - argument for consumption.
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void testUTXOSetImpl(Blackhole blackhole) {
        test(blackhole, utxoLightSet);
    }

    /**
     * Testing method for {@link UTXOSetProofImpl}.
     *
     * @param blackhole - argument for consumption.
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void testUTXOSetProofImpl(Blackhole blackhole) {
        test(blackhole, utxoProofSet);
    }

    /**
     * Testing method for {@link UTXOSetNaive}.
     *
     * @param blackhole - argument for consumption.
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void testNaiveUTXOSet(Blackhole blackhole) {
        test(blackhole, utxoNaiveSet);
    }

    private void test(Blackhole blackhole, UTXOSet utxoSet) {
        try {
            UTXOSetProof utxoProof = new UTXOSetProofImpl();

            for (String coin : coins) {
                blackhole.consume(utxoSet.add(coin));
                utxoProof.addAndSave(coin);
            }

            for (String coin : coins) {
                ElementProof proof = utxoProof.getProof(coin);
                blackhole.consume(utxoSet.verifyAndDelete(proof));
                utxoProof.verifyAndDelete(proof);
            }
        } catch (NoSuchAlgorithmException e) {
            fail();
        }
    }
}
