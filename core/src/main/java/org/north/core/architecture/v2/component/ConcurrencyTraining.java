package org.north.core.architecture.v2.component;

public class ConcurrencyTraining {
    public static class ReadWriteLock {
        private int readers;
        private int writers;
        private int writeRequests;

        // позволяет читать всем потокам, но запрещает запись
        public synchronized void lockForRead() throws InterruptedException {
            while (writers > 0 || writeRequests > 0) {
                wait();
            }
            readers++;
        }

        public synchronized void unlockForRead() {
            readers--;
            notifyAll();
        }

        // позволяет чтение и запись только для текущего потока
        public synchronized void lockForWrite() throws InterruptedException {
            writeRequests++;
            while (writers > 0 || readers > 0) {
                wait();
            }
            writeRequests--;
            writers++;
        }

        public synchronized void unlockForWrite() {
            writers--;
            notifyAll();
        }

    }
}
