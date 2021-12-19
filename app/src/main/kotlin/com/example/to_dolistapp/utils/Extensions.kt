package com.example.to_dolistapp.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }
    })
}

fun <T> Fragment.collectOnLifecycle(flow: Flow<T>, collect: suspend (T) -> Unit): Job {
    return viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(collect)
        }
    }
}

fun Fragment.showToast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    requireContext().showToast(resId, duration)
}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    requireContext().showToast(message, duration)
}

fun Context.showToast(@StringRes resId: Int, duration: Int) {
    showToast(getString(resId), duration)
}

fun Context.showToast(message: String, duration: Int) {
    Toast.makeText(this, message, duration).show()
}