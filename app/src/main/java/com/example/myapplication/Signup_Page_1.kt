package com.example.myapplication

import android.os.Bundle

import android.content.Intent
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.databinding.ActivitySignupPage1Binding


import com.example.myapplication.key.Companion.DB_USERS

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

import android.app.Activity
import android.net.Uri
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button


class Signup_Page_1 : AppCompatActivity() {

    private lateinit var binding: ActivitySignupPage1Binding
    private lateinit var database: DatabaseReference
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var webView: WebView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupPage1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        webView = findViewById(R.id.webview)

        // JavaScript 활성화
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true

        // WebViewClient 설정
        webView.webViewClient = WebViewClient()

        // 웹 페이지 로드 (서버의 주소를 사용)
        val serverIP = "192.168.56.1"
        webView.loadUrl("http://192.168.56.1:3001/address")

        binding.loginButton.setOnClickListener {
            val email = binding.idEditText.text.toString() // email 변수의 값을 'emailEditText' id를 가진 텍스트 박스에 입력 된 문자열로 받아옴
            val password = binding.pwEditText.text.toString() // password 변수의 값을 'passwordEditText' id를 가진 텍스트 박스에 입력 된 문자열로 받아옴
            val name = binding.usernameEditText.text.toString() // name 변수의 값을 'usernameEditText' id를 가진 텍스트 박스에 입력 된 문자열로 받아옴
            val Address = binding.addressEditText.text.toString()// Address 변수의 값을 'UserAddressEditText' id를 가진 텍스트 박스에 입력 된 문자열로 받아옴

            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || Address.isEmpty()) { // 기입하지 않은 정보가 있을 시
                Toast.makeText(this, "정보 중 기입되지 않은 정보가 있습니다.", Toast.LENGTH_SHORT).show() // "정보 중 기입되지 않은 정보가 있습니다." 출력
            }

            Firebase.auth.createUserWithEmailAndPassword(email, password) // 파이어베이스에 인증 도구에 접속 후 사용자에게 받은 email, password 값을 통해 계정 생성
                .addOnCompleteListener(this) { task ->
                    val currentUser = Firebase.auth.currentUser //currentUser에 값에 인증 계정 값 매칭
                    if (task.isSuccessful && currentUser != null) { // 인증 성공 및 유저가 있는 경우

                        //** 실시간 데이터 베이스 **//

                        val userId = currentUser.uid //userId변수의 값을 생선된 계정의 uid값으로 매칭

                        val user = mutableMapOf<String, Any>() // MutableMap을 사용하여 필요한 데이터를 동적으로 추가하고 관리
                        user["name"] = name  // 데이터 베이스에 들어가는 user 항목의 정보를 사용자가 입력한 name변수의 값으로 매칭
                        user["email"] = email //데이터 베이스에 들어가는 email 항목의 정보를 사용자가 입력한 email 값으로 매칭
                        user["password"] = password // 데이터 베이스에 들어가는 password 항목의 정보를 사용자가 입력한 password 값으로 매칭
                        user["Address"] = Address //데이터 베이스에 들어가는 Address 항목의 정보를 사용자가 입력한 Address값을 매칭

                        //해당 데이터베이스를 파이어베이스 실시간 데이터 베이스 안에 userId 하위에 위의 4가지 데이터가 들어가도록 위치 선언
                        Firebase.database.reference.child(DB_USERS).child(userId).updateChildren(user)


                        //회원 가입 성공
                        Toast.makeText(this, "회원가입에 성공했습니다. 로그인해주세요", Toast.LENGTH_SHORT).show()

                    } else {
                        // 회원 가입 실패
                        Toast.makeText(this, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }

                }
        }
        binding.signupText.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }


    }
}