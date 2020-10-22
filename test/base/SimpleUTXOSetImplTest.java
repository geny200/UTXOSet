package base;

import UTXOSet.UTXOSetImpl;
import UTXOSet.UTXOSet;
import org.junit.Before;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.fail;

/**
 * Test class for {@link UTXOSetImpl}.
 *
 * @author olegggatttor
 * @see SimpleUTXOSetBaseTest
 * @see UTXOSet
 * @see UTXOSetImpl
 */
public class SimpleUTXOSetImplTest extends SimpleUTXOSetBaseTest {

    /**
     * Invokes before every test from {@link SimpleUTXOSetBaseTest} and initialize UTXOSet.
     */
    @Before
    public void setUp() {
        try {
            super.utxoSet = new UTXOSetImpl();
        } catch (NoSuchAlgorithmException ignore) {
            fail();
        }
    }
}