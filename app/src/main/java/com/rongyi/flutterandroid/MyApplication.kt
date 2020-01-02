package com.rongyi.flutterandroid

import android.app.Application
import android.content.Intent
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel

const val ENGINE_ID = "1"

class MyApplication : Application() {

    var count = 0

    private lateinit var channel: MethodChannel

    override fun onCreate() {
        super.onCreate()

        val flutterEngine = FlutterEngine(this)
        flutterEngine
            .dartExecutor
            .executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
            )

        FlutterEngineCache.getInstance().put(ENGINE_ID, flutterEngine)

        channel = MethodChannel(flutterEngine.dartExecutor, "dev.flutter.example/counter")

        channel.setMethodCallHandler { call, _ ->
            when (call.method) {
                "incrementCounter" -> {
                    count++
                    reportCounter()
                }
                "requestCounter" -> {
                    reportCounter()
                }
                "goto" -> {
//                    val intent = Intent(this, TwoActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent)


                    ARouter.getInstance().build(call.arguments as String).navigation();
                }
            }
        }



        if (BuildConfig.DEBUG) {           // These two lines must be written before init, otherwise these configurations will be invalid in the init process
            ARouter.openLog();     // Print log
            ARouter.openDebug();   // Turn on debugging mode (If you are running in InstantRun mode, you must turn on debug mode! Online version needs to be closed, otherwise there is a security risk)
        }
        ARouter.init(this)

    }

    private fun reportCounter() {
        channel.invokeMethod("reportCounter", count)
    }

}