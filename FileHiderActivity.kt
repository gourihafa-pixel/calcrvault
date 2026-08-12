package com.calcvault.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class FileHiderActivity : AppCompatActivity() {

    private val hiddenFiles = mutableListOf<DocumentFile>()
    private lateinit var adapter: FileAdapter
    private var treeUri: Uri? = null

    private val pickTreeLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        if (uri == null) {
            Toast.makeText(this, "File picker cancelled", Toast.LENGTH_SHORT).show()
            return@registerForActivityResult
        }
        contentResolver.takePersistableUriPermission(
            uri, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        treeUri = uri
        val root = DocumentFile.fromTreeUri(this, uri)
        root?.listFiles()?.forEach { hiddenFiles.add(it) }
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_hider)

        val list = findViewById<RecyclerView>(R.id.filesList)
        list.layoutManager = LinearLayoutManager(this)
        adapter = FileAdapter()
        list.adapter = adapter

        findViewById<MaterialButton>(R.id.btnPickFile).setOnClickListener {
            pickTreeLauncher.launch(null)
        }

        findViewById<MaterialButton>(R.id.btnBack).setOnClickListener { finish() }
    }

    inner class FileAdapter : RecyclerView.Adapter<FileAdapter.VH>() {
        inner class VH(v: View) : RecyclerView.ViewHolder(v) {
            val name: TextView = v.findViewById(R.id.tvName)
            val size: TextView = v.findViewById(R.id.tvSize)
            unhide = v.findViewById(R.id.btnUnhide)
            private var unhide: MaterialButton = v.findViewById(R.id.btnUnhide)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
            return VH(v)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val file = hiddenFiles[position]
            holder.name.text = file.name ?: "(no name)"
            holder.size.text = "${(file.length() / 1024)} KB"
            holder.itemView.findViewById<MaterialButton>(R.id.btnReveal).setOnClickListener {
                file.uri?.let { uri ->
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(uri, contentResolver.getType(uri))
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    try { startActivity(intent) } catch (e: Exception) {
                        Toast.makeText(this@FileHiderActivity, "No app to open this file", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            holder.itemView.findViewById<MaterialButton>(R.id.btnUnhide).setOnClickListener {
                if (file.delete()) {
                    hiddenFiles.removeAt(holder.adapterPosition)
                    notifyItemRemoved(holder.adapterPosition)
                    Toast.makeText(this@FileHiderActivity, "Removed from vault", Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun getItemCount(): Int = hiddenFiles.size
    }
}
