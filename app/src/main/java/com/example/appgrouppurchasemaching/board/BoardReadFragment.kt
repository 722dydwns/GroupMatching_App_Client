package com.example.appgrouppurchasemaching.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.databinding.FragmentBoardReadBinding


class BoardReadFragment : Fragment() { //게시글 읽기 프래그먼트 화면

    //바인딩
    lateinit var binding : FragmentBoardReadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //바인딩
        binding = FragmentBoardReadBinding.inflate(inflater)
        //title
        binding.boardReadToolbar.title = "게시글 읽기"

        //Back 버튼을 툴바 상단의 navigationIcon으로 추가한다.
        val navIcon = requireContext().getDrawable(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.boardReadToolbar.navigationIcon = navIcon

        //뒤로가기 네비게이션 클릭 이벤트 처리
        binding.boardReadToolbar.setNavigationOnClickListener {
            val act = activity as BoardMainActivity
            //현재의 프래그먼트를 백스택에서 pop 제거 처리
            act.fragmentRemoveBackStack("board_read")
        }

        //'수정' 삭제' 메뉴 구성 - 바인딩 처리
        binding.boardReadToolbar.inflateMenu(R.menu.board_read_menu)
        //이벤트 처리
        binding.boardReadToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.board_read_menu_modify -> { //'수정' 클릭 시
                    val act = activity as BoardMainActivity
                    act.fragmentController("board_modify", true, true)
                    true
                }
                R.id.board_read_menu_delete -> { //'삭제' 클릭 시
                    val act = activity as BoardMainActivity
                    act.fragmentRemoveBackStack("board_read") //'우선 뒤로가기 처리''
                    true
                }
                else -> false
            }
        }

        return binding.root
    }


}