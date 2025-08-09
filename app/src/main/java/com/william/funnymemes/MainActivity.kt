package com.william.funnymemes

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.william.funnymemes.model.Meme
import com.william.funnymemes.screens.DetailsScreen
import com.william.funnymemes.screens.HomePage
import com.william.funnymemes.screens.LoginPage
import com.william.funnymemes.screens.MainScreen
import com.william.funnymemes.screens.SignupPage
import com.william.funnymemes.ui.theme.AuthViewModel
import com.william.funnymemes.ui.theme.FunnyMemesTheme
import com.william.funnymemes.utils.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class MainActivity : ComponentActivity() {
    @SuppressLint("RememberReturnType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FunnyMemesTheme {

                val navController = rememberNavController()
                var memesList by remember {
                    mutableStateOf(listOf<Meme>())
                }


                val scope = rememberCoroutineScope()

                LaunchedEffect(key1 = true){
                    scope.launch(Dispatchers.IO) {
                        val response = try {
                            RetrofitInstance.api.getMemesList()
                        }catch (e: IOException){
                            Toast.makeText(
                                this@MainActivity,
                                "app error: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@launch
                        }catch (e : HttpException){
                            Toast.makeText(
                                this@MainActivity,
                                "HTTP error: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()

                            return@launch
                        }
                        if (response.isSuccessful && response.body() != null){
                            withContext(Dispatchers.Main){
                                memesList = response.body()!!.data.memes
                            }
                        }
                    }
                }

                NavHost(navController = navController, startDestination = "MainScreen" ){

                    composable(route = "MainScreen"){
                        MainScreen(memesList = memesList, navController = navController)
                    }

                    composable (route = "HomeScreen"){
                        HomePage(
                            navController = navController,
                            modifier = Modifier,
                            authViewModel = AuthViewModel
                        )
                    }
                    composable (route = "LoginPage"){
                        LoginPage(
                            navController = navController,
                            modifier = TODO(),
                            authViewModel = TODO(),
                        )
                    }
                    composable (route = "HomeScreen?"){
                        SignupPage(
                            navController = navController,
                            modifier = TODO(),
                            authViewModel = TODO()
                        )
                    }

                    composable(route = "DetailsScreen?name={name}&url={url}",
                        arguments = listOf(
                            navArgument(name = "name"){
                                type = NavType.StringType
                            },
                            navArgument(name = "url"){
                                type = NavType.StringType
                            }
                        )
                    ){
                        DetailsScreen(
                            name= it.arguments?.getString("name"),
                            url= it.arguments?.getString("url")
                        )
                    }

                }
            }
        }
    }
}




