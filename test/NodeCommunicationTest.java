import UTXOSet.ElementProof;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * Class for testing and simulation communication between nodes in Bitcoin network.
 * Class uses {@link Block} and {@link InvalidBlock} classes for testing of
 * sending valid and invalid blocks.
 *
 * @author olegggatttor
 * @see Node
 * @see Block
 * @see InvalidBlock
 */
public class NodeCommunicationTest {
    private final static int AMOUNT_OF_NODES = 10;
    private final static Node[] nodes = new Node[AMOUNT_OF_NODES];

    /**
     * Invokes before the first test for nodes initialization.
     */
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

    /**
     * Sends given {@link Block} to other nodes and asserts the expected result.
     *
     * Verifies if {@link Block} is accepted by other nodes and {@link InvalidBlock}
     * is rejected.
     *
     * @param from - the position of sender in nodes array.
     * @param block - block for sending.
     */
    public void sendBlock(final int from, Block block) {
        for (int i = 0; i != AMOUNT_OF_NODES; ++i) {
            if (i == from) {
                continue;
            }
            if (block instanceof InvalidBlock) {
                assertFalse(nodes[i].add(block));
            } else {
                assertTrue(nodes[i].add(block));
            }
        }
    }

    /**
     * Testing of sending valid {@link Block} to other nodes. Simulates simple
     * communication between nodes in Bitcoin network.
     */
    @Test
    public void sendingValidBlocks() {
        IntStream
                .range(0, AMOUNT_OF_NODES * AMOUNT_OF_NODES)
                .forEach(nodePos -> {
                    final Block block = nodes[nodePos % AMOUNT_OF_NODES].generate();
                    sendBlock(nodePos % AMOUNT_OF_NODES, block);
                });
    }

    /**
     * Testing of sending valid and invalid block between nodes in network.
     * {@link InvalidBlock} must be rejected and {@link Block} must be
     * accepted after verification.
     */
    @Test
    public void sendingInvalidBlocks() {
        final ArrayList<ElementProof> prevProofs = new ArrayList<>();
        IntStream
                .range(0, (AMOUNT_OF_NODES))
                .forEach(nodePos -> {
                    final Block validBlock = nodes[nodePos].generate();
                    final ArrayList<ElementProof> invalidProofs = validBlock.getProofs();
                    if(!invalidProofs.isEmpty()) {
                        final Random posGenerator = new Random();
                        final int proofPos = Math.abs(posGenerator.nextInt()) % invalidProofs.size();
                        final ElementProof proof = invalidProofs.get(proofPos);
                        if (!prevProofs.isEmpty()) {
                            invalidProofs.remove(proofPos);
                            invalidProofs.add(prevProofs.get(Math.abs(posGenerator.nextInt()) % prevProofs.size()));
                            final Block invalidBlock = new InvalidBlock(validBlock.getCoinsNew(), invalidProofs);
                            sendBlock(nodePos, invalidBlock);
                        } else {
                            sendBlock(nodePos, validBlock);
                        }
                        prevProofs.add(proof);
                    } else {
                        sendBlock(nodePos % AMOUNT_OF_NODES, validBlock);
                    }
                });
    }
}
