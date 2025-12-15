package org.north.core;

public class MainThread {
    public static void main(String[] args) throws Exception {
        new Engine().run();

//        new SystemPipeline.Builder()
//                .stage("init")
//                .stage("update")
//                .stage("render")
//                .build();
    }
}
