package org.north.core.utils;

import java.util.Arrays;

public class Stopwatch {
    private static final LongStack NANO_TIME_STACK = new LongStack();

    public static void start() {
        NANO_TIME_STACK.add(System.nanoTime());
    }

    public static void stop(String messageTemplate) {
        float diffMillis = (float) (System.nanoTime() - NANO_TIME_STACK.pop()) / 1_000_000L;
        // Logger.debug(String.format("Stopwatch: <bold>%.3f[ms]</> " + messageTemplate, diffMillis));
    }

    public static class LongStack {
        private static final int DEFAULT_CAPACITY = 32;

        private long[] stack;
        private int index;
        private int capacity;

        public LongStack() {
            this(DEFAULT_CAPACITY);
        }

        public LongStack(int capacity) {
            this.capacity = capacity;
            this.index = 0;
            this.stack = new long[capacity];
        }

        public void add(long value) {
            if (index + 1 >= capacity) {
                capacity *= 2;
                stack = Arrays.copyOf(stack, capacity);
            }
            stack[index++] = value;
        }

        public long pop() {
            if (index - 1 < 0) {
                throw new IndexOutOfBoundsException("Stack is empty");
            }
            return stack[--index];
        }
    }

}
