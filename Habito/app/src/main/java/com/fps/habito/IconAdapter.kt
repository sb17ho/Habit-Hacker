package com.fps.habito

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView

class IconAdapter(private var mContext:Context, private var allIcons:ArrayList<Int>) : ArrayAdapter<Int>(mContext, 0, allIcons){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var iconView : View? = convertView

        if(iconView == null){
            iconView = LayoutInflater.from(mContext).inflate(R.layout.icon_view, parent, false)
        }

        val imageV : ImageView = iconView!!.findViewById(R.id.iconView)
        imageV.setImageResource(allIcons[position])

        return iconView
    }

}