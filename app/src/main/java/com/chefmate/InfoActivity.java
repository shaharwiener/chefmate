package com.chefmate;

import android.os.Bundle;

public class InfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.info_activity, "");
        super.showPageLayout(false);

        super.showPageLayout(true);
    }
}