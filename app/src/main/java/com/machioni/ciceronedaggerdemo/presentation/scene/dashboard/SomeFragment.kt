package com.machioni.ciceronedaggerdemo.presentation.scene.dashboard

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.machioni.ciceronedaggerdemo.R

class SomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_some, container, false)
    }

    companion object {
        fun newInstance() = SomeFragment()
    }
}