package ecs.util;

/**
 * Interface that allows an implemented object to switch its activity state
 *
 * @author cucumberbatch
 */
public interface Turntable {

    /**
     * Allows to check the activity of an object
     * @return a current state of an object
     */
    boolean isActive();

    /**
     *
     * @param activity
     */
    void setActivity(boolean activity);

    /**
     * A
     */
    void switchActivity();
}
