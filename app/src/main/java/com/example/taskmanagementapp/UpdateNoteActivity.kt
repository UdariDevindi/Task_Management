package com.example.taskmanagementapp


import android.content.Intent
import android.os.Bundle
import android.provider.BaseColumns
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanagementapp.db.DBOpenHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.example.taskmanagementapp.utils.COLUMN_NAME_DESCRIPTION
import com.example.taskmanagementapp.utils.COLUMN_NAME_TITLE
import com.example.taskmanagementapp.utils.COLUMN_NAME_PRIORITY

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var etUpdatedTitle: TextInputLayout
    private lateinit var etUpdatedDescription: TextInputLayout
    private lateinit var etPriority: TextInputLayout // New field for priority
    private lateinit var fabUpdate: FloatingActionButton
    private val dbOpenHelper = DBOpenHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_note)

        etUpdatedTitle = findViewById(R.id.et_updated_title)
        etUpdatedDescription = findViewById(R.id.et_updated_description)
        etPriority = findViewById(R.id.et_updated_priority) // Initialize priority field
        fabUpdate = findViewById(R.id.fab_update)

        val titleOld = intent.getStringExtra(COLUMN_NAME_TITLE)
        val descriptionOld = intent.getStringExtra(COLUMN_NAME_DESCRIPTION)
        val priorityOld = intent.getIntExtra(COLUMN_NAME_PRIORITY, 0) // Get priority value

        if (!titleOld.isNullOrBlank()) {
            etUpdatedTitle.editText?.text =
                Editable.Factory.getInstance().newEditable(titleOld)
            etUpdatedDescription.editText?.text =
                Editable.Factory.getInstance().newEditable(descriptionOld)
            etPriority.editText?.text =
                Editable.Factory.getInstance().newEditable(priorityOld.toString()) // Set priority value

            Log.d("UpdateNoteActivity", titleOld.toString())
            Log.d("UpdateNoteActivity", descriptionOld.toString())

        } else {
            Log.d("UpdateNoteActivity", "value was null")
            Toast.makeText(this, "Value was null", Toast.LENGTH_SHORT).show()
        }

        fabUpdate.setOnClickListener {
            updateData()
        }
    }

    private fun updateData() {
        val id = intent.getIntExtra(BaseColumns._ID, 0).toString()

        if (etUpdatedTitle.editText?.text.toString().isEmpty()) {
            etUpdatedTitle.error = "Please enter your Title"
            etUpdatedTitle.requestFocus()
            return
        }

        if (etUpdatedDescription.editText?.text.toString().isEmpty()) {
            etUpdatedDescription.error = "Please enter your Description"
            etUpdatedDescription.requestFocus()
            return
        }

        if (etPriority.editText?.text.toString().isEmpty()) {
            etPriority.error = "Please enter priority"
            etPriority.requestFocus()
            return
        }

        if (notEmpty()) {
            dbOpenHelper.updateNote(
                id,
                etUpdatedTitle.editText?.text.toString(),
                etUpdatedDescription.editText?.text.toString(),
                etPriority.editText?.text.toString().toInt() // Convert priority to Int
            )
            Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show()
            val intentToMainActivity = Intent(this, MainActivity::class.java)
            startActivity(intentToMainActivity)
            finish()
        }
    }

    private fun notEmpty(): Boolean {
        return (etUpdatedTitle.editText?.text.toString().isNotEmpty()
                && etUpdatedDescription.editText?.text.toString().isNotEmpty()
                && etPriority.editText?.text.toString().isNotEmpty())
    }
}
