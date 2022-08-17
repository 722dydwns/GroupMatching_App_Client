package com.example.appgrouppurchasemaching.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.databinding.FragmentBoardModifyBinding


class BoardModifyFragment : Fragment() { //게시글 수정 프래그먼트
    //바인딩
    lateinit var binding : FragmentBoardModifyBinding
    //Spinner 데이터 임의로 생성
    val spinnerData = arrayOf("게시판1", "게시판2", "게시판3", "게시판4")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //바인딩
        binding = FragmentBoardModifyBinding.inflate(inflater)
        //title
        binding.boardModifyToolbar.title = "게시글 수정"
        //메뉴 xml 지정
        binding.boardModifyToolbar.inflateMenu(R.menu.board_modify_menu)
        //각 메뉴 항목 이벤트 처리
        binding.boardModifyToolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.board_modify_menu_camera -> { //카메라 클릭 시
                    true
                }
                R.id.board_modify_menu_gallery -> { //갤러리 클릭 시
                    true
                }
                R.id.board_modify_menu_upload -> { //업로드 클릭 시
                    val act = activity as BoardMainActivity
                    act.fragmentRemoveBackStack("board_modify") //우선 현재 프래그먼트 종료시키기
                    true
                }
                else -> false
            }
        }



        //Spinner 구성 - 게시글 수정 시: 카테고리 변경을 할 수도 있으므로 별도의 스피너 구성한다.
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, spinnerData)
        binding.boardModifyType.adapter = spinnerAdapter

        return binding.root
    }

}