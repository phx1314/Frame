package com.mdx.framework.handle;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.os.Handler;
import android.os.Message;

public class MHandler extends Handler {
    public String id;
    public HandleMsgLisnener msglisnener;
    public int staus = 0;

    public MHandler() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void sent(int type, Object obj) {
        Message message = new Message();
        message.what = 201;
        message.arg1 = type;
        message.obj = obj;
        this.sendMessageL(message);
    }

    public void sent(Object obj) {
        Message message = new Message();
        message.what = 201;
        message.arg1 = 0;
        message.obj = obj;
        this.sendMessageL(message);
    }

    public void reload(int[] typs) {
        Message message = new Message();
        message.what = 100;
        message.obj = typs;
        this.sendMessageL(message);
    }

    public boolean sendMessageL(Message msg) {
        return super.sendMessage(msg);
    }

    public void reload() {
        this.reload((int[]) null);
    }

    public void close() {
        this.sendEmptyMessage(0);
    }

    public void setMsglisnener(HandleMsgLisnener msglisnener) {
        this.msglisnener = msglisnener;
    }

    public synchronized void handleMessage(Message msg) {
        if (this.msglisnener != null) {
            this.msglisnener.onMessage(msg);
        }

    }

    public interface HandleMsgLisnener {
        void onMessage(Message var1);
    }
}
