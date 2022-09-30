package com.example.appgrouppurchasemaching.intro

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.appgrouppurchasemaching.ServerInfo
import com.example.appgrouppurchasemaching.databinding.FragmentNickNameBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.concurrent.thread

class NickNameFragment : Fragment() { //닉네임 화면

    //바인딩
    lateinit var binding : FragmentNickNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //바인딩
        binding = FragmentNickNameBinding.inflate(inflater)
        //title 설정
        binding.nicknameToolbar.title = "닉네임 입력"

        //'입력완료' 버튼 클릭 이벤트 처리
        binding.nicknameJoinBtn.setOnClickListener {


            //뷰 바인딩 -> 뷰에서 사용자가 입력한 닉네임 값 받기
            val nickNameNickName = binding.nicknameNickname.text.toString()
            //입력값에 대한 유효성 검사 (입력X 상태에 대한)
            if(nickNameNickName == null || nickNameNickName.length == 0) {
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setTitle("닉네임 입력 오류")
                dialogBuilder.setMessage("닉네임을 입력해주세요")
                dialogBuilder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                    binding.nicknameNickname.requestFocus() //재포커싱
                }
                dialogBuilder.show()
                return@setOnClickListener
            }

            //뷰에서 받은 닉네임 값을 다시 Main액티비티의 변수에 담아둔다.
            val act = activity as MainActivity
            act.userNickName = nickNameNickName

            //FB에도 가입 시도
            act.FBControl = true // 입력완료 클릭 시 컨트롤 변수 처리
            act.FBController(act.FBControl, act.userId, act.userPw, act.userNickName) //호출

            //-> 서버와 통신 작업
            thread {
                val client = OkHttpClient() //클라이언트 객체 생성

                val site = "http://${ServerInfo.SERVER_IP}:8080/App_GroupCharge_Server/join_user.jsp"

                //서버로 보낼 '데이터'세팅
                val builder1 = FormBody.Builder()
                //JSP 파일에서 받기로한 이름, 여기 액티비티에서 보낼 변수 순으로 담는다.
                builder1.add("user_id", act.userId)
                builder1.add("user_pw", act.userPw)
                builder1.add("user_nick_name", act.userNickName)

                val formBody = builder1.build() //서버로 보낼 데이터를 'FormBody' 형태로 build 생성

                //요청 Request 생성 - POST 형식으로 세팅한 데이터 보냄
                val request = Request.Builder().url(site).post(formBody).build()

                //응답 요청하기 - 클라이언트는 요청(Request)를 실행한 뒤 받을 응답은 reponse로 받음
                val response = client.newCall(request).execute()

                if(response.isSuccessful == true) { //통신 정상적 처리 시,
                    activity?.runOnUiThread{
                        val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setTitle("가입 완료")
                        dialogBuilder.setMessage("가입이 완료되었습니다.")
                        dialogBuilder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                            //화면전환
                            val mainIntent = Intent(requireContext(), MainActivity::class.java)
                            startActivity(mainIntent)
                            activity?.finish() //현재의 액티비티는 종료 -> 액티비티 첫 사이클로 다시 실행된다.
                        }
                        dialogBuilder.show() //알림 띄우기
                    }
                }
                else { //서버 통신 비정상 처리 시
                    activity?.runOnUiThread{
                        val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setTitle("가입 오류")
                        dialogBuilder.setMessage("가입 오류기 발생했습니다.")
                        dialogBuilder.setPositiveButton("확인", null)
                        dialogBuilder.show()
                    }
                }
            }
        }

        return binding.root
    }

}