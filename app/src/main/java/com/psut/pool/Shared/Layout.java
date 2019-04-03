package com.psut.pool.Shared;

import android.view.View;

public interface Layout extends View.OnClickListener {
    void layoutComponents();

    void getLayoutComponents();

    void onClickLayout();
}
