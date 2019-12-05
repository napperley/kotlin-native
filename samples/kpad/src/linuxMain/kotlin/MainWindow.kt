package org.example.kpad

import gtk3.*
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.reinterpret

@Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")
internal class MainWindow {
    private val stableRef = StableRef.create(this)
    var winPtr: CPointer<GtkWidget>? = null
    private var editor: CPointer<GtkWidget>? = null
    private var statusBar: CPointer<GtkWidget>? = null
    var title = ""
        set(value) {
            gtk_window_set_title(winPtr?.reinterpret(), value)
            field = value
        }

    private fun createToolbar(): CPointer<GtkToolbar>? {
        val toolbar = gtk_toolbar_new()
        val newBtn = gtk_button_new_with_label("New")
        val openBtn = gtk_button_new_with_label("Open")
        val saveBtn = gtk_button_new_with_label("Save")
        val newItem = gtk_tool_item_new()
        val openItem = gtk_tool_item_new()
        val saveItem = gtk_tool_item_new()
        val toolItems = arrayOf(newItem, openItem, saveItem)

        if (openBtn != null && newBtn != null && saveBtn != null) {
            gtk_container_add(newItem?.reinterpret(), newBtn.reinterpret())
            gtk_container_add(openItem?.reinterpret(), openBtn.reinterpret())
            gtk_container_add(saveItem?.reinterpret(), saveBtn.reinterpret())
        }
        toolItems.forEachIndexed { pos, ti ->
            gtk_toolbar_insert(toolbar?.reinterpret(), ti?.reinterpret(), pos)
        }
        return toolbar?.reinterpret()
    }

    fun fetchReference() = stableRef.asCPointer()

    fun cleanUp() {
        stableRef.dispose()
    }

    fun createMainLayout(): CPointer<GtkContainer>? {
        val mainLayout = gtk_box_new(GtkOrientation.GTK_ORIENTATION_VERTICAL, 0)
        val scrolledWin = createScrolledWindow()
        val toolbar = createToolbar()
        createStatusBar()
        gtk_container_add(mainLayout?.reinterpret(), toolbar?.reinterpret())
        Controller.prependWidgetToBox(box = mainLayout?.reinterpret(), widget = scrolledWin?.reinterpret(),
            expand = true)
        Controller.appendWidgetToBox(box = mainLayout?.reinterpret(), widget = statusBar?.reinterpret())
        gtk_widget_grab_focus(editor?.reinterpret())
        return mainLayout?.reinterpret()
    }

    private fun createStatusBar() {
        statusBar = gtk_statusbar_new()
        gtk_statusbar_push(statusbar = statusBar?.reinterpret(), text = "Ready", context_id = 0u)
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
}