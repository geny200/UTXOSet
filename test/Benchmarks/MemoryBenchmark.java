package Benchmarks;

import UTXOSet.*;
import base.network.Node;
import Benchmarks.naive.UTXOSetNaive;

import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static org.junit.Assert.fail;

public class MemoryBenchmark {
    private static final long SET_SIZE = 40_000_000;
    private static final Runtime runtime = Runtime.getRuntime();

    private static long getMemoryUsage() {
        runtime.gc();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    private static String toMemory(long memory) {
        String[] name = {"byte", "Kb", "Mb", "Gb"};
        double mem = memory;
        for (int i = 0; i != name.length; ++i) {
            if (mem <= 1024)
                return String.format("%.2f %s", mem, name[i]);
            mem /= 1024;
        }
        return String.format("%.2f Tb", mem);
    }

    private static void printResult(String className, long total) {
        DecimalFormat dF = new DecimalFormat("", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        System.out.format(
                "memory test - %s (%s elem): %s;\n",
                className,
                dF.format(SET_SIZE),
                toMemory(total)
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
