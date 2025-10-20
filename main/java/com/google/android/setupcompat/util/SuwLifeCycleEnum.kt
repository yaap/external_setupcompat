package com.google.android.setupcompat.util

/** Enum for notifying an Activity that what SetupWizard flow is */
enum class SuwLifeCycleEnum(val value: Int) {
  UNKNOWN(0),
  INITIALIZATION(1),
  PREDEFERRED(2),
  DEFERRED(3),
  PORTAL(4),
  RESTORE_ANYTIME(5),
  // The 'value' property is part of the primary constructor and accessible directly.
}
