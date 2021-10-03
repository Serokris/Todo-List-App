package com.example.to_dolistapp.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

open class BaseBindingFragment<V : ViewBinding>(
    private val binder: (LayoutInflater, ViewGroup?, Boolean) -> V
) : Fragment() {

    private var contentBinding: V? = null

    protected val binding: V
        get() = requireNotNull(contentBinding) {
            "Binding is only valid between onCreateView and onDestroyView"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        contentBinding = binder.invoke(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        contentBinding = null
        super.onDestroyView()
    }
}