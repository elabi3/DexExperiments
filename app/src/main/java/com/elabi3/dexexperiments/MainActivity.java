package com.elabi3.dexexperiments;

import android.os.Bundle;

import com.elabi3.dexexperiments.utils.Constants;

public class MainActivity extends BaseMultiDexActivity {


    /**************************************************************************
     * LifeCycle
     **************************************************************************/

    //region LifeCycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupModulesFromPath(Constants.EXTERNAL_READ_APK_DIR);
        setupInternalModules();

        super.onCreate(savedInstanceState);
        setContentView(com.elabi3.dexexperiments.R.layout.activity_main);

        replaceFragment(modules.get(0).getFragmentName(), com.elabi3.dexexperiments.R.id.content);
    }

    //endregion

}
