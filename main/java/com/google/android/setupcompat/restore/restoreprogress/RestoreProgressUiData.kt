/*
 * Copyright (C) 2025 The Android Open Source Project
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

package com.google.android.setupcompat.restore.restoreprogress

import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

/** The type of progress indicator to show. */
enum class ProgressUiType {
  UNKNOWN,
  INDETERMINATE_PROGRESS,
  DETERMINATE_PROGRESS,
  ICON,
  NONE,
}

/**
 * UI data for displaying the current status of the restore process in the progress indicator shown
 * during the One Tap Setup flow.
 */
@Parcelize
data class RestoreProgressUiData(
  val progressUiType: ProgressUiType,
  val progressValue: Int = 0,
  @DrawableRes val icon: Int = 0,
  @ColorInt val iconTint: Int = -1,
  @DrawableRes val bottomSheetIcon: Int = 0,
  @ColorInt val bottomSheetIconTint: Int = -1,
  val bottomSheetTitle: String? = null,
  val bottomSheetDescription: String? = null,
  val bottomSheetButtonText: String? = null,
  val bottomSheetProgressBarVisible: Boolean = false,
  val bottomSheetCardVisible: Boolean = false,
  @DrawableRes val bottomSheetCardIcon: Int = 0,
  val bottomSheetCardTitle: String? = null,
  val bottomSheetCardDescription: String? = null,
) : Parcelable
