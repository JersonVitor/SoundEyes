package com.jerson.soundeyes.feature_app.presentation.main

import PermissionsRequest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jerson.soundeyes.R
import com.jerson.soundeyes.feature_app.presentation.navGraph.Route



@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {

    var onPermissionGranted by remember{ mutableStateOf( false) }
    val listConfig =  listOf("Desempenho", "Qualidade")
    var selectOption by remember{ mutableStateOf("Desempenho")}
    val listSound  =  listOf("Desempenho", "Qualidade")

    PermissionsRequest {
        onPermissionGranted = true
    }
    Surface(modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(vertical = 30.dp, horizontal = 20.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {

            Image(painter = painterResource(id = R.drawable.logo_preview), contentDescription = "Imagem de uma pessoa com deficiencia visual usando fones de ouvido")
            Spacer(modifier = Modifier.height(30.dp))
            Text(text = "SoundEyes",
                style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(20.dp))
            Card(modifier = Modifier
                .fillMaxWidth()) {
                Text(text = "Imagem",
                    Modifier.padding(top = 20.dp, start = 20.dp, bottom = 10.dp),
                    style = MaterialTheme.typography.headlineSmall)
               Row (horizontalArrangement = Arrangement.SpaceAround
               ,modifier = Modifier.fillMaxWidth()){
                   listConfig.forEach {option ->
                       Row(modifier = Modifier,
                           verticalAlignment = Alignment.CenterVertically
                       ) {
                           RadioButton(
                               selected = selectOption == option,
                               onClick = { selectOption = option } // Atualiza o estado ao clicar
                           )
                           Spacer(modifier = Modifier.width(4.dp))
                           Text(text = option, style = MaterialTheme.typography.labelLarge)
                       }
                   }
               }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Card(modifier = Modifier
                .fillMaxWidth()) {
                Text(text = "Voz",
                    Modifier.padding(top = 20.dp, start = 20.dp, bottom = 10.dp),
                    style = MaterialTheme.typography.headlineSmall)
                Row (horizontalArrangement = Arrangement.SpaceAround
                    ,modifier = Modifier.fillMaxWidth()){
                    listSound.forEach {option ->
                        Row(modifier = Modifier,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectOption == option,
                                onClick = { selectOption = option } // Atualiza o estado ao clicar
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(text = option, style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                navController.navigate(Route.YoloClassifierScreen.route){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true }
            }) {
                Text(text = "Iniciar")
            }


        }
    }
}
