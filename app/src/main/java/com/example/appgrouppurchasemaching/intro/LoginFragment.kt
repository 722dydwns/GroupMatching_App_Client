package com.example.appgrouppurchasemaching.intro

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.appgrouppurchasemaching.ServerInfo
import com.example.appgrouppurchasemaching.board.BoardMainActivity
import com.example.appgrouppurchasemaching.databinding.FragmentLoginBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import kotlin.concurrent.thread

class LoginFragment : Fragment() { //로그인 프래그먼트

    //바인딩
    lateinit var binding : FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //바인딩 설정
        binding = FragmentLoginBinding.inflate(inflater)
        //툴바 title 지정
        binding.loginToolbar.title = "로그인"

        //'회원가입' 버튼 클릭 시, -> 회원가입 화면 전환 이벤트 처리
        binding.loginJoinbtn.setOnClickListener {
            //현재 프래그먼트 소유 중인 액티비티 추출 가능
            val act = activity as MainActivity
            act.fragmentController("join", true, true)
        }
        //'로그인' 버튼 클릭 시, 화면 전환
        binding.loginLoginbtn.setOnClickListener {

            //사용자가 뷰에 입력한 데이터 가져오기
            val loginId = binding.loginId.text.toString()
            val loginPw = binding.loginPw.text.toString()

            //유효성 검사 id
            if(loginId == null || loginId.length == 0) {
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setTitle("아이디 입력 오류")
                dialogBuilder.setMessage("아이디를 입력해주세요")
                dialogBuilder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                    binding.loginId.requestFocus() //칸에 재포커싱
                }
                dialogBuilder.show()
                return@setOnClickListener
            }

            //pw 유효성 검사
            if(loginPw == null || loginPw.length == 0) {
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setTitle("비밀번호 입력 오류")
                dialogBuilder.setMessage("비밀번호를 입력해주세요")
                dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                    binding.loginPw.requestFocus() //칸에 재포커싱
                }
                dialogBuilder.show()
                return@setOnClickListener
            }

            // -> FB 로그인 시도
            val act = activity as MainActivity
            act.userId = loginId
            act.userPw = loginPw

            //-> 서버 통신 작업 수행
            thread {
                val client = OkHttpClient()

                val site = "http://${ServerInfo.SERVER_IP}:8080/users/login"

                val jsonObj = JSONObject();
                jsonObj.put("userId", act.userId)
                jsonObj.put("userPw", act.userPw)
                val mediaType = "application/json; charset=utf-8".toMediaType()
                val requestBody = jsonObj.toString().toRequestBody(mediaType);

                //요청 Request 생성 - POST 형식으로 세팅한 데이터 보냄
                val request = Request.Builder().url(site).post(requestBody).build()

                //3) 요청 실행 후 결과는 response로 받음
                val response = client.newCall(request).execute() //요청에 대한 응답은 response로 받음

                //응답 결과 추출
                val responseObj = JSONObject(response.body!!.string())

                if (response.code == 200) {
                    activity?.runOnUiThread {
                        val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setTitle("로그인 성공")
                        dialogBuilder.setMessage(responseObj.getString("message"))
                        dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                            //사용자 정보를 Preferences에 저장 -> 이후 Preference 접근은 액티비티로 접근하면된다.
                            //이름 = login_data, 모드 = 이 앱 안에서 데이터 공유 목적
                            val pref = activity?.getSharedPreferences("login_data", Context.MODE_PRIVATE)
                            val editor = pref?.edit() //편집 사용

                            editor?.putInt("login_user_idx", responseObj.getInt("userIdx")) //서버로부터 받은 값을 int형변환 후 put 처리
                            editor?.commit() //실행

                            // 키보드 내리기
                            val act = activity as MainActivity
                            act.hideKeyboard() //호출

                            //화면 전환 처리
                            val boardMainIntent = Intent(requireContext(),BoardMainActivity::class.java)
                            startActivity(boardMainIntent)
                            activity?.finish()
                        }
                        dialogBuilder.show()
                    }
                } else {
                    activity?.runOnUiThread {
                        val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setTitle("로그인 실패")
                        dialogBuilder.setMessage(responseObj.getString("message"))
                        dialogBuilder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                            binding.loginId.setText("") //id칸 공백처리
                            binding.loginPw.setText("") //pw칸 공백처리
                            binding.loginId.requestFocus() //id에 Re포커싱줌줌                            }
                        }
                        dialogBuilder.show()
                    }
                }

            }
        }


        return binding.root
    }

}