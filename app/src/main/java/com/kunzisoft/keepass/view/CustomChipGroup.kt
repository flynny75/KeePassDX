package com.kunzisoft.keepass.view

import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.kunzisoft.keepass.R

class CustomChipGroup @JvmOverloads constructor(context: Context, private val addTag: (() -> Unit)? = null)
    : ChipGroup(context, null, 0){

    var showCloseIcon: Boolean = false

    fun addChip(tags: ArrayList<String>, removeTag: (tag: String) -> Unit){
        tags.forEach {tagValue->
            val c = Chip(context, null, 0)
            c.isCloseIconVisible = showCloseIcon
            c.text = tagValue
            c.setOnCloseIconClickListener{
                removeTag(tagValue)
                removeView(c)
            }
            addView(c)
        }

        if(showCloseIcon){
            val c = Chip(context, null, 0)
            c.chipIcon = ContextCompat.getDrawable(context, R.drawable.ic_add_white_24dp)
            c.text = "Add tag"
            c.setOnClickListener {
                addTag?.invoke()
            }
            addView(c)
        }
    }
}