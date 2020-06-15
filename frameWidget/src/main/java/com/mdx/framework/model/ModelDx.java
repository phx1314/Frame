package com.mdx.framework.model;

import java.io.Serializable;

/**
 * Created by DELL on 2017/3/24.
 */

public class ModelDx implements Serializable {
    public boolean isChecked;
    public String string;
    public int id;

    public ModelDx(String string, int id) {
        this.string = string;
        this.id = id;
    }

    @Override
    public String toString() {
        return string;
    }
}
