package com.strangeworkings.phonepass

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }

        val tester = Profiles("jane doe", "she/her", bio = "yes")
        Toast.makeText(this, tester.toString(), Toast.LENGTH_LONG).show()
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}