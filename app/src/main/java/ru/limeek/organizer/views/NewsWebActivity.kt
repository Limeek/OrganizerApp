package ru.limeek.organizer.views

import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_news_web.*
import ru.limeek.organizer.R

class NewsWebActivity : AppCompatActivity(){
    lateinit var webView: WebView
    lateinit var uri : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_web)

        supportActionBar!!.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        uri = intent.getStringExtra("uri") as String

        webView = webViewNews
        webView.webViewClient = WebViewClient()
        webView.loadUrl(uri)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item!!.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}