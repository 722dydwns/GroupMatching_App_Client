package com.example.appgrouppurchasemaching.intro

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.databinding.ActivityMainBinding
import com.example.appgrouppurchasemaching.utils.FirebaseRef
import com.example.appgrouppurchasemaching.utils.MyInfo
import com.example.appgrouppurchasemaching.utils.UserDataModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() { //main 액티비티

    //바인딩 설정
    lateinit var binding : ActivityMainBinding

    //전환될 프래그먼트 관리 변수
    lateinit var currentFragment: Fragment

    private lateinit var auth: FirebaseAuth

    //사용자 정보 관리 변수
    var userId = ""
    var userPw = ""
    var userNickName = ""

    var uid = "" //회원 uid 관리

    //회원 가입 컨트롤 변수
    var FBControl = false //기본값

    //기존 회원 로그인 컨트롤 변수
    var FBLoginControl = false //기본값

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    // Initialize Firebase Auth
        auth = Firebase.auth
        //바인딩 설정
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 최초 실행 시 가장 기본 프래그먼트 = login 프래그먼트로 지정
        fragmentController("login", false,false)

    }

    //입력 완료 클릭 시, 여기서 Fireabse Auth 통제할건데
    fun FBController(control : Boolean, email:String, password:String, nickname : String) {

        if(control == true) { //이 변수 T 이면 회원 정보를 Firebase에 저장 시도
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val user = auth.currentUser //현재 로그인한 사용자 찍어보기
                        uid = user?.uid.toString() //현재 로그인한 User의 uid 값 세팅

                        val userModel = UserDataModel( //사용자 데이터 모델 객체
                            uid, nickname, email, password
                        )

                        //RealTIme DB 에 쓰기
                       FirebaseRef.userInfoRef.child(uid).setValue(userModel)

                    } else {
                        // If sign in fails, display a message to the user.
                       // Log.w("test", "failure", task.exception)
                    }
                }
        }
    }

    //사용자 FB 로그인 컨트롤러
    fun FBLoginController(control : Boolean, email:String, password:String) {
        //기존 사용자 로그인 로직
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show()
                }
            }
    }

    //프래그먼트 '컨트롤러' 메소드 생성 - 이름/백스택저장여부/애니메이션여부
    fun fragmentController(name:String, add:Boolean, animate:Boolean){

        when(name){
            "login" -> {
                currentFragment = LoginFragment()
            }
            "join" -> {
                currentFragment = JoinFragment()
            }
            "nick_name" -> {
                currentFragment = NickNameFragment()
            }
        }

        //트랜잭션으로 화면 전환 처리
        val trans = supportFragmentManager.beginTransaction()
        trans.replace(R.id.main_container, currentFragment)
        //만약 add값 T값 들어오면 (즉, 백스택 저장 원하면) -> 이후 '되돌아가기' 기능 위해 구현
        if(add == true) {
            trans.addToBackStack(name)
        }
        //애니메이션 적용 T값 들어오면
        if(animate == true) {
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }

        trans.commit() //위의 설정 적용
    }

    fun hideKeyboard() {
        // 키보드 내리기
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}