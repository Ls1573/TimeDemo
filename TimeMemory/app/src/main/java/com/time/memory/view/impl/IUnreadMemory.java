package com.time.memory.view.impl;

import com.time.memory.entity.Reminds;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/31.
 */
public interface IUnreadMemory  extends IBaseView {
    void setAdapter(ArrayList<Reminds> list);

    void refreshAdapter();

    void closeActivity();
}
