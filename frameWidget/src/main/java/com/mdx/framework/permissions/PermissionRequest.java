package com.mdx.framework.permissions;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import com.mdx.framework.util.Helper;

public abstract class PermissionRequest {
    public PermissionRequest() {
    }

    public void onRequest(int code, String[] permissions, int[] grantResults) {
        boolean retn = true;
        if(code == 1) {
            int[] var5 = grantResults;
            int var6 = grantResults.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                int grant = var5[var7];
                if(0 != grant) {
                    retn = false;
                    break;
                }
            }
        }

        if(retn) {
            this.onGrant(permissions, grantResults);
        } else {
            this.onUngrant(permissions, grantResults);
        }

    }

    public abstract void onGrant(String[] var1, int[] var2);

    public void onUngrant(String[] permissions, int[] grantResults) {
        Helper.toast("部分权限被拒绝" );
    }
}
