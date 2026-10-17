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
import org.jetbrains.compose.resources.painterResource

import phonepass.shared.generated.resources.Res
import phonepass.shared.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
        sign_up_menu()
    }
}

@Composable
fun sign_up_menu(){
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

        Spacer(Modifier.weight(1f))
        Button(onClick = {
            savedUser = userState.text.toString()
            savedPronouns = pronounsState.text.toString()
            savedBio = bioState.text.toString()
        }){
            Text("submit")
        }
    }
}