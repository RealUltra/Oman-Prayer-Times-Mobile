package com.codealyst.omanprayertimes.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.imageResource

@Composable
fun TiledBox(
    @DrawableRes resourceId: Int,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    alpha: Float = 1.0f,
    offset: Offset = Offset(0.0f, 0.0f),
    content: @Composable () -> Unit,
) {
    val textureBitmap = ImageBitmap.imageResource(id = resourceId);

    Box(modifier = modifier.drawBehind {
        withTransform({
            translate(left = offset.x, top = offset.y)
        }) {
            drawRect(
                brush = ShaderBrush(
                    shader = ImageShader(
                        image = textureBitmap,
                        tileModeX = TileMode.Repeated,
                        tileModeY = TileMode.Repeated,
                    )
                ),
                topLeft = Offset(-offset.x, -offset.y),
                size = size,
                colorFilter = ColorFilter.tint(
                    color = color.copy(alpha = alpha),
                    blendMode = BlendMode.Modulate,
                ),
            )
        }
    }
    ) {
        content()
    }
}
