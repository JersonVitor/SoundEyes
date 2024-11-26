package com.jerson.soundeyes.feature_app.presentation.main

import PermissionsRequest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

import com.jerson.soundeyes.feature_app.presentation.navGraph.Route



@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {

    var onPermissionGranted by remember{ mutableStateOf( false) }


    PermissionsRequest {
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

            Button(onClick = { navController.navigate(Route.YoloClassifierScreen.route){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            } }) {
                Text(text = "Yolo")
            }


        }
    }
}