package com.example.appgrouppurchasemaching.board

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.ServerInfo
import com.example.appgrouppurchasemaching.board.AttachmentSizeUtil.Companion.calculateSampleSize
import com.example.appgrouppurchasemaching.board.AttachmentSizeUtil.Companion.decodeSampledBitmapFromResource
import com.example.appgrouppurchasemaching.board.AttachmentSizeUtil.Companion.maxHeight
import com.example.appgrouppurchasemaching.board.AttachmentSizeUtil.Companion.maxWidth
import com.example.appgrouppurchasemaching.databinding.FragmentBoardWriteBinding
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import kotlin.concurrent.thread

class BoardWriteFragment : Fragment() {//글쓰기 프래그먼트 화면

    //바인딩
    lateinit var binding : FragmentBoardWriteBinding

    //경로 담을 Uri 객체
    lateinit var contentUri : Uri

    //이미지 파일 데이터를 위한 변수
    var uploadImage : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        //BoardMainActivity 에서 서버로부터 받아온 '게시글 목록'데이터 받기 위함
        val act = activity as BoardMainActivity

        //바인딩
        binding = FragmentBoardWriteBinding.inflate(inflater)
        binding.boardWriteToolbar.title = "공구 매칭 쓰기"

        //Back 버튼을 툴바 상단의 navigationIcon으로 추가한다.
        val navIcon = requireContext().getDrawable(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.boardWriteToolbar.navigationIcon = navIcon

        //뒤로가기 네비게이션 클릭 이벤트 처리
        binding.boardWriteToolbar.setNavigationOnClickListener {
            val act = activity as BoardMainActivity
            //현재의 프래그먼트를 백스택에서 pop 제거 처리
            act.fragmentRemoveBackStack("board_write")
        }

        //툴바 속 메뉴 세팅
        binding.boardWriteToolbar.inflateMenu(R.menu.board_write_menu)
        binding.boardWriteToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.board_write_menu_camera -> { //카메라 클릭 시
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
                R.id.board_write_menu_gallery -> { //앨범 클릭 시
                    val albumIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    albumIntent.type = "image/*"

                    val mimeType = arrayOf("image/*")
                    albumIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
                    startActivityForResult(albumIntent, 2)
                    true
                }
                R.id.board_write_menu_upload -> { //작성완료 클릭 시
                    val act = activity as BoardMainActivity

                    //뷰 바인딩으로 뷰 속에 사용자 입력한 내용 가져오기
                    //글 제목, 내용 데이터
                    val boardWriteSubject = binding.boardWriteSubject.text.toString()
                    val boardWriteText = binding.boardWriteText.text.toString()
                    //액티비티 단위로 호환되는 게시글 목록 idx 가져옴
                    val boardWriteType = act.boardIndexList[binding.boardWriteType.selectedItemPosition + 1]

                    Log.d("test", "boardWriteSubject: ${boardWriteSubject}")
                    Log.d("test", "boardWriteText: ${boardWriteText}")
                    Log.d("test", "boardWriteType.toString(): ${boardWriteType.toString()}")


                    //앱 단위로 호환되는 Preferences에 저장된 로그인 idx 가져옴
                    val pref = requireContext().getSharedPreferences("login_data", Context.MODE_PRIVATE)
                    val boardWriterIdx = pref.getInt("login_user_idx", 0)

                    // 1) 글 제목 부분 작성 X -> 유효성 검사
                    if(boardWriteSubject == null || boardWriteSubject.length == 0 ){
                        val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setTitle("제목 입력 오류")
                        dialogBuilder.setMessage("제목을 입력해주세요. ")
                        dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
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
                        dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                            binding.boardWriteText.requestFocus() //글 제목 작성 칸 재포커싱
                        }
                        dialogBuilder.show()
                        return@setOnMenuItemClickListener true
                    }

                    //-> 서버 통신 작업 처리
                    thread {
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

                        Log.d("test", "builder1.toString(): ${builder1.toString()}")

                        var file : File? = null
                        //사용자가 선택한 이미지 파일 존재하는 경우에 한해서
                        if(uploadImage != null) {
                            Log.d("test", "uploadImage.width(${uploadImage?.width}), uploadImage.height(${uploadImage?.height}), uploadImage.allocationByteCount(${uploadImage?.allocationByteCount})")

                            val filePath = requireContext().getExternalFilesDir(null).toString()
                            val fileName = "/temp_${System.currentTimeMillis()}.jpg"
                            val picPath = "$filePath/$fileName"
                            Log.d("test", "picPath: $picPath")

                            file = File(picPath)
                            val fos = FileOutputStream(file)
                            uploadImage?.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                            //파일 읽어서 '서버로 보낼 데이터'에 포함 시켜준다.
                            builder1.addFormDataPart(
                                "content_image",
                                file.name,
                                file.asRequestBody(MultipartBody.FORM))
                        }

                        val formBody = builder1.build() //생성
                        /* formBody logging
                        var buf = Buffer()
                        formBody.writeTo(buf)
                        Log.d("test", "buf read: ${buf.readUtf8()}")
                         */
                        //요청Request
                        val request = Request.Builder().url(site).post(formBody).build()
                        //요청 반환값은 response 변수로 받음
                        val response = client.newCall(request).execute()
                        Log.d("test", "request call execute")

                        Log.d("test", "response.isSuccessful: ${response.isSuccessful}")
                        if(response.isSuccessful == true){ //서버 통신 성공 시,
                            //서버가 보내온 응답 결과 받음 = read_content_idx값
                            val resultText = response.body?.string()!!.trim()
                            act.readContentIdx = Integer.parseInt(resultText)
                            //Log.d("test", "${act.readContentIdx}")

                            //화면 관련 작업은 runOnUiThread 처리
                            activity?.runOnUiThread {
                                //키보드 숨김 설정 - 글 작성 중이던 키보드 숨기기 처리
                                val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                inputMethodManager.hideSoftInputFromWindow(binding.boardWriteSubject.windowToken, 0)
                                inputMethodManager.hideSoftInputFromWindow(binding.boardWriteText.windowToken, 0)

                                //알림
                                val dialogBuilder = AlertDialog.Builder(requireContext())
                                dialogBuilder.setTitle("작성 완료")
                                dialogBuilder.setMessage("작성이 완료되었습니다.")
                                dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                                    //화면 전환 처리
                                    act.fragmentRemoveBackStack("board_write") //현재 프래그먼트는 제거하고
                                    act.fragmentController("board_read", true, true) //현재 글의 읽기 프래그먼트로 바로 전환
                                }
                                dialogBuilder.show()
                            }
                        } else { //서버 통신 실패 시
                            activity?.runOnUiThread {
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

        //스피너 어댑터 생성 - 액티비티 속 데이터 가져와서 스피너 목록 구성
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            act.boardNameList.drop(1)
        )

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.boardWriteType.adapter = spinnerAdapter

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

        /**
         * 카메라 혹은 갤러리 공통 Intent 를 통해 결과가 넘어오는 callback 인 것 같다.
         * 이미지 크기가 너무 크면 [ImageView.setImageBitmap] 로 이미지를 화면에 그릴 때
         * [java.lang.RuntimeException: Canvas: trying to draw too large bitmap] 예외가 발생하며 앱이 종료된다.
         *
         * 그리고 큰 이미지를 서버로 전달하면, 서버에서 네트워크를 통해 이미지를 받아오는 시간이 너무 오래걸려서
         * 서버가 소켓 연결을 끊는다.
         * -> 서버와 클라이언트 의 request time out 을 조절해도 되지만, 아래와 같이 수정하면 더 간단히 해결된다.
         *
         * 해결방법 :
         * 아예 이미지를 불러올 때 최대 크기[AttachmentsSize] 이하로 샘플링하여 불러오도록 방어로직을 구성했다.
         * 이렇게 하면 아무리 큰 이미지를 불러와도 ImageView에 잘 그려지고 서버로도 빠르게 정상적으로 전송된다.
         *
         * 최대 width: 1500, height: 1500 으로 설정했을 때(현재) 40MB 크기의 이미지를 불러와도 정상 동작하는 것을 확인했다.
         *
         * 단, 서버로의 전송 속도는 네트워크 상태에 따라 다를 수 있다.
         * 커피빈에서는 500kB의 이미지도 서버로 전송하는데 소켓 연결이 끊겼었다.
         */

        when (requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK) {
                    /**
                     * BitmapFactory.decodeFile 할 때, 이미지를 작게 샘플링하여 decode한다.
                     * 이미지 최대 사이즈는 전역상수로 선언하여 사용한다. [AttachmentsSize]
                     */
                    uploadImage = decodeSampledBitmapFromResource(contentUri.path!!, maxWidth, maxHeight)
                    binding.boardWriteImage.setImageBitmap(uploadImage) //이미지뷰에 세팅

                    val file = File(contentUri.path)
                    file.delete()
                }
            }
            2 -> {
                if (resultCode == Activity.RESULT_OK) {
                    // 선택한 이미지에 접근할 수 있는 uri
                    val uri = data?.data

                    if (uri != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            val source = ImageDecoder.createSource(activity?.contentResolver!!, uri)
                            /**
                             * ImageDecoder.decodeBitmap 할 때, 이미지를 작게 샘플링하여 decode한다.
                             * 이미지 최대 사이즈는 전역상수로 선언하여 사용한다. [AttachmentsSize]
                             * @see[https://blog.stylingandroid.com/imagedecoder-error-handling-cropping-scaling/]
                             */
                            uploadImage = ImageDecoder.decodeBitmap(source,
                                ImageDecoder.OnHeaderDecodedListener { decoder, info, source ->
                                    decoder.setTargetSampleSize(
                                        calculateSampleSize(info.size.width, info.size.height, maxWidth, maxHeight)
                                    )
                            })
                            binding.boardWriteImage.setImageBitmap(uploadImage) //이미지 뷰에 세팅
                        } else {
                            val cursor =
                                activity?.contentResolver?.query(uri, null, null, null, null)
                            if (cursor != null) {
                                cursor.moveToNext()
                                // 이미지 경로를 가져온다.
                                val index = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                                val source = cursor.getString(index)
                                /**
                                 * BitmapFactory.decodeFile 할 때, 이미지를 작게 샘플링하여 decode한다.
                                 * 이미지 최대 사이즈는 전역상수로 선언하여 사용한다. [AttachmentsSize]
                                 */
                                uploadImage = decodeSampledBitmapFromResource(source, maxWidth, maxHeight)
                                binding.boardWriteImage.setImageBitmap(uploadImage)
                            }
                        }
                    }
                }
            }
            else -> {
                Log.w("BoardWriteFragment.kt", "Unknown requestCode($requestCode)")
            }
        }
    }
}