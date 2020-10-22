package Benchmarks;

import UTXOSet.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * Main class for benchmark testing of {@link UTXOSet}
 * and {@link UTXOSetProof} implementations.
 *
 * @author olegggatttor
 */
public class BenchmarkMain {
    /**
     * Main method.
     *
     * @param args - main arguments.
     */
    public static void main(String[] args) {
        final Options options = new OptionsBuilder()
                .include(UTXOSetTimeBenchmarkTest.class.getSimpleName())
                .forks(1)
                .build();

        try {
            new Runner(options).run();
        } catch (RunnerException ignored) {
            return;
        }
    }
}
