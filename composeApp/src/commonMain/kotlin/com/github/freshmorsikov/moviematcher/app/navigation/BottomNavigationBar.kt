package com.github.freshmorsikov.moviematcher.app.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.InternalSerializationApi
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.ic_heart
import moviematcher.composeapp.generated.resources.ic_match
import moviematcher.composeapp.generated.resources.ic_swipe
import moviematcher.composeapp.generated.resources.navigation_favorites
import moviematcher.composeapp.generated.resources.navigation_matches
import moviematcher.composeapp.generated.resources.navigation_swipe
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

private val bottomRoutes: List<NavigationRoute.BottomNavigationRoute> = listOf(
    NavigationRoute.Favorite,
    NavigationRoute.Swipe,
    NavigationRoute.Matches,
)

data class BottomNavigationItemList(
    val items: List<BottomNavigationItem>
) {
    val current: BottomNavigationItem = items.find { route ->
        route.isSelected
    } ?: error("Current route not found")
}

data class BottomNavigationItem(
    val route: NavigationRoute.BottomNavigationRoute,
    val isSelected: Boolean,
    val badgeCount: Int?
)

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    newMatches: Int,
    onChangeIsCurrentMatches: (Boolean) -> Unit,
) {
    val backStack by navController.currentBackStackEntryAsState()
    val currentDestination = backStack?.destination

    LaunchedEffect(currentDestination) {
        onChangeIsCurrentMatches(currentDestination.same(NavigationRoute.Matches))
    }

    if (!currentDestination.isBottomNavigation()) {
        return
    }

    val itemList = BottomNavigationItemList(
        items = bottomRoutes.map { route ->
            BottomNavigationItem(
                route = route,
                isSelected = currentDestination.same(route),
                badgeCount = newMatches.takeIf { count ->
                    (route is NavigationRoute.Matches) && (count > 0)
                }
            )
        }
    )
    BottomNavigationBarContent(
        navController = navController,
        itemList = itemList
    )
}

@Composable
private fun BottomNavigationBarContent(
    navController: NavHostController,
    itemList: BottomNavigationItemList
) {
    Column {
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Black.copy(alpha = 0.1f)
        )
        NavigationBar(containerColor = Color.White) {
            itemList.items.forEach { item ->
                val route = item.route
                NavigationBarItem(
                    selected = item.isSelected,
                    icon = {
                        BadgedBox(
                            badge = {
                                item.badgeCount?.let { count ->
                                    Badge(containerColor = MaterialTheme.colorScheme.error) {
                                        Text(
                                            text = count.toString(),
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontSize = 12.sp,
                                                lineHeight = 16.sp,
                                            ),
                                            color = MaterialTheme.colorScheme.onError,
                                        )
                                    }
                                }
                            }
                        ) {
                            Icon(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(24.dp),
                                painter = painterResource(route.icon()),
                                contentDescription = null
                            )
                        }
                    },
                    label = {
                        Text(
                            text = stringResource(route.text()),
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    onClick = {
                        if (!item.isSelected) {
                            navController.navigate(route) {
                                popUpTo(itemList.current.route) {
                                    inclusive = true
                                }
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = MaterialTheme.colorScheme.outlineVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.outlineVariant,
                    )
                )
            }
        }
    }
}

private fun NavigationRoute.BottomNavigationRoute.icon(): DrawableResource {
    return when (this) {
        is NavigationRoute.Swipe -> Res.drawable.ic_swipe
        NavigationRoute.Favorite -> Res.drawable.ic_heart
        NavigationRoute.Matches -> Res.drawable.ic_match
    }
}

private fun NavigationRoute.BottomNavigationRoute.text(): StringResource {
    return when (this) {
        is NavigationRoute.Swipe -> Res.string.navigation_swipe
        NavigationRoute.Favorite -> Res.string.navigation_favorites
        NavigationRoute.Matches -> Res.string.navigation_matches
    }
}

@OptIn(InternalSerializationApi::class)
private fun NavDestination?.same(route: NavigationRoute): Boolean {
    if (this == null) return false
    return hasRoute(route::class)
}

private fun NavDestination?.isBottomNavigation(): Boolean {
    return bottomRoutes.any { route ->
        same(route)
    }
}

@Preview
@Composable
private fun BottomNavigationBarPreview() {
    MaterialTheme {
        BottomNavigationBarContent(
            navController = rememberNavController(),
            itemList = BottomNavigationItemList(
                items = listOf(
                    BottomNavigationItem(
                        route = NavigationRoute.Favorite,
                        isSelected = false,
                        badgeCount = null,
                    ),
                    BottomNavigationItem(
                        route = NavigationRoute.Swipe,
                        isSelected = true,
                        badgeCount = null,
                    ),
                    BottomNavigationItem(
                        route = NavigationRoute.Matches,
                        isSelected = false,
                        badgeCount = 2,
                    )
                )
            )
        )
    }
}