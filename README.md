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

### **### **프래그먼트 [boardMainFragment.kt]**

- 액티비티에 받아둔 데이터를 가져와서 띄움

```kotlin
override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
    // Inflate the layout for this fragment

    val act =activityas BoardMainActivity

    //바인딩
    binding = FragmentBoardMainBinding.inflate(inflater)
    binding.boardMainToolbar.title= act.boardNameList[act.selectedBoardType]
                                //-> 사용자 선택한 게시글 목록 idx 값에 따르 이름값을 title로 세팅하기
    //게시판 항목 메뉴 추가하기
    binding.boardMainToolbar.inflateMenu(R.menu.board_main_menu)
    // 이 항목 메뉴 item 클릭 이벤트 처리
    binding.boardMainToolbar.setOnMenuItemClickListener{
//툴바 속 존재하는 메뉴item 클릭 시. 이벤트 처리
        when(it.itemId){
            R.id.board_main_menu_board_list-> {  //다이얼로그로 게시판 목록을 띄운다.
                //액티비티에서 받아놨던 데이터 받기 위해 act
                val act =activityas BoardMainActivity
                //단 여기서 toTypedArray()로 변경해주어야 한다. Array 객체로 변경
                val boardListBuilder = AlertDialog.Builder(requireContext())
                boardListBuilder.setTitle("게시판 목록")
                boardListBuilder.setNegativeButton("취소", null)
                        //여기서 게시판 목록 클릭하는 경우 이벤트 처리
                boardListBuilder.setItems(act.boardNameList.toTypedArray()){dialogInterface: DialogInterface, i: Int->
										act.selectedBoardType = i
										//바인딩으로 각 툴바 title을 재세팅
                    binding.boardMainToolbar.title= act.boardNameList[act.selectedBoardType]
}
								boardListBuilder.show()
                true //메뉴 클릭 시 무언가 작업했으므로 T 반환시킴
            }
            R.id.board_main_menu_write-> { //글쓰기 메뉴 클릭 시
                val act =activityas BoardMainActivity
                act.fragmentController("board_write", true, true)
                true
            }
            R.id.board_main_menu_controller-> { //다른 항목 메뉴 컨트롤러 클릭 시,
                // -> 프래그먼트 이동시킬 건데,
                val act =activityas BoardMainActivity
                act.fragmentController("menu_controller", true, true)
                true
            }

            else -> false
        }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/b4cbd194-eff1-422b-bc07-e241ca603cb9/Untitled.png)

---

### **🟧 BoardWriteFragment.kt**

- **게시글 작성 화면에서도 목록 (Spinner)구성 시, BoardMainActivity 속에 담아둔 데이터를 받아서 구성**해주어야 한다.
- 스피너로 목록 작성하는 부분의 코드를 수정해서 **데이터 구성을 담는 작업을 수행**한다.
    - **게시글 목록 화면에서 선택한 게시글 목록**이 → **게시글 작성 화면에서도 동일하게 나타남**
    - → 이유: 게시판 선택 변수 selectedBoardType을 관련 프래그먼트를 관리하는 BoardMainActivity의 변수이기 때문에 여러 프래그먼트에서 공유가 가능하다.

```kotlin
//스피너 어댑터 생성 - 액티비티 속 데이터 가져와서 스피너 목록 구성
val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, act.boardNameList.drop(1))

spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
binding.boardWriteType.adapter= spinnerAdapter

//사용자가 선택한 게시글 목록에 따른 처리
if(act.selectedBoardType == 0) { //'전체' 게시판 선택 시
    binding.boardWriteType.setSelection(0)
}else{
    binding.boardWriteType.setSelection(act.selectedBoardType - 1)
}
```

## 🟦 이미지 첨부 구현

### ▶️ 이미지 첨부 구현

- **게시글 작성 화면에서 카메라/앨범을 통해 사진을 가져와 이미지 뷰에 보여주는 작업 수행**

---

# **🙋🏻‍♀️ 1) 갤러리 연동**

### **🟧 AndroidManifest.xml 에 카메라 관련 권한 추가**

- **READ_EXTERNAL_STORAGE** : 외부 저장소에서 파일 읽기 권한
- **ACEESS_MEDIA_LOCATION** : 미디어 위치 정보 액세스 권한

```kotlin
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.ACCESS_MEDIA_LOCATION"/>
```

### **🟧 BoardMainActivity.kt**

- 위에서 추가한 권한에 대한 ‘확인’ 코드 작성

```kotlin
//권한 확인 리스트
val permissionList =arrayOf(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.ACCESS_MEDIA_LOCATION
)
```

- onCreate() 메소드 내부에서 앱 실행  시, 사용자에게 권한 요청받는 콜백함수 작성

```kotlin

//권한 요청 - 자동 콜백함수
requestPermissions(permissionList, 0)
```

### **🟧 res/xml 디렉토리 추가 → file_path.xml 작성**

- 저장소 접근 경로를 xml 에 지정

```xml
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-path
        name="storage/emulated/0"
        path="."/>
</paths>
```

### **🟧 AndroidManifest.xml 에 위의 xml 등록**

```xml
<provider
    android:authorities="com.example.appgrouppurchasemaching.camera.file_provider"
    android:name="androidx.core.content.FileProvider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_path"/>
</provider>
```

# **🙋🏻‍♀️ 2) 카메라 연동**

### **🟧 BoardWriteFragment.kt**

- 카메라/갤러리 버튼 클릭 → 선택된 이미지가 이미지뷰에 Set 처리됨

```kotlin
package com.example.appgrouppurchasemaching.board

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.FileProvider
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.databinding.FragmentBoardWriteBinding
import java.io.File

class BoardWriteFragment : Fragment() {//글쓰기 프래그먼트 화면

    //바인딩
    lateinit var binding : FragmentBoardWriteBinding

    //경로 담을 Uri 객체
    lateinit var contentUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        //BoardMainActivity 에서 서버로부터 받아온 '게시글 목록'데이터 받기 위함
        val act =activityas BoardMainActivity

        //바인딩
        binding = FragmentBoardWriteBinding.inflate(inflater)
        binding.boardWriteToolbar.title= "게시글 작성"

        //Back 버튼을 툴바 상단의 navigationIcon으로 추가한다.
        val navIcon =
            requireContext().getDrawable(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.boardWriteToolbar.navigationIcon= navIcon

        //뒤로가기 네비게이션 클릭 이벤트 처리
        binding.boardWriteToolbar.setNavigationOnClickListener{
val act =activityas BoardMainActivity
            //현재의 프래그먼트를 백스택에서 pop 제거 처리
            act.fragmentRemoveBackStack("board_write")
}

//툴바 속 메뉴 세팅
        binding.boardWriteToolbar.inflateMenu(R.menu.board_write_menu)
        binding.boardWriteToolbar.setOnMenuItemClickListener{
when (it.itemId) {
                R.id.board_write_menu_camera-> { //카메라 클릭 시
                    val filePath = requireContext().getExternalFilesDir(null).toString()

                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                    // 촬영한 사진이 저장될 파일 이름
                    val fileName = "/temp_${System.currentTimeMillis()}.jpg"
                    val picPath = "$filePath/$fileName"

                    val file = File(picPath)

                    contentUri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.appgrouppurchasemaching.camera.file_provider", file
                    )

                    if (contentUri != null) {
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
                        startActivityForResult(cameraIntent, 1)
                    }

                    true
                }
                R.id.board_write_menu_gallery-> { //앨범 클릭 시
                    val albumIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    albumIntent.type= "image/*"

                    val mimeType =arrayOf("image/*")
                    albumIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
                    startActivityForResult(albumIntent, 2)
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

//스피너 어댑터 생성 - 액티비티 속 데이터 가져와서 스피너 목록 구성
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            act.boardNameList.drop(1)
        )

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.boardWriteType.adapter= spinnerAdapter

        //사용자가 선택한 게시글 목록에 따른 처리
        if (act.selectedBoardType == 0) { //'전체' 게시판 선택 시
            binding.boardWriteType.setSelection(0)
        } else {
            binding.boardWriteType.setSelection(act.selectedBoardType - 1)
        }

       return binding.root
}

    //재정의
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap = BitmapFactory.decodeFile(contentUri.path)
                    binding.boardWriteImage.setImageBitmap(bitmap) //이미지뷰에 세팅

                    val file = File(contentUri.path)
                    file.delete()
                }
            }
            2 -> {
                if (resultCode == Activity.RESULT_OK) {
                    // 선택한 이미지에 접근할 수 있는 uri
                    val uri = data?.data

if (uri != null) {
                        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q) {
                            val source =
                                ImageDecoder.createSource(activity?.contentResolver!!, uri)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            binding.boardWriteImage.setImageBitmap(bitmap) //이미지 뷰에 세팅
                        } else {
                            val cursor =
activity?.contentResolver?.query(uri, null, null, null, null)
                            if (cursor != null) {
                                cursor.moveToNext()
                                // 이미지 경로를 가져온다.
                                val index = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                                val source = cursor.getString(index)
                                val bitmap = BitmapFactory.decodeFile(source)
                                binding.boardWriteImage.setImageBitmap(bitmap)
                            }
                        }
                    }
                }
            }
        }
    }

}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/4b530e00-4640-423f-a9aa-535dadfccde6/Untitled.png)

---

## 🟦 게시글 작성 시 유효성 검사

### ▶️ 게시글 작성 유효성 검사

- 글 작성 화면에서 사용자가 입력해야 하는 부분에 대한 유효성 검사를 실시한다.
- ‘**입력하지 않고 업로드 시도하는 경우’에 대한 유효성 검사**

---

### **🟧 BoardWriteFragment.kt**

- **Toolbar 속 upload 클릭 시 → 유효성 검사 코드 작성**
- onCreateView() 속 툴바 메뉴 아이템 이벤트 리스터 내부의 **‘upload’ 메뉴 클릭 시 이벤트 처리.**
- 1) 사용자가 뷰에 입력한 값들을 우선 받아둔 뒤
- 2) 해당 값이 null or 길이 = 0 인 경우에 한해서 Dialog 알람을 띄우는 유효성 검사 실시한다.

```kotlin
R.id.board_write_menu_upload-> { //작성완료 클릭 시
    val act =activityas BoardMainActivity

    //뷰 바인딩으로 뷰 속에 사용자 입력한 내용 가져오기
    //글 제목, 내용물 값 받아서
    val boardWriteSubject = binding.boardWriteSubject.text.toString()
    val boardWriteText = binding.boardWriteText.text.toString()
    // 1) 글 제목 부분 작성 X -> 유효성 검사
    if(boardWriteSubject == null || boardWriteSubject.length == 0 ){
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("제목 입력 오류")
        dialogBuilder.setMessage("제목을 입력해주세요. ")
        dialogBuilder.setPositiveButton("확인"){dialogInterface: DialogInterface, i: Int->
binding.boardWriteSubject.requestFocus() //글 제목 작성 칸 재포커싱
}
dialogBuilder.show()
        return@setOnMenuItemClickListener true
    }
    // 2) 글 내용 부분 작성 X -> 유효성 검사
    if(boardWriteText == null || boardWriteText.length == 0 ){
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("내용 입력 오류")
        dialogBuilder.setMessage("내용을 입력해주세요.")
        dialogBuilder.setPositiveButton("확인"){dialogInterface: DialogInterface, i: Int->
binding.boardWriteText.requestFocus() //글 제목 작성 칸 재포커싱
}
dialogBuilder.show()
        return@setOnMenuItemClickListener true
    }

```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/f5bff65a-0edc-4d59-b3a8-7433bf71c8a9/Untitled.png)

---

## 🟦 작성한 게시글 업로드 처리

### ▶️ 사용자 작성 글 업로드 처리

- 사용자가 작성한 글 내용을 **‘서버’에 업로드하는 작업 수행**

---

### **🟧 [DB] content_table 내용 설명**

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c3afe421-ee92-47b4-82b8-4b100605ce77/Untitled.png)

### **🟧 [서버] add_content.jsp**

- 이곳에서 클라이언트가 작성한 **[글제목/내용/작성자/날짜/이미지] 데이터를 전달받아서 DB 상에 저장**시켜줄 것이다.

```java
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%
	//클라이언트가 전달하는 데이터 한글 깨지지 않도록 설정
	request.setCharacterEncoding("utf-8");
	
	//클라이언트가 보낸 작성 게시글 관련 데이터 추출
	String str1= request.getParameter("content_board_idx");
	int content_board_idx = Integer.parseInt(str1); //형변환
	
	String str2 = request.getParameter("content_writer_idx");
	int content_writer_idx = Integer.parseInt(str2); //형변환
	
	String content_subject = request.getParameter("content_subject");
	String content_text = request.getParameter("content_text");

	//DB 접속 정보 세팅
	String dbUrl = "jdbc:mysql://localhost:3306/groupapp_db";
	String dbId = "root";
	String dbPw = "1234";
	
	//드라이버 로딩
	Class.forName("com.mysql.cj.jdbc.Driver");
	
	//DB 실질적 접속
	Connection conn = DriverManager.getConnection(dbUrl, dbId, dbPw);
	
	//쿼리문 작성 - 게시글목록/작성자idx/글제목/글내용
	String sql = "insert into content_table "
				+ "(content_board_idx, content_writer_idx, content_subject, content_text) values (?, ?, ?, ?)";
	
	//쿼리 실행 
	PreparedStatement pstmt = conn.prepareStatement(sql);
	
	pstmt.setInt(1, content_board_idx);
	pstmt.setInt(2, content_writer_idx);
	pstmt.setString(3, content_subject);
	pstmt.setString(4, content_text);
	
	pstmt.execute();//실행 
	
	conn.close();//접속 끊기 
	
%>
```

### **🟧 [클라이언트] BoardWriteFragment.kt**

- 1) 사용자가 앱 화면 view(글 작성 화면) 에 작성한 글 제목/내용/작성자idx/게시글목록idx 데이터를 서버에 보내고 DB 에 최종 저장시킨다.
- 2) 사용자의 작성 글 관련 데이터가 서버에 성공적으로 저장될 경우 → 키보드 내려가고 작성 완료 알림을 띄운다.

```kotlin
//툴바 속 메뉴 세팅
binding.boardWriteToolbar.inflateMenu(R.menu.board_write_menu)
binding.boardWriteToolbar.setOnMenuItemClickListener{
when (it.itemId) {
        R.id.board_write_menu_camera-> { //카메라 클릭 시
	           . . . 
        }
        R.id.board_write_menu_gallery-> { //앨범 클릭 시
            . . .
        }
        R.id.board_write_menu_upload-> { //작성완료 클릭 시
            val act =activityas BoardMainActivity

            //뷰 바인딩으로 뷰 속에 사용자 입력한 내용 가져오기
            //글 제목, 내용 데이터
            val boardWriteSubject = binding.boardWriteSubject.text.toString()
            val boardWriteText = binding.boardWriteText.text.toString()
            //액티비티 단위로 호환되는 게시글 목록 idx 가져옴
            val boardWriteType = act.boardIndexList[binding.boardWriteType.selectedItemPosition+ 1]
            //앱 단위로 호환되는 Preferences에 저장된 로그인 idx 가져옴
            val pref = requireContext().getSharedPreferences("login_data", Context.MODE_PRIVATE)
            val boardWriterIdx = pref.getInt("login_user_idx", 0)
						
						// ... 유효성 검사 코드 (생략) 

      
  **//-> [서버 통신 작업 처리]**
thread{
val client = OkHttpClient()

                val site = "http://${ServerInfo.SERVER_IP}:8080/App_GroupCharge_Server/add_content.jsp"

                //보낼 데이터 세팅
                val builder1 = FormBody.Builder()
                builder1.add("content_board_idx", "$boardWriteType")
                builder1.add("content_writer_idx", "$boardWriterIdx")
                builder1.add("content_subject", boardWriteSubject)
                builder1.add("content_text", boardWriteText)
                val formBody = builder1.build() //생성

                //요청Request
                val request = Request.Builder().url(site).post(formBody).build()
                //요청 반환값은 response 변수로 받음
                val response = client.newCall(request).execute()

                if(response.isSuccessful == true){ //서버 통신 성공 시,
                    //화면 관련 작업은 runOnUiThread 처리
activity?.runOnUiThread{
//키보드 숨김 설정 - 글 작성 중이던 키보드 숨기기 처리
                        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(binding.boardWriteSubject.windowToken, 0)
                        inputMethodManager.hideSoftInputFromWindow(binding.boardWriteText.windowToken, 0)

                        //알림
                        val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setTitle("작성 완료")
                        dialogBuilder.setMessage("작성이 완료되었습니다.")
                        dialogBuilder.setPositiveButton("확인"){dialogInterface: DialogInterface, i: Int->
//화면 전환 처리
                            act.fragmentRemoveBackStack("board_write") //현재 프래그먼트는 제거하고
                            act.fragmentController("board_read", true, true) //현재 글의 읽기 프래그먼트로 바로 전환
}
dialogBuilder.show()
}
} else { //서버 통신 실패 시
activity?.runOnUiThread{
val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setTitle("작성 오류")
                        dialogBuilder.setMessage("작성 오류가 발생하였습니다.")
                        dialogBuilder.setPositiveButton("확인", null)
                        dialogBuilder.show()
		}
	}
}
true
        }
        else -> false
    }
}
```

### **📍[추가] 키보드 보이기 OR 숨기기 설정**

**inputMethodManager 클래스 제공 메소드** 

   1) **showSoftInput(view, flags) :** 키보드 보이게 설정

   2) **hideSoftInputFromWindow() :** 키보드 숨기기 설정

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/55f5070c-5737-4595-9cf6-fc5fb3a05194/Untitled.png)

---

## 🟦 서버에 ‘첨부한 이미지’ 보내기

### ▶️ 서버에 ‘사용자가 선택한 이미지’ 저장하기

- **사용자가 선택한 ‘이미지 파일’을 서버로 전송하여 서버에 저장**한다.
- 이미지 뿐 아니라 **다양한 형태의 ‘파일 데이터’를 서버로 전달하는 방법**인 셈이다.

### 📌 COS 라이브러리 사용

- **1) [서버]에서 코드 구현 시, java 기반 웹 개발의 ‘파일 업로드 처리’를 위해서 널리 사용되어지는 [cos 라이브러리]를 사용한다.**
- **2) Okhttp3를 통해 서버로 파일 데이터 전달 시에는 ‘파일 경로를 지정’해주어야 한다.**
- **[이미지 파일을 임시 저장] 후 → [서버에 데이터를 전달] 의 순서로 작업을 진행한다.**

---

### **🟧 [서버]**

**1) cos 라이브러리.jar 파일을 WEB-INF/lib 하위 폴더에 담는다.**

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/d8b21420-a8c0-4d95-8d27-b4ef50e14982/Untitled.png)

**2) cos = com.oreilly.servlet의 약자**

- 따라서 COS 라이브러리를 사용하기 위한 import 필요

```java
<%@ page import = "com.oreilly.servlet.*" %>
<%@ page import = "com.oreilly.servlet.multipart.*" %>
```

**3) 파일 업로드 처리 작업**

- MultipartRequest객체를 이용한 파일 업로드 방법

```java
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import = "com.oreilly.servlet.*" %>
<%@ page import = "com.oreilly.servlet.multipart.*" %>
<%
	//클라이언트가 전달하는 데이터 한글 깨지지 않도록 설정
	request.setCharacterEncoding("utf-8");
	
	//실제 이미지 업로드할 upload 폴더의 경로 구하기 
	String uploadPath = application.getRealPath("upload");
	//System.out.println(uploadPath);
	
	//파일 업로드 처리 
	DefaultFileRenamePolicy policy = new DefaultFileRenamePolicy(); //중복된 파일이름 변경 정책 객체
	MultipartRequest multi = new MultipartRequest(request, uploadPath, 100*1024*1024, "utf-8", policy);

	//클라이언트가 보낸 작성 게시글 관련 데이터 추출 [request -> multi 변경 ]
	String str1= multi.getParameter("content_board_idx");
	int content_board_idx = Integer.parseInt(str1); //형변환
	
	String str2 = multi.getParameter("content_writer_idx");
	int content_writer_idx = Integer.parseInt(str2); //형변환
	
	String content_subject = multi.getParameter("content_subject");
	String content_text = multi.getParameter("content_text");
	
	//이미지 데이터 
	String content_image = multi.getFilesystemName("content_image"); 
	
	//DB 접속 정보 세팅
	String dbUrl = "jdbc:mysql://localhost:3306/groupapp_db";
	String dbId = "root";
	String dbPw = "1234";
	
	//드라이버 로딩
	Class.forName("com.mysql.cj.jdbc.Driver");
	
	//DB 실질적 접속
	Connection conn = DriverManager.getConnection(dbUrl, dbId, dbPw);
	
	//쿼리문 작성 - 게시글목록/작성자idx/글제목/글내용
	String sql = "insert into content_table "
				+ "(content_board_idx, content_writer_idx, content_subject, content_text, content_image) values (?, ?, ?, ?, ?)";
	
	//쿼리 실행 
	PreparedStatement pstmt = conn.prepareStatement(sql);
	
	pstmt.setInt(1, content_board_idx);
	pstmt.setInt(2, content_writer_idx);
	pstmt.setString(3, content_subject);
	pstmt.setString(4, content_text);
	pstmt.setString(5, content_image);
	
	pstmt.execute();//실행 
	
	conn.close();//접속 끊기 
	
%>
```

### **📌 [추가] webapp 폴더의 하위에 upload 폴더를 생성**

→ 이 폴더의 실제 Path를 알아야 이미지 업로드 시 경로를 제대로 줄 수 있다.

```kotlin
//실제 이미지 업로드할 upload 폴더의 경로 구하기 
	String uploadPath = application.getRealPath("upload");
	System.out.println(uploadPath);
```

![경로확인.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/5eebf1e2-6a6a-410e-bdc7-e35c96a2730e/%EA%B2%BD%EB%A1%9C%ED%99%95%EC%9D%B8.png)

### **🟧 [클라이언트] BoardWriteFragment.kt**

- 1) 이전까지는 **‘문자열’데이터를 보내기 위해 서버로 보낼 데이터 세팅 시 FormBody 를 사용**했다.
- 2) **서버로 보낼 데이터 세팅에, 파일 데이터를 포함할 경우에는 MultipartBody 를 사용**해야 한다.
- **3) 사용자가 선택한 이미지 파일이 존재할 경우,**
    - 파일 경로 잡아주고 서버에 보낼 데이터에 최종 포함시켜준다.

```kotlin
            //-> 서버 통신 작업 처리
thread{
val client = OkHttpClient()

                val site = "http://${ServerInfo.SERVER_IP}:8080/App_GroupCharge_Server/add_content.jsp"

                //보낼 데이터 세팅 -FormBody = '문자열' 데이터 세팅
                // cf. MultipartBody = 파일 데이터까지 포함한한세팅
                val builder1 = MultipartBody.Builder()
                builder1.setType(MultipartBody.FORM) //타입 세팅 필요
                builder1.addFormDataPart("content_board_idx", "$boardWriteType")
                builder1.addFormDataPart("content_writer_idx", "$boardWriterIdx")
                builder1.addFormDataPart("content_subject", boardWriteSubject)
                builder1.addFormDataPart("content_text", boardWriteText)

                var file : File? = null
                //사용자가 선택한 이미지 파일 존재하는 경우에 한해서
                if(uploadImage != null) {
                    val filePath = requireContext().getExternalFilesDir(null).toString()
                    val fileName = "/temp_${System.currentTimeMillis()}.jpg"
                    val picPath = "$filePath/$fileName"
                    file = File(picPath)
                    val fos = FileOutputStream(file)
                    uploadImage?.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    //파일 읽어서 '서버로 보낼 데이터'에 포함 시켜준다.
                    builder1.addFormDataPart("content_image", file.name, file.asRequestBody(MultipartBody.FORM))
                }

                val formBody = builder1.build() //생성

                //요청Request
                val request = Request.Builder().url(site).post(formBody).build()
                //요청 반환값은 response 변수로 받음
                val response = client.newCall(request).execute()

                if(response.isSuccessful == true){ //서버 통신 성공 시,
                    //화면 관련 작업은 runOnUiThread 처리
activity?.runOnUiThread{
//키보드 숨김 설정 - 글 작성 중이던 키보드 숨기기 처리
                        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(binding.boardWriteSubject.windowToken, 0)
                        inputMethodManager.hideSoftInputFromWindow(binding.boardWriteText.windowToken, 0)

                        //알림
                        val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setTitle("작성 완료")
                        dialogBuilder.setMessage("작성이 완료되었습니다.")
                        dialogBuilder.setPositiveButton("확인"){dialogInterface: DialogInterface, i: Int->
//화면 전환 처리
                            act.fragmentRemoveBackStack("board_write") //현재 프래그먼트는 제거하고
                            act.fragmentController("board_read", true, true) //현재 글의 읽기 프래그먼트로 바로 전환
}
dialogBuilder.show()
}
} else { //서버 통신 실패 시
activity?.runOnUiThread{
val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setTitle("작성 오류")
                        dialogBuilder.setMessage("작성 오류가 발생하였습니다.")
                        dialogBuilder.setPositiveButton("확인", null)
                        dialogBuilder.show()
}
}
}
true
        }
        else -> false
    }
}
```

### **🟧 최종 모습**

<img width="685" alt="업로드 최종" src="https://user-images.githubusercontent.com/39732720/185343216-1d6ae5c8-659f-48e8-a5d1-1a4c48c023ef.png">

## 🟦 게시글 읽는 화면 구현하기

### ▶️ 게시글 읽기 화면 구현

- 글 작성 시 작성된 글을 보여줄 때 띄워지는 ‘게시글 읽기 화면’
- **작성한 글의 idx 번호를 가져와서, 해당 인덱스 번호 정보를 토대로 작성 글을 띄울 수 있도록 처리함**
- **첨부한 이미지가 있는 경우 → 이미지 데이터도 함께 세팅**해줌

---

🙋🏻‍♀️ 최근 작성한 content_idx값을 추출해서 서버가 응답 결과로 주고, 클라이언트가 그 결과를 받아두는 작업 처리

### **🟧 [서버] add_content.jsp**

- **1) DB에 가서 사용자의 ‘현재’ 게시글 목록 idx 값을 기준으로 가장 큰 content_idx값 갖는 (즉, 최신의 글)인 애를 read_content_idx 값으로 SELECT 처리**
- **2) DB에서 SELECT한 값 = read_content_idx 값을 다시 클라이언트에게 응답 결과로 보내준다.**

```java
  //현재 작성한 게시글 idx 값을 응답 결과로 보내준다.
	//현재의 게시글 목록 idx 중에서 가장 content_idx 가 큰 애를 가져옴 (최근 작성 순)
	String sql2 = "select max(content_idx) as read_content_idx from content_table where content_board_idx = ?";
	
	PreparedStatement pstmt2 = conn.prepareStatement(sql2);
	pstmt2.setInt(1, content_board_idx);
	
	ResultSet rs = pstmt2.executeQuery();
	rs.next();
	
	int read_content_idx = rs.getInt("read_content_idx");
```

### **🟧 [클라이언트] BoardMainActivity.kt**

- 여기에 현재 사용자가 읽고 있는 게시글(내용) idx값 데이터를 갖고 있는다.

```kotlin
//현재 읽고있는 게시글의 idx값
var readContentIdx = 0
```

### **🟧 [클라이언트] BoardWriteFragment.kt**

- **1) 서버에서 보내온 read_content_idx 값을 응답 결과로 받은 뒤, BoardMainActivity에 세팅해놓았던 readContentIdx 변수에 할당**한다.
- **2) thread{ } 로 서버와 통신 작업을 처리.**
    - 서버가 보낸 응답 결과는 read_content_idx 값이므로 이 값을 다시 BoardMainActivity의 변수인 readContentIdx 변수에 세팅해줌
    - → 이유: BoardReadFragment에서 최근 작성한 글의 idx 값을 사용해야 하기 때문이다.
    
    **3) 작성 완료 알림 띄운 뒤 → BoardReadFragment로 화면 전환 처리**됨
    

```kotlin

//-> 서버 통신 작업 처리
thread{
val client = OkHttpClient()

                val site = "http://${ServerInfo.SERVER_IP}:8080/App_GroupCharge_Server/add_content.jsp"

                //보낼 데이터 세팅 -FormBody = '문자열' 데이터 세팅
                // cf. MultipartBody = 파일 데이터까지 포함한한세팅
                val builder1 = MultipartBody.Builder()
                builder1.setType(MultipartBody.FORM) //타입 세팅 필요
                builder1.addFormDataPart("content_board_idx", "$boardWriteType")
                builder1.addFormDataPart("content_writer_idx", "$boardWriterIdx")
                builder1.addFormDataPart("content_subject", boardWriteSubject)
                builder1.addFormDataPart("content_text", boardWriteText)

                var file : File? = null
                //사용자가 선택한 이미지 파일 존재하는 경우에 한해서
                if(uploadImage != null) {
                    val filePath = requireContext().getExternalFilesDir(null).toString()
                    val fileName = "/temp_${System.currentTimeMillis()}.jpg"
                    val picPath = "$filePath/$fileName"
                    file = File(picPath)
                    val fos = FileOutputStream(file)
                    uploadImage?.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    //파일 읽어서 '서버로 보낼 데이터'에 포함 시켜준다.
                    builder1.addFormDataPart("content_image", file.name, file.asRequestBody(MultipartBody.FORM))
                }

                val formBody = builder1.build() //생성

                //요청Request
                val request = Request.Builder().url(site).post(formBody).build()
                //요청 반환값은 response 변수로 받음
                val response = client.newCall(request).execute()

                if(response.isSuccessful == true){ //서버 통신 성공 시,
                    //서버가 보내온 응답 결과 받음 = read_content_idx값
                    val resultText = response.body?.string()!!.trim()
                    act.readContentIdx = Integer.parseInt(resultText)
                    Log.d("test", "${act.readContentIdx}")

                    //화면 관련 작업은 runOnUiThread 처리
activity?.runOnUiThread{
//키보드 숨김 설정 - 글 작성 중이던 키보드 숨기기 처리
                        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(binding.boardWriteSubject.windowToken, 0)
                        inputMethodManager.hideSoftInputFromWindow(binding.boardWriteText.windowToken, 0)

                        //알림
                        val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setTitle("작성 완료")
                        dialogBuilder.setMessage("작성이 완료되었습니다.")
                        dialogBuilder.setPositiveButton("확인"){dialogInterface: DialogInterface, i: Int->
//화면 전환 처리
                            act.fragmentRemoveBackStack("board_write") //현재 프래그먼트는 제거하고
                            act.fragmentController("board_read", true, true) //현재 글의 읽기 프래그먼트로 바로 전환
}
dialogBuilder.show()
}
} else { //서버 통신 실패 시
activity?.runOnUiThread{
val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setTitle("작성 오류")
                        dialogBuilder.setMessage("작성 오류가 발생하였습니다.")
                        dialogBuilder.setPositiveButton("확인", null)
                        dialogBuilder.show()
}
}
}
true
        }
        else -> false
    }
}

```

---

🙋🏻‍♀️ **이제 ‘게시글 읽기 화면’을 제대로 세팅.**

- ‘게시글 작성’ 시 회원이 작성한 데이터를 DB 상에 저장해놓았다.
- ‘게시글 읽기’ 화면 속에 올라갈 데이터는 저장되어있는 content_table 속 데이터들이다.
- 서버에서 해당 데이터를 JSON 형태의 응답 결과로 클라이언트에게 보내주면, 클라이언트는 해당 응답결과를 뷰에 세팅해주는 작업을 하면 되는 것이다.

### **🟧 [서버] get_content.jsp**

- 1) 우선 read_content_idx 값을 받으면, 이 idx값에 일치하는 게시글 내용물 데이터를 content_table에서 SELECT하여 다시 응답결과로 클라이언트에게 보내줄 예정
- 이때, DB 상에 존재하는 데이터를 JSON 형태로 클라이언트에게 보내줄 것이다.

```java
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="org.json.simple.*" %>
<%
	//이 곳에서 DB 상에 저장해놨던 Content 테이블 데이터를 다시 응답결과로 클라이언트에게 보내주는 작업할 것
	
	request.setCharacterEncoding("utf-8");
	
	//1) 우선 클라이언트가 보낸 요청에서 read_content_idx값을 추출
	String str1 = request.getParameter("read_content_idx");
	int readContentIdx = Integer.parseInt(str1);
	
	//DB 접속 정보 세팅
	String dbUrl = "jdbc:mysql://localhost:3306/groupapp_db";
	String dbId = "root";
	String dbPw = "1234";
	
	//드라이버 로딩
	Class.forName("com.mysql.cj.jdbc.Driver");
	
	//DB 실질적 접속
	Connection conn = DriverManager.getConnection(dbUrl, dbId, dbPw);
	
	//sql 문 작성
	String sql = "select a1.content_subject, a2.user_nick_name as content_nick_name, "
			+ "date_format(a1.content_write_date, '%Y-%m-%d') as content_write_date, a1.content_text, a1.content_image "
			+ "from content_table a1, user_table a2 "
			+ "where a1.content_writer_idx = a2.user_idx "
			+ "and content_idx = ?;";
			
	//sql 실행
	PreparedStatement pstmt = conn.prepareStatement(sql);
	pstmt.setInt(1, readContentIdx);
	
	//응답 보낼 데이터 세팅
	ResultSet rs = pstmt.executeQuery();
	rs.next();
	
	//여기서 select 정보는 1행이므로 JSON Object 객체에 담을 예정
	JSONObject obj = new JSONObject();
	
	//DB 상에서 추출한 데이터 임시로 뽑아온 뒤 
	String contentSubject = rs.getString("content_subject");
	String contentNickName = rs.getString("content_nick_name");
	String contentWriteDate = rs.getString("content_write_date");
	String contentText = rs.getString("content_text");
	String contentImage = rs.getString("content_image");
	
	//json object객체에 다시 세팅 
	obj.put("content_subject", contentSubject);
	obj.put("content_nick_name", contentNickName);
	obj.put("content_write_date", contentWriteDate);
	obj.put("content_text", contentText);
	obj.put("content_image", contentImage);
	
	//접속 종료
	conn.close();
%>
<%= obj.toJSONString() %>
```

### **🟧 [DB] 가져올 데이터 SELECT 처리 시, join 작업 수행**

- 1) content_table 속 데이터에는 user_idx값은 있지만 닉네임 이름값이 없다.
- 2) content_table 과 user_table에서 idx값이 일치하는 user_nick_name을 SELECT 처리하기 위해 JOIN 작업을 수행한다.
- 3) 날짜 데이터는 date_format()으로 형식을 맞춰서 가져온다.

```sql
select a1.content_subject, a2.user_nick_name as content_nick_name, 
date_format(a1.content_write_date, '%Y-%m-%d') as content_write_date, a1.content_text, a1.content_image
from content_table a1, user_table a2
where a1.content_writer_idx = a2.user_idx
and content_idx = 1;
```

![DB .png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/f48e4fe3-001f-43df-84f5-1315eaa57359/DB_.png)

### **🟧 [클라이언트] BoardReadFragment.kt**

- 1) 서버로부터 ‘게시글 읽기 화면’ 구성할 데이터를 받아서 세팅해주어야 한다.
- 2) 우선 서버에게 최근 작성한 글 목록인 read_content_idx 값을 formBody 형태로 보내서 Request 요청을 한다.
- 3) 서버가 DB 상에 해당 idx값을 갖는 content 내용물 데이터를 다시 응답결과로 준다.
- 4) 응답 결과를 다시 뷰 바인딩으로 화면 세팅해준다
- 5) 이때, 이미지 뷰의 경우, 받은 이미지 이름값이 존재하는 경우에 한해 다시 네트워크와 통신해서 해당 url 값에 접속하여 이미지 얻어오고, bitmap으로 이미지 객체 생성하여 → 최종 게시글 읽기 화면 속 이미지 뷰에 세팅해주는 방식으로 세팅

```kotlin
//서버로부터 글 내용 데이터 받기
thread{
val client = OkHttpClient()
  val site = "http://${ServerInfo.SERVER_IP}:8080/App_GroupCharge_Server/get_content.jsp"

    //서버로 보낼 데이터 : 최근 작성 글 목록 idx값 <- 액티비티 딴에 저장해놨떤 값 받기
    val act =activityas BoardMainActivity
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

        //게시글 읽기 화면의 뷰 세팅해준다- 받은 데이터들로
activity?.runOnUiThread{
binding.boardReadSubject.text= obj.getString("content_subject")
            binding.boardReadWriter.text= obj.getString("content_nick_name")
            binding.boardReadWriteDate.text= obj.getString("content_write_date")
            binding.boardReadText.text= obj.getString("content_text")

            //이미지 파일명 받음
            val contentImage = obj.getString("content_image")
            if(contentImage == "null") { //얻어온 이미지 없다면
                binding.boardReadImage.visibility= View.GONE//화면 상에도 이미지뷰 안보이도록 처리
            }else { //이미지 있다면 네트워크 통신 처리
thread{
val imageUrl = URL("http://${ServerInfo.SERVER_IP}:8080/App_GroupCharge_Server/upload/${contentImage}")
                    //접속한 url에서 이미지 얻어온 뒤, 이미지 객체 bitmap으로 생성하기
                    val bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream())
activity?.runOnUiThread{
binding.boardReadImage.setImageBitmap(bitmap) //생성한 이미지 객체를 뷰에 세팅
}

                }
}
}

}
}

```

![최종 읽기.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/f2fd77e2-91e1-48f7-9f58-0a3d177d1265/%EC%B5%9C%EC%A2%85_%EC%9D%BD%EA%B8%B0.png)

---

## 🟦 글 목록 데이터 가져오기

### ▶️ 게시글 목록 데이터를 가져오기

- 작성한 글의 목록을 가져오도록 한다 .
- ‘전체 게시판’이 선택된 경우 모든 글을 가져온다.
- ‘특정 게시판’이 선택된 경우 해당 게시판 목록 idx값 기준으로 해당 게시판 목록의 글들만 가져오도록 처리한다.

---

![중간 글목록 결과.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ab220ac0-d53c-40da-aa83-186d217804a5/%EC%A4%91%EA%B0%84_%EA%B8%80%EB%AA%A9%EB%A1%9D_%EA%B2%B0%EA%B3%BC.png)

### **🟧 [DB] 상에서 작업할 쿼리문**

- **게시글 목록 화면에서 각 글 목록 데이터의 구성은 [작성자/날짜/글제목/idx] 값**
- → 다만 작성자의 경우 JOIN(조인)이 필요하다.
- → 마지막으로 최신 등록 순으로 목록 화면에 올라와야 하므로 **역순 정렬** 해준다.

```sql
select a1.content_subject, a2.user_nick_name as content_nick_name, 
date_format(a1.content_write_date, '%Y-%m-%d') as content_write_date, a1.content_idx
from content_table a1, user_table a2
where a1.content_writer_idx = a2.user_idx
	and a1.content_board_idx = 1
order by a1.content_idx desc;
```

### **🟧 [서버] get_content_list.jsp**

- 클라이언트가 보낸 ‘게시글 목록 idx’값을 기준으로 게시글 목록 화면을 구성해야 할 데이터 목록을 SELECT 처리한 뒤 ResultSet으로 최종 응답 결과를 JSON 형태로 보낸다.

```sql
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "java.sql.*" %>
<%@ page import = "org.json.simple.*" %>
<%
	//클라이언트가 전달하는 데이터 한글 깨지지 않도록 설정
	request.setCharacterEncoding("utf-8");
	
	//클라이언트가 전달한 데이터 - 게시판 목록 idx 값 추출
	String str1 = request.getParameter("content_board_idx");
	int content_board_idx = Integer.parseInt(str1);
	
	//DB 접속 정보 세팅
	String dbUrl = "jdbc:mysql://localhost:3306/groupapp_db";
	String dbId = "root";
	String dbPw = "1234";
	
	//드라이버 로딩
	Class.forName("com.mysql.cj.jdbc.Driver");
	
	//DB 실질적 접속
	Connection conn = DriverManager.getConnection(dbUrl, dbId, dbPw);
	
	//sql 문 작성
	String sql = "select a1.content_subject, a2.user_nick_name as content_nick_name, "
				+ "date_format(a1.content_write_date, '%Y-%m-%d') as content_write_date, a1.content_idx "
				+ "from content_table a1, user_table a2 "
				+ "where a1.content_writer_idx = a2.user_idx ";
	
	//'전체 게시판 목록을 선택한 경우 위 sql문에서 끝나고
	
	//특정 게시판 목록을 선택한 경우라면
	if(content_board_idx != 0){ 
		sql += "and a1.content_board_idx = ? "; //sql문 추가 
	}
	sql += "order by a1.content_idx desc;";
	
	//sql 실행
	PreparedStatement pstmt = conn.prepareStatement(sql);
	
	if(content_board_idx != 0) {
		pstmt.setInt(1, content_board_idx);
	}
	//응답 결과 세팅
	ResultSet rs = pstmt.executeQuery();
	
	JSONArray root = new JSONArray();
	
	while(rs.next()) {
		int contentIdx = rs.getInt("content_idx");
		String contentNickName = rs.getString("content_nick_name");
		String contentWriteDate = rs.getString("content_write_date");
		String contentSubject = rs.getString("content_subject");
		
		JSONObject obj = new JSONObject();
		obj.put("content_idx", contentIdx);
		obj.put("content_nick_name", contentNickName);
		obj.put("content_write_date", contentWriteDate);
		obj.put("Content_subject", contentSubject);
		
		root.add(obj); //JSON 배엵객체에 최종 add 처리 
	}
	
	conn.close(); //접속 종료 
%>
<%= root.toJSONString() %>
```

### **🟧 [클라이언트] BoardMainFragment.kt**

- 1) ‘게시글 목록 화면’에서 서버로 현재 ‘게시글 목록 idx’값을 함께 보낸 뒤 Request 요청한다.
- 2) 서버가 보내준 응 답 결과 데이터를 토대로 각 게시글 목록 뷰를 세팅한다.
- **→ [데이터 담을 변수 ArrayList 타입으로 선언]**

```kotlin
//Array 리스트 4개-글 목록 구성할 데이터 리스트
val contentIdxList = ArrayList<Int>()
val contentWriterList = ArrayList<String>()
val contentWriteDateList = ArrayList<String>()
val contentSubjectList = ArrayList<String>()
```

- **각 항목 하나 구성 데이터를 ‘ViewHlder’클래스에서 바인딩 처리하고, 각 항목 터치 시 자동 호출되는 onCLick() 메소드 내부에서 현재 사용자가 ‘읽기’ 선택한 내용물 idx 값을 재새팅 한다.**
- 또한 **getContentList(clear: boolean) 메소드를 선언**하여
    - **1) clear= T** 들어오면 이전 데이터 초기화
    - **2) clear = F** 들어오면 서버와 통신하여 현재 필요한 게시글 목록 상의 데이터들을 가져와서 ArrayList 로컬 변수 4개에 세팅처리 → 이후 Recycler 뷰 어댑터에게 데이터 재세팅을 요청한다.

**◾ 전체 코드**

```kotlin
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
        val act =activityas BoardMainActivity

        //바인딩
        binding = FragmentBoardMainBinding.inflate(inflater)
        binding.boardMainToolbar.title= act.boardNameList[act.selectedBoardType]
                                    //-> 사용자 선택한 게시글 목록 idx 값에 따르 이름값을 title로 세팅하기
        //게시판 항목 메뉴 추가하기
        binding.boardMainToolbar.inflateMenu(R.menu.board_main_menu)
        // 이 항목 메뉴 item 클릭 이벤트 처리
        binding.boardMainToolbar.setOnMenuItemClickListener{
//툴바 속 존재하는 메뉴item 클릭 시. 이벤트 처리
            when(it.itemId){
                R.id.board_main_menu_board_list-> {  //다이얼로그로 게시판 목록을 띄운다.
                    //액티비티에서 받아놨던 데이터 받기 위해 act
                    val act =activityas BoardMainActivity
                    //단 여기서 toTypedArray()로 변경해주어야 한다. Array 객체로 변경
                    val boardListBuilder = AlertDialog.Builder(requireContext())
                    boardListBuilder.setTitle("게시판 목록")
                    boardListBuilder.setNegativeButton("취소", null)

                    //여기서 게시판 목록 '클릭'하는 경우 이벤트 처리
                    boardListBuilder.setItems(act.boardNameList.toTypedArray()){dialogInterface: DialogInterface, i: Int->
act.selectedBoardType = i //사용자 선택한 i에 따라
                        //각 목록에 대한 데이터 처리를 위해서 이전 데이터 clear 처리 -> 각 게시판 목록 idx 값에 따른 데이터를 세팅
                        getContentList(true)

                        binding.boardMainToolbar.title= act.boardNameList[act.selectedBoardType]
}
boardListBuilder.show()
                    true //메뉴 클릭 시 무언가 작업했으므로 T 반환시킴
                }
                R.id.board_main_menu_write-> { //글쓰기 메뉴 클릭 시
                    val act =activityas BoardMainActivity
                    act.fragmentController("board_write", true, true)
                    true
                }
                R.id.board_main_menu_controller-> { //다른 항목 메뉴 컨트롤러 클릭 시,
                    // -> 프래그먼트 이동시킬 건데,
                    val act =activityas BoardMainActivity
                    act.fragmentController("menu_controller", true, true)
                    true
                }

                else -> false
            }
}

//리사이클러뷰 설정
        // (1) 어댑터 객체 생성t
        val boardMainRecyclerAdapter = BoardMainRecyclerAdapter()
        binding.boardMainRecycler.adapter= boardMainRecyclerAdapter
        // (2) 레이아웃 매니저 사용 -> 어댑터로 만든 항목 레이아웃 배치
        binding.boardMainRecycler.layoutManager= LinearLayoutManager(requireContext())
        // (3) 아이템 데코레이션 - 구분선 생성
        binding.boardMainRecycler.addItemDecoration(DividerItemDecoration(requireContext(), 1))

        //항목 속 데이터를 불러오는 함수 (F=불러오고 T=초기화함)
        getContentList(false)

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
            //사용자 클릭한 항목의 position 번째에 있는 List 속 데이터를 뷰 홀더 안에 각각 데이터 삽입처리
            holder.boardMainItemNickname.text= contentWriterList[position]
            holder.boardMainItemSubject.text= contentSubjectList[position]
            holder.boardMainItemWriteDate.text= contentWriteDateList[position]
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
                val act =activityas BoardMainActivity
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
thread{
val client = OkHttpClient()
            val site = "http://${ServerInfo.SERVER_IP}:8080/App_GroupCharge_Server/get_content_list.jsp"

            val act =activityas BoardMainActivity
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

                for(i in 0untilroot.length()) {
                    val obj = root.getJSONObject(i)

                    contentIdxList.add(obj.getInt("content_idx"))
                    contentWriterList.add(obj.getString("content_nick_name"))
                    contentWriteDateList.add(obj.getString("content_write_date"))
                    contentSubjectList.add(obj.getString("content_subject"))
                }
                //화면 구성 전환
activity?.runOnUiThread{
//Recycler 뷰 어댑터에게 Data 세팅 변경 알리고 -> 갱신 처리
                    binding.boardMainRecycler.adapter?.notifyDataSetChanged() //데이터 갱신 명령
}
}
}

}

}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/d1feb19d-6b71-4ed2-a043-792e99fec413/Untitled.png)

---

## 🟦 ‘메뉴’ 컨트롤러 프래그먼트 관련 작업

### ▶️ 메뉴 컨트롤러 프래그먼트 관련 작업 처리

- 1) 각 게시글 목록 아이콘 → 이름값 주기
- 2) 각 게시글 목록 아이콘 클릭 시 → 해당 게시글 목록 idx 값에 따라 화면 전환 처리

---

### **🟧 BoardMenuControlFragment.kt**

- 1) 각각의 아이콘 클릭 시, viewBinding 사용하여 이벤트 처리
- 2) 이 프래그먼트를 관리하는 BoardMainActivity 액티비티의 변수 act.selectedBoardType 값을 사용하여 각 아이콘의 게시글 목록 idx 값을 컨트롤했다.

◾ 코드 

```kotlin
package com.example.appgrouppurchasemaching.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appgrouppurchasemaching.databinding.FragmentMenuControlBinding

class MenuControlFragment : Fragment() { //메뉴 컨트롤할 프래그먼트

    //바인딩
    lateinit var binding: FragmentMenuControlBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //바인딩
        binding = FragmentMenuControlBinding.inflate(inflater)
        //title
        binding.menuControlToolbar.title= "메뉴 카테고리"

        //Back 버튼을 툴바 상단의 navigationIcon으로 추가한다.
        val navIcon = requireContext().getDrawable(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.menuControlToolbar.navigationIcon= navIcon

        //수정) 뒤로가기 네비게이션 클릭 이벤트 처리
        binding.menuControlToolbar.setNavigationOnClickListener{
val act =activityas BoardMainActivity
            act.fragmentRemoveBackStack("menu_controller")
}

//'전체 게시글' 목록 이동
        binding.all.setOnClickListener{
val act =activityas BoardMainActivity
            act.selectedBoardType = 0
            act.fragmentController("board_main", true, true)
}
//'배달 음식' 공구 목록 이동
        binding.delivery.setOnClickListener{
val act =activityas BoardMainActivity
            act.selectedBoardType = 1
            act.fragmentController("board_main", true, true)
}
//'일반 잡화' 공구 목록 이동
        binding.general.setOnClickListener{
val act =activityas BoardMainActivity
            act.selectedBoardType = 2
            act.fragmentController("board_main", true, true)
}
//'의류' 공구 목록 이동
        binding.clothes.setOnClickListener{
val act =activityas BoardMainActivity
            act.selectedBoardType = 3
            act.fragmentController("board_main", true, true)
}
//'회원권 양도' 글 목록 이동
        binding.toss.setOnClickListener{
val act =activityas BoardMainActivity
            act.selectedBoardType = 4
            act.fragmentController("board_main", true, true)
}

return binding.root
}

}
```

### **🟧 최종 모습**

<img width="719" alt="메뉴 컨트롤 최종" src="https://user-images.githubusercontent.com/39732720/185574562-95ca491c-1cc5-44df-ae3f-15ca45f995f1.png">
---

## 🟦 게시글 삭제 처리

### ▶️ 게시글 삭제 처리 기능

- **(1) 해당 글의 ‘작성자’에 한해서 수정, 삭제 메뉴 나타나도록 처리**한다.
- (**2) 글에서 삭제 메뉴 클릭 시 → DB 상의 글 삭제되고 게시글 목록 화면 전환**됨.

---

### 🙋🏻‍♀️**(1) 해당 글의 ‘작성자’에 한해서 수정, 삭제 메뉴 나타나도록 처리**한다.

### **🟧 [서버] get_content.jsp**

- 1) 우선 삭제할 글의 작성자 정보를 확인해야 하므로 DB 상의 게시글 내용물 SELECT 처리했던 get_content.jsp에서 content_writer_idx 값을 추출한다.
- 2) 이 추출한 값을 ‘클라이언트’에게 응답 결과로 보내준다.

```java
<% 
                             (생략) . . .
	//sql 문 작성
	String sql = "select a1.content_subject, a2.user_nick_name as content_nick_name, "
			+ "date_format(a1.content_write_date, '%Y-%m-%d') as content_write_date, a1.content_text, a1.content_image, a1.content_writer_idx "
			+ "from content_table a1, user_table a2 "
			+ "where a1.content_writer_idx = a2.user_idx "
			+ "and content_idx = ?;";
					. . .  (생략) 
			
	//여기서 select 정보는 1행이므로 JSON Object 객체에 담을 예정
	JSONObject obj = new JSONObject();
	
	//DB 상에서 추출한 데이터 임시로 뽑아온 뒤 
	String contentSubject = rs.getString("content_subject");
	String contentNickName = rs.getString("content_nick_name");
	String contentWriteDate = rs.getString("content_write_date");
	String contentText = rs.getString("content_text");
	String contentImage = rs.getString("content_image");
	//***** (작성자 idx)값 추출해서 뽑음
	int contentWriterIdx = rs.getInt("content_writer_idx");
	
	//json object객체에 다시 세팅 
	obj.put("content_subject", contentSubject);
	obj.put("content_nick_name", contentNickName);
	obj.put("content_write_date", contentWriteDate);
	obj.put("content_text", contentText);
	obj.put("content_image", contentImage);
	//***** 추출한 작성자 idx 값을 JSON 객체에 응답 결과로 포함시킴
	obj.put("content_writer_idx", contentWriterIdx);
	
	//접속 종료
	conn.close();
%>
<%= obj.toJSONString() %>
```

### **🟧 BoardReadFragment.kt**

**→ ‘게시글 읽기 화면’ 메뉴 바에서.**

**‘수정, 삭제’ 메뉴의 등장을 (현재 로그인 사용자 = 작성자 사용자 인 경우)에 한해서 이벤트 처리해주어야 하므로.   서버 통신 후 , runOnUiThread{ } 내부에서 모듈 작성**한다.

- 1) 서버로부터 데이터 받았던 부분에서 ‘**작성자 idx’값 추출**
- 2) Preferences에 저장해뒀던 **‘현재 로그인한 사용자 idx값’ 도 함께 추출**
    - **if(작성자 idx값 == 현재 로그인한 사용자 idx값)**
    - **→ 이 글에 대해서는 게시글 수정, 삭제 메뉴 나타나게 화면 구성**
    
    ```kotlin
    //서버로부터 글 내용 데이터 받기
    thread{
    val client = OkHttpClient()
      val site = "http://${ServerInfo.SERVER_IP}:8080/App_GroupCharge_Server/get_content.jsp"
    
    									 . . . 	(생략 ) . . . . . 
    
        //요청에 대한 응답은 response로 받고
        val response = client.newCall(request).execute()
    
        if(response.isSuccessful == true) { //서버 통신 성공 시
            val resultText = response.body?.string()!!.trim()
            val obj = JSONObject(resultText)
    
            **//'작성자' idx 값을 JSON 객체에서 추출하고
            val contentWriterIdx = obj.getInt("content_writer_idx")**
    
    //게시글 읽기 화면의 뷰 세팅해준다- 받은 데이터들로
    activity?.runOnUiThread{
    
    											. . . (생략) . . . 
    
        **->   //Preferences에 저장해뒀던 현재 '로그인' 사용자 idx값 가져옴
                val pref = requireContext().getSharedPreferences("login_data", Context.MODE_PRIVATE)
                val loginUserIdx = pref.getInt("login_user_idx", -1) //두 번째 매개변수는 get한 데이터 값 없을 경우 기본 반환값임
    
                if(loginUserIdx == contentWriterIdx) { //현 로그인 idx == 작성자 idx인 경우에 한해서
                    //'수정' 삭제' 메뉴 구성 - 바인딩 처리
                    binding.boardReadToolbar.inflateMenu(R.menu.board_read_menu)
                    //이벤트 처리
                    binding.boardReadToolbar.setOnMenuItemClickListener{
    when(it.itemId) {
                            R.id.board_read_menu_modify-> { //'수정' 클릭 시
                                val act =activityas BoardMainActivity
                                act.fragmentController("board_modify", true, true)
                                true
                            }
                            R.id.board_read_menu_delete-> { //'삭제' 클릭 시
                                val act =activityas BoardMainActivity
                                act.fragmentRemoveBackStack("board_read") //'우선 뒤로가기 처리''
                                true
                            }
                            else -> false
                        }**
    ```
    

<img width="551" alt="최종1" src="https://user-images.githubusercontent.com/39732720/185773491-f7b6a2e3-4594-4deb-89ea-0f0a52026816.png">
---

### 🙋🏻‍♀️(**2) 글에서 삭제 메뉴 클릭 시 → DB 상의 글 삭제되고 게시글 목록 화면 전환**됨.

### **🟧 [서버] delete_content.jsp**

1) 클라이언트로부터 ‘삭제할 게시글 idx값’을 받음

2) DB에서 삭제할 게시글 idx 값을 갖는 content 데이터 행 DELETE 처리함

```kotlin
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "java.sql.*" %>
<%

	//클라이언트가 전달하는 데이터 한글 깨지지 않도록 설정
	request.setCharacterEncoding("utf-8");

	//클라이언트가 전달한 데이터 = 삭제할 게시글 idx 값 
	String str1 = request.getParameter("content_idx");
	int contentIdx = Integer.parseInt(str1);
	
	//DB 접속 정보 세팅
	String dbUrl = "jdbc:mysql://localhost:3306/groupapp_db";
	String dbId = "root";
	String dbPw = "1234";
	
	//드라이버 로딩
	Class.forName("com.mysql.cj.jdbc.Driver");
	
	//DB 실질적 접속
	Connection conn = DriverManager.getConnection(dbUrl, dbId, dbPw);
	
	//sql 문 작성
	String sql = "delete from content_table where content_idx = ?";
	
	//실질적 sql 실행 
	PreparedStatement pstmt = conn.prepareStatement(sql);
	pstmt.setInt(1, contentIdx);
	
	pstmt.execute();
	
	//DB 접속 종료
	conn.close();
%>
```

### **🟧 [클라이언트] BoardReadFragment.kt**

- **이 화면 속 툴바의 메뉴 클릭 이벤트 처리 구문에서 ‘삭제’ 메뉴 클릭 시 이벤트 처리 수행**
    - 1) thread{ } 가동해서 서버에게 현재 읽고 있는 게시글(삭제할) idx값을 보낸다.
    - 2) 서버에서는 해당 idx값 갖는 DB 의 데이터 삭제 처리함
    - 3) 작업 마치면 ‘게시글 목록 화면’으로 전환시킴
    
    ```kotlin
    R.id.board_read_menu_delete -> { //'삭제' 클릭 시
    
      thread{
          //현재 읽고 있는 게시글 idx 번호가 액티비티에 있으므로
          val act = activity as BoardMainActivity
    
          //-> 서버에게 현재 (삭제누른) 게시글 idx 값을 보냄 -삭제처리
          val client = OkHttpClient()
          val site = "http://${ServerInfo.SERVER_IP}:8080/App_GroupCharge_Server/delete_content.jsp"
          //서버에게 보낼 데이터 세팅
          val builder1 = FormBody.Builder()
          builder1.add("content_idx", "${act.readContentIdx}")
          val formBody = builder1.build()
    
          val request = Request.Builder().url(site).put(formBody).build()
          val response = client.newCall(request).execute()
    
          if(response.isSuccessful == true) {
              activity?.runOnUiThread {  //화면 관련 처리 thread
                  val dialogBuilder = AlertDialog.Builder(requireContext())
                  dialogBuilder.setTitle("글 삭제")
                  dialogBuilder.setMessage("글이 삭제되었습니다.")
                  dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                      //'게시글 목록 화면'으로 화면 전환 처리
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
    ```
    
    ### **🟧 [클라이언트] BoardMainFragment.kt**
    
    - 삭제 처리 후 → 화면 전환이 된 ‘게시글 목록 화면’
    - 이 곳에서도 삭제된 게시글이 목록 상에 나타나지 않도록 해야 함
        - → 따라서 ) OnCreateView() 가장 마지막 블록에 getContentList(true) 값을 주어 매번 새롭게 DB 상에서 데이터 가져와 출력하여 목록 구성하도록 처리한다.
        
        ```kotlin
        //항목 속 데이터를 불러오는 함수 (F=불러오고 T=초기화함)
        getContentList(true) //싹 비우고 매번 이 화면에 오면 새롭게 DB 상에서 데이터 읽어 구성하도록
        ```
<img width="634" alt="최종2" src="https://user-images.githubusercontent.com/39732720/185773492-f555ce90-5c4a-4adc-a856-019a353f0d26.png">
    
    ---

## 🟦 게시글 수정 처리-(1)

### ▶️ 게시글 수정 처리하기

- 현재 읽고 있는 게시글 수정 처리 기능
- **‘수정’ 메뉴 클릭** 시, 게시글 수정 화면으로 전환 + 해당 idx의 작성된 글 내용값도 나타나도록 한다.
- 게시글 수정 화면에서 **카메라로 사진을 찍거나 앨범에서 사진 선택**하면, 해당 사진을 다시 **서버로 전송하여 DB UPDATE 처리**해야 함
- 수정 완료되면 다시 수정된 데이터 담은  게시글 읽기 화면으로 전환된다.

---

### **🟧 [서버] get_content.jsp**

- 우선 사용자가 현재 선택한 게시글의 idx값을 추출해놓아야 한다.
- 따라서 DB 상에 SELECT 해올 데이터 중에 content_board_idx값도 추가하여 추출하고 JSON 객체에도 추가해준다.
- **→ (수정 전) 게시글 내용물 데이터를 ‘게시글 수정 화면’에도 띄워주어야 하기 때문에 필요함**

### **🟧 [클라이언트] BoardModifyFragment.kt**

- 서버 get_content.jsp 에서 보내주는 데이터를 사용하기 위해 네트워크 통신.
- **(1) 스피너 Spinner 구성하기**
    - 사용자가 수정을 원하는 게시글 목록 idx 값이 다를 수 있으므로 DB 상에 존재하는 글 목록을 구성할 스피너를 ‘수정 화면’ 상에도 한 번 더 세팅해준다.
    
    ```kotlin
    //Spinner 구성 - 게시글 수정 시: 카테고리 변경을 할 수도 있으므로 별도의 스피너 구성한다.
    				val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, act.boardNameList.drop(1))
    				spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    				binding.boardModifyType.adapter= spinnerAdapter
    				binding.boardModifyType.setSelection(obj.getInt("content_board_idx") - 1)
    ```
    

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/73517796-d9ac-49db-ac31-5af0dbe5a05c/Untitled.png)

- **(2) 게시글 수정 화면에도 기존 (수정처리 전)  게시글 데이터를 화면 상에 띄워주기**

```kotlin
//서버 통신 작업
thread{
		val client = OkHttpClient()
    val site = "http://${ServerInfo.SERVER_IP}:8080/App_GroupCharge_Server/get_content.jsp"

    //현재 읽고 있는 게시글 idx값을 서버에 보낼 데이터로 세팅
    val builder1 = FormBody.Builder()
    builder1.add("read_content_idx", "${act.readContentIdx}")
    val formBody = builder1.build()

    val request = Request.Builder().url(site).post(formBody).build()

    val response = client.newCall(request).execute()

    if(response.isSuccessful == true){ //'서버 통신' 성공 시,
        val resultText = response.body?.string()!!.trim() //데이터 응답 받아서
        val obj = JSONObject(resultText)

**//게시글 수정 화면에 데이터 처리 = 수정하려고 선택한 게시글의 내용물 데이터를 세팅**
        act.runOnUiThread{
						binding.boardModifySubject.setText(obj.getString("content_subject"))
            binding.boardModifyText.setText(obj.getString("content_text"))
            val contentImage = obj.getString("content_image")

            if(contentImage == "null"){ //DB 데이터 상 이미지 null값인 경우
                binding.boardModifyImage.visibility= View.GONE
																												//이미지 뷰 사라지게 함
            }else { //DB 데이터 상 이미지 존재하면
								thread{
								val imageUrl = URL("http://${ServerInfo.SERVER_IP}:8080/App_GroupCharge_Server/upload/$contentImage")
								val bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream())
								activity?.runOnUiThread{
								binding.boardModifyImage.setImageBitmap(bitmap) //이미지 세팅 처리
			}
   }
}
```

- **(3) Toolbar 위에 존재하는 ‘카메라/갤러리’ 탭 버튼에 대한 이벤트 처리**
    - → ‘게시글 수정’ 시에 사용자가 이미지 변경을 원한다면 그에 대한 처리를 해야 하기 때문.
    - → ‘게시글 작성 화면’에서 카메라/갤러리 탭 메뉴에 처리했떤 입네트 처리를 그대로 구성하되, 이전 이미지 데이터도 구성할 수 있도록 이미지 뷰를 보이도록 처리.

```kotlin
binding.boardModifyImage.visibility = View.VISIBLE //이미지 보이게
```

---

## 🟦 게시글 수정 처리-(1)

### **🟧 [서버] modify_content.jsp**

- **→ (수정 후) 게시글 내용물을 다시 서버에게 보내서 DB 상에 저장시켜야 하기 때문에 필요함**

```kotlin
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import = "com.oreilly.servlet.*" %>
<%@ page import = "com.oreilly.servlet.multipart.*" %>
<%
	//클라이언트가 전달하는 데이터 한글 깨지지 않도록 설정
	request.setCharacterEncoding("utf-8");

	//실제 이미지 업로드할 upload 폴더의 경로 구하기 
	String uploadPath = application.getRealPath("upload");
	
	//중복된 이름에 대한 정책 객체 
	DefaultFileRenamePolicy policy = new DefaultFileRenamePolicy();
	//Request 이미지 담을 multiRequest
	MultipartRequest multi = new MultipartRequest(request, uploadPath, 100*1024*1024, policy);
		
	//클라이언트가 보낸 데이터 추출 
	// 수정할 글 번호 idx값 추출
	String str1 = multi.getParameter("content_idx");
	int contentIdx = Integer.parseInt(str1);

	//수정 이후 처리된 게시글 내용 데이터 차례로 받아 추출
	String contentSubject = multi.getParameter("content_subject"); //글 제목
	String contentText = multi.getParameter("content_text"); //글 내용text
	String contentImage = multi.getFilesystemName("content_image"); //첨부 이미지 
	String str2 = multi.getParameter("content_board_idx"); //게시글 목록 idx
	int contentBoardIdx = Integer.parseInt(str2);

	//DB 접속 정보 세팅
	String dbUrl = "jdbc:mysql://localhost:3306/groupapp_db";
	String dbId = "root";
	String dbPw = "1234";
	
	//드라이버 로딩
	Class.forName("com.mysql.cj.jdbc.Driver");
	
	//DB 실질적 접속
	Connection conn = DriverManager.getConnection(dbUrl, dbId, dbPw);
	
	//이미지 존재 유무에 따르 처리 분기
	if(contentImage == null) {
		String sql = "update content_table "
					+ "set content_subject = ?, content_text = ?, content_board_idx = ? "
					+ "where content_idx = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, contentSubject);
		pstmt.setString(2, contentText);
		pstmt.setInt(3, contentBoardIdx);
		pstmt.setInt(4, contentIdx);
		
		pstmt.execute();
	}else{
		String sql = "update content_table "
				+ "set content_subject = ?, content_text = ?, content_board_idx = ?, content_image = ? "
				+ "where content_idx = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, contentSubject);
		pstmt.setString(2, contentText);
		pstmt.setInt(3, contentBoardIdx);
		pstmt.setString(4, contentImage);
		pstmt.setInt(5, contentIdx);
		
		pstmt.execute();
	}	
	//데이터 베이스 접속 종료
	conn.close();

%>
```

### **🟧 [클라이언트] BoardModifyFragment.kt**

- **1) 사용자가 수정한 (게시글 내용 데이터)를 다시 서버에 올려서 DB 상에 업로드 처리를 해야 한다.**
- 2) ‘게시글 수정 완료’ 후 **사용자가 ‘upload’ 버튼 클릭 시**
- → binding하여 사용자 입력값을 추출 → 데이터에 대한 유효성 검사 처리 → 서버 접속하여 이 데이터값들을 MultipartBody로 묶어서 세팅하고 → 서버에 이 데이터들을 DB에 업로드할 수 있게 POST 요청한다.

```kotlin
        R.id.board_modify_menu_upload-> { //업로드 클릭 시

            //서버에 업로드 처리할 데이터(수정 후 내용물을 바인딩처리로) 추출
            val boardModifySubject = binding.boardModifySubject.text.toString()
            val boardModifyText = binding.boardModifyText.text.toString()
            val boardModifyType = act.boardIndexList[binding.boardModifyType.selectedItemPosition+ 1]

            //유효성 검사 하기
            // 게시글 제목 유효성 검사
            if(boardModifySubject == null || boardModifySubject.length == 0){
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setTitle("제목 입력 오류")
                dialogBuilder.setMessage("제목을 입력해주세요")
                dialogBuilder.setPositiveButton("확인"){dialogInterface: DialogInterface, i: Int->
binding.boardModifySubject.requestFocus() //입력칸에 재포커싱
}
dialogBuilder.show()
                return@setOnMenuItemClickListener true
            }
            // 게시글 내용 text 유효성 검사
            if(boardModifyText == null || boardModifyText.length == 0) {
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setTitle("내용 입력 오류")
                dialogBuilder.setMessage("내용을 입력해주세요")
                dialogBuilder.setPositiveButton("확인"){dialogInterface: DialogInterface, i: Int->
binding.boardModifyText.requestFocus() //입력칸에 재포커싱
}
dialogBuilder.show()
                return@setOnMenuItemClickListener true
            }

            //'서버 접속'
thread{
val client = OkHttpClient()
                val site =
                    "http://${ServerInfo.SERVER_IP}:8080/App_GroupCharge_Server/modify_content.jsp"

                //첨부이미지 존재할 수 있으므로 FormBody대신 MultipartBody로 사용
                //서버에 보낼 작업 데이터 세팅
                val builder1 = MultipartBody.Builder()
                builder1.setType(MultipartBody.FORM)
                builder1.addFormDataPart("content_idx", "${act.readContentIdx}")
                builder1.addFormDataPart("content_subject", boardModifySubject)
                builder1.addFormDataPart("content_text", boardModifyText)
                builder1.addFormDataPart("content_board_idx", "$boardModifyType")

                //이미지 데이터 세팅
                var file: File? = null
                if (uploadImage != null) { //이미지 null값 아니라면
                    val filePath = requireContext().getExternalFilesDir(null).toString()
                    val fileName = "/temp_${System.currentTimeMillis()}.jpg"
                    val picPath = "$filePath/$fileName"
                    file = File(picPath)
                    val fos = FileOutputStream(file)
                    uploadImage?.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    //서버에 보낼 데이터에 마저 세팅
                    builder1.addFormDataPart(
                        "content_image",
                        file.name,
                        file.asRequestBody(MultipartBody.FORM)
                    )
                }
                //서버에 요청
                val formBody = builder1.build()
                val request = Request.Builder().url(site).post(formBody).build()
                val response = client.newCall(request).execute()

                if (response.isSuccessful == true) { //통신 성공 시
activity?.runOnUiThread{
val inputMethodManager =
                            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(
                            binding.boardModifySubject.windowToken,
                            0
                        )
                        inputMethodManager.hideSoftInputFromWindow(
                            binding.boardModifyText.windowToken,
                            0
                        )

                        val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setTitle("수정완료")
                        dialogBuilder.setMessage("수정이 완료되었습니다.")
                        dialogBuilder.setPositiveButton("확인"){dialogInterface: DialogInterface, i: Int->
act.fragmentRemoveBackStack("board_modify") //우선 현재 프래그먼트 종료시키기
}
dialogBuilder.show()
}
} else { //통신 실패한 경우
activity?.runOnUiThread{
val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setTitle("수정오류")
                        dialogBuilder.setMessage("수정 오류가 발생하였습니다.")
                        dialogBuilder.setPositiveButton("확인", null)
                        dialogBuilder.show()
}
}
}
true
        }
        else -> false
    }
}
```

### **🟧 수정 처리 전**

![수정전.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/53a2ad0b-50f8-49ca-8cbb-554ffd2d2661/%EC%88%98%EC%A0%95%EC%A0%84.png)

### **🟧 수정 처리 후**

![수정 후 .png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/d9c8fd17-1065-4037-99b6-89cf9d72984e/%EC%88%98%EC%A0%95_%ED%9B%84_.png)

### **🟧 게시글 목록 화면의 수정 전 후 상태 비교**

![수정전후 모음.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ed62445c-f125-488f-88fb-49c4359eebad/%EC%88%98%EC%A0%95%EC%A0%84%ED%9B%84_%EB%AA%A8%EC%9D%8C.png)

---

## 🟦 새로 고침 기능 구현하기

### ▶️ 새로 고침 기능 구현

- ‘**게시글 목록 화면’**에서 **사용자가 아래로 당기기를 할 경우, 목록이 새로고침 되도록 처리.**
    
    ### 📍**Swiperefreshlayout**
    
- → ‘**구글이 제공하는 새로 고침용 레이아웃’**
- → **라이브러리 추가하여 사용**한다.

```kotlin
implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
```

---

### **🟧 1) Xml 레이아웃 코드에 SwipeRefreshLayout 추가**

**◾ fragment_board_main.xml**

- 구글 제공 라이브러리에서 새로고침 관련 레이아웃으로 목록 구성하던 RecyclerView를 감싸준다.

```xml

<androidx.swiperefreshlayout.widget.SwipeRefreshLayout
    android:id="@+id/board_main_swipe"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/board_main_recycler"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</androidx.swiperefreshlayout.widget.SwipeRefreshLayout>

```

### **🟧 2) 새로고침 이벤트 처리**

- **BoardMainFragment.kt 에서** 조금 전 추가했던 **SwipeRefreshLayout의 id를 binding하여 이벤트 처리 수행**

```kotlin

//새로고침 기능 이벤트 처리 -> swiper
binding.boardMainSwipe.setOnRefreshListener{
getContentList(true) //다시 데이터 한 번 더 새롭게 가져오고
    binding.boardMainSwipe.isRefreshing= false //계속 스와이프 돌아가는 것 없애줌
}
```

### **🟧 3) 새로운 에뮬레이터 실행해서 비교하기**

[video1767686573 (online-video-cutter.com).mp4](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/a463f206-c6ba-4a7b-8d2f-dda95df4994c/video1767686573_(online-video-cutter.com).mp4)

---

## 🟦 무한 스크롤 기능 구현

### ▶️ 무한 스크롤 기능 구현하기

- **서버가 모든 글들의 글 목록 데이터를 클라이언트에게 전달해 줄 경우 너무 많은 데이터가 전달 될 수도 있다.**
- 따라서, **현재 화면 구성에 필요한 데이터만 가져온 후 RcyclerView를 구성**
- **그 이후 사용자가 아래롤 스크롤을 해서 마지막 까지 도착했을 경우 → 추가 데이터를 가져와 아래쪽에도 구성**
- 그렇게 되면 **계속 스크롤을 해서 가장 마지막 글 목록까지 나올 수 있으므로 이 기능 자체를 ‘무한 스크롤’이라고 명명**한다.

---

### **🟧 [서버] get_content_list.jsp**

- 게시글 목록 화면을 구성해주는 작업을 하므로 이 곳에서 처리

```kotlin
//  -> 각 페이지별로 목록 구성할 것. page_num도 받을 것이디ㅏ.
	String str2 = request.getParameter("page_num");
	int page_num = Integer.parseInt(str2);
	
	int startIndex = (page_num -1) * 10; //각 목록 시작 idx 값 구함

// sql 문에 limit 두어 데이터 가져오기 
sql += "order by a1.content_idx desc limit ?, 10;";
                             // ?값에 시작 목록 번호 ~ 10개씩 데이터 가져옴
```

### **🟧 [클라이언트]**

**1) BoardMainActivity.kt 에서 각 화면에 구성할 목록 데이터 10개 당 한 page로 설정할 용도로 nowPage 변수 선언** 

**2) BoardMainFragment.kt**

- (1) 액티비티의 **변수 nowPage를 사용**
- (2) RecyclerView에서 **사용자가 ‘스크롤’ 시 이벤트 처리**

```kotlin
    //리사이클러뷰에서 사용자의 스크롤 시 이벤트 처리
binding.boardMainRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener(){
        // 스크롤이 끝나면 자동 호출되는 함수 재정의
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

            //현재 화면에 보이는 항목 중 가장 마지막 화면의 idx 값 가져오기
            val index1 = (recyclerView.layoutManageras LinearLayoutManager).findLastVisibleItemPosition()

            //리사이클러 뷰가 관리하는 항목의 총 개수
            val count1 = recyclerView.adapter?.itemCount

if(index1 + 1 == count1) {
                act.nowPage = act.nowPage + 1
                getContentList(false) //계속 뒤에 이어붙여서 데이터 가져와야 하므로
            }
    }
})
```

---

---

# 🙋🏻‍♀️ [지도] 연동하기 !

## 🟦 앱에 LBS 지도 서비스 연동하기

### ▶️ LBS : Location Based Service

- **위치 기반의 서비스를 제공하는 Service 약자**
- **현재 위치를 기준으로 사용자 위치를 ‘지속적으로’ 읽어와서 화면 상에 표시하고, 구글 open api를 활용하여 주변 정보를 표시**한다.

---

### ▶️ **구글 지도 사용하기**

- **애플리케이션에서 구글 지도를 표시하는 작업** 수행
- 구글 지도을 사용하기 위해서는 애플리케이션 등록부터 시작해서 많은 작업을 수행해야 한다.

---

### **🟧 1) 애플리케이션 등록**

- **구글 지도 사용을 위해서는 구글 개발자 콘솔에 애플리케이션 등록 후 키 값을 발급 받아야 한다.**
    
    **(1) 프로젝트 생성**
    
    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/6db257fe-9a55-46fd-8379-88e65c64fc2b/Untitled.png)
    
    **(2) API 서버스 사용 설정** 
    
    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/183f0963-d333-4ddd-b3ce-113529fb15c1/Untitled.png)
    
    **(3) Maps SDK for Android 사용 선택**
    
    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c246ca08-a8c3-4ce7-89cc-eeb52e7da9cb/Untitled.png)
    
    **(4) 애플리케이션 등록** 
    
    - API Key 발급을 위해 인증 정보를 등록 한다.
    - 좌측 상단 부분을 클릭한다.
    
    **(5) 사용자 인증 정보 만들기** 
    
    - API 키 발급 받고, 키에 대한 제한 설정
    - 항목 추가 → 앱 패키지명/SHA-1 인증서 디지털 지문을 입력
        - → cmd 창에 안드로이드 설치 경로로 이동해서 명령어 수행한 뒤 인증서 지문 문자열 복사
    - 설정 완료 후 , 발급받은  API 키 값을 따로 저장시켜놓는다.
    
    **(6) AndroidManifest.xml에 다음과 같이 API 키 등록** 시킴
    
    ```xml
    <meta-data
        android:name="com.google.android.geo.API_KEY"
        android:value="AIzaSyCWxie_s84vOaG1VMT5dRgndafpXe1Ntw8"/>
    ```
    
    **(7) Google Play Service 패키지 설치**
    
    - Tools > SDK Manager에서 설치한다.
    
    **(8) App 수준의 build.gradle 에 다음 라이브러리를 추가**한다. 
    
    ```xml
    implementation 'com.google.android.gms:play-services-maps:18.1.0'
    implementationDependenciesMetadata 'com.google.android.gms:play-services-location:20.0.0'
    ```
    

---

### **🟧 ServiceActivity.kt**

- **구글 맵을 띄울 액티비티 생성하고, 이 액티비티에서 구글 지도 화면을 띄울 예정이기 때문에 layout → xml 파일에 Map 프래그먼트 규격을 기본값으로 추가**한다.

```xml
<fragment
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/map_fragment"
    android:name="com.google.android.gms.maps.SupportMapFragment"/>

```

### **🟧 MenuControlFragment.kt**

- 이 곳에서 **‘지도’ 클릭 시 → 위의 ServiceActivity로 화면 전환 처리**를 하도록 이벤트 처리
    
    ```kotlin
    //'지도' 클릭 시 지도 액티비티로 화면 전환 처리
    binding.map.setOnClickListener{
    //화면 전환 처리
        val Intent = Intent(requireContext(), ServiceActivity::class.java)
        startActivity(Intent)
    }
    ```
    

### **🟧 최종 모습**

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ea61380f-ce9b-4cc4-910b-3fc5fa4e1fb4/Untitled.png)

---

## 🟦 현재 위치 표시하기

### ▶️ 사용자 현재 위치 측정하여 화면 표시

- 지도 화면이 나타날 떄, 사용자의 현재 위치를 측정하여 해당 위치로 지도를 이동시킨다.

---

### **🟧 1) AndroidManifest.xm에 필요한 권한 등록**

```kotlin
<uses-permission android:name="android.permission.ACCESS_MEDIA_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
```

### **🟧 2) ServiceActivity에서 위 권한을 사용자에게 요청하기**

- 이 액티비티 실행 시 사용자에게 허용 궈한을 입력받기 위해 onCreate() 메소드 내부에서 권한을 요청한다.

```kotlin
//허용받을 권한 목록
    val permission_list = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
```

```kotlin
//권한 요청하기 사용자에게
requestPermissions(permission_list, 0)
```

---

### 🟦 **[현재 위치 측정하기]**

### **🟧 1)  OnMapReadyCallback 인터페이스 구현**

- 우선 지도 Fragment를 관리하는 Activity 클래스는 지도 관련 자동 호출 함수 onMapReady()메소드를 갖는 **OnMapReadyCallback 인터페이스를 구현**

### **🟧 2) OnMapReady() 메소드 재정의 구현**

### **🟧 3) onCreate() 내부에 앱 사태 변경되면 호출될 메소드로 OnMapReady()를 등록한다.**

```
위치 정보를 얻어오는 함수는 getLastKnownLocation( )인데 이 함수는 필요한 순간 한 번만 이용할 수 있습니다. 그러나 때로는 일정 시간 동안 반복해서 위치 정보를 얻어와야 할 때도 있습니다. 이를 위해 LocationListener를 제공합니다.
```

# **[에러 onStatusChanged() 관련 ]**

```
onStatusChanged (String provider, int status, Bundle extras) 함수는 위치 정보 제공자의 상태 변경 시 호출되며 상태 정보 값으로 OUT_OF_SERVICE, TEMPORARILY_ UNAVAILABLE, AVAILABLE의 상수 변수가 전달됩니다. onLocationChanged ( ) 함수가 위치 정보를 전달하기 위해 자동으로 호출되는 함수입니다. 이렇게 정의한 LocationListener를 LocationManager에 등록하여 위치 값을 지속해서 얻을 수 있습니다.
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/4e7c3e22-819f-4a4e-a520-488d87a09d64/Untitled.png)

### **🟧 ServiceActivity.kt**

- 툴바에 메뉴 xml 등록하여 이벤트 처리 시 → 아이콘 클릭하면 현재 위치정보 가져와서 화면에 띄우고 마커로 지정됨

```kotlin
package com.example.appgrouppurchasemaching

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.appgrouppurchasemaching.databinding.ActivityServiceBinding
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

class ServiceActivity : AppCompatActivity() , OnMapReadyCallback { //서비스 제공 액티비티
    //binding 설정
    lateinit var binding : ActivityServiceBinding

    // 허용받을 권한 목록
    val permission_list =arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
)

    lateinit var manager : LocationManager
    lateinit var locationListener : LocationListener
    lateinit var googleMap : GoogleMap
    var myMarker : Marker? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding 처리
        binding = ActivityServiceBinding.inflate(layoutInflater)
        binding.mapToolbar.title= "Google Map 현재위치"
        //툴바에 메뉴 추가
        binding.mapToolbar.inflateMenu(R.menu.map_menu)

        binding.mapToolbar.setOnMenuItemClickListener{
when(it.itemId){
                R.id.main_menu_location->{
                    getMyLocation()
                    true
                }
                else -> false
            }
}

setContentView(binding.root)

        //권한 요청하기 사용자에게
        requestPermissions(permission_list, 0)

        // 맵의 상태가 변경되면 호출될 메서드가 구현되어 있는 곳을 등록한다.
        val mapFragment =supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    // 지도가 준비가 완료되면 호출되는 메서드
    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        getMyLocation()
    }

    // 현재 위치를 측정하는 메서드
    fun getMyLocation(){
        // 위치 정보를 관리하는 매니저를 추출한다.
        manager = getSystemService(LOCATION_SERVICE) as LocationManager

        // 저장되어 있는 위치값이 있으면 가져온다.
        val a1 = ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
val a2 = ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED

if (a1 && a2) {
            return
        }
        //getLastKnownLocation()
        val location1 = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val location2 = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        // 새로운 위치 측정을 요청
        locationListener =LocationListener{
setUserLocation(it)
}

if(location1 != null){
            setUserLocation(location1)
        } else if(location2 != null){
            setUserLocation(location2)
        }

        if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true){
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                0f, locationListener)
        } else if(manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true){
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                0f, locationListener)
        }
    }

    // 위치 값을 받아 지도를 이동시킨다.
    fun setUserLocation(location:Location){
        // 위치 측정을 중단한다.
        manager.removeUpdates(locationListener)

        // 위도와 경도값을 관리하는 객체
        val loc1 = LatLng(location.latitude, location.longitude)
        // 지도를 이동시키기 위한 객체를 생성한다.
        val loc2 = CameraUpdateFactory.newLatLngZoom(loc1, 15f)
        // 이동한다.
        googleMap.animateCamera(loc2)

        // marker가 표시가 되어 있다면..
        if(myMarker != null){
            myMarker?.remove()
        }

        // 현재 위치에 마커를 표시한다.
        val myMarkerOptions = MarkerOptions()
        myMarkerOptions.position(loc1)

        myMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.my_location))

        myMarker = googleMap.addMarker(myMarkerOptions)
    }

}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/7c209bfa-035c-4d8d-a08a-d53d13e52e56/Untitled.png)

---

## 🟦 구글 지도 옵션 설정하기

### ▶️ 구글 지도 옵션 설정

- 화면에 표시한 구글 지도에 다양한 옵션을 설정해 기능을 활성화 할 수 있다.

### **🟧 [사전 설정] 권한 확인**

- **일부 옵션은 ACCESS_FINE_LOCATION과 ACCESS_COARSE_LOCATION 권한 확인이 필요**하다.
- 따라서 **해당 권한 확인 코드를 넣어준다.**
- 이 모든 옵션들은 **모두 지도 준비 완료되면 자동 호출되는 재정의 메소드 onMapReady() 내부에서 작성**한다.

```kotlin
// 지도가 준비가 완료되면 호출되는 메서드
    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0

        //구글 지도의 일부 옵션 설정을 위해 권환 확인 코드 추가한다.
        val a1 = Manifest.permission.ACCESS_FINE_LOCATION
        val a2 = Manifest.permission.ACCESS_COARSE_LOCATION

        if(ActivityCompat.checkSelfPermission(this, a1) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, a2) == PackageManager.PERMISSION_GRANTED){
        }
        
        getMyLocation() //현재 위치 측정 메소드 호출 
    }
```

### **🟧 옵션 1) 지도 화면 상에 ‘확대/축소’ 버튼 추가**

```kotlin
if(ActivityCompat.checkSelfPermission(this, a1) == PackageManager.PERMISSION_GRANTED
&& ActivityCompat.checkSelfPermission(this, a2) == PackageManager.PERMISSION_GRANTED){

    //확대 축소 버튼
    googleMap.uiSettings.isZoomControlsEnabled= true
}
```

### **🟧 옵션 2) 현재 위치 표시하기**

- 현재 위치 표시 기능을 제공하는 옵션
- 마커 표시하면 중복되어 나타나므로 둘 중 하나만 사용한다.
- 이때, 지도 상에 현재 위치로 이동할 수 있는 버튼이 나타난다.

```kotlin
//구글 제공 - 현재 위치 표시
googleMap.isMyLocationEnabled= true

//현재 위치 표시 마커 중복 시, 제거 O
googleMap.uiSettings.isMyLocationButtonEnabled = false
```

### **🟧 옵션 3) 구글 지도 타입 설정**

- 구글 지도 타입을 설정할 수 있다.
- 기본적으로 **MAP_TYPE_NORMAL로 설정**되어 있다.

```kotlin
googleMap.mapType = GoogleMap.MAP_TYPE_NONE
googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL (기본값)
googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
```

---
## 🟦 위치 측정 서비스 구현

### 📌 이 서비스 사용하지 않아도 되는 경우

- 1) 사용자의 현재 위치를 **‘일시적으로 측정’**해서 사용하는 경우
    - cf. ***지속적으로 사용자의 위치를 측정해야 할 경우에는 위치 측정 서비스가 필요***하다.
    
    2) **서비스로 운영하지 않았을 때, 애프리케이션이 일시 정지하면 위치 측정이 중단**된다. 
    
    3) 따라서 ‘**서비스’로 운영하는 방법**을 살펴볼 것이다. 
    
    - Activity에서도 현재 위치를 사용해야 하므로 IPC를 활용한다.
    
    ---
    

### **🟧 1) AndroidManifest.xml에 권한 추가하기**

- **포그라운드 서비스의 사용을 위해 권한 추가**

```kotlin
<uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>
```

### **🟧 MyLocationService.kt**

- 별도의 서비스 생성하여 **백그라운드(액티비티 종료해도)에서도 지속적으로 위치 측정 되도록 설정.**

```kotlin
package com.example.appgrouppurchasemaching.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat

class MyLocationService : Service() {

    lateinit var manager : LocationManager
    lateinit var locationListener: LocationListener

    override fun onBind(intent: Intent): IBinder {
TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel("myLocationService", "myLocationService",
                                                NotificationManager.IMPORTANCE_HIGH)
            //알림 띄우기
            manager.createNotificationChannel(channel)
            val builder = NotificationCompat.Builder(this, "myLocationService")
            builder.setSmallIcon(android.R.drawable.ic_menu_mylocation)
            builder.setContentTitle("현재 위치 측정")
            builder.setContentText("현재 위치를 측정 중 입니다.")
            val notifiaction = builder.build()
            startForeground(10, notifiaction)
        }

        //위치 측정
        manager = getSystemService(LOCATION_SERVICE) as LocationManager

        val a1 = Manifest.permission.ACCESS_FINE_LOCATION
val a2 = Manifest.permission.ACCESS_COARSE_LOCATION

if(ActivityCompat.checkSelfPermission(this, a1) == PackageManager.PERMISSION_DENIED
|| ActivityCompat.checkSelfPermission(this, a2) == PackageManager.PERMISSION_DENIED) {

            return super.onStartCommand(intent, flags, startId)
        }

        val location1 = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val location2 = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        locationListener =LocationListener{
getUserLocation(it) // 사용자 위치 정보 가져옴
}

if(location1 != null) {
            getUserLocation(location1)
        }else if(location2 != null) {
            getUserLocation(location2)
        }

        if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
        }
        if(manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true) {
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)
        }

        return super.onStartCommand(intent, flags, startId)
    }

    //사용자 위치 추적 메소드
    fun getUserLocation(location : Location){
        Log.d("test", "${location.longitude}, ${location.latitude}")
    }

    //이 서비스 중단될 경우 자동 호출
    override fun onDestroy() {
        super.onDestroy()
        manager.removeUpdates(locationListener)
    }
}
```

## 🟦 구글 지도에 적용하기

### ▶️ 구글 지도에 적용하기

- ACtivity가 Service에서 측정한 위치 정보를 받아와 지도에 적용하는 작업 수행

---

### **📌 IPC [Inter-Process Communication]**

- 프로세스 사이의 데이터 통신은 IPC를 매개로 한다.
- 프로세스 간 통신

---

### **🟧 ServiceActivity.kt**

- 이 곳에서 **MyLocationService 접속 정보 세팅해서 해당 서비스에 접속**한다.

```kotlin
package com.example.appgrouppurchasemaching.service

import android.Manifest
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.SystemClock
import androidx.core.app.ActivityCompat
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.databinding.ActivityServiceBinding
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlin.concurrent.thread

class ServiceActivity : AppCompatActivity() , OnMapReadyCallback { //서비스 제공 액티비티
    //binding 설정
    lateinit var binding: ActivityServiceBinding

    lateinit var manager: LocationManager
    lateinit var locationListener: LocationListener
    lateinit var googleMap: GoogleMap
    var myMarker: Marker? = null

    //서비스 intent 변수
    lateinit var serviceIntent: Intent

    //IPC 사용
    var ipcService: MyLocationService? = null
    var serviceRunning = false //현재 서비스 실행 중 여부 변수
    var myLocation : Location? = null

    //서비스 접속 관리 객체
    val connection = object : ServiceConnection {
        //서비스 접속 시
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            //사용할 서비스 추출한다.
            val binder = service as MyLocationService.MyLocationServiceBinder
            ipcService = binder.getService() //변수에 담아주고
        }

        //서비스 접속 해제 시
        override fun onServiceDisconnected(name: ComponentName?) {
            ipcService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding 처리
        binding = ActivityServiceBinding.inflate(layoutInflater)
        binding.mapToolbar.title= "Google Map 현재 위치 확인"

        setContentView(binding.root)

        // 맵의 상태가 변경되면 호출될 메서드가 구현되어 있는 곳을 등록한다.
        val mapFragment =
supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //서비스 가동 중 아닌 경우를 확인한 뒤 ->  서비스 가동시킨다.
        val chk = isServiceRunning("com.example.appgrouppurchasemaching.MyLocationService")
        serviceIntent = Intent(this, MyLocationService::class.java)

        if (chk == false) { //접속 X
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent) //백그라운드에서 실행
            } else { //접속 O
                startService(serviceIntent) //서비스가 실행
            }
        }

        //서비스에 접속한다.
        bindService(serviceIntent, connection,BIND_AUTO_CREATE)

    }

    // 지도가 준비가 완료되면 호출되는 메서드
    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0

        // 구글 지도의 옵션 설정을 위해 권한을 확인한다.
        val a1 = Manifest.permission.ACCESS_FINE_LOCATION
val a2 = Manifest.permission.ACCESS_COARSE_LOCATION

if (ActivityCompat.checkSelfPermission(this, a1) == PackageManager.PERMISSION_GRANTED
&& ActivityCompat.checkSelfPermission(this, a2) == PackageManager.PERMISSION_GRANTED
) {

            // 확대 축소 버튼
            googleMap.uiSettings.isZoomControlsEnabled= true

            // 현재 위치를 표시한다.
            googleMap.isMyLocationEnabled= true

        }

        //서비스에서 현 위치값을 가져오는 쓰레드 가동시키기
        serviceRunning = true

thread{
while (serviceRunning) {
                SystemClock.sleep(1000) //1초마다
                myLocation = ipcService?.returnUserLocation()

                runOnUiThread{
if (myLocation != null) {
                        setUserLocation(myLocation!!, true)
                        serviceRunning = false
                    }
						}
			}
		}
	}
    fun setUserLocation(location:Location, zoom : Boolean){

        // 위도와 경도값을 관리하는 객체
        val loc1 = LatLng(location.latitude, location.longitude)
        if(zoom == true) {
            // 지도를 이동시키기 위한 객체를 생성한다.
            val loc2 = CameraUpdateFactory.newLatLngZoom(loc1, 15f)
            // 이동한다.
            googleMap.animateCamera(loc2)
        } else {
            val loc2 = CameraUpdateFactory.newLatLng(loc1)
            googleMap.animateCamera(loc2)
        }
    }

    //서비스 가동 여부 확인 메소드
    fun isServiceRunning(name : String) : Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        //현재 실행 중인 서비스를 가져온다.
        val serviceList = manager.getRunningServices(Int.MAX_VALUE)

        for(serviceInfo in serviceList) {
            //서비스의 이름이 원하는 이름인지 확인
            if(serviceInfo.service.className== name) {
                return true
            }
       }
        return false
    }

    //액티비티 종료 시 서비스 종료시킴
    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection) //서비스 접속 해제시킴
        stopService(serviceIntent)
    }
}
```

---
