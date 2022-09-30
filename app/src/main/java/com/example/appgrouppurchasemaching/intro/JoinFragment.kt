package com.example.appgrouppurchasemaching.intro

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.appgrouppurchasemaching.databinding.FragmentJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class JoinFragment : Fragment() { //회원가입 프래그먼트 화면

    //바인딩 설정
    lateinit var binding : FragmentJoinBinding

    //파이어베이스 회원 정보 등록
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //binding 설정
        binding = FragmentJoinBinding.inflate(inflater)

        //toolbar title 설정
        binding.joinToolbar.title = "회원가입"

        //'다음'버튼 이벤트 처리
        binding.joinNextBtn.setOnClickListener {
            //회원가입 User 정보를 받아둔다.
            val joinId = binding.joinId.text.toString()
            val joinPw = binding.joinPw.text.toString()

            //가입 ID : 사용자 X 입력 상태에 유효성 검사
            if(joinId == null || joinId.length == 0) {
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setTitle("아이디 입력 오류")
                dialogBuilder.setMessage("아이디를 입력해주세요")
                dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                    binding.joinId.requestFocus() //다시 id 입력칸에 재포커싱
                }
                dialogBuilder.show()
                return@setOnClickListener
            }
            //가입 Pw : 사용자 X 입력 상태에 유효성 검사
            if(joinPw == null || joinPw.length == 0){
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setTitle("비밀번호 입력 오류")
                dialogBuilder.setMessage("비밀번호를 입력해주세요")
                dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                    binding.joinPw.requestFocus() //다시 pw 입력칸에 재포커싱
                }
                dialogBuilder.show()
                return@setOnClickListener
            }


            //닉네임 설정 화면으로 전환
            //아직 회원가입 중인 상태라서 여기서 입력한 가입 정보를 다시 닉네임 화면에도 보내주어야 한다.
            val act = activity as MainActivity
            //전체 관리 중인 Main 액티비티의 변수에 현재 프래그먼트 상에 입력된 정보값을 담아둔다.
            act.userId = joinId
            act.userPw = joinPw


            act.fragmentController("nick_name", true, true)
        }


        return binding.root
    }

}