package com.keyboard3.practice

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import com.keyboard3.practice.ui.theme.KotlinPracticeTheme
import com.keyboard3.practice.ui.theme.Teal200
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KotlinPracticeTheme {
                val model: MyViewModel by viewModels()

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainContent(model)
                }
            }
        }
    }
}

@Composable
fun MainContent(viewModel: MyViewModel) {
    val cats = viewModel.getUsers().observeAsState();
    Column {
        cats.value?.forEach { cat ->
            Text(
                text = "名字: ${cat.name}",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.h5
            )
            Text(
                text = "年龄: ${cat.age}",
                modifier = Modifier.padding(bottom = 4.dp),
                style = MaterialTheme.typography.h6
            )
            Text(
                text = "品种: ${cat.breed}",
                modifier = Modifier.padding(bottom = 4.dp),
                style = MaterialTheme.typography.h6
            )
            Divider(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .padding(start = 10.dp, end = 10.dp),
                color = Teal200,
            )
        }

    }

}

val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
    throwable.printStackTrace()
}
class MyViewModel: ViewModel() {
    private val users: MutableLiveData<List<Cat>> by lazy {
        MutableLiveData<List<Cat>>().also {
            it.value = emptyList();
            loadCats()
        }
    }

    fun getUsers(): LiveData<List<Cat>> {
        return users
    }

    private fun loadCats() {
        viewModelScope.launch(Dispatchers.IO+coroutineExceptionHandler) {
            val result = ApiService.getInstance().getCats();
            Log.d("practice","result:"+result.body()?.size)
            if (result != null)
                users.postValue(result.body())
        }
    }
}

@Composable
fun HelloText() {
    Text (
        text="hello"
    )
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HelloText()
}