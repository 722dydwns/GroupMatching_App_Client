package com.example.appgrouppurchasemaching.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.ServerInfo
import com.example.appgrouppurchasemaching.databinding.ActivityBoardMainBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import kotlin.concurrent.thread

class BoardMainActivity : AppCompatActivity() { //게시판 메인 액티비티
    //바인딩
    lateinit var binding : ActivityBoardMainBinding
    //프래그먼트 컨트롤 변수
    lateinit var currentFragment : Fragment

    //게시판 목록 '이름'들을 받을 List<> 변수
    val boardNameList = ArrayList<String>()
    //게시판 목록 'idx' 받을 List<> 변수
    val boardIndexList = ArrayList<Int>()
    //현재 선택된 게시판 목록 idx 값
    var selectedBoardType = 0 //기본값 - 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding
        binding = ActivityBoardMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //전체 게시판(모두 포함하는) 추가가
        boardNameList.add("전체 게시판")
        boardIndexList.add(0)

        //서버와 통신
        thread {
            val client = OkHttpClient()
            val site = "http://${ServerInfo.SERVER_IP}:8080/App_GroupCharge_Server/get_board_list.jsp"
                                    //GET 방식으로 그냥 가져오기만 하면 되기 때문
            val request = Request.Builder().url(site).get().build()
            val response = client.newCall(request).execute()

            if(response.isSuccessful == true) { //통신 성공 시
                val resultText = response.body?.string()!!.trim()
                val root = JSONArray(resultText) //가져온 객체를 다시 JSON 배열에 담고

                for(i in 0 until root.length()){
                    val obj = root.getJSONObject(i)
                    //JSON 속 데이터 담고
                    val boardIdx = obj.getInt("board_idx")
                    val boardName = obj.getString("board_name")

                    //액티비티 속 데이터인 List에 담기
                    boardIndexList.add(boardIdx)
                    boardNameList.add(boardName)
                }
            }
        }


        //초기 화면 프래그먼트 설정
        //fragmentController("board_main", false,false)
        fragmentController("board_main", false, false)
    }

    //게시글 관련 프래그먼트 컨트롤러 메소드 작성
    fun fragmentController(name:String, add:Boolean, animate:Boolean) {
        when(name){
            "board_main" -> {
                currentFragment = BoardMainFragment()
            }
            "board_read" -> {
                currentFragment = BoardReadFragment()
            }
            "board_write" -> {
                currentFragment = BoardWriteFragment()
            }
            "menu_controller" -> {
                currentFragment = MenuControlFragment()
            }
            "board_modify" -> {
                currentFragment = BoardModifyFragment()
            }

        }
        //트랜잭션으로 화면 전환 처리
        val trans = supportFragmentManager.beginTransaction()
        trans.replace(R.id.board_main_container, currentFragment)
        //백스택 여부 T인 경우
        if(add == true){
            trans.addToBackStack(name)
        }
        //애니메이션 사용 T 인 경우
        if(animate == true) {
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }

        trans.commit() ///실행
    }

    //'뒤로가기' 구현을 위해 name 프래그먼트를 백스택에서 제거 메소드 작성
    fun fragmentRemoveBackStack(name:String){
        supportFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

}