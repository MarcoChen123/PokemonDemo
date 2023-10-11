package com.example.pokemonkotlindemo.base.activity

import android.view.View
import androidx.viewbinding.ViewBinding
import com.example.pokemonkotlindemo.base.utils.inflateBindingWithGeneric
import com.example.pokemonkotlindemo.base.viewmodel.BaseViewModel

abstract class BaseVmVbActivity<VM : BaseViewModel, VB : ViewBinding> : BaseVmActivity<VM>() {

    override fun layoutId(): Int = 0

    lateinit var mViewBind: VB

    /**
     * 创建DataBinding
     */
    override fun initDataBind(): View? {
        mViewBind = inflateBindingWithGeneric(layoutInflater)
        return mViewBind.root

    }
}