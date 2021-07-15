package com.dupat.newsqu.utils

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.dupat.newsqu.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

fun View.snackbar(msg: String, ctx: Context) {
    Snackbar.make(this, msg, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setBackgroundTint(ContextCompat.getColor(ctx, R.color.white))
        snackbar.setTextColor(ContextCompat.getColor(ctx, R.color.black))
        snackbar.setActionTextColor(ContextCompat.getColor(ctx, R.color.black))
        snackbar.setAction("OK") {
            snackbar.dismiss()
        }
    }.show()
}

@ExperimentalCoroutinesApi
fun EditText.textChanges() : Flow<CharSequence?> {
    return callbackFlow<CharSequence?> {
        val listener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun afterTextChanged(s: Editable?) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                trySend(s)
            }
        }
        addTextChangedListener(listener)
        awaitClose{removeTextChangedListener(listener)}
    }.onStart { emit(text) }
}

fun Context.shareNews(url: String?){
    val sendIntent = Intent()
    sendIntent.action = Intent.ACTION_SEND
    sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi, i've read news at ${url ?: "https://newsapi.org"}")
    sendIntent.type = "text/plain"
    startActivity(sendIntent)
}