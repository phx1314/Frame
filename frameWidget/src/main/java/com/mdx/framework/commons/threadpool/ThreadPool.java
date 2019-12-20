package com.mdx.framework.commons.threadpool;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import com.mdx.framework.commons.CanIntermit;
import com.mdx.framework.commons.threadpool.PRunable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ThreadPool {
    private List<PRunable> runing = Collections.synchronizedList(new ArrayList());
    private List<PRunable> watrun = Collections.synchronizedList(new ArrayList());
    private List<Thread> threads = Collections.synchronizedList(new ArrayList());
    private int maxThreadSize = 5;
    private ThreadPool.OnThreadEmpty onThreadEmpty;

    public ThreadPool() {
    }

    public ThreadPool(int maxThreadSize) {
        this.maxThreadSize = 5;
        this.maxThreadSize = maxThreadSize;
    }

    public void setMaxThreadSize(int size) {
        this.maxThreadSize = size;
        this.initThread();
    }

    public synchronized void execute(PRunable run) {
        this.watrun.add(run);
        run.setThreadpool(this);
        this.initThread();
    }

    public synchronized void intermitAll() {
        this.watrun.clear();
        List var1 = this.runing;
        synchronized(this.runing) {
            Iterator var2 = this.runing.iterator();

            while(true) {
                if(!var2.hasNext()) {
                    break;
                }

                PRunable run = (PRunable)var2.next();
                run.intermit();
            }
        }

        this.initThread();
    }

    public synchronized void submit(PRunable run) {
        this.execute(run);
    }

    private void initThread() {
        while(this.watrun.size() > 0 && this.threads.size() < this.maxThreadSize) {
            try {
                PRunable e = (PRunable)this.watrun.remove(0);
                this.runing.add(e);
                ThreadPool.PThread pt = new ThreadPool.PThread(e);
                this.threads.add(pt);
                pt.start();
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }

        if(this.watrun.size() == 0 && this.runing.size() == 0 && this.onThreadEmpty != null) {
            this.onThreadEmpty.onThreadEmpty();
        }

    }

    public void remove(PRunable run) {
        if(this.watrun.contains(run)) {
            this.watrun.remove(run);
        }

    }

    public List<PRunable> getRuning() {
        return this.runing;
    }

    public List<PRunable> getWatrun() {
        return this.watrun;
    }

    public int size() {
        return this.watrun.size() + this.runing.size();
    }

    public int runingSize() {
        return this.runing.size();
    }

    public void setOnThreadEmpty(ThreadPool.OnThreadEmpty onThreadEmpty) {
        this.onThreadEmpty = onThreadEmpty;
    }

    public interface OnThreadEmpty {
        void onThreadEmpty();
    }

    public class PThread extends Thread implements CanIntermit {
        private PRunable runable;
        private ArrayList<CanIntermit> canIntermits = new ArrayList();

        public PThread(PRunable runable) {
            this.runable = runable;
            if(this.runable != null) {
                this.runable.setThread(this);
            }

        }

        public void addIntermit(CanIntermit can) {
            this.canIntermits.add(can);
        }

        public void intermit() {
            this.interrupt();
            Iterator var1 = this.canIntermits.iterator();

            while(var1.hasNext()) {
                CanIntermit can = (CanIntermit)var1.next();
                can.intermit();
            }

        }

        public void run() {
            this.runRunAble(this.runable);

            while(ThreadPool.this.watrun.size() > 0) {
                try {
                    this.runable = (PRunable)ThreadPool.this.watrun.remove(0);
                } catch (Exception var2) {
                    var2.printStackTrace();
                }

                ThreadPool.this.runing.add(this.runable);
                this.runRunAble(this.runable);
            }

            ThreadPool.this.threads.remove(this);
        }

        public void runRunAble(Runnable runable) {
            try {
                if(runable != null) {
                    runable.run();
                }
            } catch (Exception var6) {
                var6.printStackTrace();
            } finally {
                this.canIntermits.clear();
                ThreadPool.this.runing.remove(runable);
            }

        }
    }
}
