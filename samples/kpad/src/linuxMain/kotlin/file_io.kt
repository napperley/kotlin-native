@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")

package org.example.kpad

import gtk3.GError
import gtk3.g_file_set_contents
import kotlinx.cinterop.*
import platform.posix.*

internal fun saveFile(filePath: String, txt: String): String = memScoped {
    val error = alloc<CPointerVar<GError>>()
    g_file_set_contents(filename = filePath, contents = txt, length = txt.length.toLong(), error = error.ptr)
    return error.pointed?.message?.toKString() ?: ""
}

internal fun fileName(filePath: String): String = if ('/' in filePath && filePath.length >= 2) {
    @Suppress("ReplaceRangeToWithUntil")
    filePath.slice((filePath.lastIndexOf("/") + 1)..(filePath.length - 1))
} else {
    ""
}

internal fun readTextFile(filePath: String): String {
    val size = fileSize(filePath)
    var result = ""
    val readMode = "r"
    val file = fopen(filePath, readMode)

    memScoped {
        val buffer = allocArray<ByteVar>(size)
        // Read the entire file and store the contents into the buffer.
        fread(__stream = file, __ptr = buffer, __size = size.toULong(), __n = 1u)
        result = buffer.toKString()
    }
    fclose(file)
    return result
}

internal fun fileSize(filePath: String): Long {
    val readMode = "r"
    val file = fopen(filePath, readMode)
    fseek(__stream = file, __off = 0, __whence = SEEK_END)
    return ftell(file)
}