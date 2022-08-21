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
import com.example.appgrouppurchasemaching.ServerInfo
import com.example.appgrouppurchasemaching.databinding.BoardMainRecyclerItemBinding
import com.example.appgrouppurchasemaching.databinding.FragmentBoardMainBinding
import com.example.appgrouppurchasemaching.intro.MainActivity
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import kotlin.concurrent.thread

class BoardMainFragment : Fragment() { //게시판 목록 메인 프래그먼트
    //바인딩 설정
    lateinit var binding : FragmentBoardMainBinding

    //Array 리스트 4개-글 목록 구성할 데이터 리스트
    val contentIdxList = ArrayList<Int>()
    val contentWriterList = ArrayList<String>()
    val contentWriteDateList = ArrayList<String>()
    val contentSubjectList = ArrayList<String>()

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

                    //여기서 게시판 목록 '클릭'하는 경우 이벤트 처리
                    boardListBuilder.setItems(act.boardNameList.toTypedArray()){ dialogInterface: DialogInterface, i: Int ->
                        act.selectedBoardType = i //사용자 선택한 i에 따라
                        //각 목록에 대한 데이터 처리를 위해서 이전 데이터 clear 처리 -> 각 게시판 목록 idx 값에 따른 데이터를 세팅
                        getContentList(true)

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

        //항목 속 데이터를 불러오는 함수 (F=불러오고 T=초기화함)
        getContentList(true) //싹 비우고 매번 이 화면에 오면 새롭게 DB 상에서 데이터 읽어 구성하도록

        return binding.root
    }

    //리사이클러 뷰 사용 위한 클래스 생성 - 내부에서 재정의 필요한 함수
    inner class BoardMainRecyclerAdapter : RecyclerView.Adapter<BoardMainRecyclerAdapter.ViewHolderClass>(){
        //1) 재정의 : onCReateViewHolder() 뷰 홀더준비 위해 '자동 호출'됨
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
            //사용자 클릭한 항목의 position 번째에 있는 List 속 데이터를 뷰 홀더 안에 각각 데이터 삽입처리
            holder.boardMainItemNickname.text = contentWriterList[position]
            holder.boardMainItemSubject.text = contentSubjectList[position]
            holder.boardMainItemWriteDate.text = contentWriteDateList[position]
        }
        //3) 재정의 : getITemCount() 항목 개수 판단을 위해 '자동 호출'됨
        override fun getItemCount(): Int {
            return contentIdxList.size
        }

        //  뷰 홀더 클래스 Inner 클래스로 생성
        inner class ViewHolderClass(boardMainRecyclerItemBinding:BoardMainRecyclerItemBinding)
            : RecyclerView.ViewHolder(boardMainRecyclerItemBinding.root), View.OnClickListener{

            //'각 항목 하나 구성하는 데이터 주소'값을 각각 여기서 바인딩 처리
            val boardMainItemNickname = boardMainRecyclerItemBinding.boardMainItemNickname
            val boardMainItemSubject = boardMainRecyclerItemBinding.boardMainItemSubject
            val boardMainItemWriteDate = boardMainRecyclerItemBinding.boardMainItemWriteDate

            //'각 항목' 터치 시 자동 호출 메소드()
            override fun onClick(v: View?) {
                val act = activity as BoardMainActivity
                //액티비티의 '읽기 idx'값 <- 현재 항목 터치한 내용물 idx값 줌
                //여기서 처리하면 '게시글 읽기 화면'에서 이 idx 값을 기준으로 게시글 읽기 화면 구성 O
                act.readContentIdx = contentIdxList[adapterPosition]

                act.fragmentController("board_read", true, true)
            }
        }
    }

    //항목데 담을 데이터를 초기화 or 세팅하는 함수
    fun getContentList(clear:Boolean){ //T : 4개의 데이터 list 초기화 / F: 냅둠

        if(clear == true){ //4개 목록 데이터리스트 초기화시킴
            contentIdxList.clear()
            contentWriterList.clear()
            contentSubjectList.clear()
            contentWriteDateList.clear()
        }

        //서버 통신 - 데이터 가져와서 채움
        thread {
            val client = OkHttpClient()
            val site = "http://${ServerInfo.SERVER_IP}:8080/App_GroupCharge_Server/get_content_list.jsp"

            val act = activity as BoardMainActivity
            //현재 선택한 게시판 목록 idx값을 서버로 보낼 데이터로 세팅 처리
            val builder1 = FormBody.Builder()
            builder1.add("content_board_idx", "${act.selectedBoardType}")

            val formBody = builder1.build()

            val request = Request.Builder().url(site).post(formBody).build()
            val response = client.newCall(request).execute()

            if(response.isSuccessful == true)  { //통신 성공 시
                val resultText = response.body?.string()!!.trim()
                val root = JSONArray(resultText) //Array 에 담아준 뒤
                //for문 돌면서 각 JSON 객체의 데이터를 옮김김

                for(i in 0 until root.length()) {
                    val obj = root.getJSONObject(i)

                    contentIdxList.add(obj.getInt("content_idx"))
                    contentWriterList.add(obj.getString("content_nick_name"))
                    contentWriteDateList.add(obj.getString("content_write_date"))
                    contentSubjectList.add(obj.getString("content_subject"))
                }
                //화면 구성 전환
                activity?.runOnUiThread {
                    //Recycler 뷰 어댑터에게 Data 세팅 변경 알리고 -> 갱신 처리
                    binding.boardMainRecycler.adapter?.notifyDataSetChanged() //데이터 갱신 명령
                }
            }
        }

    }

}