package com.example.appgrouppurchasemaching.message

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.utils.UserDataModel

class MsgAdapter(val context : Context, val items : MutableList<MsgModel>) : BaseAdapter() {
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

        //화면 표시용
        val nicknameArea = convertView!!.findViewById<TextView>(R.id.listViewItemNicknameArea)
        val textArea = convertView!!.findViewById<TextView>(R.id.listViewItemNickname)
        nicknameArea.text = items[position].senderInfo
        textArea.text = items[position].sendTxt


        return convertView!!
    }
}