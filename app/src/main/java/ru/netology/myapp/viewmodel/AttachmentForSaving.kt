package ru.netology.myapp.viewmodel

import android.net.Uri
import ru.netology.myapp.dto.AttachmentType
import java.io.File

data class AttachmentForSaving(
    val uri: Uri? = null,
//    абстрактный или физический ресурс
    val file: File? = null,
    val type: AttachmentType
)