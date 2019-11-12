//
//  BaseItem
//
//  Created by df on 2016-10-10 16:01:26
//  Copyright (c) df All rights reserved.


/**
   
*/

package com.mdx.framework.item;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

public class BaseItem implements OnClickListener {
	protected Context context;
	protected View contentview;

	@Override
	public void onClick(View v) {

	}

	public View findViewById(int id) {
         return this.contentview.findViewById(id);
    }

}

