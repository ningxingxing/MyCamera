package com.example.administrator.mycamera.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager

fun setStatusBarColor(context: Activity, useThemeStatusBarColor: Boolean,color:Int) {
   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //5.0及以上
      val decorView = context.window.decorView
      val option = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
              or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
              or View.SYSTEM_UI_FLAG_VISIBLE)
      decorView.systemUiVisibility = option

      //根据上面设置是否对状态栏单独设置颜色
      if (useThemeStatusBarColor) {
         context.window.statusBarColor = color
         context.window.navigationBarColor = color
      } else {
         context.window.statusBarColor = Color.TRANSPARENT
      }
   } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //4.4到5.0
      val localLayoutParams = context.window.attributes
      localLayoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or localLayoutParams.flags
   }
}