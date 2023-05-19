package ecs.utils;

public class Stopwatch {
    // todo: better to add a stack structure to do nested measurement
    private static long timeInNanos;

    public static void start() {
        timeInNanos = System.nanoTime();
    }

    public static void stop(String messageTemplate) {
        float diffMillis = (float) (java.lang.System.nanoTime() - timeInNanos) / 1_000_000L;
        Logger.debug(String.format(messageTemplate, diffMillis));
    }

}
