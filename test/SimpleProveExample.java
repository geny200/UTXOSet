import UTXOSet.*;

import java.security.NoSuchAlgorithmException;

public class SimpleProveExample {
    final static String MESSAGE_ONE = "Hello world";
    final static String MESSAGE_TWO = "Hello world2";
    final static String MESSAGE_THREE = "Hello world3";

    public static void main(String[] args) throws NoSuchAlgorithmException {
        UTXOSetProof set = new UTXOSetProofImpl();
        set.saveProof(set.add(MESSAGE_ONE));
        System.out.println(set.verify(set.getProof(MESSAGE_ONE)));              //true

        set.saveProof(set.add(MESSAGE_TWO));
        System.out.println(set.verify(set.getProof(MESSAGE_ONE)));              //true
        System.out.println(set.verify(set.getProof(MESSAGE_TWO)));              //true

        set.saveProof(set.add(MESSAGE_THREE));
        System.out.println(set.verify(set.getProof(MESSAGE_ONE)));              //true
        System.out.println(set.verify(set.getProof(MESSAGE_TWO)));              //true
        System.out.println(set.verify(set.getProof(MESSAGE_THREE)));            //true

        System.out.println(set.verifyAndDelete(set.getProof(MESSAGE_ONE)));     //true
        System.out.println(set.verifyAndDelete(set.getProof(MESSAGE_TWO)));     //true
        System.out.println(set.verifyAndDelete(set.getProof(MESSAGE_THREE)));   //true

        System.out.println(set.verifyAndDelete(set.getProof(MESSAGE_THREE)));   //false
    }
}
