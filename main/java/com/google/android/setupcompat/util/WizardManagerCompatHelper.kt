package com.google.android.setupcompat.util

import android.content.Context
import android.content.Intent
import com.android.onboarding.common.SETUP_WIZARD
import com.android.onboarding.contracts.setupwizard.orchestrator.IowaWaitingScreenContract
import com.android.onboarding.contracts.setupwizard.orchestrator.SetupWizardOrchestratorContract
import com.android.onboarding.utils.persistable.PersistableIntent
import com.android.onboarding.versions.OnboardingChanges
import com.android.onboarding.versions.changes.JOINED_UP_LOADING
import com.android.onboarding.versions.changes.UNIFIED_LOADING_EVERYWHERE
import com.android.onboarding.wizardmanager.actions.PersistableWizardResult
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper
import com.google.android.wizardmanager.wizardManagerExtras

/**
 * Helper to interact with Wizard Manager in setup wizard, which should be used when a screen is
 * shown inside the setup flow. This includes things like parsing extras passed by Wizard Manager,
 * and invoking Wizard Manager to start the next action.
 */
object WizardManagerCompatHelper {
  /**
   * Gets an intent that will invoke the next step of setup wizard. This method checks if the
   * orchestrator activity is enabled. If it is, an intent to the orchestrator will be returned.
   * Otherwise, it will fall back to the legacy wizard manager activity.
   *
   * @param context The context to create the intent by [SetupWizardOrchestratorContract].
   * @param onboardingChanges The onboarding changes to check if the current process supports the
   *   orchestrator activity.
   * @param originalIntent The original intent that was used to start the step, usually via
   *   [android.app.Activity.getIntent].
   * @param resultCode The result code of the step. See [ResultCodes].
   * @param data An intent containing extra result data.
   * @return A new intent that can be used with [android.app.Activity.startActivityForResult] to
   *   start the next step of the setup flow.
   */
  @JvmStatic
  fun getNextIntent(
    context: Context,
    onboardingChanges: OnboardingChanges,
    originalIntent: Intent,
    resultCode: Int,
    data: Intent?,
  ): Intent {
    // TODO: Do not forward if joined_up_waiting isn't enabled.
    return if (
      onboardingChanges.componentSupportsChange(SETUP_WIZARD, UNIFIED_LOADING_EVERYWHERE) &&
        onboardingChanges.componentSupportsChange(SETUP_WIZARD, JOINED_UP_LOADING) &&
        PartnerConfigHelper.isSuwJoinedUpLoadingEnabled(context)
    ) {
      getOrchestratorNextIntent(context, originalIntent, resultCode, data)
    } else {
      // fallback to legacy wizard manager
      WizardManagerHelper.getNextIntent(originalIntent, resultCode, data)
    }
  }

  private fun getOrchestratorNextIntent(
    context: Context,
    originalIntent: Intent,
    resultCode: Int,
    data: Intent?,
  ): Intent {
    val result = PersistableWizardResult(resultCode, PersistableIntent(data ?: Intent()))

    val action = SetupWizardOrchestratorContract.Action.ResultAction(result)

    val extras = originalIntent.wizardManagerExtras

    val argument = SetupWizardOrchestratorContract.OrchestratorArgument(extras, action)

    return IowaWaitingScreenContract.createIntent(context, argument)
  }
}
