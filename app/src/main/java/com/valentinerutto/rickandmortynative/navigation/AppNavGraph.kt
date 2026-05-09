package com.valentinerutto.rickandmortynative.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.valentinerutto.rickandmortynative.CharacterViewmodel
import com.valentinerutto.rickandmortynative.ui.screen.CharacterDetailScreen
import com.valentinerutto.rickandmortynative.ui.screen.CharacterScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val viewModel: CharacterViewmodel = koinViewModel()

    NavHost(
        navController = navController,
        startDestination = CharacterRoutes.List
    ) {
        composable(CharacterRoutes.List) {
            CharacterScreen(
                viewModel = viewModel,
                onCharacterClick = { characterId ->
                    navController.navigate(CharacterRoutes.detail(characterId))
                }
            )
        }

        composable(
            route = CharacterRoutes.Detail,
            arguments = listOf(navArgument(CharacterRoutes.CharacterIdArg) { type = NavType.IntType })
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getInt(CharacterRoutes.CharacterIdArg) ?: return@composable
            CharacterDetailScreen(
                viewModel = viewModel,
                characterId = characterId,
                onBackClick = navController::popBackStack
            )
        }
    }
}

private object CharacterRoutes {
    const val List = "characters"
    const val CharacterIdArg = "characterId"
    const val Detail = "character/{$CharacterIdArg}"

    fun detail(characterId: Int): String = "character/$characterId"
}
