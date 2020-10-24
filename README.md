User Login Conditional Navigation Guide Issue
=============================================

## Overview

This project demonstrates an issue in [User Login Conditional Navigation Guide](https://developer.android.com/guide/navigation/navigation-conditional#login).  
Guide recommends to listen login result in `onCreate` method.
```kotlin
class ProfileFragment : Fragment() {
    ...

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navController = findNavController()

        val currentBackStackEntry = navController.currentBackStackEntry!!
        val savedStateHandle = currentBackStackEntry.savedStateHandle
        savedStateHandle.getLiveData<Boolean>(LoginFragment.LOGIN_SUCCESSFUL)
                .observe(currentBackStackEntry, Observer { success ->
                    if (!success) {
                        val startDestination = navController.graph.startDestination
                        val navOptions = NavOptions.Builder()
                                .setPopUpTo(startDestination, true)
                                .build()
                        navController.navigate(startDestination, null, navOptions)
                    }
                })
    }

    ...
}
```

## Issue

This code leads to crash when activity is re-created, because navController is not available in `onCreate`.
```
10-24 22:12:00.874 5770-5770/com.silentnuke.conditionalnavigationguideissue E/AndroidRuntime: FATAL EXCEPTION: main
    Process: com.silentnuke.conditionalnavigationguideissue, PID: 5770
    java.lang.RuntimeException: Unable to start activity ComponentInfo{com.silentnuke.conditionalnavigationguideissue/com.silentnuke.conditionalnavigationguideissue.MainActivity}: java.lang.IllegalStateException: NavController is not available before onCreate()
        at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2416)
        at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:2476)
        at android.app.ActivityThread.handleRelaunchActivity(ActivityThread.java:4077)
        at android.app.ActivityThread.-wrap15(ActivityThread.java)
        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1350)
        at android.os.Handler.dispatchMessage(Handler.java:102)
        at android.os.Looper.loop(Looper.java:148)
        at android.app.ActivityThread.main(ActivityThread.java:5417)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:726)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:616)
     Caused by: java.lang.IllegalStateException: NavController is not available before onCreate()
        at androidx.navigation.fragment.NavHostFragment.getNavController(NavHostFragment.java:184)
        at androidx.navigation.fragment.NavHostFragment.findNavController(NavHostFragment.java:105)
        at androidx.navigation.fragment.FragmentKt.findNavController(Fragment.kt:29)
        at com.silentnuke.conditionalnavigationguideissue.ProfileFragment.onCreate(ProfileFragment.kt:18)
        at androidx.fragment.app.Fragment.performCreate(Fragment.java:2684)
        at androidx.fragment.app.FragmentStateManager.create(FragmentStateManager.java:280)
        at androidx.fragment.app.FragmentManager.moveToState(FragmentManager.java:1175)
        at androidx.fragment.app.FragmentManager.moveToState(FragmentManager.java:1356)
        at androidx.fragment.app.FragmentManager.moveFragmentToExpectedState(FragmentManager.java:1434)
        at androidx.fragment.app.FragmentManager.moveToState(FragmentManager.java:1504)
        at androidx.fragment.app.FragmentManager.dispatchStateChange(FragmentManager.java:2625)
        at androidx.fragment.app.FragmentManager.dispatchCreate(FragmentManager.java:2571)
        at androidx.fragment.app.Fragment.restoreChildFragmentState(Fragment.java:1707)
        at androidx.fragment.app.Fragment.onCreate(Fragment.java:1683)
        at androidx.navigation.fragment.NavHostFragment.onCreate(NavHostFragment.java:206)
        at androidx.fragment.app.Fragment.performCreate(Fragment.java:2684)
        at androidx.fragment.app.FragmentStateManager.create(FragmentStateManager.java:280)
        at androidx.fragment.app.FragmentManager.moveToState(FragmentManager.java:1175)
        at androidx.fragment.app.FragmentManager.moveToState(FragmentManager.java:1356)
        at androidx.fragment.app.FragmentManager.moveFragmentToExpectedState(FragmentManager.java:1434)
        at androidx.fragment.app.FragmentManager.moveToState(FragmentManager.java:1497)
        at androidx.fragment.app.FragmentManager.dispatchStateChange(FragmentManager.java:2625)
        at androidx.fragment.app.FragmentManager.dispatchCreate(FragmentManager.java:2571)
        at androidx.fragment.app.FragmentController.dispatchCreate(FragmentController.java:236)
        at androidx.fragment.app.FragmentActivity.onCreate(FragmentActivity.java:315)
        at androidx.appcompat.app.AppCompatActivity.onCreate(AppCompatActivity.java:115)
        at com.silentnuke.conditionalnavigationguideissue.MainActivity.onCreate(MainActivity.kt:8)
        at android.app.Activity.performCreate(Activity.java:6237)
        at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1107)
        at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2369)
        at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:2476) 
        at android.app.ActivityThread.handleRelaunchActivity(ActivityThread.java:4077) 
        at android.app.ActivityThread.-wrap15(ActivityThread.java) 
        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1350) 
        at android.os.Handler.dispatchMessage(Handler.java:102) 
        at android.os.Looper.loop(Looper.java:148) 
        at android.app.ActivityThread.main(ActivityThread.java:5417) 
        at java.lang.reflect.Method.invoke(Native Method) 
        at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:726) 
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:616) 
```