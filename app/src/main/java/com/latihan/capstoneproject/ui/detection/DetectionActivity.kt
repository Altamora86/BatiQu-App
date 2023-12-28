package com.latihan.capstoneproject.ui.detection

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.latihan.capstoneproject.R

@Suppress("DEPRECATION")
class DetectionActivity : AppCompatActivity() {

    private val STORAGE_PERMISSION_REQUEST_CODE = 1001
    private var filePathCallback: ValueCallback<Array<Uri>>? = null
    private var fileChooserParams: WebChromeClient.FileChooserParams? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val webView = findViewById<WebView>(R.id.webView)

        // Check and request storage permission
        if (hasStoragePermission()) {
            initializeWebView(webView)
        } else {
            requestStoragePermission()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initializeWebView(webView: WebView) {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowFileAccess = true
        webView.settings.allowContentAccess = true

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                view.loadUrl("javascript:alert('Detection Menu is loading')")
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(view: WebView, url: String, message: String, result: android.webkit.JsResult): Boolean {
                Toast.makeText(this@DetectionActivity, message, Toast.LENGTH_LONG).show()
                result.confirm()
                return true
            }

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                this@DetectionActivity.filePathCallback = filePathCallback
                this@DetectionActivity.fileChooserParams = fileChooserParams

                // Implement file chooser logic here
                openFileChooser()

                return true
            }
        }

        webView.loadUrl("https://flask-app-mrd65lqryq-et.a.run.app/")
    }

    private fun hasStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, initialize WebView
                val webView = findViewById<WebView>(R.id.webView)
                initializeWebView(webView)
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
                // You may want to finish the activity or show a message to the user
            }
        }
    }

    private fun openFileChooser() {
        val intent = fileChooserParams?.createIntent()
        if (intent != null) {
            startActivityForResult(intent, FILE_CHOOSER_REQUEST_CODE)
        }
    }

    companion object {
        private const val FILE_CHOOSER_REQUEST_CODE = 1002
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILE_CHOOSER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val result = if (data == null || data.data == null) {
                    // Handle the case where the file is selected from camera or other sources
                    fileChooserParams?.let { arrayOf(it.createIntent().data!!) } ?: emptyArray()
                } else {
                    // Handle the case where the file is selected from the device storage
                    arrayOf(data.data!!)
                }
                filePathCallback?.onReceiveValue(result)
            } else {
                filePathCallback?.onReceiveValue(null)
            }
            filePathCallback = null
        }
    }

}
