package com.example.appgrouppurchasemaching.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.databinding.FragmentBoardWriteBinding

class BoardWriteFragment : Fragment() {//글쓰기 프래그먼트 화면

    //바인딩
    lateinit var binding : FragmentBoardWriteBinding

    //스피너에 담을 게시판 목록 데이터 변수
    val spinner_data = arrayOf("게시판1", "게시판2", "게시판3", "게시판4")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //바인딩
        binding = FragmentBoardWriteBinding.inflate(inflater)

        //title
        binding.boardWriteToolbar.title = "게시글 작성"


        //Back 버튼을 툴바 상단의 navigationIcon으로 추가한다.
        val navIcon = requireContext().getDrawable(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.boardWriteToolbar.navigationIcon = navIcon

        //뒤로가기 네비게이션 클릭 이벤트 처리
        binding.boardWriteToolbar.setNavigationOnClickListener {
            val act = activity as BoardMainActivity
            //현재의 프래그먼트를 백스택에서 pop 제거 처리
            act.fragmentRemoveBackStack("board_write")
        }

        //툴바 속 메뉴 세팅
        binding.boardWriteToolbar.inflateMenu(R.menu.board_write_menu)
        binding.boardWriteToolbar.setOnMenuItemClickListener{
            when(it.itemId) {
                R.id.board_write_menu_camera -> { //카메라 클릭 시
                    true
                }
                R.id.board_write_menu_gallery -> { //앨범 클릭 시
                    true
                }
                R.id.board_write_menu_upload -> { //작성완료 클릭 시
                    val act = activity as BoardMainActivity
                    act.fragmentRemoveBackStack("board_write") //현재 프래그먼트는 제거하고
                    act.fragmentController("board_read", true, true) //현재 글의 읽기 프래그먼트로 바로 전환
                    true
                }
                else -> false
            }
        }


        //스피너 어댑터 생성
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, spinner_data)

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.boardWriteType.adapter = spinnerAdapter


       return binding.root
    }


}