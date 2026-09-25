package com.matthewblott.jimlog

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dev.hotwire.navigation.activities.HotwireActivity
import dev.hotwire.navigation.navigator.NavigatorConfiguration
import dev.hotwire.navigation.util.applyDefaultImeWindowInsets
import dev.hotwire.core.bridge.KotlinXJsonConverter
import dev.hotwire.core.config.Hotwire
import dev.hotwire.navigation.config.registerBridgeComponents
import dev.hotwire.core.bridge.BridgeComponentFactory
import com.matthewblott.jimlog.components.BackComponent
import com.masilotti.bridgecomponents.button.ButtonComponent
import com.matthewblott.jimlog.components.AuthenticatedComponent
import dev.hotwire.core.turbo.config.PathConfiguration

class MainActivity : HotwireActivity() {
//  companion object {
//    private const val REQUEST_CODE_LOCAL_NETWORK = 1001
//  }

  private val requestLocalNetwork = registerForActivityResult(
    ActivityResultContracts.RequestPermission()
  ) { /* proceed regardless; Hotwire will just fail to load if denied */ }

  private fun ensureLocalNetworkPermission() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_LOCAL_NETWORK)
      != PackageManager.PERMISSION_GRANTED) {
      requestLocalNetwork.launch(Manifest.permission.ACCESS_LOCAL_NETWORK)
    }
  }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    ensureLocalNetworkPermission()
    Hotwire.loadPathConfiguration(
      context = this,
      location = PathConfiguration.Location(
        assetFilePath = "json/path-configuration.json",
      ),
    )

    Hotwire.config.jsonConverter = KotlinXJsonConverter()
    Hotwire.registerBridgeComponents(
      BridgeComponentFactory("button", ::ButtonComponent),
      BridgeComponentFactory("back", ::BackComponent),
      BridgeComponentFactory("authenticated", ::AuthenticatedComponent),
      )

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    findViewById<View>(R.id.main_nav_host).applyDefaultImeWindowInsets()

//    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_LOCAL_NETWORK)
//      != PackageManager.PERMISSION_GRANTED) {
//      ActivityCompat.requestPermissions(
//        this,
//        arrayOf(Manifest.permission.ACCESS_LOCAL_NETWORK),
//        REQUEST_CODE_LOCAL_NETWORK
//      )
//    }
  }

  override fun navigatorConfigurations() = listOf(
    NavigatorConfiguration(
      name = "main",
      startLocation = Settings.current.url,
      navigatorHostId = R.id.main_nav_host
    )
  )
}