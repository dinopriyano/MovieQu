package com.dupat.newsqu.utils

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.dupat.newsqu.R
import com.google.android.material.snackbar.Snackbar

fun View.snackbar(msg: String, ctx: Context){
    Snackbar.make(this,msg, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setBackgroundTint(ContextCompat.getColor(ctx, R.color.white))
        snackbar.setTextColor(ContextCompat.getColor(ctx, R.color.black))
        snackbar.setActionTextColor(ContextCompat.getColor(ctx, R.color.black))
        snackbar.setAction("OK") {
            snackbar.dismiss()
        }
    }.show()
}