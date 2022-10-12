package com.example.appgrouppurchasemaching.matching

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.utils.UserDataModel


class ListViewAdapter(val context : Context, val items : MutableList<UserDataModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView

        if(convertView == null ){
            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.list_view_item, parent, false)
        }

        //내가 좋아요한 사람의 닉네임을 담아주기
        val nickname = convertView!!.findViewById<TextView>(R.id.listViewItemNickname)
        nickname.text = items[position].nickname

        // 이미지뷰 세팅  - 단, 매칭 성공한 애들에 한해서
        if (items[position].isMatch!!) {
            val matching = convertView!!.findViewById<ImageView>(R.id.matching_success)
            matching.visibility = View.VISIBLE //보이게 설정
        }


        return convertView!!
    }
}