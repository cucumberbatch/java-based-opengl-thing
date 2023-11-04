package ecs.managment;

public class FrameTiming {
    private static final int  NANO_TIME_STEP = 100;
    private static final long NANO_SECONDS_IN_SECOND = 1_000_000_000L;

    private long previousNanoTime;
    private long loopStartTimeInNano;
    private long elapsedTimeInNano;
    private long loopSlotInNano;

    private int  targetFrameRate;
    private int  actualFrameRate;

    public FrameTiming() {
        setTargetFrameRate(60);
        previousNanoTime = getCurrentNanoTime();
    }

    public void updateTiming() {
        loopStartTimeInNano = getCurrentNanoTime();
        elapsedTimeInNano   = loopStartTimeInNano - previousNanoTime;
        previousNanoTime    = loopStartTimeInNano;
        actualFrameRate     = (int) (NANO_SECONDS_IN_SECOND / elapsedTimeInNano);
    }

    public long getCurrentNanoTime() {
        return System.nanoTime();
    }

    public void sync() {
        long endTimeInNano = loopStartTimeInNano + loopSlotInNano - NANO_TIME_STEP;
        while (getCurrentNanoTime() < endTimeInNano) {
            try {
                Thread.sleep(0, NANO_TIME_STEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public float getElapsedTime() {
        return (float) this.elapsedTimeInNano / 1_000_000_000L;
    }

    public void setTargetFrameRate(int targetFrameRate) {
        if (targetFrameRate < 1) return;
        this.targetFrameRate = targetFrameRate;
        this.loopSlotInNano = NANO_SECONDS_IN_SECOND / targetFrameRate;
    }

    public int getActualFrameRate() {
        return this.actualFrameRate;
    }
}
