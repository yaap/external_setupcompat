/*
 * Copyright (C) 2015 The Android Open Source Project
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

package com.google.android.setupcompat.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import com.google.android.setupcompat.R;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.util.Logger;
import java.util.Locale;

/**
 * A FrameLayout subclass that will responds to onApplyWindowInsets to draw a drawable in the top
 * inset area, making a background effect for the navigation bar. To make use of this layout,
 * specify the system UI visibility {@link android.view.View#SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN} and
 * set specify fitsSystemWindows.
 *
 * <p>This view is a normal FrameLayout if either of those are not set, or if the platform version
 * is lower than Lollipop.
 */
public class StatusBarBackgroundLayout extends FrameLayout {

  private static final Logger LOG = new Logger("StatusBarBgLayout");

  private Drawable statusBarBackground;
  private WindowInsets lastInsets;

  public StatusBarBackgroundLayout(Context context) {
    super(context);
  }

  public StatusBarBackgroundLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @TargetApi(VERSION_CODES.HONEYCOMB)
  public StatusBarBackgroundLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      if (lastInsets == null) {
        requestApplyInsets();
      }
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      if (lastInsets != null) {
        final int insetTop = lastInsets.getSystemWindowInsetTop();
        if (insetTop > 0) {
          statusBarBackground.setBounds(
              /* left= */ 0, /* top= */ 0, /* right= */ getWidth(), /* bottom= */ insetTop);
          statusBarBackground.draw(canvas);
        }
      }
    }
  }

  public void setStatusBarBackground(Drawable background) {
    statusBarBackground = background;
    if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      setWillNotDraw(background == null);
      setFitsSystemWindows(background != null);
      invalidate();
    }
  }

  public Drawable getStatusBarBackground() {
    return statusBarBackground;
  }

  @Override
  @SuppressLint("NewApi")
  public WindowInsets onApplyWindowInsets(WindowInsets insets) {
    lastInsets =
        shouldApplyEdgeToEdge(insets.getSystemWindowInsetBottom())
            ? applyEdgeToEdge(insets)
            : insets;
    return super.onApplyWindowInsets(lastInsets);
  }

  private boolean shouldApplyEdgeToEdge(int windowInsetBottom) {
    boolean glifExpressiveEnabled = PartnerConfigHelper.isGlifExpressiveEnabled(getContext());
    boolean isAtLeastLollipop = VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP;
    LOG.atInfo(
        String.format(
            Locale.US,
            "Apply edge to edge, glifExpressiveEnabled: %s, isAtLeastLollipop: %s,"
                + " windowInsetBottom: %d",
            glifExpressiveEnabled,
            isAtLeastLollipop,
            windowInsetBottom));
    return glifExpressiveEnabled && isAtLeastLollipop && windowInsetBottom > 0;
  }

  @SuppressLint("NewApi")
  private WindowInsets applyEdgeToEdge(WindowInsets insets) {
    return insets.replaceSystemWindowInsets(
        0,
        insets.getSystemWindowInsetTop(),
        0,
        findViewById(R.id.suc_layout_status).getPaddingBottom());
  }
}
