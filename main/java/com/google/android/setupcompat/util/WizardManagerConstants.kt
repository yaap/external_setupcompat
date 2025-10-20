package com.google.android.setupcompat.util

object WizardManagerConstants {

  /** Extra for notifying an Activity that what SetupWizard flow is. */
  const val EXTRA_SUW_LIFECYCLE = "suw_lifecycle"
  /** Action for notifying an Activity that the next step of setup wizard should be started. */
  const val ACTION_NEXT = "com.android.wizard.NEXT"
  /** Extra for including the wizard manager bundle in the intent. */
  const val EXTRA_WIZARD_BUNDLE = "wizardBundle"
  /** Extra used for including the resultcode of a wizardmanager action. */
  const val EXTRA_RESULT_CODE = "com.android.setupwizard.ResultCode"
  /** Extra for notifying an Activity that it is inside the first SetupWizard flow or not. */
  const val EXTRA_IS_FIRST_RUN = "firstRun"
  /** Extra for notifying an Activity that it is inside the Deferred SetupWizard flow or not. */
  const val EXTRA_IS_DEFERRED_SETUP = "deferredSetup"
  /** Extra for notifying an Activity that it is inside the "Pre-Deferred Setup" flow. */
  const val EXTRA_IS_PRE_DEFERRED_SETUP = "preDeferredSetup"
  /** Extra for notifying an Activity that it is inside the "Portal Setup" flow. */
  const val EXTRA_IS_PORTAL_SETUP = "portalSetup"
  /**
   * Extra for including a persistable map of Onboarding Node Id to MetadataStore.
   * * <p>This will only be read and used by loading screens. Other screens should just pass this
   *   forwards.
   */
  const val EXTRA_PENDING_ACTIVITY_METADATA = "pendingActivityMetadata"
  /**
   * Extra for notifying an Activity that it is inside the any setup flow.
   * * <p>Apps that target API levels below {@link android.os.Build.VERSION_CODES#Q} is able to
   *   determine whether Activity is inside the any setup flow by one of
   *   {@link #EXTRA_IS_FIRST_RUN}, {@link #EXTRA_IS_DEFERRED_SETUP}, and
   *   {@link #EXTRA_IS_PRE_DEFERRED_SETUP} is true.
   */
  const val EXTRA_IS_SETUP_FLOW = "isSetupFlow"
  /** Extra for notifying an activity that was called from suggested action activity. */
  const val EXTRA_IS_SUW_SUGGESTED_ACTION_FLOW = "isSuwSuggestedActionFlow"
  const val EXTRA_THEME = "theme"
  const val EXTRA_USE_IMMERSIVE_MODE = "useImmersiveMode"
  const val SETTINGS_GLOBAL_DEVICE_PROVISIONED = "device_provisioned"
  const val SETTINGS_SECURE_USER_SETUP_COMPLETE = "user_setup_complete"

}
