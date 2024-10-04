package com.jerson.soundeyes.feature_app.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jerson.soundeyes.feature_app.camera.CameraPermissionRequest
import com.jerson.soundeyes.feature_app.presentation.navGraph.NavGraph
import com.jerson.soundeyes.feature_app.presentation.navGraph.Route
import com.jerson.soundeyes.ui.theme.SoundEyesTheme


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    mainViewModel: MainViewModel = hiltViewModel()
) {

    var onPermissionGranted by remember{ mutableStateOf( false) }

    CameraPermissionRequest {
        onPermissionGranted = true
    }
    Surface(modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Button(onClick = { navController.navigate("cameraScreen/yoloClassifierScreen"){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            } }) {
                Text(text = "Yolo")
            }
            Button(onClick = { navController.navigate("cameraScreen/mobileNetScreen"){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            } }) {
                Text(text = "MobileNet")
            }

        }
    }
}