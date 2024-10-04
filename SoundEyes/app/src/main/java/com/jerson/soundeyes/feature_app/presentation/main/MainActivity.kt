package com.jerson.soundeyes.feature_app.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.jerson.soundeyes.feature_app.presentation.navGraph.NavGraph
import com.jerson.soundeyes.ui.theme.SoundEyesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoundEyesTheme {
                // A surface container using the background color from the theme
                //com.jerson.soundeyes.feature_app.camera.CameraPermissionRequest()
                val navController = rememberNavController()
                NavGraph(navHostController = navController)
            }
        }
    }
}


