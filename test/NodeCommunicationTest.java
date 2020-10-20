import org.junit.BeforeClass;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.util.stream.IntStream;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class NodeCommunicationTest {
    private final static int AMOUNT_OF_NODES = 10;
    private final static Node[] nodes = new Node[AMOUNT_OF_NODES];

    @BeforeClass
    public static void init() {
        try {
            for (int i = 0; i != nodes.length; ++i) {
                nodes[i] = new Node();
            }
        } catch (NoSuchAlgorithmException ignore) {
            fail();
        }
    }

    public void sendBlock(final int from, Block block) {
        for (int i = 0; i != AMOUNT_OF_NODES; ++i) {
            if (i == from) {
                continue;
            }
            assertTrue(nodes[i].add(block));
        }
    }

    @Test
    public void testCommunication() {
        IntStream
                .range(0, AMOUNT_OF_NODES)
                .map(i -> i % AMOUNT_OF_NODES)
                .boxed()
                .forEach(nodePos -> {
                    Block block = nodes[nodePos].generate();
                    sendBlock(nodePos, block);
                });
    }
}
