/*
 * Copyright 2019 Jeremy Jamet / Kunzisoft.
 *     
 * This file is part of KeePassDX.
 *
 *  KeePassDX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  KeePassDX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with KeePassDX.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.kunzisoft.keepass.activities.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.kunzisoft.keepass.R


class EntryTagDialogFragment: DatabaseDialogFragment() {

    private var newTag: TextView? = null

    private var addTagListener: EntryAddTagListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            addTagListener = context as EntryAddTagListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(context.toString()
                    + " must implement " + EntryAddTagListener::class.java.name)
        }
    }

    override fun onDetach() {
        addTagListener = null
        super.onDetach()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        activity?.let { activity ->
            val root = activity.layoutInflater.inflate(R.layout.fragment_entry_new_tag, null)
            newTag = root?.findViewById(R.id.entry_custom_tag)

            newTag?.text = arguments?.getString(KEY_FIELD)

            val builder = AlertDialog.Builder(activity)
            builder.setView(root)
                    .setPositiveButton(android.R.string.ok, null)
                    .setNegativeButton(android.R.string.cancel) { _, _ -> }
            val dialogCreated = builder.create()

            newTag?.requestFocus()
            newTag?.imeOptions = EditorInfo.IME_ACTION_DONE
            newTag?.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addTagListener?.AddTag(newTag?.text.toString())
                    dismiss()
                }
                false
            }

            dialogCreated.window?.setSoftInputMode(SOFT_INPUT_STATE_VISIBLE)
            return dialogCreated

        }
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        // To prevent auto dismiss
        val d = dialog as AlertDialog?
        if (d != null) {
            val positiveButton = d.getButton(Dialog.BUTTON_POSITIVE) as Button
            positiveButton.setOnClickListener {
                addTagListener?.AddTag(newTag?.text.toString())
                dismiss()
            }
        }
    }

    interface EntryAddTagListener {
        fun AddTag(newTag: String)
    }

    companion object {

        private const val KEY_FIELD = "KEY_FIELD"

        fun getInstance(): EntryTagDialogFragment {
            return EntryTagDialogFragment()
        }

        fun getInstance(tag: String): EntryTagDialogFragment {
            return EntryTagDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_FIELD, tag)
                }
            }
        }
    }
}
