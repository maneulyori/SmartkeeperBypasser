package org.maneulyori.smartkeeperbypasser;

import static de.robv.android.xposed.XposedHelpers.*;

import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class SmartkeeperBypasser implements IXposedHookLoadPackage {

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		if (!lpparam.packageName.equals("com.netcube.smartkeeper.child"))
			return;
		
		XposedBridge.log("We are in damn smartkeeper!");
/*
		XposedBridge.log("Hooking com.netcube.smartkeeper.child.service.a#run()");
		findAndHookMethod("com.netcube.smartkeeper.child.service.a",
				lpparam.classLoader, "run", new XC_MethodReplacement() {

					@Override
					protected Object replaceHookedMethod(MethodHookParam param)
							throws Throwable {
						while (true) {
							Thread.sleep(200L);
						}
					}
				});
	*/	
		try {
            Class c = Class.forName("com.netcube.smartkeeper.child");
            Method m[] = c.getDeclaredMethods();
            for (int i = 0; i < m.length; i++)
            XposedBridge.log(m[i].toString());
        }
        catch (Throwable e) {
            XposedBridge.log(e);
        }
		
		findAndHookMethod("com.netcube.smartkeeper.child.service.DeviceReceiver", 
				lpparam.classLoader, "onDisable", new XC_MethodReplacement() {

					@Override
					protected Object replaceHookedMethod(MethodHookParam param)
							throws Throwable {
						// TODO Auto-generated method stub
						return null;
					}
				
		});

		findAndHookMethod("com.netcube.smartkeeper.child.service.MainService",
				lpparam.classLoader, "h", new XC_MethodReplacement() {

					@Override
					protected Object replaceHookedMethod(MethodHookParam param)
							throws Throwable {
						return true;
					}
				});

		findAndHookMethod(
				"com.netcube.smartkeeper.child.service.MyStartupIntentReceiver",
				lpparam.classLoader, "onReceive", new XC_MethodReplacement() {

					@Override
					protected Object replaceHookedMethod(MethodHookParam param)
							throws Throwable {
						return null;
					}
				});
	}
}
