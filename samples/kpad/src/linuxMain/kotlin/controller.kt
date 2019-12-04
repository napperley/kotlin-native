@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")

package org.example.kpad

import gtk3.*
import kotlinx.cinterop.CFunction
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret

@ThreadLocal
internal object Controller {
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