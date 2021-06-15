package ecs.math;

public class Mathf {

    public static float lerp(float begin, float end, float ratio) {
        return begin + (end - begin) * ratio;
    }

    public static float lerpSmooth(float begin, float end, float beginSpeed, float endSpeed, float ratio) {
        return begin + (end - begin) * (beginSpeed + (endSpeed - beginSpeed) * ratio);
    }


}
