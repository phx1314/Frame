//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.mdx.framework.utility;

import com.mdx.framework.utility.Util;

public abstract class MAsyncTask<Params, Progress, Result> {
    public MAsyncTask() {
    }

    public void execute(final Params... params) {
        (new Thread(new Runnable() {
            public void run() {
                final Object r = MAsyncTask.this.doInBackground(params);
                Util.post(new Runnable() {
                    public void run() {
                        MAsyncTask.this.finish((Result) r);
                    }
                });
            }
        })).start();
    }

    protected abstract Result doInBackground(Params... var1);

    public void finish(Result result) {
    }

    public void progressUpdate(final Progress... values) {
        Util.post(new Runnable() {
            public void run() {
                MAsyncTask.this.onProgressUpdate(values);
            }
        });
    }

    public void onProgressUpdate(Progress... values) {
    }
}
