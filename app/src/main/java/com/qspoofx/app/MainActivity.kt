package com.qspoofx.app
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
 override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContent { QSpoofXApp() } }
}
@Composable fun QSpoofXApp() {
 var rootStatus by remember { mutableStateOf("Checking root access…") }
 var selectedProfile by remember { mutableStateOf("Balanced") }
 var engineEnabled by remember { mutableStateOf(false) }
 LaunchedEffect(Unit) { rootStatus = withContext(Dispatchers.IO) { checkRoot() } }
 MaterialTheme(colorScheme = darkColorScheme()) {
  Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
   LazyColumn(contentPadding=PaddingValues(20.dp), verticalArrangement=Arrangement.spacedBy(16.dp)) {
    item { Text("QSpoof X", style=MaterialTheme.typography.headlineLarge); Text("Gaming toolkit · starter build") }
    item { ElevatedCard(Modifier.fillMaxWidth()) { Column(Modifier.padding(18.dp), verticalArrangement=Arrangement.spacedBy(8.dp)) {
     Text("Engine", style=MaterialTheme.typography.titleLarge); Text(if(engineEnabled) "Starter engine enabled" else "Engine idle")
     Button(onClick={engineEnabled=!engineEnabled}) { Text(if(engineEnabled) "Stop engine" else "Start engine") }
     Text(rootStatus, style=MaterialTheme.typography.bodySmall)
    } } }
    item { Text("Performance profile", style=MaterialTheme.typography.titleLarge) }
    items(listOf("Balanced","Performance","Battery Saver")) { profile ->
     Card(Modifier.fillMaxWidth()) { Row(Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment=Alignment.CenterVertically) {
      Column(Modifier.weight(1f)) { Text(profile, style=MaterialTheme.typography.titleMedium); Text("Profile selection preview (no tuning applied)") }
      RadioButton(selected=selectedProfile==profile,onClick={selectedProfile=profile})
     } }
    }
    item { ElevatedCard(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement=Arrangement.spacedBy(8.dp)) {
     Text("Modules",style=MaterialTheme.typography.titleLarge); Text("• Overlay monitoring — planned"); Text("• Reversible tuning — planned"); Text("• Game profiles — planned"); Text("• Restore defaults — planned")
    } } }
    item { Text("This starter only checks whether su is available. Profile buttons are UI-only.",style=MaterialTheme.typography.bodySmall) }
   }
  }
 }
}
private fun checkRoot(): String = try {
 val p=Runtime.getRuntime().exec(arrayOf("su","-c","id")); val s=p.inputStream.bufferedReader().readText().trim(); val c=p.waitFor()
 if(c==0 && s.contains("uid=0")) "Root access available" else "Root not confirmed"
} catch(_:Exception) { "Root access not detected" }
