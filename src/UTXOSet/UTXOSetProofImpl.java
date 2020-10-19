package UTXOSet;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Implementation of {@link UTXOSetProof}
 *
 * @author Geny200
 * @see UTXOSetProof
 */
public class UTXOSetProofImpl extends UTXOSetImpl implements UTXOSetProof {
    final HashMap<String, ElementProofImpl> proofHashMap;
    final ArrayList<HashSet<ElementProofImpl>> proofArrayList;

    public UTXOSetProofImpl() throws NoSuchAlgorithmException {
        this.proofArrayList = new ArrayList<>();
        this.proofHashMap = new HashMap<>();
    }

    void addProof(int index, ElementProofImpl proof) {
        while (index >= proofArrayList.size()) {
            proofArrayList.add(null);
        }
        if (proofArrayList.get(index) == null)
            proofArrayList.set(index, new HashSet<>());
        proofArrayList.get(index).add(proof);
    }

    /**
     * Every time the tree changes, it is guaranteed that the
     * proofs added by this function remain correct. To get a
     * correct proof, use {@link UTXOSetProof#getProof(String)}.
     *
     * @param proof - {@link ElementProof} current correct
     *              proof.
     * @return - True if the item will be maintained,
     * false otherwise
     * @see UTXOSetProof#getProof(String)
     * @see UTXOSetProof#deleteProof(String)
     */
    @Override
    public boolean saveProof(ElementProof proof) {
        if (!verify(proof))
            return false;
        ElementProofImpl localProof = new ElementProofImpl(proof);
        proofHashMap.put(localProof.element(), localProof);
        addProof(localProof.size(), localProof);
        return true;
    }

    /**
     * After calling this function, the structure ceases to
     * maintain the proof for this wallet.
     *
     * @param coin - {@link String} representation of wallet
     *             data.
     * @see UTXOSetProof#saveProof(ElementProof)
     */
    @Override
    public void deleteProof(String coin) {
        ElementProofImpl localProof = proofHashMap.remove(coin);
        if (localProof == null)
            return;
        proofArrayList.get(localProof.size()).remove(localProof);
    }

    /**
     * Returns the correct proof of the item if it is maintain;
     * otherwise, it returns an invalid proof.
     *
     * @param coin - {@link String} representation of wallet
     *             data.
     * @return {@link ElementProof} proof of coin.
     * @see UTXOSetProof#saveProof(ElementProof)
     */
    @Override
    public ElementProof getProof(String coin) {
        ElementProofImpl localProof = proofHashMap.get(coin);
        if (localProof != null)
            return new ElementProofImpl(localProof);
        return new ElementProofImpl(coin);
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
        ElementProof proof = super.add(coin);

        while (proof.size() >= proofArrayList.size()) {
            proofArrayList.add(null);
        }

        byte[] curHash = leaf(proof.element());
        HashSet<ElementProofImpl> treeAcc = new HashSet<>();
        for (int i = 0; i != proof.size(); ++i) {
            StepProof curStep = proof.get(i);
            for (ElementProofImpl item : treeAcc) {
                item.pushRight(curStep.hash());
            }

            if (proofArrayList.get(i) != null) {
                for (ElementProofImpl item : proofArrayList.get(i)) {
                    item.pushLeft(curHash);
                }
                treeAcc.addAll(proofArrayList.get(i));
                proofArrayList.set(i, null);
            }

            curHash = merge(curHash, curStep.hash());
        }

        proofArrayList.set(proof.size(), treeAcc);
        return proof;
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
        if (!super.verify(proof))
            return false;

        while (proof.size() >= proofArrayList.size()) {
            proofArrayList.add(null);
        }

        ArrayList<HashSet<ElementProofImpl>> decomposite =
                decompose(new ElementProofImpl(proof), proofArrayList.get(proof.size()));
        HashSet<ElementProofImpl> treeAcc = new HashSet<>();
        byte[] curHash = null;

        for (int i = 0; i != proof.size(); ++i) {
            StepProof curStep = proof.get(i);
            if (curHash == null) {
                if (rootList.get(i) == null) {
                    proofArrayList.set(i, decomposite.get(i));
                    rootList.set(i, curStep.hash());
                } else {
                    for (ElementProofImpl item : decomposite.get(i)) {
                        item.pushRight(rootList.get(i));
                    }
                    treeAcc = decomposite.get(i);

                    if (proofArrayList.get(i) != null) {
                        for (ElementProofImpl item : proofArrayList.get(i)) {
                            item.pushLeft(curStep.hash());
                        }
                        treeAcc.addAll(proofArrayList.get(i));
                    }

                    curHash = merge(curStep.hash(), rootList.get(i));
                    rootList.set(i, null);
                    proofArrayList.set(i, null);
                }
            } else {
                for (ElementProofImpl item : treeAcc) {
                    item.pushLeft(curStep.hash());
                }
                for (ElementProofImpl item : decomposite.get(i)) {
                    item.pushRight(curHash);
                }
                treeAcc.addAll(decomposite.get(i));
                curHash = merge(curStep.hash(), curHash);
            }
        }

        proofArrayList.set(proof.size(), treeAcc);
        rootList.set(proof.size(), curHash);
        return true;
    }

    /**
     * Creates a decomposition of tree elements when its
     * leaf is deleted.
     *
     * @param proof      - {@link ElementProofImpl} proof of the
     *                   leaf being deleted.
     * @param levelProof - all proofs that are maintained in
     *                   this tree.
     * @return decomposition of tree elements.
     */
    ArrayList<HashSet<ElementProofImpl>> decompose(ElementProofImpl proof,
                                                   HashSet<ElementProofImpl> levelProof) {
        ArrayList<HashSet<ElementProofImpl>> result;
        if (proof.size() == 0) {
            result = new ArrayList<>();
            ElementProofImpl localProof = proofHashMap.remove(proof.element());
            if (localProof != null) {
                levelProof.remove(localProof);
            }
            return result;
        }

        if (levelProof == null) {
            result = new ArrayList<>();
            for (int i = 0; i != proof.size() - 1; ++i)
                result.add(new HashSet<>());
            return result;
        }


        HashSet<ElementProofImpl> leftAcc = new HashSet<>();
        for (ElementProofImpl item : levelProof) {
            if (item.back().isLeft() == proof.back().isLeft()) {
                leftAcc.add(item);
            }
            item.pop();
        }

        if (leftAcc.size() == 0) {
            result = new ArrayList<>();
            for (int i = 0; i != proof.size() - 1; ++i)
                result.add(new HashSet<>());
            result.add(levelProof);
            return result;
        }

        levelProof.removeAll(leftAcc);
        proof.pop();
        result = decompose(proof, leftAcc);
        result.add(levelProof);

        return result;
    }
}
