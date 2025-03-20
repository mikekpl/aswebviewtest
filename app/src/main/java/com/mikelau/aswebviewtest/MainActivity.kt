package com.mikelau.aswebviewtest

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikelau.aswebview.InAppWebViewActivity
import com.mikelau.aswebviewtest.ui.theme.AppTheme
import com.mikelau.aswebviewtest.ui.theme.ColorMvp
import com.mikelau.aswebviewtest.ui.theme.ColorMvpGold75k
import com.mikelau.aswebviewtest.ui.theme.ColorPrimary
import com.mikelau.aswebviewtest.ui.theme.Purple40

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme(darkTheme = false) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val tokenTest = ""
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "The Standalone App",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(text = "Non Authenticated")
        Button(
            onClick = {
                val intent = Intent(context, InAppWebViewActivity::class.java)
                intent.putExtra("title", "Google")
                intent.putExtra("url", "https://google.com")
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                Purple40
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = "Google")
        }
        Button(
            onClick = {
                val intent = Intent(context, InAppWebViewActivity::class.java)
                intent.putExtra("title", "AS Upgrade to Premium")
                intent.putExtra("url", "https://www.alaskaair.com/content/travel-info/flight-experience/premium-class")
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                ColorPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        ) {
            Text(text = "AS Upgrade to Premium")
        }
        Button(
            onClick = {
                val intent = Intent(context, InAppWebViewActivity::class.java)
                intent.putExtra("title", "Perks")
                intent.putExtra("url", "https://www.alaskaair.com/account/perks")
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                ColorMvp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        ) {
            Text(text = "PerksHub Not Authenticated")
        }
        Text(text = "Authenticated", modifier = Modifier.padding(top = 16.dp))
        Button(
            onClick = {
                val intent = Intent(context, InAppWebViewActivity::class.java)
                intent.putExtra("title", "Perks")
                intent.putExtra("url", "https://www.alaskaair.com/account/perks")
                intent.putExtra("isAuthenticated", true)
                intent.putExtra("jwt", tokenTest)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                ColorMvpGold75k
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = "PerksHub")
        }
        Button(
            onClick = {
                val intent = Intent(context, InAppWebViewActivity::class.java)
                intent.putExtra("title", "Booking")
                intent.putExtra("url", "https://www.alaskaair.com/search")
                intent.putExtra("isAuthenticated", true)
                intent.putExtra("jwt", tokenTest)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                ColorPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        ) {
            Text(text = "AS Booking")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTheme {
        Greeting()
    }
}