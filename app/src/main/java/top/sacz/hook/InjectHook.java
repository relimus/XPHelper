package top.sacz.hook;

import android.content.Context;
import android.content.ContextWrapper;

import java.lang.reflect.Method;

import io.github.libxposed.api.XposedInterface;
import io.github.libxposed.api.XposedModule;
import io.github.libxposed.api.XposedModuleInterface;
import top.sacz.xphelper.XpHelper;

public class InjectHook extends XposedModule {

    public static final String TAG = "XpHelper";
    public static XposedModuleInterface.PackageReadyParam loadPackageParam;
    public static XposedInterface xposed;
    private final HookSteps hookSteps = new HookSteps();

    @Override
    public void onPackageReady(XposedModuleInterface.PackageReadyParam loadParam) {
        if (loadParam.isFirstPackage()) {
            loadPackageParam = loadParam;
            Method applicationCreateMethod = hookSteps.getApplicationCreateMethod(loadParam);
            applicationCreateMethod.setAccessible(true);

            hook(applicationCreateMethod).intercept(chain -> {
                Object result = chain.proceed();
                ContextWrapper context = (ContextWrapper) chain.getThisObject();
                entryHook(context.getBaseContext());
                return result;
            });
        }
    }

    @Override
    public void onModuleLoaded(XposedModuleInterface.ModuleLoadedParam param) {
        xposed = this;
        XpHelper.initModulePath(getModuleApplicationInfo().sourceDir);
    }

    private void entryHook(Context context) {
        //初始化context
        XpHelper.initContext(context);
        //进入自己的Hook逻辑
        hookSteps.initHook(context);
    }
}
