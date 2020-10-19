package UTXOSet;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Implementation of {@link UTXOSet}
 *
 * @author Geny200
 * @see UTXOSet
 */
public class UTXOSetImpl implements UTXOSet {
    protected final ArrayList<byte[]> rootList;
    final MessageDigest sha256;

    private static final byte[] BYTE_ZERO = {0};
    private static final byte[] BYTE_ONE = {1};
    private static final byte[] BYTE_TWO = {2};

    public UTXOSetImpl() throws NoSuchAlgorithmException {
        this.rootList = new ArrayList<>();
        this.sha256 = MessageDigest.getInstance("SHA-256");
    }

    protected byte[] leaf(String coin) {
        synchronized (sha256) {
            sha256.reset();
            sha256.update(BYTE_ZERO);
            sha256.update(coin.getBytes(StandardCharsets.UTF_8));
            return sha256.digest();
        }
    }

    protected byte[] merge(byte[] left, byte[] right) {
        synchronized (sha256) {
            sha256.reset();
            sha256.update(BYTE_ONE);
            sha256.update(left);
            sha256.update(BYTE_TWO);
            sha256.update(right);
            return sha256.digest();
        }
    }

    /**
     * Adds wallet to UTXO set.
     *
     * @param coin - {@link String} representation of wallet
     *             data.
     * @return {@link ElementProof} proof of coin.
     */
    @Override
    public ElementProof add(String coin) {
        ElementProofImpl proof = new ElementProofImpl(coin);
        byte[] currentHash = leaf(coin);

        for (int i = 0; i != rootList.size(); ++i) {
            if (rootList.get(i) == null) {
                rootList.set(i, currentHash);
                return proof;
            }
            proof.pushRight(rootList.get(i));
            currentHash = merge(currentHash, rootList.get(i));
            rootList.set(i, null);
        }

        rootList.add(currentHash);
        return proof;
    }

    /**
     * Adds array of wallet to UTXO set. It works the same as
     * {@link UTXOSet#add(String)} for each item separately.
     *
     * @param coins - array {@link String} representation of
     *              wallets data.
     * @see UTXOSet#add(String)
     */
    @Override
    public void add(String[] coins) {
        for (String coin : coins)   // trivial implementation
            add(coin);              // TODO make an implementation
    }

    /**
     * Verifies proof of the element's existence.
     *
     * @param proof - {@link ElementProof} the proof of the
     *              existence of the wallet.
     */
    @Override
    public boolean verify(ElementProof proof) {
        if (proof.size() > rootList.size())
            return false;
        byte[] currentHash = leaf(proof.element());

        for (int i = 0; i != proof.size(); ++i) {
            StepProof curStep = proof.get(i);
            if (curStep.isLeft())
                currentHash = merge(currentHash, curStep.hash());
            else
                currentHash = merge(curStep.hash(), currentHash);
        }
        return Arrays.equals(rootList.get(proof.size()), currentHash);
    }

    /**
     * Verifies proof of the element's existence and then
     * deletes it if it is validated.
     *
     * @param proof - {@link ElementProof} the proof of the
     *              existence of the wallet.
     * @return True if the verified element is deleted,
     * False otherwise.
     */
    @Override
    public boolean verifyAndDelete(ElementProof proof) {
        if (!verify(proof))
            return false;
        byte[] curHash = null;
        for (int i = 0; i != proof.size(); ++i) {
            StepProof curStep = proof.get(i);
            if (curHash == null) {
                if (rootList.get(i) == null) {
                    rootList.set(i, curStep.hash());
                } else {
                    curHash = merge(curStep.hash(), rootList.get(i));
                    rootList.set(i, null);
                }
            } else {
                curHash = merge(curStep.hash(), curHash);
            }
        }
        rootList.set(proof.size(), curHash);
        return true;
    }
}
