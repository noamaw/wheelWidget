package com.spinwheelrn

import com.facebook.react.bridge.ReactApplicationContext

class SpinwheelrnModule(reactContext: ReactApplicationContext) :
  NativeSpinwheelrnSpec(reactContext) {

  override fun multiply(a: Double, b: Double): Double {
    return a * b
  }

  companion object {
    const val NAME = NativeSpinwheelrnSpec.NAME
  }
}
