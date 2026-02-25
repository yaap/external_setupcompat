
/*
 * Copyright (C) 2026 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.setupcompat.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import com.google.android.setupcompat.R
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper

/**
 * Helper class for applying focus indicator {@link
 * R.drawable#suc_global_focus_indicator_rounded_rectangle} to the given view. A focus ring is a
 * visual indicator that indicates which view currently has focus, this is represented by a colored
 * ring around the focused view, which helps users navigate the UI using a keyboard.
 */
object FocusIndicatorHelper {

  /**
   * Applies the focus ring drawable to the given view.
   *
   * @param context The context to get the drawable from.
   * @param view The view to apply the drawable to.
   * @param color The color of the drawable.
   */
  @JvmStatic
  fun applyFocusRingDrawable(context: Context, view: View, @ColorInt color: Int) {
    if (!PartnerConfigHelper.isSuwUseFocusRingEnabled(context)) {
      return
    }

    val drawableResId = R.drawable.suc_global_focus_indicator_rounded_rectangle
    val focusIndicatorDrawable: Drawable? = context.getDrawable(drawableResId)
    if (focusIndicatorDrawable != null) {
      focusIndicatorDrawable.mutate()
      focusIndicatorDrawable.setTint(color)
      view.foreground = focusIndicatorDrawable
    }
  }
}
