package com.example.appgrouppurchasemaching.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appgrouppurchasemaching.databinding.FragmentMenuControlBinding

class MenuControlFragment : Fragment() { //메뉴 컨트롤할 프래그먼트

    //바인딩
    lateinit var binding: FragmentMenuControlBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //바인딩
        binding = FragmentMenuControlBinding.inflate(inflater)
        //title
        binding.menuControlToolbar.title = "메뉴 카테고리"

        //Back 버튼을 툴바 상단의 navigationIcon으로 추가한다.
        val navIcon = requireContext().getDrawable(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.menuControlToolbar.navigationIcon = navIcon

        //수정) 뒤로가기 네비게이션 클릭 이벤트 처리
        binding.menuControlToolbar.setNavigationOnClickListener {
            val act = activity as BoardMainActivity
            act.fragmentRemoveBackStack("menu_controller")
        }


        return binding.root
    }

}