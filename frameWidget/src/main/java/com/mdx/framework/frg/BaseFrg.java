//
//  BaseFrg
//
//  Created by wchj on 2015-08-17 14:28:55
//  Copyright (c) wchj All rights reserved.

/**

 */

package com.mdx.framework.frg;

import android.os.Bundle;
import android.view.View;

import com.mdx.framework.activity.MFragment;

public abstract class BaseFrg extends MFragment implements View.OnClickListener {


    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initcreate(Bundle savedInstanceState) {
        super.initcreate(savedInstanceState);
    }


}
