package com.ti4all.agendaapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.ti4all.agendaapp.data.BookViewModel
import com.ti4all.agendaapp.data.Livro
import com.ti4all.agendaapp.data.LivroViewModel
import com.ti4all.agendaapp.ui.theme.AgendaAppTheme

class MainActivity : ComponentActivity() {
    private val bookViewModel: BookViewModel by viewModels()
    private val livroViewModel: LivroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AgendaAppTheme {
                BookListScreen(viewModel = bookViewModel, livroViewModel = livroViewModel)
            }
        }
    }
}

@Composable
fun BookListScreen(
    viewModel: BookViewModel = viewModel(),
    livroViewModel: LivroViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    val personalBooks by livroViewModel.livroList.collectAsState()
    val livrosApi by viewModel.livros

    val filteredBooks = livrosApi.filter { apiBook ->
        !personalBooks.any { personalBook -> personalBook.titulo == apiBook.titulo }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "MyBooksList",
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        TextField(
            value = searchQuery,
            onValueChange = { newValue ->
                searchQuery = newValue
                isSearching = newValue.isNotBlank()
            },
            label = { Text("Digite o nome do livro") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        Button(
            onClick = {
                viewModel.fetchBooks(searchQuery)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Buscar")
        }

        if (!isSearching) {
            Text(
                text = "Livros Pessoais",
                color = Color.Black,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(vertical = 8.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )
        }

        LazyColumn {
            val booksToShow = if (isSearching) filteredBooks else personalBooks

            if (!isSearching && personalBooks.isEmpty()) {
                item {
                    Text(
                        text = "Nenhum livro adicionado ainda, pesquise por algum livro e o adicione a sua lista!",
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp)
                    )
                }
            } else {
                items(booksToShow) { livro ->
                    LivroItem(livro, livroViewModel)
                }
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun LivroItem(livro: Livro, livroViewModel: LivroViewModel) {
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    var isUpdateFormVisible by remember { mutableStateOf(false) }
    var isLido by remember { mutableStateOf(livro.lido) }

    val originalLidoState = remember { mutableStateOf(livro.lido) }
    val isBookInPersonalList = livroViewModel.livroList.value.any { it.titulo == livro.titulo }

    Row(modifier = Modifier.padding(8.dp)) {
        val imagemCapaURL = livro.imagemCapa?.let {
            if (it.startsWith("http://")) {
                it.replace("http://", "https://")
            } else {
                it
            }
        }
        AsyncImage(
            model = imagemCapaURL,
            contentDescription = "Imagem de teste",
            modifier = Modifier.size(150.dp),
            onError = {
                Log.e("ImageLoadingError", "Erro ao carregar a imagem: ${it.result.throwable.message}")
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text("Título: ${livro.titulo}")
            Text("Data de Publicação: ${livro.dataPublicacao}")
            Text("Categoria: ${livro.categoria}")
            Text("Número de Páginas: ${livro.numeroDePaginas}")
            Text("Avaliação Média: ${livro.avaliacaoMedia}")

            if (isBookInPersonalList) {
                if (isLido) {
                    Text("Status: Lido", color = Color.Green, modifier = Modifier.padding(top = 8.dp))
                } else {
                    Text("Status: Não Lido", color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                }
            }

            if (!isBookInPersonalList) {
                Button(
                    onClick = {
                        livroViewModel.inserir(livro)
                        snackbarMessage = "Livro adicionado com sucesso!"
                        showSnackbar = true
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Adicionar à minha lista")
                }
            } else {
                Text(
                    text = "Este livro já está na sua lista!",
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Button(
                    onClick = {
                        isUpdateFormVisible = true
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Atualizar livro")
                }

                Button(
                    onClick = {
                        livroViewModel.deletar(livro.id.toInt())
                        snackbarMessage = "Livro removido com sucesso!"
                        showSnackbar = true
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Deletar livro")
                }
            }

            // Mover o botão de compartilhar para dentro de uma função composable
            ShareButton(livro = livro)
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    if (isUpdateFormVisible) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Atualizar Status de Leitura", style = MaterialTheme.typography.titleLarge)

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Lido?")
                Checkbox(
                    checked = isLido,
                    onCheckedChange = { isLido = it }
                )
            }

            Button(
                onClick = {
                    val updatedLivro = livro.copy(lido = isLido)
                    livroViewModel.atualizar(updatedLivro)
                    snackbarMessage = "Livro atualizado com sucesso!"
                    showSnackbar = true
                    isUpdateFormVisible = false
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Salvar alterações")
            }

            Button(
                onClick = {
                    isLido = originalLidoState.value
                    isUpdateFormVisible = false
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Cancelar")
            }
        }
    }

    if (showSnackbar) {
        Snackbar(
            action = {
                Button(onClick = { showSnackbar = false }) {
                    Text("OK")
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = snackbarMessage)
        }
    }
}

@Composable
fun ShareButton(livro: Livro) {
    val context = LocalContext.current // Agora estamos dentro de uma função composable

    Button(
        onClick = {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT,
                    "Confira o livro '${livro.titulo}'\n\n" +
                            "Categoria: ${livro.categoria}\n" +
                            "Avaliação Média: ${livro.avaliacaoMedia}\n" +
                            "Número de Páginas: ${livro.numeroDePaginas}\n\n" +
                            "Confira mais sobre esse livro no seu app favorito!")
                type = "text/plain"
            }
            context.startActivity(Intent.createChooser(shareIntent, "Compartilhar com"))
        },
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Text("Compartilhar")
    }
}
