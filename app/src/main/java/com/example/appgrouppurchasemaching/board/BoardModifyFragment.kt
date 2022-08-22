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
import com.example.appgrouppurchasemaching.databinding.FragmentBoardModifyBinding
import okhttp3.*
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.net.URI
import java.net.URL
import kotlin.concurrent.thread


class BoardModifyFragment : Fragment() { //게시글 수정 프래그먼트
    //바인딩
    lateinit var binding : FragmentBoardModifyBinding

    //사진 관련 변수
    lateinit var contentUri: Uri //사진에 접근 가능한 uri 담을 변수
    var uploadImage : Bitmap? = null //서버에 업로드할 이미지 담을 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val act = activity as BoardMainActivity

        //바인딩
        binding = FragmentBoardModifyBinding.inflate(inflater)
        //title
        binding.boardModifyToolbar.title = "게시글 수정"
        //메뉴 xml 지정
        binding.boardModifyToolbar.inflateMenu(R.menu.board_modify_menu)
        //각 메뉴 항목 이벤트 처리
        binding.boardModifyToolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.board_modify_menu_camera -> { //카메라 클릭 시
                    val filePath = requireContext().getExternalFilesDir(null).toString()

                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                    // 촬영한 사진이 저장될 파일 이름
                    val fileName = "/temp_${System.currentTimeMillis()}.jpg"
                    val picPath = "$filePath/$fileName"

                    val file = File(picPath)

                    contentUri = FileProvider.getUriForFile(requireContext(),
                        "com.example.appgrouppurchasemaching.camera.file_provider", file)

                    if(contentUri != null){
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
                        startActivityForResult(cameraIntent, 1)
                    }
                    true
                }
                R.id.board_modify_menu_gallery -> { //갤러리 클릭 시
                    val albumIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    albumIntent.type = "image/*"

                    val mimeType = arrayOf("image/*")
                    albumIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
                    startActivityForResult(albumIntent, 2)
                    true
                }
                R.id.board_modify_menu_upload -> { //업로드 클릭 시

                    //서버에 업로드 처리할 데이터(수정 후 내용물을 바인딩처리로) 추출
                    val boardModifySubject = binding.boardModifySubject.text.toString()
                    val boardModifyText = binding.boardModifyText.text.toString()
                    val boardModifyType = act.boardIndexList[binding.boardModifyType.selectedItemPosition + 1]

                    //유효성 검사 하기
                    // 게시글 제목 유효성 검사
                    if(boardModifySubject == null || boardModifySubject.length == 0){
                        val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setTitle("제목 입력 오류")
                        dialogBuilder.setMessage("제목을 입력해주세요")
                        dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
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
                        dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                            binding.boardModifyText.requestFocus() //입력칸에 재포커싱
                        }
                        dialogBuilder.show()
                        return@setOnMenuItemClickListener true
                    }

                    //'서버 접속'
                    thread {
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
                            activity?.runOnUiThread {
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
                                dialogBuilder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                                    act.fragmentRemoveBackStack("board_modify") //우선 현재 프래그먼트 종료시키기
                                }
                                dialogBuilder.show()
                            }
                        } else { //통신 실패한 경우
                            activity?.runOnUiThread {
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

                //게시글 수정 화면에 데이터 처리 = 수정하려고 선택한 게시글의 내용물 데이터를 세팅처리
                act.runOnUiThread {
                    binding.boardModifySubject.setText(obj.getString("content_subject"))
                    binding.boardModifyText.setText(obj.getString("content_text"))
                    val contentImage = obj.getString("content_image")

                    if(contentImage == "null"){ //DB 데이터 상 이미지 null값인 경우
                        binding.boardModifyImage.visibility = View.GONE //이미지 뷰 사라지게 함
                    }else { //DB 데이터 상 이미지 존재하면
                        thread {
                            val imageUrl = URL("http://${ServerInfo.SERVER_IP}:8080/App_GroupCharge_Server/upload/$contentImage")
                            val bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream())
                            activity?.runOnUiThread {
                                binding.boardModifyImage.setImageBitmap(bitmap) //이미지 세팅 처리
                            }
                        }
                    }

                    //Spinner 구성 - 게시글 수정 시: 카테고리 변경을 할 수도 있으므로 별도의 스피너 구성한다.
                    val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, act.boardNameList.drop(1))
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.boardModifyType.adapter = spinnerAdapter
                    binding.boardModifyType.setSelection(obj.getInt("content_board_idx") - 1)

                }
            }
        }

        return binding.root
    }


    //재정의
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK) {
                    uploadImage = BitmapFactory.decodeFile(contentUri.path)
                    binding.boardModifyImage.setImageBitmap(uploadImage) //이미지뷰에 세팅
                    binding.boardModifyImage.visibility = View.VISIBLE //이미지 보이게

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
                            uploadImage = ImageDecoder.decodeBitmap(source)
                            binding.boardModifyImage.setImageBitmap(uploadImage) //이미지 뷰에 세팅
                            binding.boardModifyImage.visibility = View.VISIBLE //이미지 보이게
                        } else {
                            val cursor =
                                activity?.contentResolver?.query(uri, null, null, null, null)
                            if (cursor != null) {
                                cursor.moveToNext()
                                // 이미지 경로를 가져온다.
                                val index = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                                val source = cursor.getString(index)
                                uploadImage = BitmapFactory.decodeFile(source)
                                binding.boardModifyImage.setImageBitmap(uploadImage)
                                binding.boardModifyImage.visibility = View.VISIBLE //이미지 보이게
                            }
                        }
                    }
                }
            }
        }
    }




}