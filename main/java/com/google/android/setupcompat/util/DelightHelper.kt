package com.google.android.setupcompat.util

import android.content.Context
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper

/** Helper class to handle delightful setup */
object DelightHelper {

  /** Returns whether the delightful setup is enabled. */
  @JvmStatic
  fun shouldApplyDelightfulSetup(context: Context?): Boolean {
    return context?.let { PartnerConfigHelper.isDelightfulSetupEnabled(it) } ?: false
  }

  /** Returns whether the animated icon is enabled. It is based on the delightful setup flag. */
  @JvmStatic
  fun shouldApplyAnimatedIcon(context: Context?): Boolean {
    return context?.let { PartnerConfigHelper.isAnimatedIconEnabled(it) } ?: false
  }

  /** Returns whether the animated qr code is enabled. It is based on the delightful setup flag. */
  @JvmStatic
  fun shouldApplyAnimatedQrCode(context: Context?): Boolean {
    return context?.let { PartnerConfigHelper.isAnimatedQrCodeEnabled(it) } ?: false
  }
}
