package com.example.pokemonkotlindemo.widget

import android.graphics.Color
import android.text.TextUtils
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.pokemonkotlindemo.R
import com.example.pokmonkotlindemo.NameSearchQuery

class PokemonAdapter(data: MutableList<NameSearchQuery.Pokemon_v2_pokemonspecy>)
    :BaseDelegateMultiAdapter<NameSearchQuery.Pokemon_v2_pokemonspecy, BaseViewHolder>(data){

    init {
        // 第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<NameSearchQuery.Pokemon_v2_pokemonspecy>() {
            override fun getItemType(data: List<NameSearchQuery.Pokemon_v2_pokemonspecy>, position: Int): Int {
                return 0
            }
        })
        // 第二步，绑定 item 类型
        getMultiTypeDelegate()?.let {
            it.addItemType(0, R.layout.item_pokemon)
        }
    }

    override fun convert(holder: BaseViewHolder, item: NameSearchQuery.Pokemon_v2_pokemonspecy) {
        item.run {
            holder.setText(R.id.name, "name: ${item.name}")
            holder.setText(R.id.capture_rate, "capture_rate: ${item.capture_rate}")
            when(item.pokemon_v2_pokemoncolor?.id) {
                1 -> holder.setBackgroundColor(R.id.content_layout, context.resources.getColor(android.R.color.black))
                2 -> holder.setBackgroundColor(R.id.content_layout, context.resources.getColor(android.R.color.holo_blue_light))
                3 -> holder.setBackgroundColor(R.id.content_layout, Color.parseColor("#A67D3D"))
                4 -> holder.setBackgroundColor(R.id.content_layout, context.resources.getColor(android.R.color.darker_gray))
                5 -> holder.setBackgroundColor(R.id.content_layout, context.resources.getColor(android.R.color.holo_green_light))
                6 -> holder.setBackgroundColor(R.id.content_layout, Color.parseColor("#BC8F8F"))
                7 -> holder.setBackgroundColor(R.id.content_layout, context.resources.getColor(android.R.color.holo_purple))
                8 -> holder.setBackgroundColor(R.id.content_layout, context.resources.getColor(android.R.color.holo_red_light))
                9 -> holder.setBackgroundColor(R.id.content_layout, context.resources.getColor(android.R.color.white))
                10 -> holder.setBackgroundColor(R.id.content_layout, Color.parseColor("#FFFF00"))

                else -> holder.setBackgroundColor(R.id.content_layout, context.resources.getColor(android.R.color.white))

            }
        }
    }
}