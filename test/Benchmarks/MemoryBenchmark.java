package Benchmarks;

import UTXOSet.*;
import base.network.Node;
import Benchmarks.naive.UTXOSetNaive;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.fail;

public class MemoryBenchmark {
    private static final long SET_SIZE = 2_000_000;
    private static final Runtime runtime = Runtime.getRuntime();

    private static long getMemoryUsage() {
        runtime.gc();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    private static String toMemory(long memory) {
        String[] name = {" byte", " Kb", " Mb", " Gb"};
        for (int i = 0; i != name.length - 1; ++i) {
            if (memory < 1024)
                return memory + name[i];
            memory /= 1024;
        }
        return memory + " Tb";
    }

    private static void printResult(String className, long total) {
        System.out.println("memory test - "
                + className
                + " (" + SET_SIZE + " elem): "
                + toMemory(total) + ";"
        );
    }

    private static void test(UTXOSet utxoSet) {
        long totalUsage = getMemoryUsage();

        for (int i = 0; i < SET_SIZE; i++) {
            utxoSet.add(Node.genCoin());
        }

        long setUsage = getMemoryUsage() - totalUsage;
        printResult(utxoSet.getClass().getSimpleName(), setUsage);
    }

    public static void main(String[] args) {
        runtime.gc();
        try {

            test(new UTXOSetNaive());
            test(new UTXOSetImpl());
            test(new UTXOSetProofImpl());

        } catch (NoSuchAlgorithmException e) {
            fail();
        }
    }
}
