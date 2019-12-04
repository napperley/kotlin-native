package org.example.kpad

import gtk3.*
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.reinterpret

internal class MainWindow {
    val stableRef = StableRef.create(this)
    var winPtr: CPointer<GtkWidget>? = null

    fun createMainLayout(): CPointer<GtkContainer>? {
        val mainLayout = gtk_box_new(GtkOrientation.GTK_ORIENTATION_VERTICAL, 0)
        return mainLayout?.reinterpret()
    }

    fun updateTitle(title: String) {
        gtk_window_set_title(winPtr?.reinterpret(), title)
    }
}