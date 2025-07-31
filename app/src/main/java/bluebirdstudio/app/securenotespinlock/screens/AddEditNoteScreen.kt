package bluebirdstudio.app.securenotespinlock.screens

import android.app.Activity
import android.net.Uri
import android.view.View
import android.webkit.WebView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import bluebirdstudio.app.securenotespinlock.model.Note
import bluebirdstudio.app.securenotespinlock.model.NotesViewModel
import bluebirdstudio.app.securenotespinlock.ui.components.RichTextEditor
import com.yalantis.ucrop.UCrop
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteId: Int?,
    viewModel: NotesViewModel
) {
    val note by viewModel.currentNote.collectAsState()

    var title by remember { mutableStateOf("") }
    var formattedContent by remember { mutableStateOf("") }

    var showExitDialog by remember { mutableStateOf(false) }

    var initialTitle by remember { mutableStateOf("") }
    var initialContent by remember { mutableStateOf("") }

    // WebView reference
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    val context = LocalContext.current

    // Launcher برای نتیجه uCrop
    val cropLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val resultUri = result.data?.let { UCrop.getOutput(it) }
            resultUri?.let { uri ->
                // خواندن فایل و تبدیل به Base64
                val inputStream = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes()
                inputStream?.close()

                if (bytes != null) {
                    val base64Image = android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
                    val dataUrl = "data:image/jpeg;base64,$base64Image"

                    // درج در Quill
                    webViewRef?.post {
                        webViewRef?.evaluateJavascript(
                            "quill.insertEmbed(quill.getSelection(true).index, 'image', '$dataUrl');",
                            null
                        )
                    }
                }
            }
        }
    }


    // Launcher برای گالری → فرستادن به uCrop
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // فایل خروجی کراپ داخل filesDir
            val destinationFile = File(context.filesDir, "cropped_image_${System.currentTimeMillis()}.jpg")
            val destinationUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                destinationFile
            )

            // تنظیمات uCrop
            val options = UCrop.Options().apply {
                setFreeStyleCropEnabled(true)        // اجازه کراپ آزاد
                setMaxScaleMultiplier(10f)           // بزرگنمایی تا 10 برابر
            }

            val intent = UCrop.of(it, destinationUri)
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(1080, 1080)
                .withOptions(options)
                .getIntent(context)

            cropLauncher.launch(intent)
        }
    }


    LaunchedEffect(noteId) {
        if (noteId != null) {
            viewModel.loadNoteById(noteId)
        } else {
            viewModel.resetCurrentNote()
        }
    }

    LaunchedEffect(note) {
        note?.let {
            title = it.title
            formattedContent = it.content
            initialTitle = it.title
            initialContent = it.content
        }
    }

    val isChanged = title != initialTitle || formattedContent != initialContent

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (noteId == null) "New Note" else "Edit Note") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (isChanged) {
                            showExitDialog = true
                        } else {
                            viewModel.resetCurrentNote()
                            navController.popBackStack()
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.saveNote(
                            Note(
                                id = note?.id ?: 0,
                                title = title,
                                content = formattedContent
                            )
                        )
                        viewModel.resetCurrentNote()
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Title") },
                textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Toolbar ابزارها
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                // OCR
                IconButton(onClick = { /* TODO: OCR */ }) {
                    Icon(Icons.Default.DocumentScanner, contentDescription = "OCR")
                }
                Spacer(modifier = Modifier.width(16.dp))

                // Voice
                IconButton(onClick = { /* TODO: Voice to Text */ }) {
                    Icon(Icons.Default.Mic, contentDescription = "Voice to Text")
                }
                Spacer(modifier = Modifier.width(16.dp))

                // Image
                IconButton(onClick = { imagePicker.launch("image/*") }) {
                    Icon(Icons.Default.AddPhotoAlternate, contentDescription = "Insert Image")
                }

                Spacer(modifier = Modifier.width(24.dp))

                // Divider ساده
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(24.dp)
                        .padding(horizontal = 4.dp)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
                )

                Spacer(modifier = Modifier.width(24.dp))

                // AI
                IconButton(onClick = { /* TODO: AI */ }) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = "AI Suggestions")
                }
            }

            // Rich Text Editor
            RichTextEditor(
                initialContent = formattedContent,
                onContentChanged = { newContent -> formattedContent = newContent },
                modifier = Modifier.fillMaxSize(),
                onWebViewCreated = { webView ->
                    webViewRef = webView
                    ViewCompat.setOnApplyWindowInsetsListener(webView) { _: View, insets ->
                        val isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
                        webView.evaluateJavascript("setKeyboardState($isKeyboardVisible)", null)
                        insets
                    }
                }
            )
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Discard changes?") },
            text = { Text("You have unsaved changes. Are you sure you want to go back?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.resetCurrentNote()
                    navController.popBackStack()
                }) {
                    Text("Discard")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
