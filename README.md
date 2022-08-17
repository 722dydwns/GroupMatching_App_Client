# App_GroupPurchaseMatching

# 졸업작품 | AppGroupPurchaseMatching 안드로이드 어플

## 🟦 프로젝트 생성

### ▶️ App수준의 build.gradle에 ViewBinding 설정하기

```tsx
buildFeatures{
viewBinding = true
}
```

### **🟧 기본 설정 처리**

**1) 상단의 기본 Title 바, 액션바 제거 (themes.xml)**

```xml
<item name="windowNoTitle">true</item>
<item name="windowActionBar">false</item>
```

**2) 최상단의 기본 색상 = mainColor 값을 values 파일에 설정하여 통일시킴**

`<color name = "mainColor">#81c147</color>`

**3) 최초 실행할 Activity 지정**

- **manifest 파일에서 다음 태그를 갖는 activity가 최초 실행됨.**

```xml
<intent-filter>
    <action android:name="android.intent.action.MAIN" />

    <category android:name="android.intent.category.LAUNCHER" />
</intent-filter>
```

---

# 🙋🏻‍♀️ 앱 화면 기본 UI 레이아웃 짜기

### **🟧 IntroActivity.kt (인트로 액티비티)**

- ‘시작하기’ 버튼 클릭 시 → MainActivity로 화면 전환 이벤트 처리

```kotlin
package com.example.appgrouppurchasemaching

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appgrouppurchasemaching.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() { //intro 액티비티 화면
    //바인딩 설정
    lateinit var binding : ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //바인딩 설정
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //시작하기 버튼 클릭 시 메인 액티비티로 이동
        binding.startbtn.setOnClickListener{
						var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
				}
		}
}
```

![인트로.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/096dff94-2da3-4331-97b4-495047b0e2ef/%EC%9D%B8%ED%8A%B8%EB%A1%9C.png)

## 🟦 로그인 프래그먼트 화면 구성

### **🟧 mainActivity.kt [메인 액티비티]**

- **Intro 액티비티에서 ‘시작하기’ 버튼 클릭으로 전환된 main 액티비티.**
- 여기서 **여러 개의 프래그먼트를 관리할 수 있다. (컨테이너 역할)**
- **main 액티비티 안에는 별도의 fragmentController() 메소드를 생성하여 프래그먼트 이름값으로 프래그먼트 화면 전환을 컨트롤하고자 한다.**

1) 우선 레이아웃xml 파일 → id = main_container 전환 

2) 전체 레이아웃은 FrameLayout 전환 

### **📍 애플리케이션 구성**

- Activity는 Fragment 관리하는 역할
- 하나의 기능이 완료되면 새로운 Activity 실행하는 구조로 처리할 예정이다.

```kotlin
package com.example.appgrouppurchasemaching.IntroMain

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //바인딩 설정
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 최초 실행 시 가장 기본 프래그먼트 = login 프래그먼트로 지정
        fragmentController("login", true,false)
    }

    //프래그먼트 '컨트롤러' 메소드 생성 - 이름/백스택저장여부/애니메이션여부
    fun fragmentController(name:String, add:Boolean, animate:Boolean){

        when(name){
            "login" -> {
                currentFragment = LoginFragment()
            }
        }
        //트랜잭션으로 화면 전환 처리
        val trans =supportFragmentManager.beginTransaction()
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
```

### **🟧 loginFragment.kt**

- 로그인 화면은 ‘프래그먼트’ 화면으로 구성하며 이 프래그먼트는 mainActivity 소속이다.
    
    ![로그인.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/1fa5f8cf-c204-4bc0-8c15-965a0771d2d8/%EB%A1%9C%EA%B7%B8%EC%9D%B8.png)
    

## 🟦 회원 가입 화면 구성

### ▶️ 회원 가입 Fragment 화면으로 구성

- 이 화면에서는 사용자에게 아이디/비번 입력받는다.
- ‘다음’ 버튼 클릭 시 → 닉네임 입력 화면으로 전환되도록 한다.

### ▶️ 닉네임 입력 Fragment 화면으로 구성

- 이 화면에서 사용자에게 가입할 닉네임 입력받는다.
- ‘입력완료’ 버튼 클릭 시 → 최종 가입 완료되도록 한다.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/b8dde9ab-9845-4eeb-9525-638c1e2626e2/Untitled.png)

### 🙋🏻‍♀️ [정리]

- IntroActivity 에서 ‘시작하기’ 버튼 이벤트 처리
- **MainActivity 에서 관리하는 프래그먼트는 다음과 같다.**
    
    1) 로그인 화면
    
    2) 회원가입 화면
    
    3) 닉네임설정 화면
    

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/83855009-ff77-4c77-81b7-c15fdcd8e473/Untitled.png)

---

### **🟧 액티비티 한 사이클 종료**

- 만약 한 User가 회원가입/로그인/닉네임 설정 까지 한 사이클을 모두 마친 상태가 되면, 이들을 관리하는 Activitiy는 ‘뒤로가기’ 버튼을 눌러도 완전 종료되도록 설정해주어야 한다.
- 즉, 해당 액티비티의 BackStack에 저장해놓은 여러 프래그먼트 화면들을 완전 종료시켜주어야 한다.

### ◾ **NickNameFragment.kt (닉네임 설정 화면)**

- 이 곳이 Main액티비티 마지막 사이클이 되므로,
- 이 곳에서 ‘입력완료’ 버튼 클릭 후→ 현재까지 백스택에 저장됐던 모든 프래그먼트 기록들이 지워지고 완전히 새로운 액티비티 화면으로 실행되도록 이벤트 처리한다.

```kotlin
package intro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.databinding.FragmentNickNameBinding

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
        binding.nicknameToolbar.title= "닉네임 입력"

        //'입력완료' 버튼 클릭 이벤트 처리
        binding.nicknameJoinBtn.setOnClickListener{
val mainIntent = Intent(requireContext(), MainActivity::class.java)
            startActivity(mainIntent)
activity?.finish() //현재의 액티비티는 종료 -> 액티비티 첫 사이클로 다시 실행된다.
}

return binding.root
}

}
```

---

### **🟧(1) LoginFragment.kt**

- 우선, User가 로그인 성공 처리 됐을 경우에 한해서만 앱 Main 액티비티로 전환되도록 처리한다.

```kotlin
//'로그인' 버튼 클릭 시, 화면 전환
binding.loginLoginbtn.setOnClickListener{
val boardMainIntent = Intent(requireContext(), BoardMainActivity::class.java)
    startActivity(boardMainIntent)
activity?.finish()
}
```

## 🟦 게시글 메인 Activity 구성

### ▶️ 게시글 메인 액티비티 화면 구성

- 이 액티비티에서는 ‘게시글’ 관련 프래그먼트 화면들을 관리할 예정이다.

### **(1) BoardMainActivity.kt**

```kotlin
package board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.databinding.ActivityBoardMainBinding

class BoardMainActivity : AppCompatActivity() { //게시판 메인 액티비티
    //바인딩
    lateinit var binding : ActivityBoardMainBinding
    //프래그먼트 컨트롤 변수
    lateinit var currentFragment : Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding
        binding = ActivityBoardMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //초기 화면 프래그먼트 설정
        fragmentController("board_main", false,false)
    }

    //게시글 관련 프래그먼트 컨트롤러 메소드 작성
    fun fragmentController(name:String, add:Boolean, animate:Boolean) {
        when(name){
            "board_main" -> {
                currentFragment = BoardMainFragment()
            }
        }
        //트랜잭션으로 화면 전환 처리
        val trans =supportFragmentManager.beginTransaction()
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

}
```

### **(2) BoardMainFragment.kt**

```kotlin
package board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.databinding.FragmentBoardMainBinding

class BoardMainFragment : Fragment() { //게시판 '메인' 프래그먼트
    //바인딩 설정
    lateinit var binding:FragmentBoardMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //바인딩
        binding = FragmentBoardMainBinding.inflate(inflater)
        return binding.root
}

}
```

### **🟧 RecyclerView 구성 → 게시글 목록화면 구성**

**1) 개별 게시글 ‘항목’ 뷰 레이아웃 xml 파일 별도로 생성**

**2) BoardMainFragment.kt** 

- 여기서 RecyclerView로 한 번 감싼 영역에 다시 개별 항목 뷰 레이아웃 xml을 배치하는 방식으로 ‘현재 게시판의 글 목록’을 보여준다.

```kotlin
package board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.databinding.BoardMainRecyclerItemBinding
import com.example.appgrouppurchasemaching.databinding.FragmentBoardMainBinding

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
        //바인딩
        binding = FragmentBoardMainBinding.inflate(inflater)
        binding.boardMainToolbar.title= "게시판이름"

        //리사이클러뷰 설정
        // (1) 어댑터 객체 생성
        val boardMainRecyclerAdapter = BoardMainRecyclerAdapter()
        binding.boardMainRecycler.adapter= boardMainRecyclerAdapter
        // (2) 레이아웃 매니저 사용 -> 어댑터로 만든 항목 레이아웃 배치
        binding.boardMainRecycler.layoutManager= LinearLayoutManager(requireContext())
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
                ViewGroup.LayoutParams.WRAP_CONTENT//세로 길이
            )
            //위 설정을 레이아웃에 세팅
            boardMainRecyclerItemBinding.root.layoutParams= layoutParams
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
            //각 항목 터치 시 자동 호출 메소드()
            override fun onClick(v: View?) {

            }
        }
    }
}
```

### 📌 ‘목록화면’ 전체 구성 순서

(1) 뷰 홀더 준비

(2) 어댑터로 뷰 홀더 속 각 항목 뷰 객체에 데이터 대입하여 각각의 항목을 구성

(3) 레이아웃 매니저는 어댑터가 만든 항목 배치를 결정

(4) 리사이클러 뷰에 위의 내용을 최종 출력

![화면 캡처 2022-08-10 171053.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/9a4e5d8b-c4a1-483f-8dc5-60ad0b35561f/%ED%99%94%EB%A9%B4_%EC%BA%A1%EC%B2%98_2022-08-10_171053.png)

---

## 🟦 게시판 목록 메뉴 구성

### ▶️ 게시판 목록 메뉴 구성하기

- 상단의 Toolbar에 메뉴 설정 후, 해당 메뉴 클릭 시 원하는 게시판 종류 선택 가능하게 이벤트 처리를 한다.

### **🟧 (1) 메뉴 Resource 파일 생성**

- type이 Menu인 새로운 Resource 파일을 res 폴더 하위에 생성한다.
- Menu 하위에 메뉴 item 을 추가한다.
- id 값 = board_main_menu

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/e743eb94-e9b2-4e9a-a331-eb601343913c/Untitled.png)

### **🟧 (2) Toolbar에 위 메뉴 추가하기**

- BoardMainFragment.kt 의 툴바에 inflateMenu()메소드를 사용하여 위에서 만든 메뉴 항목을 집어넣을 것이다.

```kotlin
//메뉴 추가하기
binding.boardMainToolbar.inflateMenu(R.menu.board_main_menu)
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/36d58c8f-468e-4b03-bfb2-aecf22d3cfa5/Untitled.png)

### **🟧 (3) 메뉴 속 item 목록을 다이얼로그 알림으로 띄우도록 이벤트 처리**

![게시판목록띄움.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/bab2770a-cc00-4781-9d58-5b003639770d/%EA%B2%8C%EC%8B%9C%ED%8C%90%EB%AA%A9%EB%A1%9D%EB%9D%84%EC%9B%80.png)

---

## 🟦 게시판 읽기 화면 구성

### ▶️ 게시판 읽기 화면 구성하기

- **1) 게시글 목록 화면에서 각 항목 선택 시 나타나는 게시글 읽기 화면 구성**
- **2) Back 버튼 클릭 시, 다시 게시글 목록 화면으로 이동**

### **🟧 (1) BoardMainFragment.kt 속 각 리사이클러 글 속에 뷰 홀더로 감싸져있는 각각의 글 항목 클릭 시 → 해당 게시글 읽기 화면 전환 이벤트 처리**

◾ 코드 

- 뷰 홀더 클래스 속에 해당 뷰 홀더의 각 항목 클릭 시 자동 호출되도록 재정의된 onClick() 메소드 안에 해당 이벤트 처리 작업을 수행한다.

```kotlin
//  뷰 홀더 클래스 Inner 클래스로 생성
inner class ViewHolderClass(boardMainRecyclerItemBinding:BoardMainRecyclerItemBinding)
    : RecyclerView.ViewHolder(boardMainRecyclerItemBinding.root), View.OnClickListener{
    //'각 항목' 터치 시 자동 호출 메소드()
    override fun onClick(v: View?) {
        val act =activityas BoardMainActivity
        act.fragmentController("board_read", true, true)
    }
}
```

### **🟧 (3) 게시글 읽기 화면 레이아웃 짜기**

- ScrollView를 배치해서 장문의 길이의 게시글도 확인할 수 있도록 한다.

### **🟧 (4) BoardReadFragment.kt**

- 1) 툴바 상단에 ‘뒤로가기’ 버튼을 navigationIcon으로 추가
- 2) ‘뒤로가기’ 이벤트 처리를 위해서 BoardMainActivity에서 백스택에 현재 name 프래그먼트를 제거하는 메소드를 작성한 뒤, 게시글 읽기 프래그먼트에서 해당 메소드 호출하여 뒤로가기 기능 구현한다.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/b9e98ec7-8188-4d6b-a6c7-6ce884d3ac5d/Untitled.png)

## 🟦 게시글 작성 화면 구성

### ▶️ 게시글 작성 화면 구성하기

- 게시글 목록 화면에서 작성 메뉴 클릭 시 게시글 작성하는 화면으로 전환된다.
- 게시글 작성 프래그먼트에서는 Toolbar에 이미지 첨부가 가능하도록 구현할 것이다.

### **🟧 Spinner 스피너 사용**

- 게시글 작성 시, 카테고리를 스피너로 지정하여 작성할 수 있게 한다.
- **(1) 담을 항목 데이터 생성** - 이후 서버로 받아올 부분

```kotlin
//스피너에 담을 게시판 목록 데이터 변수
val spinner_data =arrayOf("게시판1", "게시판2", "게시판3", "게시판4")
```

- **(2) Spinner 어댑터 생성**

```kotlin
//스피너 어댑터 생성
val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, spinner_data)

spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
binding.boardWriteType.adapter= spinnerAdapter
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/7c6d2c9d-ea1e-4a37-ba64-79acda5382a4/Untitled.png)

### **🟧 게시글 작성 Toolbar에 ‘메뉴’ 생성**

**1) ‘뒤로가기’ - navigateIcon으로 생성** 

   - 사용자 클릭 시 → 백스택에 현재 프래그먼트 Remove 처리해서 구현

**2) ‘카메라’ - 메뉴 item으로 생성**

**3) ‘갤러리’ - 메뉴 item으로 생성**

**4) ‘업로드’ - 메뉴 item으로 생성** 

   - 사용자 클릭 시 → 백스택에 현재의 프래그먼트 제거 후, 게시글 읽기 프래그먼트 화면 전환

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/7e06f6d4-d112-45f9-b719-03281f8df097/Untitled.png)

```kotlin

//Back 버튼을 툴바 상단의 navigationIcon으로 추가한다.
val navIcon = requireContext().getDrawable(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
binding.boardWriteToolbar.navigationIcon= navIcon
// -> 이 색상 힌색으로 변경
if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q) {
    binding.boardWriteToolbar.navigationIcon?.colorFilter= BlendModeColorFilter(
        Color.parseColor("#FFFFFF"), BlendMode.SRC_ATOP)
} else {
    binding.boardWriteToolbar.navigationIcon?.setColorFilter(
        Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP
)
}
//뒤로가기 네비게이션 클릭 이벤트 처리
binding.boardWriteToolbar.setNavigationOnClickListener{
val act =activityas BoardMainActivity
    //현재의 프래그먼트를 백스택에서 pop 제거 처리
    act.fragmentRemoveBackStack("board_write")
}

//툴바 속 메뉴 세팅
binding.boardWriteToolbar.inflateMenu(R.menu.board_write_menu)
binding.boardWriteToolbar.setOnMenuItemClickListener{
when(it.itemId) {
        R.id.board_write_menu_camera-> { //카메라 클릭 시
            true
        }
        R.id.board_write_menu_gallery-> { //앨범 클릭 시
            true
        }
        R.id.board_write_menu_upload-> { //작성완료 클릭 시
            val act =activityas BoardMainActivity
            act.fragmentRemoveBackStack("board_write") //현재 프래그먼트는 제거하고
            act.fragmentController("board_read", true, true) //현재 글의 읽기 프래그먼트로 바로 전환
            true
        }
        else -> false
    }
}
```

---

## 🟦 앱 전체 메뉴 컨트롤러 화면 생성

### ▶️ 전체 메뉴 카테고리 바로가기 화면 프래그먼트

- 최초 실행 시, 게시글 목록 화면으로 전환되는데, 이 프래그먼트 Toolbar에 메뉴탭 버튼 배치하여 ‘전체 메뉴 카테고리’를 바로갈 수 있는 프래그먼트를 생성했다.
- 이동의 용이성을 위해서 별도의 프래그먼트를 생성한 것이고, 이곳에서 ImageView를 배치하여 여러 개의 메뉴 카테고리 이동을 가능하도록 할 예정이다.

### **🟧 메뉴 컨트롤러 화면 구성**

- 화면 레이아웃만 구성한 상태이고, 아직 별도의 이벤트 처리는 해두지 않았다.
- ImageView 또한 임시로 배치한 상태이다.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/30f731c3-b341-4cb5-b784-6b0bd6d06422/Untitled.png)

---

## 🟦 게시글 읽는 화면 ‘메뉴’ 구성

### ▶️ 게시글 읽기 화면 속 ‘메뉴’ 구성

- **‘수정’과 ‘삭제’ 메뉴를 둘 것**이다.
- 다만, 이 두 메뉴는 **반드시 해당 글을 작성한 글쓴이에 한해서만 등장**하도록 처리할 것이다.

### **🟧 메뉴 xml 파일에 ‘수정’, ‘삭제’ 메뉴 생성**

- 이후 BoardReadFragment.kt 안에서 해당 메뉴에 대한 이벤트 처리를 수행

◾ 코드 

```kotlin

//'수정' 삭제' 메뉴 구성 - 바인딩 처리
binding.boardReadToolbar.inflateMenu(R.menu.board_read_menu)
//이벤트 처리
binding.boardReadToolbar.setOnMenuItemClickListener{
when(it.itemId) {
        R.id.board_read_menu_modify-> { //'수정' 클릭 시
            true
        }
        R.id.board_read_menu_delete-> { //'삭제' 클릭 시
            val act =activityas BoardMainActivity
            act.fragmentRemoveBackStack("board_read") //'우선 뒤로가기 처리''
            true
        }
        else -> false
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/3b276aca-a964-426e-966a-a1db58aca4f1/Untitled.png)

## 🟦 게시글 수정 화면 구성

### ▶️ 게시글 수정 화면의 구성

- **게시글 ‘읽기’ 화면 속 ‘수정’메뉴 클릭 → 게시글 ‘수정 화면 전환됨**
- **수정 화면에는 이전에 작성한 글 내용이 다시 재등장되어야 한다.**

### **🟧 Adapter - 어댑터란?**

- **View**와 View에 올라갈 **데이터** 사이를 연결하는 일종의 다리 역할 객체
- 데이터 원본을 받아서 → 뷰가 출력할 수 있는 형태로 데이터를 제공하는 ‘중간 객체 역할’이다.
- 즉, 어댑터는 어댑터 뷰가 출력할 수 있는 데이터로 만드는 공간을 의미하고, 어댑터 뷰는 가공된 데이터를 출력하는 역할을 한다.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/cfbf0533-b9cd-47d9-8453-c044cf058914/Untitled.png)

### **🟧 게시글 수정 화면 구성**

- 1) Spinner 구성 : 게시글 수정 시 카테고리를 변경할 수 있으므로 별도의 Spinner를 다시 구성해준다.
- 2) Toolbar에 메뉴 3개 추가 : 카메라/갤러리/업로드 메뉴.
- 3) 각 메뉴에 대한 이벤트 처리 시킬 것
    
    ![수정화면 최종.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/cf7075c2-4217-4058-b05b-21cffcd7bac2/%EC%88%98%EC%A0%95%ED%99%94%EB%A9%B4_%EC%B5%9C%EC%A2%85.png)
    

---

# 🙋🏻‍♀️ 앱 서버 프로그래밍 준비하기

## 🟦 서버 프로그래밍 준비

- **안드로이드 클라이언트와 통신할 서버프로그램 구현 준비 작업 수행**
- **서버로 jsp 활용한다.**

### **🟧 데이터베이스 생성**

- DB 이름 : groupAPP_dp
- table 종류
    - 1) 사용자 회원 정보 테이블
    - 2) 게시글 정보 테이블
    - 3) 게시글 내용 테이블

```sql
create database groupApp_db;

use groupApp_db;

create table user_table(
user_idx int not null primary key auto_increment,
user_id varchar(100) not null unique,
user_pw varchar(100) not null,
user_nick_name varchar(100) not null unique
);

create table board_table(
board_idx int not null primary key auto_increment,
board_name varchar(100) not null unique
);

insert into board_table (board_name) values ("게시판1");
insert into board_table (board_name) values ("게시판2");
insert into board_table (board_name) values ("게시판3");
insert into board_table (board_name) values ("게시판4");

create table content_table(
content_idx int not null primary key auto_increment,
content_board_idx int not null references board_table(board_idx),
content_writer_idx int not null references user_table(user_idx),
content_subject varchar(500) not null,
content_write_date datetime not null,
content_text longtext not null,
content_image varchar(500)
);
```

---

# 🙋🏻‍♀️ 앱 ‘회원 관리’ 기능 구현하기

## 🟦 회원 가입 기본 기능 구현

### ▶️ 회원 가입 기본 기능

- 회원 가입 화면에서 아이디/비번/닉네임에 대한 **입력X 상태의 유효성 검사 코드 작성**

### **🟧 MainActivity.kt**

- 우선 이 Main액티비티가 관리하고 있는 계정 관련 프래그먼트는 다음과 같다.

로그인 → 회원가입 → 닉네임 입력

- 세 가지 프래그먼트는 User 데이터를 공유해야 하기 때문에, 3가지 프래그먼트를 모두 관리하는 Main액티비티에서 사용자 정보 관리하는 변수를 선언한다.

```kotlin
//사용자 정보 관리 변수
var userId = ""
var userPw = ""
var userNickName = ""
```

### **🟧 JoinFragment.kt [회원가입 화면]**

- 우선 회원가입 시, 사용자가 이 화면에서 입력한 정보들을 받아두어야 한다.
- 다만, 여기서 받은 정보가 “NickName” 프래그먼트로도 넘어가야 하고, 해당 프래그먼트에서 최종 ‘가입’ 버튼 클릭 시, 가입이 완료되도록 만들어야 한다.
- 따라서, 이들 프래그먼트를 한 주기에서 관리하고 있는 MainActivity에서 선언했떤 User 정보 관련 변수에 현재 사용자가 프래그먼트 상에서 입력한 값들을 받아서 넘겨주고 이들을 공유해야 한다.
- 입력X → 모두 입력오류를 알림(Dialog) 토대로 알림 메시지를 띄워준다.

```kotlin
//'다음'버튼 이벤트 처리
binding.joinNextBtn.setOnClickListener{
//회원가입 User 정보를 받아둔다.
    val joinId = binding.joinId.text.toString()
    val joinPw = binding.joinPw.text.toString()

    //가입 ID : 사용자 X 입력 상태에 유효성 검사
    if(joinId == null || joinId.length == 0) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("아이디 입력 오류류")
        dialogBuilder.setMessage("아이디를 입력해주세요")
        dialogBuilder.setPositiveButton("확인"){dialogInterface: DialogInterface, i: Int->
binding.joinId.requestFocus() //다시 id 입력칸에 재포커싱
}
dialogBuilder.show()
        return@setOnClickListener
    }
    //가입 Pw : 사용자 X 입력 상태에 유효성 검사
    if(joinPw == null || joinPw.length == 0){
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("비밀번호 입력 오류류")
        dialogBuilder.setMessage("비밀번호를 입력해주세요")
        dialogBuilder.setPositiveButton("확인"){dialogInterface: DialogInterface, i: Int->
binding.joinPw.requestFocus() //다시 pw 입력칸에 재포커싱
}
dialogBuilder.show()
        return@setOnClickListener
    }

    //닉네임 설정 화면으로 전환
    //아직 회원가입 중인 상태라서 여기서 입력한 가입 정보를 다시 닉네임 화면에도 보내주어야 한다.
    val act =activityas MainActivity
    //전체 관리 중인 Main 액티비티의 변수에 현재 프래그먼트 상에 입력된 정보값을 담아둔다.
    act.userId = joinId
    act.userPw = joinPw

    act.fragmentController("nick_name", true, true)
}
```

### **🟧 NickNameFragment.kt**

- 이 프래그먼트는 앞선 JoinFragment.kt에서 넘겨준 값들과 함께 추가로 ‘닉네임’값을 사용자에게 받는 곳이다.
- 이 곳에서 닉네임 값까지 받은 상태에서 ‘입력완료’ 버튼을 클릭해야 최종 회원가입 성공이 된다.
- 따라서 이곳에서 사용자가 입력한 닉네임값도 역시 Main액티비티의 변수에 담아두고, 사용자가 아무런 입력 없이 가입 시도할 때 오류 메시지를 Dialog 형태로 띄운다.

```kotlin
//'입력완료' 버튼 클릭 이벤트 처리
binding.nicknameJoinBtn.setOnClickListener{
//사용자가 입력한 닉네임 값 받기
    val nickNameNickName = binding.nicknameNickname.text.toString()
    //입력값에 대한 유효성 검사 (입력X 상태에 대한)
    if(nickNameNickName == null || nickNameNickName.length == 0) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("닉네임 입력 오류")
        dialogBuilder.setMessage("닉네임을 입력해주세요")
        dialogBuilder.setPositiveButton("확인"){dialogInterface: DialogInterface, i: Int->
binding.nicknameNickname.requestFocus() //재포커싱
}
dialogBuilder.show()
        return@setOnClickListener
    }

    //현재의 닉네임 값을 다시 Main액티비티의 변수에 담아둔다.
    val act =activityas MainActivity
    act.userNickName = nickNameNickName
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/6b025f2c-9c4a-4287-b162-314413c2198f/Untitled.png)

---

## 🟦 회원 가입 처리

### ▶️ 회원 가입 처리 기능

- **사용자가 ‘회원 가입 화면’ 안에 입력한 회원 정보를 서버로 전달하여 서버에 저장하는 작업 처리**

### 📗 서버-클라이언트 ‘통신’ 관련 처리

### **🟧[서버] App_GroupCharge_Servrer 프로젝트 생성**

- **이 서버가 MySql 데이터베이스와 연동되도록 설정이 필요하다.**
    
    **1) mysql-connector-java.jar 파일을 lib 폴더에 추가한다. [커넥터]** 
    
    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c98be5b5-d7bc-48d1-bbe5-001f0a950c9e/Untitled.png)
    
    **2) jsp 파일에 [ java.sql.*] import 처리해준다.**
    
    - jsp 파일 안에서 DB 관련 작업 처리 가능해짐

### **🟧 [클라이언트] 앱 프로젝트에 OkHttp 라이브러리 설정 추가**

- **서버와 연동을 위함이다.**
    
    **1) 앱 수준의 build.gradle 파일에 다음을 추가**
    
    `**implementation 'com.squareup.okhttp3:okhttp:4.9.0'`** 
    
    **2) AndroidManifest.xml 에 관련 설정, 권한 추가**
    
    `android:usesCleartextTraffic="true"`
    
    `<uese-permission android:name="android.permission.INTERNET"/>` 
    
     **→ 인터넷 권한 추가** 
    

---

### **🟧 [클라이언트] NickNameFragment.kt**

- **회원가입 처리 시, 닉네임 정보 입력 프래그먼트가 가장 마지막 가입 화면**이다.
- 1) 이곳에서 **서버로 보낼 데이터를 최종 세팅**한다.
- 2) 이곳에서 **thread{ } 단위로 서버와 통신 작업을 수행**한다.
- 3) 서버에 요청한 **응답 response로 서버 통신 성공 여부에 따라 작업 수행**을 한다.
    
    ```kotlin
    package intro
    
    import android.content.DialogInterface
    import android.content.Intent
    import android.os.Bundle
    import android.util.Log
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.appcompat.app.AlertDialog
    import com.example.appgrouppurchasemaching.R
    import com.example.appgrouppurchasemaching.databinding.FragmentNickNameBinding
    import okhttp3.FormBody
    import okhttp3.OkHttp
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
            binding.nicknameToolbar.title= "닉네임 입력"
    
            //'입력완료' 버튼 클릭 이벤트 처리
            binding.nicknameJoinBtn.setOnClickListener{
    //뷰 바인딩 -> 뷰에서 사용자가 입력한 닉네임 값 받기
                val nickNameNickName = binding.nicknameNickname.text.toString()
                //입력값에 대한 유효성 검사 (입력X 상태에 대한)
                if(nickNameNickName == null || nickNameNickName.length == 0) {
                    val dialogBuilder = AlertDialog.Builder(requireContext())
                    dialogBuilder.setTitle("닉네임 입력 오류")
                    dialogBuilder.setMessage("닉네임을 입력해주세요")
                    dialogBuilder.setPositiveButton("확인"){dialogInterface: DialogInterface, i: Int->
    binding.nicknameNickname.requestFocus() //재포커싱
    }
    dialogBuilder.show()
                    return@setOnClickListener
                }
    
                //뷰에서 받은 닉네임 값을 다시 Main액티비티의 변수에 담아둔다.
                val act =activityas MainActivity
                act.userNickName = nickNameNickName
    
                //-> 서버와 통신 작업
    thread{
    val client = OkHttpClient() //클라이언트 객체 생성
    
                    val site = "http://192.168.200.150:8080/App_GroupCharge_Server/join_user.jsp"
    
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
                            dialogBuilder.setPositiveButton("확인"){dialogInterface: DialogInterface, i: Int->
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
    ```
    

### **🟧 [서버] join_user.jsp**

- 1) 이곳에서 **클라이언트가 요청한 POST 방식의 데이터를 getParamger로 얻은 뒤, 그 값을 DB 상에 저장시켜주는 역할을 하도록 만들 것이다.**

```java
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>

<% 
	//이 jsp 에서는 클라이언트가 DB 상에 가입 처리를 원하는 회원 정보를 POST 형태로 받아서
	//다시 DB 상에 저장시켜주는 기능을 할 것이다.
	
	//클라이언트가 전달하는 데이터 한글 깨지지 않도록 설정
	request.setCharacterEncoding("utf-8");
		
	//클라이언트가 전달한 데이터 추출
	String userId = request.getParameter("user_id");
	String userPw = request.getParameter("user_pw");
	String userNickName = request.getParameter("user_nick_name");
	
	//DB 접속 정보 세팅
	String dbUrl = "jdbc:mysql://localhost:3306/groupapp_db";
	String dbId = "root";
	String dbPw = "1234";
	
	//드라이버 로딩
	Class.forName("com.mysql.cj.jdbc.Driver");
	
	//DB 실질적 접속
	Connection conn = DriverManager.getConnection(dbUrl, dbId, dbPw);
	
	//쿼리문 작성
	String sql = "insert into user_table "
				+ "(user_id, user_pw, user_nick_name) "
				+ "values (?, ?, ?)";
	//쿼리 실행
	PreparedStatement pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, userId);
	pstmt.setString(2, userPw);
	pstmt.setString(3, userNickName);
	
	pstmt.execute();
	
	//접속 종료
	conn.close();

%>
```

### **🟧** [DB] groupApp_db 데이터베이스

- user_table 테이블 속에 서버가 클라이언트로부터 받은 user_id, user_pw, user_nick_name 값을 차례로 저장됐다.
    
    ![최종설명.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/50bc288f-af58-453a-ba27-36359d5d2247/%EC%B5%9C%EC%A2%85%EC%84%A4%EB%AA%85.png)
    

---

## 🟦 로그인 기본 처리

### ▶️ 로그인 기본 처리

- **로그인 시도 시, 입력 정보의 유효성 검사 진행**
- **여기서는 입력 여부만 확인한다.**

### **🟧 [클라이언트] LoginFragment.kt**

- 이 화면에서는 **사용자가 ‘ID/PW’ 값을 입력 후 ‘로그인’ 클릭 시, 사용자 입력값 null 검사하여 유효성 검사 처리**를 한다.

```kotlin
package intro

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import board.BoardMainActivity
import com.example.appgrouppurchasemaching.databinding.FragmentLoginBinding

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
        binding.loginToolbar.title= "로그인"

        //'회원가입' 버튼 클릭 시, -> 회원가입 화면 전환 이벤트 처리
        binding.loginJoinbtn.setOnClickListener{
//현재 프래그먼트 소유 중인 액티비티 추출 가능
            val act =activityas MainActivity
            act.fragmentController("join", true, true)
}
//'로그인' 버튼 클릭 시, 화면 전환
        binding.loginLoginbtn.setOnClickListener{

//사용자가 뷰에 입력한 데이터 가져오기
            val loginId = binding.loginId.text.toString()
            val loginPw = binding.loginPw.text.toString()

            //유효성 검사 id
            if(loginId == null || loginId.length == 0) {
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setTitle("아이디 입력 오류")
                dialogBuilder.setMessage("아이디를 입력해주세요")
                dialogBuilder.setPositiveButton("확인"){dialogInterface: DialogInterface, i: Int->
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
                dialogBuilder.setPositiveButton("확인"){dialogInterface: DialogInterface, i: Int->
binding.loginPw.requestFocus() //칸에 재포커싱
}
dialogBuilder.show()
                return@setOnClickListener
            }

            //화면 전환 처리리
            //val boardMainIntent = Intent(requireContext(), BoardMainActivity::class.java)
            //startActivity(boardMainIntent)
           // activity?.finish()
}

return binding.root
}

}
```

---

## 🟦 로그인 처리

### ▶️ 로그인 처리하기

- **로그인 화면에서 사용자가 입력한 정보값을 토대로 ‘로그인 처리’ 수행**
- **로그인 성공 시, 서버가 보내주는 사용자 정보를 Preference에 저장하는 처리 작업도 수행**

### **🟧 [서버] login_user.jsp**

```java
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>

<%
	//login_user.jsp에서는 클라이언트에서 로그인 시도 시 입력받은 사용자 id/pw 데이터값을 
	//받아서 실제로 해당 id/pw값의 user_idx 값을 반환하는 용도이다.
	
	//클라이언트가 전달하는 데이터 한글 깨지지 않도록 설정
	request.setCharacterEncoding("utf-8");
	//클라이언트가 보낸 데이터 추출
	String userId = request.getParameter("user_id");
	String userPw = request.getParameter("user_pw");

	//DB 접속 정보 세팅
	String dbUrl = "jdbc:mysql://localhost:3306/groupapp_db";
	String dbId = "root";
	String dbPw = "1234";
	
	//드라이버 로딩
	Class.forName("com.mysql.cj.jdbc.Driver");
	
	//DB 실질적 접속
	Connection conn = DriverManager.getConnection(dbUrl, dbId, dbPw);
	
	//쿼리문 작성
	String sql = "select user_idx from user_table "
				+ "where user_id = ? and user_pw = ?";
	//쿼리 실행 세팅
	PreparedStatement pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, userId);
	pstmt.setString(2, userPw);
	
	//쿼리 실행 반환값은 ResultSet으로 받음
	ResultSet rs = pstmt.executeQuery();

	if(rs.next() == false){ //가져온 값이 없다면
		out.write("FAIL");
	}else{ //가져온 값이 존재할 경우 
		int user_idx = rs.getInt("user_idx");
		out.write(user_idx+ "");
	}
	
	//DB접속 종료
	conn.close();
%>
```

### **🟧 [클라이언트] loginFragment.kt**

- **서버와 통신 작업 처리 thread{ }**
- **1) 서버 통신 성공 시 → 응답 결과값 추출**
    - 1) 추출한 값 FAIL = 로그인 실패(DB 상 id/pw불일치)
    - 2) 그 외 = **로그인 성공**
        - **로그인 성공 시, 해당 회원의 USER정보를 Preferences에 저장**한다.
            - → 이 앱 안에서 해당 정보를 공유할 수 있도록 저장 처리.
    
    **2) 서버 통신 실패 시 → 실패 알림 띄움**
    

```kotlin
    //-> 서버 통신 작업 수행
thread{
val client = OkHttpClient() //클라이언트 객체

        val site = "http://192.168.200.150:8080/App_GroupCharge_Server/login_user.jsp"

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
activity?.runOnUiThread{
val dialogBuilder = AlertDialog.Builder(requireContext())
                    dialogBuilder.setTitle("로그인 실패")
                    dialogBuilder.setMessage("아이디나 비밀번호가 잘못되었습니다.")
                    dialogBuilder.setPositiveButton("확인"){dialogInterface: DialogInterface, i: Int->
binding.loginId.setText("") //id칸 공백처리
                        binding.loginPw.setText("") //pw칸 공백처리
                        binding.loginId.requestFocus() //id에 Re포커싱줌줌                            }
}
dialogBuilder.show()
}
}
            else{ //서버로부터 받은 응답 결과값 = 로그인 성공
activity?.runOnUiThread{
val dialogBuilder = AlertDialog.Builder(requireContext())
                    dialogBuilder.setTitle("로그인 성공")
                    dialogBuilder.setMessage("로그인 성공하였습니다.")
                    dialogBuilder.setPositiveButton("확인"){dialogInterface: DialogInterface, i: Int->
//사용자 정보를 Preferences에 저장 -> 이후 Preference 접근은 액티비티로 접근하면된다.
                                                                //이름 = login_data, 모드 = 이 앱 안에서 데이터 공유 목적
                        val pref =activity?.getSharedPreferences("login_data", Context.MODE_PRIVATE)
                        val editor = pref?.edit() //편집 사용

                        editor?.putInt("login_user_idx", Integer.parseInt(result_text)) //서버로부터 받은 값을 int형변환 후 put 처리
                        editor?.commit() //실행

                        //화면 전환 처리
                        val boardMainIntent = Intent(requireContext(), BoardMainActivity::class.java)
                        startActivity(boardMainIntent)
activity?.finish()
}
dialogBuilder.show()
}
}
        }else{ //통신 실패 시 작업 처리
activity?.runOnUiThread{
val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setTitle("로그인 오류")
                dialogBuilder.setMessage("로그인 오류 발생")
                dialogBuilder.setPositiveButton("확인", null)
                dialogBuilder.show()
}
}

}
}
```

### **📍안드로이드의 Preferences란?**

- xml 형태로 키-값 데이터 생성하는 안드로이드 기본 제공 간단한 데이터 저장 방식.
- **1) SharedPreferences 선언**

```kotlin
SharedPreferences pref = getSharedPreferences(고유키이름, 프리퍼런스_저장모드)
```

**→ 두번째 매개변수(MODE_PRIVATE) : 프리퍼런스의 저장 모드를 정의한다.**

**[MODE_PRIVATE : 이 앱안에서 데이터 공유]**

**[MODE_WORLD_READABLE : 다른 앱과 데이터 읽기 공유]**

**[MODE_WORLD_WRITEABLE : 다른 앱과 데이터 쓰기 공유]**

---

![최종.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ef8b2e99-276e-4ed0-b1f7-7c5de5333902/%EC%B5%9C%EC%A2%85.png)

---

## 📌 서버 IP 정보 따로 관리해주기

- 매번 바뀌는 서버 IP 정보 관리를 위해서 companion object 로 서버 IP 값만 따로 관리
- **추후에 서버 IP 값이 바뀔 경우 이 변수 부분만 변경해주면 되도록 관리**

### **🟧 ServerInfo.class**

```kotlin
class ServerInfo { //'서버' IP 정보 객체

 companion object { //자바의 static 과 같은 역할
     const val SERVER_IP = "192.168.200.150"
 }
}
```

---

# 🙋🏻‍♀️ 앱 ‘게시판 관련 기능’ 구현하기

## 🟦 게시판 ‘목록’ 데이터 가져오기

### ▶️ 게시판 목록 데이터 가져오기

- 게시글 목록 화면에서 게시판 목록 메뉴 구성
- **서버로부터 게시판 목록의 이름, 인덱스번호를 받아와서 이를 토대로 게시판 목록 메뉴(Spinner)를 구성**한다.
- **이 데이터는 게시글 작성 화면 (목록 선택)에도, 게시글 목록 화면에서도, 게시글 수정 화면(목록 선택 변경)에서도 모두 등장**해야 한다.
- 따라서 여러 게시글 관련 프래그먼트에서 해당 데이터들을 사용할 수 있도록, **상위의 BoardMainActivity 액티비티에서 데이터를 받아올 것이다.**

---

### ▶️ **JSON 타입으로 게시판 목록 데이터 가져오기**

### **🟧 [서버] get_board_list.jsp**

- 여기서는 곧바로 DB 속 게시글 목록 데이터 select 한 뒤, JSON 데이터 형태로 클라이언트에게 보내주는 일을 한다.

```kotlin
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "java.sql.*" %>
<%@ page import = "org.json.simple.*" %>

<%
	//여기서 할 작업은 곧장 클라이언트에 DB 속 게시글 목록 데이터를 추출해서 JSON 타입으로 보내주는 것
	
	//DB 접속 정보 세팅
	String dbUrl = "jdbc:mysql://localhost:3306/groupapp_db";
	String dbId = "root";
	String dbPw = "1234";
	
	//드라이버 로딩
	Class.forName("com.mysql.cj.jdbc.Driver");
	
	//DB 실질적 접속
	Connection conn = DriverManager.getConnection(dbUrl, dbId, dbPw);
	
	//쿼리문
	String sql = "select board_idx, board_name from board_table order by board_idx"	;
	
	PreparedStatement pstmt = conn.prepareStatement(sql);
	ResultSet rs = pstmt.executeQuery(); //쿼리 실행 
	
	//JSON 배열 객체 생성 
	JSONArray root = new JSONArray();
	
	while(rs.next()){
		int boardIdx = rs.getInt("board_idx");
		String boardName = rs.getString("board_name");
		
		//각각의 데이터 (idx, name)를  하나의 JSON 객체로 담음
		JSONObject obj = new JSONObject();
		obj.put("board_idx", boardIdx);
		obj.put("board_name", boardName);
		
		//위의 한 JSON 객체를 JSON배열 객체에 다시 담는다.
		root.add(obj);
	}
	
	//연결 종료
	conn.close();
%>
<%= root.toJSONString() %>
```

![JSON 문서.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/24d0718e-782b-4b12-893e-f02eddd9614c/JSON_%EB%AC%B8%EC%84%9C.png)

---

### **🟧 json-simple 라이브러리**

- JSON 데이터 쉽게 처리 위한 자바의 라이브러리
    
    ### **◾ Json-simple 라이브러리 특징**
    
    1) 내부적으로 JSON 데이터 처리를 위해 Map과 List 사용한다.
    
    2) JSON 데이터를 구문 분석하고 JSON 파일에 기록 가능하다
    
    3) 타사 라이브러리에 대한 의존성이 없다
    
    4) 매우 가벼운 API 이며 간단한 JSON 데이터 처리에 적합하다.
    
    ### **◾ Json-simple 라이브러리의 주요 클래스**
    
    org.json.simple Class **JSONObject**
    
    - **JSON ‘객체’를 추상화한 클래스**
    - java.util.HashMap 클래스를 상속받고 있으므로 대부분의 메소드가 HashMap 클래스로부터 상속받은 것들이다.
    
    org.json.simple Class **JSONArray**
    
    - **JSON ‘배열’을 추상화한 클래스**
    - java.util.ArrayList 클래스를 상속받고 있으므로 대부분의 메소드 사용법은 ArrayList와 흡사
    
    org.json.simple Class **JSONParser**
    
    - **JSON 데이터 파싱 기능 구현한 클래스**
    
    org.json.simple Class **JSONValue**
    
    - **JSON 데이터 다루기 위한 몇 가지 메소드들 제공**
    
    org.json.simple Class **JSONException**
    
    - **JSONParser 클래스를 사용하여 파싱할 때 발생 가능한 예외 사항 추상화 클래스**

---

### **🟧 [클라이언트]**

### ◾ **액티비티 [boardMainActivity.kt]**

- 이곳에서 여러 프래그먼트들이 사용할 게시글 목록 데이터들을 **ArrayList<>타입**으로 받아둔다.

```kotlin
//게시판 목록 '이름'들을 받을 List<> 변수
val boardNameList = ArrayList<String>()
//게시판 목록 'idx' 받을 List<> 변수
val boardIndexList = ArrayList<Int>()
```

### **◾ 프래그먼트 [boardMainFragment.kt]**

- 액티비티에 받아둔 데이터를 가져와서 띄움
