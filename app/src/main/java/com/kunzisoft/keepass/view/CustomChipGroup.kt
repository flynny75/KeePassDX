package com.kunzisoft.keepass.view

import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.kunzisoft.keepass.R

class CustomChipGroup @JvmOverloads constructor(context: Context,
                                                private val addTag: (() -> Unit)? = null,
                                                private val removeTag: ((tag:String) -> Unit)? = null)
    : ChipGroup(context, null, 0){

    var showCloseIcon: Boolean = false

    fun addChips(tags: ArrayList<String>){
        tags.forEach {tagValue->
            addChip(tagValue)
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

    fun addChip(tag: String){
        val c = Chip(context, null, 0)
        c.isCloseIconVisible = showCloseIcon
        c.text = tag
        c.setOnCloseIconClickListener{
            removeTag?.invoke(tag)
            removeView(c)
        }
        addView(c)
    }
}