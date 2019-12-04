@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")

package org.example.kpad

import gtk3.*
import kotlinx.cinterop.*

fun main() {
    val mainWin = MainWindow()
    val app = gtk_application_new("org.example.kpad", G_APPLICATION_FLAGS_NONE)!!
    connectGtkSignal(
        obj = app,
        actionName = "activate",
        action = staticCFunction(::activate),
        data = mainWin.stableRef.asCPointer()
    )
    val status = g_application_run(app.reinterpret(), 0, null)
    g_object_unref(app)
    g_print("Application Status: %d", status)
}

/**
 * Connects a signal (event) to a slot (event handler). Note that all callback parameters must be primitive types or
 * nullable C pointers.
 */
private fun <F : CFunction<*>> connectGtkSignal(
    obj: CPointer<*>,
    actionName: String,
    action: CPointer<F>,
    data: gpointer? = null,
    connectFlags: GConnectFlags = 0u
) {
    g_signal_connect_data(
        instance = obj.reinterpret(),
        detailed_signal = actionName,
        c_handler = action.reinterpret(),
        data = data,
        destroy_data = null,
        connect_flags = connectFlags
    )
}

private fun activate(app: CPointer<GtkApplication>, userData: gpointer) {
    val mainWin = userData.asStableRef<MainWindow>().get()
    mainWin.winPtr = gtk_application_window_new(app)
    val mainLayout = mainWin.createMainLayout()
    mainWin.updateTitle("KPad")
    gtk_window_set_default_size(window = mainWin.winPtr?.reinterpret(), width = 600, height = 400)
    gtk_container_add(mainWin.winPtr?.reinterpret(), mainLayout?.reinterpret())
    gtk_widget_show_all(mainWin.winPtr)
}
