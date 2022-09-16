package com.example.appgrouppurchasemaching.board

import android.content.Context
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.ServerInfo
import com.example.appgrouppurchasemaching.databinding.FragmentBoardReadBinding
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.net.URL
import kotlin.concurrent.thread


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
        binding.boardReadToolbar.title = "공구 매칭 읽기"

        //Back 버튼을 툴바 상단의 navigationIcon으로 추가한다.
        val navIcon = requireContext().getDrawable(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.boardReadToolbar.navigationIcon = navIcon

        //뒤로가기 네비게이션 클릭 이벤트 처리
        binding.boardReadToolbar.setNavigationOnClickListener {
            val act = activity as BoardMainActivity
            //현재의 프래그먼트를 백스택에서 pop 제거 처리
            act.fragmentRemoveBackStack("board_read")
        }

        //서버로부터 글 내용 데이터 받기
        thread {
          val client = OkHttpClient()
          val site = "http://${ServerInfo.SERVER_IP}:8080/App_GroupCharge_Server/get_content.jsp"

            //서버로 보낼 데이터 : 최근 작성 글 목록 idx값 <- 액티비티 딴에 저장해놨떤 값 받기
            val act = activity as BoardMainActivity
            //데이터 세팅
            val builder1 = FormBody.Builder()
            builder1.add("read_content_idx", "${act.readContentIdx}")
            val formBody = builder1.build()
            //Request로 요청 보내고 (데이터보내서)
            val request = Request.Builder().url(site).post(formBody).build()
            //요청에 대한 응답은 response로 받고
            val response = client.newCall(request).execute()

            if(response.isSuccessful == true) { //서버 통신 성공 시
                val resultText = response.body?.string()!!.trim()
                val obj = JSONObject(resultText)
                //'작성자' idx 값을 JSON 객체에서 추출하고
                val contentWriterIdx = obj.getInt("content_writer_idx")

                //게시글 읽기 화면의 뷰 세팅해준다- 받은 데이터들로
                activity?.runOnUiThread {
                    binding.boardReadSubject.text = obj.getString("content_subject")
                    binding.boardReadWriter.text = obj.getString("content_nick_name")
                    binding.boardReadWriteDate.text = obj.getString("content_write_date")
                    binding.boardReadText.text = obj.getString("content_text")

                    //이미지 파일명 받음
                    val contentImage = obj.getString("content_image")
                    if(contentImage == "null") { //얻어온 이미지 없다면
                        binding.boardReadImage.visibility = View.GONE //화면 상에도 이미지뷰 안보이도록 처리
                    }else { //이미지 있다면 네트워크 통신 처리
                        thread{
                            val imageUrl = URL("http://${ServerInfo.SERVER_IP}:8080/App_GroupCharge_Server/upload/${contentImage}")
                            //접속한 url에서 이미지 얻어온 뒤, 이미지 객체 bitmap으로 생성하기
                            val bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream())
                            activity?.runOnUiThread {
                                binding.boardReadImage.setImageBitmap(bitmap) //생성한 이미지 객체를 뷰에 세팅
                            }
                        }
                    }

                    //Preferences에 저장해뒀던 현재 '로그인' 사용자 idx값 가져옴
                    val pref = requireContext().getSharedPreferences("login_data", Context.MODE_PRIVATE)
                    val loginUserIdx = pref.getInt("login_user_idx", -1) //두 번째 매개변수는 get한 데이터 값 없을 경우 기본 반환값임

                    if(loginUserIdx == contentWriterIdx) { //현 로그인 idx == 작성자 idx인 경우에 한해서
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

                                R.id.board_read_menu_delete -> {

                                    thread {
                                        val act = activity as BoardMainActivity

                                        val client = OkHttpClient()
                                        val site = "http://${ServerInfo.SERVER_IP}:8080/App_GroupCharge_Server/delete_content.jsp"

                                        val builder1 = FormBody.Builder()
                                        builder1.add("content_idx", "${act.readContentIdx}")
                                        val formBody = builder1.build()

                                        val request = Request.Builder().url(site).post(formBody).build()
                                        val response = client.newCall(request).execute()

                                        if(response.isSuccessful == true){
                                            activity?.runOnUiThread {
                                                val dialogBuilder = AlertDialog.Builder(requireContext())
                                                dialogBuilder.setTitle("글 삭제")
                                                dialogBuilder.setMessage("글이 삭제되었습니다")
                                                dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                                                    val act = activity as BoardMainActivity
                                                    act.fragmentRemoveBackStack("board_read")
                                                }
                                                dialogBuilder.show()
                                            }

                                        }
                                    }


                                    true
                                }
                                else -> false
                            }
                        }
                    }
                }
            }
        }


        return binding.root
    }


}