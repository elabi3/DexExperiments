package com.elabi3.dexexperiments.data;

/**
 * Created by elabi3 on 4/2/16.
 **/
public class ApkInfo {
    private String path;
    private String fragmentName;

    public ApkInfo(String path, String fragmentName) {
        this.path = path;
        this.fragmentName = fragmentName;
    }

    public String getPath() {
        return path;
    }

    public String getFragmentName() {
        return fragmentName;
    }
}
