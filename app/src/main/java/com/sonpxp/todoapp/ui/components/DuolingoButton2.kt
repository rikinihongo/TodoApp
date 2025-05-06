package com.sonpxp.todoapp.ui.components

import android.view.MotionEvent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.sonpxp.todoapp.ui.theme.Green
import com.sonpxp.todoapp.ui.theme.ShadowColor

/**
 * Button configuration values used across the DuolingoButton component.
 */
private object DuolingoButtonDefaults {
    val cornerRadius = 16.dp
    val shadowSize = 4.dp
    val pressedAlphaFactor = 0.8f
    const val COLOR_ANIMATION_DURATION = 100
    const val PRESS_ANIMATION_DURATION = 50
    const val RELEASE_ANIMATION_DURATION = 150
    val contentPadding = PaddingValues(0.dp, 14.dp)
    val textSize = 18.sp
    val letterSpacing = 0.5.sp
}

/**
 * A custom button component that mimics Duolingo's button style with shadow and press animation.
 *
 * @param modifier Modifier to be applied to the button
 * @param text Text to display inside the button
 * @param color Background color of the button (default: Green)
 * @param textColor Color of the button text (default: White)
 * @param shadowColor Color of the button shadow (default: ShadowColor)
 * @param onClick Callback to be invoked when the button is clicked
 */
@Composable
fun DuolingoButton2(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Green,
    textColor: Color = Color.White,
    shadowColor: Color = ShadowColor,
    onClick: () -> Unit
) {
    // Button state - use stable state holder to reduce recomposition costs
    val buttonState = remember { DuolingoButtonState() }

    // UI interaction source for ripple control - disabled by default for custom press effect
    val interactionSource = remember { MutableInteractionSource() }

    // Animated states
    val buttonColor by animateColorAsState(
        targetValue = if (buttonState.isPressed)
            color.copy(alpha = DuolingoButtonDefaults.pressedAlphaFactor)
        else
            color,
        animationSpec = tween(DuolingoButtonDefaults.COLOR_ANIMATION_DURATION),
        label = "buttonColor"
    )

    val buttonElevation by animateDpAsState(
        targetValue = buttonState.animatedY,
        animationSpec = tween(
            durationMillis = if (buttonState.isPressed)
                DuolingoButtonDefaults.PRESS_ANIMATION_DURATION
            else
                DuolingoButtonDefaults.RELEASE_ANIMATION_DURATION,
            easing = if (buttonState.isPressed) LinearEasing else DecelerateEasing
        ),
        label = "buttonElevation"
    )

    ConstraintLayout(modifier = modifier) {
        val (shadow, button) = createRefs()

        // Shadow element
        Spacer(
            modifier = Modifier
                .constrainAs(shadow) {
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                    start.linkTo(button.start)
                    end.linkTo(button.end)
                    top.linkTo(button.top)
                    bottom.linkTo(button.bottom)
                    translationY = DuolingoButtonDefaults.shadowSize
                }
                .clip(RoundedCornerShape(DuolingoButtonDefaults.cornerRadius))
                .background(shadowColor)
        )

        // Button element
        Button(
            onClick = { /* Handled in pointerInteropFilter */ },
            modifier = Modifier
                .constrainAs(button) {
                    width = Dimension.matchParent
                    top.linkTo(parent.top)
                    translationY = buttonElevation
                }
                .indication(
                    interactionSource = interactionSource,
                    indication = null // Disable default ripple
                )
                .onGloballyPositioned { buttonState.btnSize = it.size }
                .pointerInteropFilter { motionEvent ->
                    handleMotionEvent(
                        event = motionEvent,
                        buttonState = buttonState,
                        onClick = onClick
                    )
                },
            contentPadding = DuolingoButtonDefaults.contentPadding,
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = textColor
            ),
            shape = RoundedCornerShape(DuolingoButtonDefaults.cornerRadius)
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = DuolingoButtonDefaults.textSize,
                letterSpacing = DuolingoButtonDefaults.letterSpacing
            )
        }
    }
}

/**
 * State holder class for DuolingoButton to minimize recompositions.
 * Marked as Stable to help Compose's recomposition optimizations.
 */
@Stable
private class DuolingoButtonState {
    var isPressed by mutableStateOf(false)
    var btnSize by mutableStateOf(IntSize.Zero)
    var animatedY by mutableStateOf(0.dp)
}

/**
 * Handles motion events for the button to create a responsive touch experience.
 *
 * @param event The MotionEvent to handle
 * @param buttonState State holder for the button's current state
 * @param onClick Callback to invoke when button is clicked
 * @return Always returns true to consume the event
 */
private fun handleMotionEvent(
    event: MotionEvent,
    buttonState: DuolingoButtonState,
    onClick: () -> Unit
): Boolean {
    val isInBounds = event.x.toInt() in 0..buttonState.btnSize.width &&
            event.y.toInt() in 0..buttonState.btnSize.height

    when (event.action) {
        MotionEvent.ACTION_DOWN -> {
            buttonState.isPressed = true
            buttonState.animatedY = DuolingoButtonDefaults.shadowSize
        }
        MotionEvent.ACTION_UP -> {
            buttonState.isPressed = false
            buttonState.animatedY = 0.dp
            if (isInBounds) {
                onClick()
            }
        }
        MotionEvent.ACTION_CANCEL -> {
            buttonState.isPressed = false
            buttonState.animatedY = 0.dp
        }
        MotionEvent.ACTION_MOVE -> {
            if (!isInBounds && buttonState.isPressed) {
                buttonState.isPressed = false
                buttonState.animatedY = 0.dp
            } else if (isInBounds && !buttonState.isPressed) {
                buttonState.isPressed = true
                buttonState.animatedY = DuolingoButtonDefaults.shadowSize
            }
        }
    }
    return true
}

/**
 * Custom deceleration easing function for button animations.
 * Implemented as a singleton object to avoid recreating the interpolator.
 */
private object DecelerateEasing : Easing {
    // Create interpolator only once for the entire application lifecycle
    private val interpolator = android.view.animation.DecelerateInterpolator()

    override fun transform(fraction: Float): Float {
        return interpolator.getInterpolation(fraction)
    }
}

/**
 * Preview for the DuolingoButton
 */
@Preview(showBackground = false)
@Composable
private fun PreviewDuolingoButton() {
    DuolingoButton2(
        text = "Preview",
        onClick = {}
    )
}