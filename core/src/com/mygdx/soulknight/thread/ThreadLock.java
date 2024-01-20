package com.mygdx.soulknight.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadLock {
    public static Lock updateLock = new ReentrantLock();
    public static Lock pauselock = new ReentrantLock();
    public static Lock clientlock = new ReentrantLock();
}
