import UTXOSet.UTXOSetProofImpl;
import org.junit.Before;
import java.security.NoSuchAlgorithmException;

public class SimpleUTXOSetImplTest extends SimpleUTXOSetBaseTest {

    @Before
    public void setUp() {
        try {
            super.utxoSet = new UTXOSetProofImpl();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}