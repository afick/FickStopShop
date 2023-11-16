package hu.ait.fickstopshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import hu.ait.fickstopshop.screen.shop.ShopScreen
import hu.ait.fickstopshop.screen.splash.SplashScreen
import hu.ait.fickstopshop.screen.summary.SummaryScreen
import hu.ait.fickstopshop.ui.navigation.Screen
import hu.ait.fickstopshop.ui.theme.FickStopShopTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FickStopShopTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShopNavHost()
                }
            }
        }
    }
}

@Composable
fun ShopNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController, startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToShopScreen = {
                    navController.navigate(Screen.ShopList.route)
                }
            )
        }
        composable(Screen.ShopList.route) {
            ShopScreen(
                onNavigateToSummary = {
                    navController.navigate(Screen.Summary.route)
                }
            )
        }
        composable(Screen.Summary.route) {
            SummaryScreen(onNavigateToShopScreen = {
                navController.navigate(Screen.ShopList.route)
            })

        }
    }
}