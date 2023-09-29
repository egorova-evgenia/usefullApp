package ru.netology.myapp.viewmodel

import android.net.Uri
import ru.netology.myapp.dto.Attachment
import ru.netology.myapp.dto.AttachmentType
import java.io.File

data class AttachmentModel(
    val uri: Uri? = null,
    val file: File? = null,
    val type: AttachmentType
)