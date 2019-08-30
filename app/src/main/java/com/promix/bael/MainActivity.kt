package com.promix.bael

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.promix.baelui.view.SegmentGroup
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        segmentView.setOnSegmentSelectedListener(object : SegmentGroup.OnSegmentSelectedListener {
            override fun onSegmentSelected(v: View?, selectedIndex: Int) {
                Toast.makeText(this@MainActivity, (v as TextView).text, Toast.LENGTH_LONG).show()
            }
        })
    }
}
