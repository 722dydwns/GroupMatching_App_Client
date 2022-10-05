package com.example.appgrouppurchasemaching.board

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.service.ServiceActivity
import com.example.appgrouppurchasemaching.databinding.FragmentMenuControlBinding
import com.example.appgrouppurchasemaching.intro.IntroActivity
import com.example.appgrouppurchasemaching.intro.MyPageActivity
import com.example.appgrouppurchasemaching.matching.MyLikeListActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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
        binding.menuControlToolbar.title = "                 공동구매 메이트 매칭 홈 "

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

        //나의 매칭좋아요 리스트 목록 클릭 이벤트 처리
        binding.matchingList.setOnClickListener {
            val intent = Intent(requireContext(), MyLikeListActivity::class.java)
            startActivity(intent)
        }

        //회원정보 페이지 클릭 시 이벤트 처리
        binding.myPage.setOnClickListener {
            val intent = Intent(requireContext(), MyPageActivity::class.java)
            startActivity(intent)
        }

        //Logout 버튼 클릭 시 이벤트 처리
        binding.MyLogout.setOnClickListener {
            val auth = Firebase.auth
            auth.signOut() //로그아웃 처리

            //인트로 화면 전환
            val intent = Intent(requireContext(), IntroActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

}