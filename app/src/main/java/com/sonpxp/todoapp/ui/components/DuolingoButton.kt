package com.sonpxp.todoapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.sonpxp.todoapp.ui.theme.Green
import com.sonpxp.todoapp.ui.theme.NunitoFontFamily
import com.sonpxp.todoapp.ui.theme.ShadowColor
import com.sonpxp.todoapp.ui.theme.TodoAppTheme

/**
 * Nút tùy chỉnh theo phong cách Duolingo với bóng và hiệu ứng nhấn xuống.
 *
 * @param modifier Modifier để tùy chỉnh bố cục hoặc giao diện
 * @param text Văn bản hiển thị trên nút
 * @param textColor Màu chữ của nút
 * @param textSize Kích thước chữ
 * @param letterSpacing Khoảng cách giữa các chữ
 * @param fontWeight Độ đậm của chữ
 * @param textAllCaps Hiển thị văn bản dưới dạng chữ in hoa
 * @param color Màu nền của nút
 * @param shadowColor Màu bóng của nút
 * @param cornerRadius Bán kính góc bo tròn
 * @param shadowSize Kích thước bóng đổ
 * @param contentPadding Padding cho nội dung nút
 * @param onClick Hàm gọi khi nút được nhấn
 */
@Composable
fun DuolingoButton(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = Color.White,
    textSize: TextUnit = 18.sp,
    letterSpacing: TextUnit = 0.5.sp,
    fontWeight: FontWeight = FontWeight.ExtraBold,
    textAllCaps: Boolean = false,
    color: Color = Green,
    shadowColor: Color = ShadowColor,
    cornerRadius: Dp = 16.dp,
    shadowSize: Dp = 4.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 0.dp, vertical = 14.dp),
    onClick: () -> Unit
) {
    val isPressed = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val onClickState = rememberUpdatedState(onClick)

    // Hiệu ứng màu
    val buttonColor by animateColorAsState(
        targetValue = if (isPressed.value) color.copy(alpha = Animation.PRESSED_ALPHA_FACTOR) else color,
        animationSpec = tween(Animation.COLOR_ANIMATION_DURATION_MS),
        label = "buttonColor"
    )

    // Hiệu ứng nhấn xuống (translationY)
    val elevation by animateDpAsState(
        targetValue = if (isPressed.value) shadowSize else 0.dp,
        animationSpec = tween(
            durationMillis = if (isPressed.value) Animation.PRESS_ANIMATION_DURATION_MS else Animation.RELEASE_ANIMATION_DURATION_MS
        ),
        label = "buttonElevation"
    )

    ConstraintLayout(modifier = modifier) {
        val (shadow, button) = createRefs()

        // Bóng
        Spacer(
            modifier = Modifier
                .constrainAs(shadow) {
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                    linkTo(button.start, button.end)
                    linkTo(button.top, button.bottom)
                    translationY = shadowSize
                }
                .clip(RoundedCornerShape(cornerRadius))
                .background(shadowColor)
        )

        // Nút
        Button(
            onClick = { /* Xử lý trong pointerInput */ },
            modifier = Modifier
                .constrainAs(button) {
                    width = Dimension.matchParent
                    top.linkTo(parent.top)
                    translationY = elevation
                }
                .clip(RoundedCornerShape(cornerRadius))
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            when (event.type) {
                                androidx.compose.ui.input.pointer.PointerEventType.Press -> {
                                    isPressed.value = true
                                }

                                androidx.compose.ui.input.pointer.PointerEventType.Release -> {
                                    if (isPressed.value) {
                                        isPressed.value = false
                                        onClickState.value()
                                    }
                                }

                                androidx.compose.ui.input.pointer.PointerEventType.Exit -> {
                                    isPressed.value = false
                                }
                            }
                        }
                    }
                },
            contentPadding = contentPadding,
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = textColor
            ),
            shape = RoundedCornerShape(cornerRadius),
            interactionSource = interactionSource
        ) {
            Text(
                text = if (textAllCaps) text.uppercase() else text,
                fontWeight = fontWeight,
                fontSize = textSize,
                letterSpacing = letterSpacing,
                textAlign = TextAlign.Center,
                style = TextStyle(fontFamily = NunitoFontFamily),
            )
        }
    }
}

/**
 * Cấu hình hiệu ứng cho DuolingoButton.
 */
object Animation {
    // Độ mờ khi nhấn nút
    const val PRESSED_ALPHA_FACTOR = 0.8f

    // Thời gian hiệu ứng màu (ms)
    const val COLOR_ANIMATION_DURATION_MS = 80

    // Thời gian hiệu ứng nhấn (ms)
    const val PRESS_ANIMATION_DURATION_MS = 40

    // Thời gian hiệu ứng thả (ms)
    const val RELEASE_ANIMATION_DURATION_MS = 120
}

/**
 * Preview cho DuolingoButton với theme.
 */
@Preview(showBackground = true)
@Composable
private fun DuolingoButtonPreview() {
    TodoAppTheme {
        DuolingoButton(
            text = "Bắt Đầu",
            onClick = {}
        )
    }
}