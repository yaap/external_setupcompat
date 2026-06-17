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
package com.google.android.setupcompat.util

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.StateSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.google.errorprone.annotations.CanIgnoreReturnValue

/**
 * A helper class to programmatically create a focus indicator drawable that correctly handles
 * clipping and padding, with extensive customization options.
 *
 * <p><b>Why this class exists:</b> Static XML drawables are insufficient for focus indicators in
 * complex lists where items can be partially visible (clipped) or require different shapes based on
 * their position. This class solves these problems by:
 * <ol>
 * <li><b>Dynamic Clipping:</b> It queries the {@link Canvas}'s clip bounds at draw time to ensure
 *   the focus ring never draws outside the visible area of a view.
 * <li><b>Flexible Sizing:</b> It provides fine-grained control over how the view's padding is used,
 *   allowing the focus ring to be sized relative to the view's content or its absolute bounds.
 * <li><b>Positional Shaping:</b> The {@link Builder} can automatically calculate corner radii based
 *   on an item's position in a list (e.g., rounding only the top corners for the first item).
 * </ol>
 *
 * This centralization provides a consistent, powerful, and reusable way to create focus indicators,
 * reducing code duplication and eliminating the need for many specialized XML drawable assets.
 */
object FocusIndicatorDrawable {

  /**
   * A fluent builder for creating [Drawable] focus indicators. This is the primary entry point for
   * using the helper.
   */
  class Builder(private val context: Context) {
    private var horizontalPaddingAdjustmentDp: Int = 0
    private var verticalPaddingAdjustmentDp: Int = 0
    private var cornerRadiusDp: Int = 2
    private var cornerRadiiPx: FloatArray? = null
    private var horizontalOffsetDp: Int = 0

    @ColorInt private var color: Int = resolveColorAttr(context, android.R.attr.colorPrimary)

    /**
     * Sets an amount to adjust the horizontal spacing of the focus ring, in dp. A positive value
     * makes the ring tighter, and a negative value makes it wider. This is applied to the view's
     * padding if [withHorizontalPadding] is true, otherwise it is applied to the default inset.
     *
     * @param dp The padding adjustment value in dp.
     */
    @CanIgnoreReturnValue
    fun withHorizontalPaddingAdjustment(dp: Int) = apply { this.horizontalPaddingAdjustmentDp = dp }

    /**
     * Sets an amount to adjust the vertical spacing of the focus ring, in dp. A positive value
     * makes the ring tighter, and a negative value makes it wider. This is applied to the view's
     * padding if [withVerticalPadding] is true, otherwise it is applied to the default inset.
     *
     * @param dp The padding adjustment value in dp.
     */
    @CanIgnoreReturnValue
    fun withVerticalPaddingAdjustment(dp: Int) = apply { this.verticalPaddingAdjustmentDp = dp }

    /**
     * Sets the base corner radius to be used for calculations, in dp.
     *
     * @param dp The corner radius in dp.
     */
    @CanIgnoreReturnValue
    fun withCornerRadius(dp: Int) = apply {
      cornerRadiusDp = dp
      val cornerRadiusPx = dpToPx(context, dp).toFloat()
      cornerRadiiPx =
        floatArrayOf(
          cornerRadiusPx,
          cornerRadiusPx,
          cornerRadiusPx,
          cornerRadiusPx,
          cornerRadiusPx,
          cornerRadiusPx,
          cornerRadiusPx,
          cornerRadiusPx,
        )
    }

    /**
     * Sets the corner radii for each corner individually, in dp. This overrides any value set by
     * [withCornerRadius] or [withPositionalCornerRadii].
     *
     * @param topLeftDp Radius for the top-left corner, in dp.
     * @param topRightDp Radius for the top-right corner, in dp.
     * @param bottomRightDp Radius for the bottom-right corner, in dp.
     * @param bottomLeftDp Radius for the bottom-left corner, in dp.
     */
    @CanIgnoreReturnValue
    fun withCornerRadii(topLeftDp: Int, topRightDp: Int, bottomRightDp: Int, bottomLeftDp: Int) =
      apply {
        val tl = dpToPx(context, topLeftDp).toFloat()
        val tr = dpToPx(context, topRightDp).toFloat()
        val br = dpToPx(context, bottomRightDp).toFloat()
        val bl = dpToPx(context, bottomLeftDp).toFloat()
        cornerRadiiPx = floatArrayOf(tl, tl, tr, tr, br, br, bl, bl)
      }

    /**
     * Calculates and sets the corner radii based on the item's position in a list. This method uses
     * the radius set via [withCornerRadius].
     *
     * @param position The adapter position of the item.
     * @param itemCount The total number of items in the adapter.
     */
    @CanIgnoreReturnValue
    fun withPositionalCornerRadii(position: Int, itemCount: Int) = apply {
      val cornerRadiusPx = dpToPx(context, cornerRadiusDp).toFloat()
      val defaultRadiusPx = dpToPx(context, 3).toFloat()
      val radii =
        floatArrayOf(
          defaultRadiusPx,
          defaultRadiusPx,
          defaultRadiusPx,
          defaultRadiusPx,
          defaultRadiusPx,
          defaultRadiusPx,
          defaultRadiusPx,
          defaultRadiusPx,
        )

      val isFirst = position == 0
      val isLast = position == itemCount - 1

      if (isFirst) {
        radii[0] = cornerRadiusPx // top-left-x
        radii[1] = cornerRadiusPx // top-left-y
        radii[2] = cornerRadiusPx // top-right-x
        radii[3] = cornerRadiusPx // top-right-y
      }

      if (isLast) {
        radii[4] = cornerRadiusPx // bottom-right-x
        radii[5] = cornerRadiusPx // bottom-right-y
        radii[6] = cornerRadiusPx // bottom-left-x
        radii[7] = cornerRadiusPx // bottom-left-y
      }
      cornerRadiiPx = radii
    }

    /**
     * Sets the color attribute to be used for the focus ring.
     *
     * @param colorAttr The color attribute resource ID.
     */
    @CanIgnoreReturnValue
    fun withColor(@AttrRes colorAttr: Int) = apply {
      this.color = resolveColorAttr(context, colorAttr)
    }

    /**
     * Sets the color integer to be used for the focus ring.
     *
     * @param color The color integer.
     */
    @CanIgnoreReturnValue fun withColorInt(@ColorInt color: Int) = apply { this.color = color }

    /**
     * Sets the color resource to be used for the focus ring.
     *
     * @param colorRes The color resource ID.
     */
    @CanIgnoreReturnValue
    fun withColorRes(@ColorRes colorRes: Int) = apply { this.color = context.getColor(colorRes) }

    /**
     * Sets a horizontal offset for the focus ring, in dp. A positive value shifts the ring to the
     * left, and a negative value shifts it to the right.
     *
     * @param dp The offset value in dp.
     */
    @CanIgnoreReturnValue fun withHorizontalOffset(dp: Int) = apply { this.horizontalOffsetDp = dp }

    /**
     * Builds the final [Drawable] and associates it with the target view. The returned drawable is
     * a [StateListDrawable] that will show the focus ring only when the view has focus.
     *
     * @return A [StateListDrawable] ready to be set as the view's foreground.
     */
    fun build(): Drawable {
      // If no specific radii were ever configured, default to a uniform radius.
      if (cornerRadiiPx == null) {
        withCornerRadius(cornerRadiusDp)
      }

      val focusedDrawable: Drawable =
        ClippedBoundsOutlineDrawable(
          cornerRadiiPx!!,
          dpToPx(context, 3), // Outline width
          dpToPx(context, 3), // Inset
          dpToPx(context, horizontalPaddingAdjustmentDp),
          dpToPx(context, verticalPaddingAdjustmentDp),
          dpToPx(context, horizontalOffsetDp),
          color,
        )
      // In the unfocused state, we draw nothing in the foreground.
      val defaultDrawable: Drawable = ColorDrawable(Color.TRANSPARENT)

      return StateListDrawable().apply {
        addState(intArrayOf(android.R.attr.state_focused), focusedDrawable)
        addState(StateSet.WILD_CARD, defaultDrawable)
      }
    }
  }

  /**
   * The internal drawable implementation that performs the actual drawing during the focused state.
   * It handles all the clipping and padding logic.
   */
  private class ClippedBoundsOutlineDrawable(
    cornerRadii: FloatArray,
    outlineWidthPx: Int,
    private val insetPx: Int,
    private val horizontalPaddingAdjustmentPx: Int,
    private val verticalPaddingAdjustmentPx: Int,
    private val horizontalOffsetPx: Int,
    @ColorInt outlineColor: Int,
  ) : Drawable() {

    private val gradientDrawable =
      GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        setColor(Color.TRANSPARENT) // The stroke is the visible part.
        setStroke(outlineWidthPx, outlineColor)
        this.cornerRadii = cornerRadii
      }
    private val tempRect = Rect()

    override fun draw(canvas: Canvas) {
      // This is the core logic. We get the visible portion of the view from the canvas
      // itself at the exact moment of drawing. This is the only reliable way to handle
      // clipping during scrolling.
      if (canvas.getClipBounds(tempRect)) {
        // Calculate the final bounds by starting with the visible clip rect and then
        // applying the relevant padding, adjustment, and inset values.
        val drawableLeft =
          tempRect.left + horizontalPaddingAdjustmentPx + insetPx - horizontalOffsetPx
        val drawableTop = tempRect.top + verticalPaddingAdjustmentPx + insetPx
        val drawableRight =
          tempRect.right - horizontalPaddingAdjustmentPx - insetPx - horizontalOffsetPx
        val drawableBottom = tempRect.bottom - verticalPaddingAdjustmentPx - insetPx

        // Only draw if the calculated bounds are valid.
        if (drawableLeft < drawableRight && drawableTop < drawableBottom) {
          gradientDrawable.setBounds(drawableLeft, drawableTop, drawableRight, drawableBottom)
          gradientDrawable.draw(canvas)
        }
      }
    }

    override fun setAlpha(alpha: Int) {
      gradientDrawable.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
      gradientDrawable.colorFilter = colorFilter
    }

    @Deprecated("This method is no longer used in graphics optimizations")
    override fun getOpacity(): Int {
      return PixelFormat.TRANSLUCENT
    }
  }

  // --- Helper Methods ---
  private fun dpToPx(context: Context, dp: Int): Int =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        context.resources.displayMetrics,
      )
      .toInt()

  @ColorInt
  private fun resolveColorAttr(context: Context, @AttrRes colorAttr: Int): Int {
    val ta = context.obtainStyledAttributes(intArrayOf(colorAttr))
    @ColorInt val color = ta.getColor(0, Color.MAGENTA)
    ta.recycle()
    return color
  }
}
