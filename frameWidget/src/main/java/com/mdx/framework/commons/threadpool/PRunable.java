package com.mdx.framework.commons.threadpool;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import com.mdx.framework.commons.CanIntermit;
import com.mdx.framework.commons.threadpool.ThreadPool;
import com.mdx.framework.commons.threadpool.ThreadPool.PThread;

public abstract class PRunable implements Runnable, CanIntermit {
    private PThread thread;
    private ThreadPool threadpool;
    protected boolean stoped = false;

    public PRunable() {
    }

    public void intermit() {
        this.onIntermit();
        if(this.thread != null) {
            this.thread.intermit();
            this.stoped = true;
        }

        if(this.threadpool != null) {
            this.threadpool.remove(this);
        }

    }

    public void onIntermit() {
    }

    public CanIntermit addIntermit(CanIntermit can) {
        if(this.thread != null) {
            this.thread.addIntermit(can);
        }

        return can;
    }

    public void setThread(PThread thread) {
        this.thread = thread;
    }

    public void setThreadpool(ThreadPool threadpool) {
        this.threadpool = threadpool;
    }
}
