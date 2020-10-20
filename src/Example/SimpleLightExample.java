package Example;

import UTXOSet.*;

import java.security.NoSuchAlgorithmException;

public class SimpleLightExample {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        UTXOSet set = new UTXOSetImpl();
        ElementProof proof = set.add("Hello world");
        System.out.println(set.verify(proof));                  //true

        ElementProof secondProof = set.add("Hello world");
        System.out.println(set.verify(proof));                  //false
        System.out.println(set.verify(secondProof));            //true

        System.out.println(set.verifyAndDelete(proof));         //false
        System.out.println(set.verifyAndDelete(secondProof));   //true
    }
}
