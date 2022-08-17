package com.example.appgrouppurchasemaching.intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() { //main 액티비티

    //바인딩 설정
    lateinit var binding : ActivityMainBinding

    //전환될 프래그먼트 관리 변수
    lateinit var currentFragment: Fragment

    //사용자 정보 관리 변수
    var userId = ""
    var userPw = ""
    var userNickName = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //바인딩 설정
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 최초 실행 시 가장 기본 프래그먼트 = login 프래그먼트로 지정
        fragmentController("login", false,false)
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


}