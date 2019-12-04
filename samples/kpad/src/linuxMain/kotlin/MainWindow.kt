package org.example.kpad

import gtk3.*
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.reinterpret

internal class MainWindow {
    val stableRef = StableRef.create(this)
    var winPtr: CPointer<GtkWidget>? = null
    private var editor: CPointer<GtkWidget>? = null

    fun createMainLayout(): CPointer<GtkContainer>? {
        val mainLayout = gtk_box_new(GtkOrientation.GTK_ORIENTATION_VERTICAL, 0)
        val scrolledWin = createScrolledWindow()
        Controller.prependWidgetToBox(box = mainLayout?.reinterpret(), widget = scrolledWin?.reinterpret(),
            expand = true)
        gtk_widget_grab_focus(editor?.reinterpret())
        return mainLayout?.reinterpret()
    }

    private fun createScrolledWindow(): CPointer<GtkScrolledWindow>? {
        val scrolledWin = gtk_scrolled_window_new(null, null)
        val margins = mapOf("left" to 10, "right" to 10, "top" to 10)
        editor = gtk_text_view_new()
        gtk_widget_set_margin_left(scrolledWin, margins["left"] ?: 0)
        gtk_widget_set_margin_right(scrolledWin, margins["right"] ?: 0)
        gtk_widget_set_margin_top(scrolledWin, margins["top"] ?: 0)
        gtk_text_view_set_cursor_visible(editor?.reinterpret(), TRUE)
        gtk_container_add(scrolledWin?.reinterpret(), editor)
        return scrolledWin?.reinterpret()
    }

    fun updateTitle(title: String) {
        gtk_window_set_title(winPtr?.reinterpret(), title)
    }
}