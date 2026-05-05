package top.sacz.hook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import top.sacz.hook.activity.ModuleActivity

class ActivityTest {

    fun hook() {
        //activity的onCreate
        val activityOnCreate = Activity::class.java.getDeclaredMethod("onCreate", Bundle::class.java)
        activityOnCreate.isAccessible = true
        InjectHook.xposed.hook(activityOnCreate).intercept { chain ->
            val result = chain.proceed()
            val activity = chain.thisObject as Activity
            //延迟三秒后跳转
            Handler(activity.mainLooper).postDelayed({
                val intent = Intent(activity, ModuleActivity::class.java)
                activity.startActivity(intent)
            }, 3000)
            result
        }
    }
}
