package ecs.managment;

public class FrameTiming {
    private double previous;
    private double loopStartTime;
    private double elapsedTime;
    private int    targetFrameRate;
    private int    actualFrameRate;

    public void updateTiming() {
        loopStartTime   = getCurrentTime();
        elapsedTime     = loopStartTime - previous;
        previous        = loopStartTime;
        targetFrameRate = 60;
        actualFrameRate = (int) (1 / elapsedTime);
    }

    public double getCurrentTime() {
        return (double) System.currentTimeMillis() / 1_000L;
    }

    public void sync() {
        double loopSlot = 1.0d / targetFrameRate;
        double endTime = loopStartTime + loopSlot;
        while (getCurrentTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public float getElapsedTime() {
        return (float) this.elapsedTime;
    }

    public void setTargetFrameRate(int targetFrameRate) {
        if (targetFrameRate < 1) return;
        this.targetFrameRate = targetFrameRate;
    }

    public int getActualFrameRate() {
        return this.actualFrameRate;
    }
}
