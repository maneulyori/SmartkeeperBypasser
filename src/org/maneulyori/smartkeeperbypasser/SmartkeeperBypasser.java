package org.maneulyori.smartkeeperbypasser;

import static de.robv.android.xposed.XposedHelpers.*;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class SmartkeeperBypasser implements IXposedHookLoadPackage {

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		if (!lpparam.packageName.equals("com.netcube.smartkeeper.child"))
			return;

		final LoadPackageParam finalLpparam = lpparam;

		XposedBridge.log("We are in damn smartkeeper!");

		/*
		 * XposedBridge.log("Hooking com.netcube.smartkeeper.child.service.a#run()"
		 * ); findAndHookMethod("com.netcube.smartkeeper.child.service.a",
		 * lpparam.classLoader, "run", new XC_MethodReplacement() {
		 * 
		 * @Override protected Object replaceHookedMethod(MethodHookParam param)
		 * throws Throwable { while (true) { Thread.sleep(200L); } } });
		 */

		XC_MethodHook flagger = new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param)
					throws Throwable {
				XposedBridge.log(Arrays.toString(param.args));
				if (param.args.length >= 2) {
					XposedBridge.log(param.args[1].toString());
					XposedBridge
							.log(""
									+ findClass(
											"com.netcube.smartkeeper.child.activity.ActivityStatus",
											finalLpparam.classLoader)
											.toString().equals(
													param.args[1].toString()));
				}
				if (param.args.length >= 2) {
					if(findClass(
								"com.netcube.smartkeeper.child.activity.ActivityStatus",
								finalLpparam.classLoader).toString().equals(
								param.args[1].toString())) {
					XposedBridge.log("Hello, Screen? I wanna disable you.");
					param.args[1] = Activity.class;
					}
				}
			}

			@Override
			protected void afterHookedMethod(MethodHookParam param)
					throws Throwable {
				// Intent intent = (Intent) param.thisObject;
				// intent.addFlags(0x00002000);
			}
		};

		XposedBridge.hookAllConstructors(android.content.Intent.class, flagger);

		final LoadPackageParam finalParam = lpparam;
		findAndHookMethod(
				"com.netcube.smartkeeper.child.service.DeviceReceiver",
				lpparam.classLoader, "onDisabled",
				android.content.Context.class, android.content.Intent.class,
				new XC_MethodReplacement() {

					@Override
					protected Object replaceHookedMethod(MethodHookParam param)
							throws Throwable {
						XposedBridge
								.log("SmartKeeperBypasser: Notification routine disabled!");
						return null;
					}

				});

		findAndHookMethod(
				"com.netcube.smartkeeper.child.service.DeviceReceiver",
				lpparam.classLoader, "onDisableRequested",
				android.content.Context.class, android.content.Intent.class,
				new XC_MethodReplacement() {

					@Override
					protected Object replaceHookedMethod(MethodHookParam param)
							throws Throwable {
						XposedBridge
								.log("SmartKeeperBypasser: Triggered onDisableRequested!");
						return null;
					}

				});

		findAndHookMethod("android.app.admin.DevicePolicyManager",
				lpparam.classLoader, "isAdminActive",
				android.content.ComponentName.class,
				new XC_MethodReplacement() {

					@Override
					protected Object replaceHookedMethod(MethodHookParam param)
							throws Throwable {
						XposedBridge.log("We hooked isAdminActive.");

						for (StackTraceElement element : Thread.currentThread()
								.getStackTrace()) {
							XposedBridge.log(element.toString());
						}

						return true;
					}

				});
		/*
		 * final LoadPackageParam slpparam = lpparam;
		 * findAndHookMethod("com.netcube.smartkeeper.child.activity.ActivityStatus"
		 * , lpparam.classLoader, "onResume", new XC_MethodReplacement() {
		 * 
		 * @Override protected Object replaceHookedMethod(MethodHookParam param)
		 * throws Throwable { XposedBridge.log("We hooked onResume"); Class c =
		 * XposedHelpers.findClass( "android.app.Activity",
		 * slpparam.classLoader);
		 * 
		 * Method m[] = c.getDeclaredMethods(); Method onResumeMethod = null;
		 * for (int i = 0; i < m.length; i++) {
		 * XposedBridge.log("Method Found: " + m[i].toString());
		 * if(m[i].toString().contains("onResume")) {
		 * XposedBridge.log("OnResume Found!"); onResumeMethod = m[i]; } }
		 * 
		 * //XposedBridge.log(c.getCanonicalName() + "#" +
		 * c.getMethod("onResume").getName());
		 * onResumeMethod.invoke(param.thisObject); return null; }
		 * 
		 * });
		 */

		/*
		 * 
		 * findAndHookMethod("com.netcube.smartkeeper.child.service.DeviceReceiver"
		 * , lpparam.classLoader, "onDisable", new XC_MethodReplacement() {
		 * 
		 * @Override protected Object replaceHookedMethod(MethodHookParam param)
		 * throws Throwable { // TODO Auto-generated method stub return null; }
		 * 
		 * });
		 * 
		 * findAndHookMethod("com.netcube.smartkeeper.child.service.MainService",
		 * lpparam.classLoader, "h", new XC_MethodReplacement() {
		 * 
		 * @Override protected Object replaceHookedMethod(MethodHookParam param)
		 * throws Throwable { return true; } });
		 * 
		 * findAndHookMethod(
		 * "com.netcube.smartkeeper.child.service.MyStartupIntentReceiver",
		 * lpparam.classLoader, "onReceive", new XC_MethodReplacement() {
		 * 
		 * @Override protected Object replaceHookedMethod(MethodHookParam param)
		 * throws Throwable { return null; } });
		 */
	}
}
