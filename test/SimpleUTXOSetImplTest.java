import UTXOSet.UTXOSetProofImpl;
import org.junit.Before;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.fail;

public class SimpleUTXOSetImplTest extends SimpleUTXOSetBaseTest {

    @Before
    public void setUp() {
        try {
            super.utxoSet = new UTXOSetProofImpl();
        } catch (NoSuchAlgorithmException ignore) {
            fail();
        }
    }
}