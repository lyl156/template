package org.example.limiter;

public class Main {
    private static Factory globalFactory;

    public static void main(String[] args) {
        mustInit();

        try {
            Limiter limiter = globalFactory.genLimiter(true);
            System.out.println(limiter);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static void mustInit() {
        globalFactory = new Factory();
    }
}
