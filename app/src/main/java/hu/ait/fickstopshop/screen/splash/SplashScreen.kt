package hu.ait.fickstopshop.screen.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.ait.fickstopshop.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToShopScreen: () -> Unit) = Box(
    Modifier
        .fillMaxSize()
) {
    val scale = remember {
        androidx.compose.animation.core.Animatable(0.0f)
    }
    LaunchedEffect(key1 = Unit) {
        scale.animateTo(
            targetValue = 0.8f,
            animationSpec = tween(1500)
        )
        // 3 second delay then navigate to main screen
        delay(1500)
        onNavigateToShopScreen()
    }
    Image(
        painter = painterResource(id = R.drawable.shopcarticon),
        contentDescription = stringResource(R.string.splash_text),
    alignment = Alignment.Center,
    modifier = Modifier
        .fillMaxSize()
        .padding(10.dp)
        .scale(scale.value)
    )
    Text(
        text = stringResource(R.string.AppNameSplash),
        textAlign = TextAlign.Center,
        fontSize = 30.sp,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 180.dp)
    )
}