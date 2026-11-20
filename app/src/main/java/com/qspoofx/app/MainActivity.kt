package com.qspoofx.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val BGMI_PACKAGE = "com.pubg.imobile"

class MainActivity : ComponentActivity() {
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  setContent {
   var root by remember { mutableStateOf("Checking root…") }
   var thermal by remember { mutableStateOf("Not checked") }
   var session by remember { mutableStateOf(false) }
   var status by remember { mutableStateOf("Ready") }
   val device = "${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL} · Android ${android.os.Build.VERSION.RELEASE}"
   LaunchedEffect(Unit) {
    root = withContext(Dispatchers.IO) { Root.check() }
    thermal = withContext(Dispatchers.IO) { Root.thermal() }
   }
   MaterialTheme(colorScheme = darkColorScheme()) {
    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
     Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
      Text("QSpoof X", style = MaterialTheme.typography.headlineLarge)
      Text("BGMI Stable 90 Edition · v2.0")
      ElevatedCard { Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
       Text("BGMI profile", style = MaterialTheme.typography.titleLarge)
       Text("Target: 90 FPS · Priority: stable frame delivery")
       Text("Package: $BGMI_PACKAGE", style = MaterialTheme.typography.bodySmall)
       Button(onClick = {
        val i = packageManager.getLaunchIntentForPackage(BGMI_PACKAGE)
        if (i != null) { startActivity(i); status = "Launching BGMI" }
        else status = "BGMI India app not found (package $BGMI_PACKAGE)"
       }, modifier = Modifier.fillMaxWidth()) { Text("Launch BGMI") }
      } }
      ElevatedCard { Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
       Text("Session monitor", style = MaterialTheme.typography.titleLarge)
       Text(if (session) "Session active" else "Session stopped")
       Button(onClick = { session = !session }) { Text(if (session) "Stop session" else "Start session") }
       Text("This switch tracks your session only; it does not force CPU/GPU clocks.", style = MaterialTheme.typography.bodySmall)
      } }
      ElevatedCard { Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
       Text("Device & root", style = MaterialTheme.typography.titleLarge)
       Text(device); Text(root)
       OutlinedButton(onClick = { root = Root.check() }) { Text("Check root again") }
      } }
      ElevatedCard { Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
       Text("Thermal status (read-only)", style = MaterialTheme.typography.titleLarge)
       Text(thermal)
       OutlinedButton(onClick = { thermal = Root.thermal() }) { Text("Refresh thermal status") }
       Text("Thermal data depends on firmware. This app never disables thermal protection.", style = MaterialTheme.typography.bodySmall)
      } }
      ElevatedCard { Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
       Text("90 FPS target", style = MaterialTheme.typography.titleLarge)
       Text("90 FPS is a target, not an unlock. BGMI must support the option on your device and game version.")
       Text("No game files, anti-cheat, or device identity are modified.", style = MaterialTheme.typography.bodySmall)
      } }
      Text(status)
      Text("Live FPS/GPU telemetry and reversible tuning are not implemented in v2.0. This is a safe launcher and diagnostic build.", style = MaterialTheme.typography.bodySmall)
     }
    }
   }
  }
 }
}

private object Root {
 private fun run(command: String): Pair<Int,String> = try {
  val p = Runtime.getRuntime().exec(arrayOf("su","-c",command))
  val out = p.inputStream.bufferedReader().use { it.readText() }
  val err = p.errorStream.bufferedReader().use { it.readText() }
  val code = p.waitFor()
  Pair(code, (out.ifBlank { err }).trim())
 } catch (e: Exception) { Pair(-1, e.message ?: "su unavailable") }
 fun check(): String {
  val r = run("id")
  return if (r.first == 0 && r.second.contains("uid=0")) "Root access available" else "Root not confirmed"
 }
 fun thermal(): String {
  val r = run("dumpsys thermalservice | head -n 30")
  return if (r.first == 0 && r.second.isNotBlank()) r.second.take(850) else "Thermal service data unavailable"
 }
}
