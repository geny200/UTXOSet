package Benchmarks;

import java.util.HashSet;

public class NaiveUTXOSet {
    private final HashSet<String> coins;

    NaiveUTXOSet() {
        coins = new HashSet<>();
    }


    public void add(String coin) {
        coins.add(coin);
    }


    public boolean verifyAndDelete(String coin) {
        return coins.remove(coin);
    }


    public boolean verify(String coin) {
        return coins.contains(coin);
    }
}
