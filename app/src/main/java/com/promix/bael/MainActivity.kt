package com.promix.bael

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.promix.baelui.bind.base.VmBase
import com.promix.baelui.bind.binder.ComposeItemBuilder
import com.promix.baelui.ext.bindFilterBy
import com.promix.baelui.ext.bindItems
import com.promix.baelui.ext.bindView
import com.promix.baelui.helper.IBindPredicate
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.bindView(
            ComposeItemBuilder.Builder().addItemBinder(
                VmFilter::class,
                BR.model,
                R.layout.item_filter
            ).build()
        )

        val items = mutableListOf<VmFilter>()
        for (i in 1..100) {
            items.add(VmFilter("Title $i"))
        }
        recyclerView.bindItems(items)

        edtSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString().toLowerCase()
                recyclerView.bindFilterBy(object : IBindPredicate<VmFilter> {
                    override fun condition(item: VmFilter): Boolean {
                        return if (text.isEmpty())
                            true
                        else
                            item.title?.toLowerCase()?.contains(text) ?: false
                    }
                })
            }
        })
    }
}

class VmFilter(title: String) : VmBase<String>(title) {
    override fun getUnique(): Long {
        return model.hashCode().toLong()
    }

    val title: String?
        get() = model
}