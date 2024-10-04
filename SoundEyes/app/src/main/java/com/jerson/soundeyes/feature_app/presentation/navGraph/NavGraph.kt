package com.jerson.soundeyes.feature_app.presentation.navGraph

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jerson.soundeyes.feature_app.presentation.camera.CameraScreen
import com.jerson.soundeyes.feature_app.presentation.yoloClassifier.YoloClassifierScreen
import com.jerson.soundeyes.feature_app.presentation.main.MainScreen
import com.jerson.soundeyes.feature_app.presentation.mobNetClassifier.MobNetScreen

@Composable
fun NavGraph(navHostController: NavHostController) {

    NavHost(navController = navHostController, startDestination = Route.MainScreen.route) {
        composable(Route.MainScreen.route) {MainScreen(navController = navHostController) }

        composable(Route.YoloClassifierScreen.route) { backStackEntry ->
            val imageUri = backStackEntry.arguments?.getString("imageUri") ?: ""
            YoloClassifierScreen(imageUri = Uri.decode(imageUri))}

        composable(Route.MobileNetScreen.route) { backStackEntry ->
            val imageUri = backStackEntry.arguments?.getString("imageUri") ?: ""
            MobNetScreen(imageUri = Uri.decode(imageUri))}

        composable(Route.CameraScreen.route){ backStackEntry ->
            val destination = backStackEntry.arguments?.getString("destination") ?: ""
            CameraScreen(navController = navHostController, destination = destination)
        }
    }
}