package com.strangeworkings.phonepass

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer

import org.jetbrains.compose.resources.painterResource

import phonepass.shared.generated.resources.Res
import phonepass.shared.generated.resources.compose_multiplatform




object Routes{
    const val Home = "Home"
    const val Passing = "Passing"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // 2. Set up the NavHost with a start destination
    NavHost(navController = navController, startDestination = Routes.Home) {

        // First Page: Home Screen
        composable(Routes.Home) {
            Home(navController = navController)
        }

        // Second Page: Detail Screen
        composable(Routes.Passing) {
            Passing()
        }
    }
}

@Composable
fun Home(navController: NavController) {
        MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var savedUser by remember { mutableStateOf("") }
            var savedPronouns by remember { mutableStateOf("") }
            var savedBio by remember { mutableStateOf("") }
            var userState = rememberTextFieldState(initialText = "")
            var pronounsState = rememberTextFieldState(initialText = "")
            var bioState = rememberTextFieldState(initialText = "")
            TextField(
                state = userState,
                label = {Text("profile")}
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            TextField(
                state = pronounsState,
                label = {Text("pronouns")}
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            TextField(
                state = bioState,
                label = {Text("bio")}
            )
            Text("User: $savedUser")
            Text("Pronouns: $savedPronouns")
            Text("Bio: $savedBio")
            var tester = Profiles(savedUser, savedPronouns, savedBio)
            var testing = tester.toString()
            Text("profile: $testing")
            Spacer(Modifier.weight(1f))
            Button(onClick = {
                savedUser = userState.text.toString()
                savedPronouns = pronounsState.text.toString()
                savedBio = bioState.text.toString()
                tester.setthisPronouns(savedPronouns)
                tester.setthisName(savedUser)
                tester.setthisBio(savedBio)
                testing = tester.toString()
                navController.navigate("Passing")

            }){
                Text("submit")
            }
            /*
            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Test: $greeting")
                }
            }

             */
        }
    }
}

@Composable
fun Passing(){
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Button(onClick = {


            }) {
                Text("start passing")
            }
        }
    }
}