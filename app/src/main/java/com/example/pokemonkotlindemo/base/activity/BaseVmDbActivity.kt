package com.example.pokemonkotlindemo.base.activity

import android.view.View
import androidx.databinding.ViewDataBinding
import com.example.pokemonkotlindemo.base.utils.inflateBindingWithGeneric
import com.example.pokemonkotlindemo.base.viewmodel.BaseViewModel

abstract class BaseVmDbActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmActivity<VM>() {

    override fun layoutId() = 0

    lateinit var mDatabind: DB

    /**
     * 创建DataBinding
     */
    override fun initDataBind(): View? {
        mDatabind = inflateBindingWithGeneric(layoutInflater)
        return mDatabind.root
    }
}