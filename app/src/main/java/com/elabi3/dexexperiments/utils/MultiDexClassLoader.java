package com.elabi3.dexexperiments.utils;

import android.app.Fragment;
import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;

import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;

public class MultiDexClassLoader extends ClassLoader {

    private static MultiDexClassLoader instance = new MultiDexClassLoader();
    private LinkedList<DexClassLoader> classLoaders;

    /**************************************************************************
     * Constructor
     *************************************************************************/

    //region Constructor
    public static MultiDexClassLoader getInstance() {
        return instance;
    }

    private MultiDexClassLoader() {
        super(ClassLoader.getSystemClassLoader());
        this.classLoaders = new LinkedList<>();
    }

    //endregion


    /**************************************************************************
     * Find classes
     **************************************************************************/

    //region Find classes
    @Override
    public Class<?> findClass(String className) throws ClassNotFoundException {
        Class<?> clazz = null;
        for (DexClassLoader classLoader : this.classLoaders) {
            try {
                clazz = classLoader.loadClass(className);
            } catch (ClassNotFoundException e) {
                continue;
            }
            if (clazz != null) {
                return clazz;
            }
        }

        throw new ClassNotFoundException(className + " in loader " + this);
    }

    public Fragment findFragment(String fragmentName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Fragment fragment = null;
        try {
            Class<Fragment> fragmentClass = (Class<Fragment>) MultiDexClassLoader.getInstance().loadClass("es.tecnocom.myapplication2.MyFragment");
            fragment = fragmentClass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException(fragmentName + " in loader " + this);
        } catch (InstantiationException e) {
            throw new InstantiationException(fragmentName + " in loader " + this);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessException(fragmentName + " in loader " + this);
        }
        return fragment;
    }

    //endregion


    /**************************************************************************
     * Auxiliar
     **************************************************************************/

    //region Auxiliar
    public void install(Context context, String jarPath) {
        File dexOutputDir = context.getDir("dex", 0);
        DexClassLoader dexClassLoader = new DexClassLoader(jarPath,
                dexOutputDir.getAbsolutePath(), null, context.getClassLoader());
        this.classLoaders.addLast(dexClassLoader);
    }

    public void printClasses(Context context, String jarPath) {
        try {
            DexFile dx = DexFile.loadDex(jarPath, File.createTempFile("opt", "dex", context.getCacheDir()).getPath(), 0);
            for (Enumeration<String> classNames = dx.entries(); classNames.hasMoreElements(); ) {
                String className = classNames.nextElement();
                System.out.println("class: " + className);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //endregion

}
