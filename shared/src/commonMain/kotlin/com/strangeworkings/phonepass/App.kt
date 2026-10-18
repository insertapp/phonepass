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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer

import org.jetbrains.compose.resources.painterResource

import phonepass.shared.generated.resources.Res
import phonepass.shared.generated.resources.compose_multiplatform
import sh.calvin.autolinktext.rememberAutoLinkText


object Routes{
    const val Home = "Home"
    const val Passing = "Passing"
}

@Composable

fun AppNavigation(onAndroidAction: () -> Unit, sumbitProfile: () -> Unit, profdb: ProfileDB) {
    val navController = rememberNavController()

    // 2. Set up the NavHost with a start destination
    NavHost(navController = navController, startDestination = Routes.Home) {

        // First Page: Home Screen
        composable(Routes.Home) {
            Home(navController = navController, onAndroidAction, sumbitProfile, profdb)
        }

        // Second Page: Detail Screen
        composable(Routes.Passing) {
            Passing(profdb)
        }
    }
}

@Composable
fun Home(navController: NavController, onAndroidAction: ()-> Unit, sumbitProfile: ()-> Unit, profdb: ProfileDB) {
        MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var user = profdb.meQueries.GetProfile().executeAsOne()
            var savedUser by remember { mutableStateOf(user.full_name) }
            var savedPronouns by remember { mutableStateOf(user.pronouns) }
            var savedBio by remember { mutableStateOf(user.bio!!) }
            var userState = rememberTextFieldState(initialText = user.full_name)
            var pronounsState = rememberTextFieldState(initialText = user.pronouns)
            var bioState = rememberTextFieldState(initialText = user.bio!!)



            Spacer(
                modifier = Modifier.height(50.dp)
            )
            Text(text = "Welcome to phonepass! \n Please fill out your information below. This will be shared with others :)", fontSize = 20.sp, textAlign = TextAlign.Center  )
            Spacer(
                modifier = Modifier.height(40.dp)
            )

            TextField(
                state = userState,
                label = {Text("profile")}
            )

            Spacer(
                modifier = Modifier.height(30.dp)
            )

            TextField(
                state = pronounsState,
                label = {Text("pronouns")}
            )

            Spacer(
                modifier = Modifier.height(30.dp)
            )


            val maxChar = 325

            TextField(
                value = savedBio,
                onValueChange = { newValue ->
                    if (newValue.length <= maxChar) {
                        savedBio = newValue //
                    }
                },
                label = { Text("Bio") }
            )
            val tester = UserProfile(savedUser, savedPronouns, savedBio)
            Spacer(Modifier.weight(1f))
            Button(onClick = {
                savedUser = userState.text.toString()
                savedPronouns = pronounsState.text.toString()
                tester.setthisPronouns(savedPronouns)
                tester.setthisName(savedUser)
                tester.setthisBio(savedBio)
                profdb.meQueries.UpdateProfile(savedUser,savedPronouns,savedBio)
                navController.navigate("Passing")
                onAndroidAction()

            }){
                Text("Save & View Passes")
            }
            Spacer(Modifier.height(50.dp))
        }
    }
}

@Composable
fun Passing(profdb: ProfileDB){
    var printpasses by remember { mutableStateOf(emptyList<profiles.data.Profiles>()) }
    LaunchedEffect(profdb) {
        while (true) {
            printpasses = profdb.profleQueries.GetProfiles().executeAsList()
            delay(3000)
        }
    }
    MaterialTheme {


        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            Text("Here are all your passes!", fontSize = 20.sp)

            Spacer(modifier = Modifier.height(50.dp))

            LazyColumn(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .safeContentPadding()
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                items(printpasses) { printpasses ->
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                        modifier = Modifier
                            .size(width = 300.dp, height = 150.dp)
                    ) {
                        Text(
                            text = AnnotatedString.rememberAutoLinkText("Name: ${printpasses.full_name}\nPronouns: ${printpasses.pronouns}\nBio: ${printpasses.bio!!}"),
                            modifier = Modifier
                                .padding(16.dp),
                            textAlign = TextAlign.Left,
                        )

                    }
                    Spacer(modifier = Modifier.height(25.dp))

                }

            }
        }
    }
}