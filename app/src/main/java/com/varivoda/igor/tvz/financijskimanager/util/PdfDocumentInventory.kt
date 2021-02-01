package com.varivoda.igor.tvz.financijskimanager.util

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Environment
import androidx.core.content.res.ResourcesCompat
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.model.ProductStockDTO
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class PdfDocumentInventory {

    fun createPdf(bm: Bitmap, resources: Resources, list: MutableList<ProductStockDTO>, fullName: String, context: Context){
        val document = PdfDocument()
        var pageInfo = PdfDocument.PageInfo.Builder(300, 500, 1).create()
        var page = document.startPage(pageInfo)
        var canvas: Canvas = page.canvas
        val paint = Paint()
        val paintDark = Paint()
        paintDark.color = Color.GRAY
        paintDark.strokeWidth = 10f
        canvas.drawRect(0f,0f,300f,90f,paintDark)
        val d = ResourcesCompat.getDrawable(resources, R.drawable.logo_transparent,null)
        d?.setBounds(105, 0, 195, 90)
        d?.draw(canvas)
        paint.color = Color.BLACK

        canvas.drawText("Zitnjak",10f,110f,paint)
        val sdf = SimpleDateFormat("dd.MM.yyyy. HH:mm",Locale.getDefault())
        val current: String = sdf.format(Date())
        canvas.drawText(current,200f,110f,paint)

        canvas.drawLine(0f,120f,300f,120f,paint)

        var start = 140f
        list.forEach {
            if(start >= 460f){
                canvas.drawText("${it.productName}: ${it.quantity}",10f,start,paint)
                document.finishPage(page)
                pageInfo = PdfDocument.PageInfo.Builder(300, 500, 2).create()
                page = document.startPage(pageInfo)
                canvas = page.canvas
                start = 20f
            }
            canvas.drawText("${it.productName}: ${it.quantity}",10f,start,paint)
            start += 20f
        }



        paint.color = Color.BLACK

        canvas.drawLine(0f,470f,300f,470f,paint)
        canvas.drawText("Izdao: $fullName", 10f,490f,paint)
        canvas.drawBitmap(bm,null, RectF(200f,475f,300f,490f),null)

        document.finishPage(page)

        val sdf1 = SimpleDateFormat("dd-MM-yyyy-HH-mm", Locale.getDefault())
        val current1: String = sdf1.format(Date())
        val directoryPath: String = Environment.getExternalStorageDirectory().getPath().toString() + "/financijski_manager"
        val file = File(directoryPath)
        if (!file.exists()) {
            file.mkdirs()
        }
        val targetPdf = "$directoryPath/inventura$current1.pdf/"
        val filePath = File(targetPdf)
        try {
            document.writeTo(FileOutputStream(filePath))
        } catch (e: Exception) {
            showSelectedToast(context, context.getString(R.string.error_pdf_create))
        }
        document.close()
    }
}