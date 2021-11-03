package com.kunzisoft.keepass.view

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.kunzisoft.keepass.R

class CustomChipGroup @JvmOverloads constructor(context: Context,
                                                private val addTag: (() -> Unit)? = null,
                                                private val removeTag: ((tag:String) -> Unit)? = null)
    : FrameLayout(context, null, 0){

    var editMode: Boolean = false
    private var chipGroup: ChipGroup
    private var chipInfo: TextView

    init{
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        inflater?.inflate(R.layout.view_tags, this)

        chipGroup = findViewById(R.id.tags_chipgroup)
        chipInfo = findViewById(R.id.tags_infolabel)
    }

    fun addChips(tags: List<String>){
        tags.forEach {tagValue->
            addChip(tagValue)
        }

        if(editMode){
            val c = Chip(context, null, 0)
            c.chipIcon = ContextCompat.getDrawable(context, R.drawable.ic_add_white_24dp)
            c.text = "Add tag"
            c.setOnClickListener {
                addTag?.invoke()
            }
            c.tag = "addtag"
            chipGroup.addView(c)
        }

        showInfo()
    }

    fun addChip(tag: String){
        val c = Chip(context, null, 0)
        c.isCloseIconVisible = editMode
        c.text = tag
        c.setOnCloseIconClickListener{
            removeTag?.invoke(tag)
            chipGroup.removeView(c)
        }

        if(chipGroup.childCount > 0 && chipGroup.children.last().tag == "addtag"){
            // Swap this new chip and the last AddTag chip
            chipGroup.addView(c, chipGroup.childCount-1)
        }else{
            chipGroup.addView(c)
        }
    }

    private fun showInfo(){
        chipInfo.visibility = if(!editMode && chipGroup.childCount == 0)
            VISIBLE
        else
            GONE
    }
}