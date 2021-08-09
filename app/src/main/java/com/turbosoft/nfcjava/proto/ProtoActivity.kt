package com.turbosoft.nfcjava.proto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.turbosoft.nfcjava.R

class ProtoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proto)
        val tvOrg = findViewById<TextView>(R.id.tv_org_proto)
        val id = findViewById<EditText>(R.id.et_id_proto)
        val name = findViewById<EditText>(R.id.et_name_proto)
        val btn = findViewById<Button>(R.id.btn_save_proto)

        btn.setOnClickListener {
            val orgId = id.text.toString()
            val orgName = name.text.toString()

        }
    }
}