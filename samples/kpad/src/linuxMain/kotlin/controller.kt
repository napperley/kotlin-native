@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")

package org.example.kpad

import gtk3.*
import kotlinx.cinterop.*

@ThreadLocal
internal object Controller {
    private var filePath = ""
//    val mainWin = MainWindow()

    fun clearFilePath() {
        filePath = ""
    }

    fun fetchFilePath() = filePath

    fun textFromTextBuffer(buffer: CPointer<GtkTextBuffer>?): String = memScoped {
        val start = alloc<GtkTextIter>().ptr
        val end = alloc<GtkTextIter>().ptr
        gtk_text_buffer_get_start_iter(buffer, start)
        gtk_text_buffer_get_end_iter(buffer, end)
        return gtk_text_buffer_get_text(
            buffer = buffer,
            start = start,
            end = end,
            include_hidden_chars = FALSE
        )?.toKString() ?: ""
    }

    fun showOpenDialog(mainWin: MainWindow) {
        val dialog = createOpenDialog(mainWin.winPtr?.reinterpret())
        val resp = gtk_dialog_run(dialog?.reinterpret())
        if (resp == GTK_RESPONSE_ACCEPT) {
            filePath = gtk_file_chooser_get_filename(dialog?.reinterpret())?.toKString() ?: ""
            with(mainWin) {
                updateStatusBar("Opening $filePath...")
                updateEditor(filePath)
                title = "KPad - ${fileName(filePath)}"
                updateStatusBar("File opened")
            }
        }
        gtk_widget_destroy(dialog)
    }

    private fun createOpenDialog(parent: CPointer<GtkWindow>?) = gtk_file_chooser_dialog_new(
        "Open File",
        parent,
        GtkFileChooserAction.GTK_FILE_CHOOSER_ACTION_OPEN,
        "gtk-cancel",
        GTK_RESPONSE_CANCEL,
        "gtk-open",
        GTK_RESPONSE_ACCEPT,
        null
    )

    fun showSaveDialog(mainWin: MainWindow, buffer: CPointer<GtkTextBuffer>?) {
        val dialog = createSaveDialog(mainWin.winPtr?.reinterpret())
        val resp = gtk_dialog_run(dialog?.reinterpret())
        if (resp == GTK_RESPONSE_ACCEPT) {
            filePath = gtk_file_chooser_get_filename(dialog?.reinterpret())?.toKString() ?: ""
            with(mainWin) {
                updateStatusBar("Saving $filePath...")
                title = "KPad - ${fileName(filePath)}"
                saveFile(filePath, textFromTextBuffer(buffer))
                updateStatusBar("File saved")
            }
        }
        gtk_widget_destroy(dialog)
    }

    private fun createSaveDialog(parent: CPointer<GtkWindow>?) = gtk_file_chooser_dialog_new(
        parent = parent,
        title = "Save File",
        first_button_text = "gtk-cancel",
        action = GtkFileChooserAction.GTK_FILE_CHOOSER_ACTION_SAVE,
        variadicArguments = *arrayOf(GTK_RESPONSE_CANCEL, "gtk-save", GTK_RESPONSE_ACCEPT, null)
    )

    /**
     * With a vertical box a [widget] is added to the *top* of the [box]. If the box is horizontal then a [widget] is
     * added to the *left* of the [box].
     * @param box A [pointer][CPointer] to the GtkBox.
     * @param widget A [pointer][CPointer] to the GtkWidget.
     * @param fill If *true* then the added [widget] will be sized to use all of the available space.
     * @param expand If *true* then the added [widget] will be resized every time the [box] is resized.
     * @param padding The amount of padding to use for the [widget] which is in pixels. By default no padding is used.
     */
    fun prependWidgetToBox(
        box: CPointer<GtkBox>?,
        widget: CPointer<GtkWidget>?,
        fill: Boolean = true,
        expand: Boolean = false,
        padding: UInt = 0u
    ): Unit = gtk_box_pack_start(
        box = box,
        child = widget,
        expand = if (expand) TRUE else FALSE,
        fill = if (fill) TRUE else FALSE,
        padding = padding
    )

    /**
     * With a vertical box a [widget] is added to the *bottom* of the [box]. If the box is horizontal then a [widget] is
     * added to the *right* of the [box].
     * @param box A [pointer][CPointer] to the GtkBox.
     * @param widget A [pointer][CPointer] to the GtkWidget.
     * @param fill If *true* then the added [widget] will be sized to use all of the available space.
     * @param expand If *true* then the added [widget] will be resized every time the [box] is resized.
     * @param padding The amount of padding to use for the [widget] which is in pixels. By default no padding is used.
     */
    fun appendWidgetToBox(
        box: CPointer<GtkBox>?,
        widget: CPointer<GtkWidget>?,
        fill: Boolean = true,
        expand: Boolean = false,
        padding: UInt = 0u
    ): Unit = gtk_box_pack_end(
        box = box,
        child = widget,
        expand = if (expand) TRUE else FALSE,
        fill = if (fill) TRUE else FALSE,
        padding = padding
    )

    /**
     * Connects a signal (event) to a slot (event handler). Note that all callback parameters must be primitive types or
     * nullable C pointers.
     * @param obj The object to use for connecting a [signal][actionName] to a [slot][action].
     * @param actionName The name of the signal to connect to.
     * @param action The slot to use for handling the signal.
     * @param data User data to pass through to the [action]. By default no user data is passed through.
     * @param connectFlags The flags to use.
     */
    fun <F : CFunction<*>> connectGtkSignal(
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
}