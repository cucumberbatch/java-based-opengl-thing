package ecs.utils;

import java.util.Stack;

public class Stopwatch {
    private static final Stack<Long> NANO_TIME_STACK = new Stack<>();

    public static void start() {
        NANO_TIME_STACK.add(System.nanoTime());
    }

    public static void stop(String messageTemplate) {
        float diffMillis = (float) (System.nanoTime() - NANO_TIME_STACK.pop()) / 1_000_000L;
        Logger.debug(String.format("Stopwatch: <bold>%.3f[ms]</> " + messageTemplate, diffMillis));
    }

}
