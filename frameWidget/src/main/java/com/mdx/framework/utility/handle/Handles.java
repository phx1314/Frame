package com.mdx.framework.utility.handle;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.graphics.Bitmap;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Iterator;

public class Handles {
    public ArrayList<MHandler> HANDLES = new ArrayList();
    public Bitmap[][] MenuIcon;
    public String[] RadioListImg = new String[0];

    public Handles() {
    }

    public void add(MHandler handler) {
        this.HANDLES.add(handler);
    }

    public void remove(MHandler handler) {
        this.HANDLES.remove(handler);
    }

    public int size() {
        return this.HANDLES.size();
    }

    public Handler get(int ind) {
        return (Handler) this.HANDLES.get(ind);
    }

    public ArrayList<MHandler> get(String id) {
        ArrayList retn = new ArrayList();

        for (int i = 0; i < this.HANDLES.size(); ++i) {
            if (((MHandler) this.HANDLES.get(i)).id.equals(id)) {
                retn.add(this.HANDLES.get(i));
            }
        }

        return retn;
    }

    public ArrayList<MHandler> getEnd(String id) {
        ArrayList retn = new ArrayList();

        for (int i = 0; i < this.HANDLES.size(); ++i) {
            if (((MHandler) this.HANDLES.get(i)).id.endsWith(id)) {
                retn.add(this.HANDLES.get(i));
            }
        }

        return retn;
    }

    public void closeOne(String id) {
        Iterator var2 = this.get(id).iterator();
        if (var2.hasNext()) {
            MHandler handle = (MHandler) var2.next();
            handle.sendEmptyMessage(0);
        }

    }

    public void close(String id) {
        Iterator var2 = this.get(id).iterator();

        while (var2.hasNext()) {
            MHandler fhand = (MHandler) var2.next();
            fhand.sendEmptyMessage(0);
        }

    }

    public void closeAll() {
        Iterator var1 = this.HANDLES.iterator();

        while (var1.hasNext()) {
            MHandler fhand = (MHandler) var1.next();
            fhand.sendEmptyMessage(0);
        }

    }

    public void closeIds(String ids) {
        ids = "," + ids + ",";
        Iterator var2 = this.HANDLES.iterator();

        while (var2.hasNext()) {
            MHandler fhand = (MHandler) var2.next();
            if (ids != null && ids.indexOf("," + fhand.getId() + ",") >= 0) {
                fhand.sendEmptyMessage(0);
            }
        }

    }

    public void reloadAll(String ids, int[] typs) {
        String[] ides = ids.split(",");

        for (int i = 0; i < ides.length; ++i) {
            if (ides[i] != null && ides[i].length() > 0) {
                Iterator var5 = this.get(ides[i]).iterator();

                while (var5.hasNext()) {
                    MHandler hand = (MHandler) var5.next();
                    hand.reload(typs);
                }
            }
        }

    }

    public void reloadAll(String ids) {
        this.reloadAll(ids, (int[]) null);
    }

    public void reloadOne(String id) {
        this.reloadOne(id, (int[]) null);
    }

    public void reloadOne(String id, int[] typs) {
        Iterator var3 = this.get(id).iterator();

        while (var3.hasNext()) {
            MHandler fhand = (MHandler) var3.next();
            fhand.reload(typs);
        }

    }

    public void sentAll(String ids, int type, Object obj) {
        String[] ides = ids.split(",");

        for (int i = 0; i < ides.length; ++i) {
            if (ides[i] != null && ides[i].length() > 0) {
                Iterator var6 = this.get(ides[i]).iterator();

                while (var6.hasNext()) {
                    MHandler hand = (MHandler) var6.next();
                    hand.sent(type, obj);
                }
            }
        }

    }

    public void sentAll(int type, Object obj) {
        Iterator var1 = this.HANDLES.iterator();
        while (var1.hasNext()) {
            MHandler fhand = (MHandler) var1.next();
            fhand.sent(type, obj);
        }
    }

    public void close2one(String id) {
        ArrayList list = this.get(id);

        for (int i = 0; i < list.size() - 1; ++i) {
            MHandler mh = (MHandler) list.get(i);
            mh.close();
        }

    }

    public void closeWidthOut(String ids) {
        ids = "," + ids + ",";
        Iterator var2 = this.HANDLES.iterator();

        while (var2.hasNext()) {
            MHandler fhand = (MHandler) var2.next();
            if (ids != null && ids.indexOf("," + fhand.getId() + ",") < 0) {
                fhand.sendEmptyMessage(0);
            }
        }

    }
}
