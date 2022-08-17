package com.example.appgrouppurchasemaching.board

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.databinding.BoardMainRecyclerItemBinding
import com.example.appgrouppurchasemaching.databinding.FragmentBoardMainBinding
import com.example.appgrouppurchasemaching.intro.MainActivity

class BoardMainFragment : Fragment() { //게시판 목록 메인 프래그먼트
    //바인딩 설정
    lateinit var binding : FragmentBoardMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val act = activity as BoardMainActivity

        //바인딩
        binding = FragmentBoardMainBinding.inflate(inflater)
        binding.boardMainToolbar.title = act.boardNameList[act.selectedBoardType]
                                    //-> 사용자 선택한 게시글 목록 idx 값에 따르 이름값을 title로 세팅하기
        //게시판 항목 메뉴 추가하기
        binding.boardMainToolbar.inflateMenu(R.menu.board_main_menu)
        // 이 항목 메뉴 item 클릭 이벤트 처리
        binding.boardMainToolbar.setOnMenuItemClickListener {
            //툴바 속 존재하는 메뉴item 클릭 시. 이벤트 처리
            when(it.itemId){
                R.id.board_main_menu_board_list -> {  //다이얼로그로 게시판 목록을 띄운다.
                    //액티비티에서 받아놨던 데이터 받기 위해 act
                    val act = activity as BoardMainActivity
                    //단 여기서 toTypedArray()로 변경해주어야 한다. Array 객체로 변경
                    val boardListBuilder = AlertDialog.Builder(requireContext())
                    boardListBuilder.setTitle("게시판 목록")
                    boardListBuilder.setNegativeButton("취소", null)
                            //여기서 게시판 목록 클릭하는 경우 이벤트 처리
                    boardListBuilder.setItems(act.boardNameList.toTypedArray()){ dialogInterface: DialogInterface, i: Int ->
                        act.selectedBoardType = 1
                        binding.boardMainToolbar.title = act.boardNameList[act.selectedBoardType]
                    }
                    boardListBuilder.show()
                    true //메뉴 클릭 시 무언가 작업했으므로 T 반환시킴
                }
                R.id.board_main_menu_write -> { //글쓰기 메뉴 클릭 시
                    val act = activity as BoardMainActivity
                    act.fragmentController("board_write", true, true)
                    true
                }
                R.id.board_main_menu_controller -> { //다른 항목 메뉴 컨트롤러 클릭 시,
                    // -> 프래그먼트 이동시킬 건데,
                    val act = activity as BoardMainActivity
                    act.fragmentController("menu_controller", true, true)
                    true
                }

                else -> false
            }
        }

        //리사이클러뷰 설정
        // (1) 어댑터 객체 생성t
        val boardMainRecyclerAdapter = BoardMainRecyclerAdapter()
        binding.boardMainRecycler.adapter = boardMainRecyclerAdapter
        // (2) 레이아웃 매니저 사용 -> 어댑터로 만든 항목 레이아웃 배치
        binding.boardMainRecycler.layoutManager = LinearLayoutManager(requireContext())
        // (3) 아이템 데코레이션 - 구분선 생성
        binding.boardMainRecycler.addItemDecoration(DividerItemDecoration(requireContext(), 1))

        return binding.root
    }

    //리사이클러 뷰 사용 위한 클래스 생성 - 내부에서 재정의 필요한 함수
    inner class BoardMainRecyclerAdapter : RecyclerView.Adapter<BoardMainRecyclerAdapter.ViewHolderClass>(){
        //1) 재정의 : onCReateViewHolder() 뷰 홀더준비 위해 자동 호출됨
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
                //바인딩
            val boardMainRecyclerItemBinding = BoardMainRecyclerItemBinding.inflate(layoutInflater)
            val holder = ViewHolderClass(boardMainRecyclerItemBinding)
            //각 항목 하나 당 레이아웃 크기 설정
            val layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, //가로 길이
                ViewGroup.LayoutParams.WRAP_CONTENT //세로 길이
            )
            //위 설정을 레이아웃에 세팅
            boardMainRecyclerItemBinding.root.layoutParams = layoutParams
            //각 항목 터치 시 호출될 리스너 설정해둠
            boardMainRecyclerItemBinding.root.setOnClickListener(holder)

            return holder
        }
        //2) 재정의 : onBindViewHolder() 뷰 홀더 각 항목에 데이터 출력 위한 역할 (자동 호출됨)
        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {

        }
        //3) 재정의 : getITemCount() 항목 개수 판단을 위해 자동 호출됨
        override fun getItemCount(): Int {
            return 10
        }

        //  뷰 홀더 클래스 Inner 클래스로 생성
        inner class ViewHolderClass(boardMainRecyclerItemBinding:BoardMainRecyclerItemBinding)
            : RecyclerView.ViewHolder(boardMainRecyclerItemBinding.root), View.OnClickListener{
            //'각 항목' 터치 시 자동 호출 메소드()
            override fun onClick(v: View?) {
                val act = activity as BoardMainActivity
                act.fragmentController("board_read", true, true)
            }
        }
    }
}