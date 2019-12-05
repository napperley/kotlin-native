@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")

package org.example.kpad

import gtk3.*
import kotlinx.cinterop.*

fun main() {
    val mainWin = MainWindow()
    val app = gtk_application_new("org.example.kpad", G_APPLICATION_FLAGS_NONE)!!
    Controller.connectGtkSignal(
        obj = app,
        actionName = "activate",
        action = staticCFunction(::activate),
        data = mainWin.fetchReference()
    )
    val status = g_application_run(argc = 0, argv = null, application = app.reinterpret())
    g_object_unref(app)
    mainWin.cleanUp()
    g_print("Application Status: %d", status)
}

private fun activate(app: CPointer<GtkApplication>, userData: gpointer) {
    val mainWin = userData.asStableRef<MainWindow>().get()
    mainWin.winPtr = gtk_application_window_new(app)
    val mainLayout = mainWin.createMainLayout()
    mainWin.title = "KPad"
    gtk_window_set_default_size(window = mainWin.winPtr?.reinterpret(), width = 600, height = 400)
    gtk_container_add(mainWin.winPtr?.reinterpret(), mainLayout?.reinterpret())
    gtk_widget_show_all(mainWin.winPtr)
}
