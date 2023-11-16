package hu.ait.fickstopshop.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object ShopList : Screen("shoplist")
    object Summary : Screen("summary")
}