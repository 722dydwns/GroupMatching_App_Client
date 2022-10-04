package com.example.appgrouppurchasemaching.board

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appgrouppurchasemaching.service.ServiceActivity
import com.example.appgrouppurchasemaching.databinding.FragmentMenuControlBinding
import com.example.appgrouppurchasemaching.message.MyLikeListActivity
import com.example.appgrouppurchasemaching.message.MyMsgActivity

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

        //'전체 게시글' 목록 이동
        binding.all.setOnClickListener {
            val act = activity as BoardMainActivity
            act.selectedBoardType = 0
            act.fragmentController("board_main", true, true)
        }
        //'배달 음식' 공구 목록 이동
        binding.delivery.setOnClickListener {
            val act = activity as BoardMainActivity
            act.selectedBoardType = 1
            act.fragmentController("board_main", true, true)
        }
        //'일반 잡화' 공구 목록 이동
        binding.general.setOnClickListener {
            val act = activity as BoardMainActivity
            act.selectedBoardType = 2
            act.fragmentController("board_main", true, true)
        }
        //'의류' 공구 목록 이동
        binding.clothes.setOnClickListener {
            val act = activity as BoardMainActivity
            act.selectedBoardType = 3
            act.fragmentController("board_main", true, true)
        }
        //'회원권 양도' 글 목록 이동
        binding.toss.setOnClickListener {
            val act = activity as BoardMainActivity
            act.selectedBoardType = 4
            act.fragmentController("board_main", true, true)
        }

        //'지도' 클릭 시 지도 액티비티로 화면 전환 처리
        binding.map.setOnClickListener {
            //화면 전환 처리
            val Intent = Intent(requireContext(), ServiceActivity::class.java)
            startActivity(Intent)
        }

        //매칭 리스트 목록 클릭 이벤트 처리
        binding.matchingList.setOnClickListener {
            val intent = Intent(requireContext(), MyLikeListActivity::class.java)
            startActivity(intent)
        }
        //메시지함 목록 클릭 이벤트 처리
        binding.messagingList.setOnClickListener {
            val intent = Intent(requireContext(), MyMsgActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

}