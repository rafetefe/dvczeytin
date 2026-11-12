package io.github.rafetefe

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfPrintHelper {

    fun printForm(context: Context, items: List<FormRow>) {
        val printManager = (context.getSystemService(Context.PRINT_SERVICE) as? PrintManager) ?: return
        val jobName = "DVCZeytin_Form_${System.currentTimeMillis()}"

        printManager.print(
            jobName,
            object : PrintDocumentAdapter() {
                private var pdfDocument: PdfDocument? = null

                override fun onLayout(
                    oldAttributes: PrintAttributes?,
                    newAttributes: PrintAttributes?,
                    cancellationSignal: CancellationSignal?,
                    callback: LayoutResultCallback?,
                    extras: Bundle?,
                ) {
                    pdfDocument = PdfDocument()

                    if (cancellationSignal?.isCanceled == true) {
                        callback?.onLayoutCancelled()
                        return
                    }

                    val info = PrintDocumentInfo.Builder("Form_Report.pdf")
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(1)
                        .build()

                    callback?.onLayoutFinished(info, true)
                }

                override fun onWrite(
                    pages: Array<out PageRange>?,
                    destination: ParcelFileDescriptor?,
                    cancellationSignal: CancellationSignal?,
                    callback: WriteResultCallback?,
                ) {
                    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size in points
                    val page = pdfDocument?.startPage(pageInfo) ?: return

                    val canvas: Canvas = page.canvas
                    val paint = Paint()

                    // Title on Left
                    paint.color = Color.BLACK
                    paint.textSize = 18f
                    paint.isFakeBoldText = true
                    canvas.drawText("DVCOĞLU Zeytin Alım Raporu", 40f, 50f, paint)

                    // Date and Time on Right Top Side
                    paint.textSize = 11f
                    paint.isFakeBoldText = false
                    val currentDateTime = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date())
                    val dateWidth = paint.measureText(currentDateTime)
                    canvas.drawText(currentDateTime, 555f - dateWidth, 50f, paint)

                    // Line separator
                    paint.strokeWidth = 1f
                    canvas.drawLine(40f, 65f, 555f, 65f, paint)

                    // Table Header
                    paint.textSize = 12f
                    paint.isFakeBoldText = true
                    canvas.drawText("Cins", 40f, 90f, paint)
                    canvas.drawText("Boyut", 160f, 90f, paint)
                    canvas.drawText("Miktar", 300f, 90f, paint)
                    canvas.drawText("Toplam", 440f, 90f, paint)

                    canvas.drawLine(40f, 100f, 555f, 100f, paint)

                    // Table Rows
                    paint.isFakeBoldText = false
                    var y = 125f
                    for ((_, cins, boyut, miktar, toplam) in items) {
                        canvas.drawText(cins, 40f, y, paint)
                        canvas.drawText(boyut, 160f, y, paint)
                        canvas.drawText(miktar, 300f, y, paint)
                        canvas.drawText(toplam, 440f, y, paint)
                        y += 25f
                        if (y > 800f) break
                    }

                    pdfDocument?.finishPage(page)

                    try {
                        FileOutputStream(destination?.fileDescriptor).use { output ->
                            pdfDocument?.writeTo(output)
                        }
                    } catch (e: IOException) {
                        callback?.onWriteFailed(e.message)
                        return
                    } finally {
                        pdfDocument?.close()
                        pdfDocument = null
                    }

                    callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
                }
            },
            null,
        )
    }
}
