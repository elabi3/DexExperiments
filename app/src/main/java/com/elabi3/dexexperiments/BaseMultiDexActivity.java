package com.elabi3.dexexperiments;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.elabi3.dexexperiments.data.ApkInfo;
import com.elabi3.dexexperiments.utils.MultiDexClassLoader;
import com.elabi3.dexexperiments.utils.FilesUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by elabi3 on 4/2/16.
 **/
public class BaseMultiDexActivity extends AppCompatActivity {

    // Data

    public List<ApkInfo> modules = new ArrayList<>();


    /**************************************************************************
     * Setup Modules
     **************************************************************************/

    //region Setup Modules
    public void setupModulesFromPath(String input) {
        FilesUtils.createFolderIfNeeded(input);

        // Read external modules
        for (String file : FilesUtils.readFilesFromDir(input)) {
            FilesUtils.moveFile(input, getFilesDir().getAbsolutePath(), file);
            String packageName = FilesUtils.getPackageNameByAPK(this, getFilesDir().getAbsolutePath() + File.separator + file);
            modules.add(new ApkInfo(getFilesDir().getAbsolutePath() + File.separator + file, packageName + "MyFragment"));
        }

        for (ApkInfo apkInfo : modules) {
            registerModule(apkInfo.getPath());
        }
    }

    public void setupInternalModules() {
        // Read internal modules
        for (String file : FilesUtils.readFilesFromDir(getFilesDir().getAbsolutePath())) {
            String packageName = FilesUtils.getPackageNameByAPK(this, getFilesDir().getAbsolutePath() + File.separator + file);
            modules.add(new ApkInfo(getFilesDir().getAbsolutePath() + File.separator + file, packageName + "MyFragment"));
        }

        for (ApkInfo apkInfo : modules) {
            registerModule(apkInfo.getPath());
        }
    }

    public void registerModule(String moduleName) {
        MultiDexClassLoader.getInstance().install(this, moduleName);

        try {
            getAssets().getClass().getMethod("addAssetPath", String.class).invoke(getAssets(), moduleName);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Resources superRes = super.getResources();
        Resources res = new Resources(getAssets(), superRes.getDisplayMetrics(),
                superRes.getConfiguration());
        Resources.Theme theme = res.newTheme();
        theme.setTo(super.getTheme());
    }

    //endregion


    /**************************************************************************
     * Fragment Replacement
     **************************************************************************/

    //region Fragment Replacement
    public Fragment replaceFragment(String fragmentName, int id) {
        Fragment fragment = null;
        try {
            fragment = MultiDexClassLoader.getInstance().findFragment(fragmentName);
            getFragmentManager().beginTransaction().replace(id, fragment).commit();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fragment;
    }

    public Fragment replaceFragment(String fragmentName, Bundle bundle, int id) {
        Fragment fragment = null;
        try {
            fragment = MultiDexClassLoader.getInstance().findFragment(fragmentName);
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(id, fragment).commit();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fragment;
    }

    //endregion

}
