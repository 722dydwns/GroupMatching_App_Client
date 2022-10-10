package com.example.appgrouppurchasemaching.intro

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import com.example.appgrouppurchasemaching.ServerInfo
import com.example.appgrouppurchasemaching.board.BoardMainActivity
import com.example.appgrouppurchasemaching.databinding.FragmentLoginBinding
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
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
            act.FBLoginControl = true
            act.FBLoginController(act.FBControl, loginId, loginPw) //호출


            //-> 서버 통신 작업 수행
            thread {
                val client = OkHttpClient() //클라이언트 객체

                val site = "http://${ServerInfo.SERVER_IP}:8080/App_GroupCharge_Server/login_user.jsp"

                //1) 사용자가 로그인 시도한 데이터값을 우선 서버에 전달하기 위해 세팅
                val builder1 = FormBody.Builder()
                builder1.add("user_id", loginId)
                builder1.add("user_pw", loginPw)
                val formBody = builder1.build()

                //2) 요청
                val request = Request.Builder().url(site).post(formBody).build()
                //3) 요청 실행 후 결과는 response로 받음
                val response = client.newCall(request).execute() //요청에 대한 응답은 response로 받음

                // 통신 성공 여부에 따른 분기
                if(response.isSuccessful == true) { //통신 성공 시,
                    //응답 결과 추출
                    val result_text = response.body?.string()!!.trim()

                    if (result_text == "FAIL") { //서버로부터 받은 응답 결과값이 FAIL이면
                        activity?.runOnUiThread {
                            val dialogBuilder = AlertDialog.Builder(requireContext())
                            dialogBuilder.setTitle("로그인 실패")
                            dialogBuilder.setMessage("아이디나 비밀번호가 잘못되었습니다.")
                            dialogBuilder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                                binding.loginId.setText("") //id칸 공백처리
                                binding.loginPw.setText("") //pw칸 공백처리
                                binding.loginId.requestFocus() //id에 Re포커싱줌줌                            }
                            }
                            dialogBuilder.show()
                        }
                    }
                    else{ //서버로부터 받은 응답 결과값 = 로그인 성공
                        activity?.runOnUiThread {
                            val dialogBuilder = AlertDialog.Builder(requireContext())
                            dialogBuilder.setTitle("로그인 성공")
                            dialogBuilder.setMessage("로그인 성공하였습니다.")
                            dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                                //사용자 정보를 Preferences에 저장 -> 이후 Preference 접근은 액티비티로 접근하면된다.
                                                                        //이름 = login_data, 모드 = 이 앱 안에서 데이터 공유 목적
                                val pref = activity?.getSharedPreferences("login_data", Context.MODE_PRIVATE)
                                val editor = pref?.edit() //편집 사용

                                editor?.putInt("login_user_idx", Integer.parseInt(result_text)) //서버로부터 받은 값을 int형변환 후 put 처리
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
                    }
                }else{ //통신 실패 시 작업 처리
                    activity?.runOnUiThread {
                        val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setTitle("로그인 오류")
                        dialogBuilder.setMessage("로그인 오류 발생")
                        dialogBuilder.setPositiveButton("확인", null)
                        dialogBuilder.show()
                    }
                }

            }
        }


        return binding.root
    }

}