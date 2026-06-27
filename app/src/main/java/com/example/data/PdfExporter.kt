package com.example.data

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfExporter {

    suspend fun generateAndShareProjectPdf(
        context: Context,
        project: Project,
        customer: Customer?,
        rooms: List<RoomEntity>,
        openingsMap: Map<Long, List<OpeningEntity>>,
        tasks: List<WorkTaskEntity>
    ) {
        try {
            val dao = AppDatabase.getDatabase(context).craftsmanDao()
            val contractorName = dao.getSetting("contractor_name")?.value ?: "المقدّر الذكي للمقاولات"
            val contractorPhone = dao.getSetting("contractor_phone")?.value ?: ""
            val contractorAddress = dao.getSetting("contractor_address")?.value ?: ""
            val contractorTaxId = dao.getSetting("contractor_tax_id")?.value ?: ""

            val pdfDocument = PdfDocument()
            
            // Paint settings
            val paint = Paint().apply {
                isAntiAlias = true
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            }
            
            val boldPaint = Paint().apply {
                isAntiAlias = true
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }

            var pageNumber = 1
            var pageInfo = PdfDocument.PageInfo.Builder(595, 842, pageNumber).create()
            var page = pdfDocument.startPage(pageInfo)
            var canvas = page.canvas
            
            var y = 40f
            
            fun drawHeader(canvas: Canvas, pNumber: Int) {
                // Header Background Slate Blue
                paint.color = Color.parseColor("#1E293B")
                canvas.drawRect(30f, 30f, 565f, 100f, paint)
                
                // App Logo text
                boldPaint.color = Color.WHITE
                boldPaint.textSize = 16f
                canvas.drawText(contractorName, 50f, 65f, boldPaint)
                
                paint.color = Color.parseColor("#94A3B8")
                paint.textSize = 10f
                canvas.drawText("عرض سعر وتفاصيل المواد الهندسية", 50f, 85f, paint)
                
                // Page Number
                paint.color = Color.WHITE
                paint.textSize = 10f
                canvas.drawText("صفحة $pNumber", 510f, 65f, paint)
            }
            
            drawHeader(canvas, pageNumber)
            y = 130f
            
            // --- Customer & Project Info Block ---
            paint.color = Color.parseColor("#F1F5F9")
            canvas.drawRect(30f, y, 565f, y + 90f, paint)
            
            boldPaint.color = Color.parseColor("#0F172A")
            boldPaint.textSize = 12f
            paint.color = Color.parseColor("#334155")
            paint.textSize = 11f
            
            // Client details (Left column)
            canvas.drawText("تفاصيل العميل / الزبون:", 50f, y + 20f, boldPaint)
            if (customer != null) {
                canvas.drawText("الاسم: ${customer.name}", 50f, y + 40f, paint)
                canvas.drawText("الجوال: ${customer.phone}", 50f, y + 55f, paint)
                canvas.drawText("العنوان: ${customer.address}", 50f, y + 70f, paint)
            } else {
                canvas.drawText("الاسم: عميل مباشر / عام", 50f, y + 40f, paint)
            }
            
            // Project details (Right column)
            val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            canvas.drawText("تفاصيل المشروع:", 350f, y + 20f, boldPaint)
            canvas.drawText("المشروع: ${project.name}", 350f, y + 40f, paint)
            canvas.drawText("نوع المنشأة: ${project.type}", 350f, y + 55f, paint)
            canvas.drawText("التاريخ: ${dateFormat.format(Date(project.createdAt))}", 350f, y + 70f, paint)
            
            y += 110f
            
            // --- Section 1: Rooms Table ---
            boldPaint.color = Color.parseColor("#1E3A8A")
            boldPaint.textSize = 14f
            canvas.drawText("1. تفاصيل المساحات والقياسات (الغرف)", 40f, y, boldPaint)
            y += 15f
            
            // Table Header
            paint.color = Color.parseColor("#E2E8F0")
            canvas.drawRect(30f, y, 565f, y + 25f, paint)
            
            boldPaint.color = Color.parseColor("#1E293B")
            boldPaint.textSize = 10f
            canvas.drawText("المساحة (ط x ع x ق)", 45f, y + 17f, boldPaint)
            canvas.drawText("الجدران (صافي)", 180f, y + 17f, boldPaint)
            canvas.drawText("السقف/الأرضية", 280f, y + 17f, boldPaint)
            canvas.drawText("الفتحات المخصومة", 390f, y + 17f, boldPaint)
            canvas.drawText("التكلفة التقديرية", 480f, y + 17f, boldPaint)
            
            y += 25f
            
            var totalGrossWallArea = 0.0
            var totalNetWallArea = 0.0
            var totalCeilingArea = 0.0
            var totalFlooringArea = 0.0
            var totalOpeningArea = 0.0
            
            var totalWallCost = 0.0
            var totalCeilingCost = 0.0
            var totalFlooringCost = 0.0
            
            paint.textSize = 10f
            paint.color = Color.BLACK
            
            for (room in rooms) {
                // If space runs out, create new page
                if (y > 750f) {
                    pdfDocument.finishPage(page)
                    pageNumber++
                    pageInfo = PdfDocument.PageInfo.Builder(595, 842, pageNumber).create()
                    page = pdfDocument.startPage(pageInfo)
                    canvas = page.canvas
                    drawHeader(canvas, pageNumber)
                    y = 120f
                }
                
                // Draw room title
                boldPaint.color = Color.parseColor("#475569")
                boldPaint.textSize = 10f
                canvas.drawText("■ ${room.name} (${room.length}م × ${room.width}م × ${room.height}م)", 40f, y + 15f, boldPaint)
                
                // Calculations
                val perimeter = 2 * (room.length + room.width)
                val grossWallArea = perimeter * room.height
                val ceilingArea = if (room.hasCeiling) (room.length * room.width) else 0.0
                val flooringArea = if (room.hasFlooring) (room.length * room.width) else 0.0
                
                // Openings deduction
                val roomOpenings = openingsMap[room.id] ?: emptyList()
                var openingArea = 0.0
                for (op in roomOpenings) {
                    openingArea += op.width * op.height * op.quantity
                }
                
                val netWallArea = maxOf(0.0, grossWallArea - openingArea)
                
                totalGrossWallArea += grossWallArea
                totalNetWallArea += netWallArea
                totalCeilingArea += ceilingArea
                totalFlooringArea += flooringArea
                totalOpeningArea += openingArea
                
                // Cost calculation
                val wallCost = netWallArea * room.wallPricePerM2
                val ceilingCost = ceilingArea * room.ceilingPricePerM2
                val flooringCost = flooringArea * room.flooringPricePerM2
                
                totalWallCost += wallCost
                totalCeilingCost += ceilingCost
                totalFlooringCost += flooringCost
                
                val roomTotalCost = wallCost + ceilingCost + flooringCost
                
                paint.color = Color.parseColor("#475569")
                paint.textSize = 9f
                canvas.drawText("جدران: ${String.format(Locale.ENGLISH, "%.1f", grossWallArea)}م²", 180f, y + 15f, paint)
                canvas.drawText("صافي: ${String.format(Locale.ENGLISH, "%.1f", netWallArea)}م²", 180f, y + 27f, paint)
                
                if (room.hasCeiling) {
                    canvas.drawText("سقف: ${String.format(Locale.ENGLISH, "%.1f", ceilingArea)}م²", 280f, y + 15f, paint)
                }
                if (room.hasFlooring) {
                    canvas.drawText("أرضية: ${String.format(Locale.ENGLISH, "%.1f", flooringArea)}م²", 280f, y + 27f, paint)
                }
                
                canvas.drawText("${roomOpenings.size} فتحات (${String.format(Locale.ENGLISH, "%.1f", openingArea)}م²)", 390f, y + 15f, paint)
                
                boldPaint.color = Color.parseColor("#0F172A")
                boldPaint.textSize = 10f
                canvas.drawText("${String.format(Locale.ENGLISH, "%.2f", roomTotalCost)} د.ج", 480f, y + 20f, boldPaint)
                
                // Line separator
                paint.color = Color.parseColor("#F1F5F9")
                canvas.drawLine(30f, y + 35f, 565f, y + 35f, paint)
                
                y += 40f
            }
            
            // If space runs out, create new page
            if (y > 680f) {
                pdfDocument.finishPage(page)
                pageNumber++
                pageInfo = PdfDocument.PageInfo.Builder(595, 842, pageNumber).create()
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                drawHeader(canvas, pageNumber)
                y = 120f
            }
            
            // --- Section 2: Material Requirements Analysis ---
            y += 15f
            boldPaint.color = Color.parseColor("#1E3A8A")
            boldPaint.textSize = 14f
            canvas.drawText("2. تقدير كميات المواد المطلوبة للاستخدام", 40f, y, boldPaint)
            y += 15f
            
            // Calculations for Materials
            var totalPuttyCoatsWeight = 0.0
            var totalPaintLiters = 0.0
            for (room in rooms) {
                val perimeter = 2 * (room.length + room.width)
                val grossWall = perimeter * room.height
                val ops = openingsMap[room.id] ?: emptyList()
                val opA = ops.sumOf { it.width * it.height * it.quantity }
                val netWall = maxOf(0.0, grossWall - opA)
                
                // Putty: 1.2 kg per m2 per coat
                totalPuttyCoatsWeight += netWall * room.puttyCoats * 1.2 * room.materialCostFactor
                
                // Paint: 10 m2 per liter per coat
                val wallPaintLiters = (netWall * room.paintCoats) / 10.0
                val ceilingPaintLiters = if (room.hasCeiling) {
                    ((room.length * room.width) * room.paintCoats) / 10.0
                } else 0.0
                
                totalPaintLiters += (wallPaintLiters + ceilingPaintLiters) * room.materialCostFactor
            }
            
            val puttyBags = Math.ceil(totalPuttyCoatsWeight / 25.0).toInt()
            val paintDrums18L = Math.ceil(totalPaintLiters / 18.0).toInt()
            val sandpaperSheets = Math.ceil((totalNetWallArea + totalCeilingArea) / 8.0).toInt()
            val estimatedDays = Math.ceil((totalNetWallArea + totalCeilingArea + totalFlooringArea) / 25.0).toInt()
            
            // Draw Material cards
            paint.color = Color.parseColor("#F8FAFC")
            canvas.drawRect(30f, y, 565f, y + 80f, paint)
            
            paint.color = Color.parseColor("#475569")
            paint.textSize = 10f
            
            canvas.drawText("• كمية المعجون الإجمالية: ${String.format(Locale.ENGLISH, "%.1f", totalPuttyCoatsWeight)} كجم (حوالي $puttyBags كيس وزن 25 كجم)", 50f, y + 20f, paint)
            canvas.drawText("• كمية الدهان المطلوبة: ${String.format(Locale.ENGLISH, "%.1f", totalPaintLiters)} لتر (حوالي $paintDrums18L برميل سعة 18 لتر)", 50f, y + 38f, paint)
            canvas.drawText("• ورق الصنفرة المطلوب: $sandpaperSheets ورقة (تقديري)", 50f, y + 56f, paint)
            canvas.drawText("• المدة الزمنية المقدرة للعمل: $estimatedDays أيام عمل (بمعدل حرفي واحد)", 50f, y + 74f, paint)
            
            y += 100f
            
            // --- Section 3: Custom Tasks ---
            if (tasks.isNotEmpty()) {
                if (y > 700f) {
                    pdfDocument.finishPage(page)
                    pageNumber++
                    pageInfo = PdfDocument.PageInfo.Builder(595, 842, pageNumber).create()
                    page = pdfDocument.startPage(pageInfo)
                    canvas = page.canvas
                    drawHeader(canvas, pageNumber)
                    y = 120f
                }
                
                boldPaint.color = Color.parseColor("#1E3A8A")
                boldPaint.textSize = 14f
                canvas.drawText("3. أعمال إضافية مخصصة وعقود مصنعية", 40f, y, boldPaint)
                y += 15f
                
                // Table of Tasks
                paint.color = Color.parseColor("#E2E8F0")
                canvas.drawRect(30f, y, 565f, y + 20f, paint)
                
                boldPaint.color = Color.parseColor("#1E293B")
                boldPaint.textSize = 9f
                canvas.drawText("بيان العمل المخصص", 45f, y + 14f, boldPaint)
                canvas.drawText("الوحدة", 220f, y + 14f, boldPaint)
                canvas.drawText("الكمية", 300f, y + 14f, boldPaint)
                canvas.drawText("سعر الوحدة", 380f, y + 14f, boldPaint)
                canvas.drawText("الإجمالي التقديري", 480f, y + 14f, boldPaint)
                
                y += 20f
                
                paint.textSize = 9f
                paint.color = Color.BLACK
                
                var totalTasksCost = 0.0
                for (task in tasks) {
                    if (y > 750f) {
                        pdfDocument.finishPage(page)
                        pageNumber++
                        pageInfo = PdfDocument.PageInfo.Builder(595, 842, pageNumber).create()
                        page = pdfDocument.startPage(pageInfo)
                        canvas = page.canvas
                        drawHeader(canvas, pageNumber)
                        y = 120f
                    }
                    
                    canvas.drawText(task.title, 45f, y + 15f, paint)
                    canvas.drawText(task.unit, 220f, y + 15f, paint)
                    canvas.drawText("${task.quantity}", 300f, y + 15f, paint)
                    canvas.drawText("${task.unitPrice} د.ج", 380f, y + 15f, paint)
                    
                    val cost = task.quantity * task.unitPrice
                    totalTasksCost += cost
                    
                    boldPaint.color = Color.parseColor("#0F172A")
                    boldPaint.textSize = 9f
                    canvas.drawText("${String.format(Locale.ENGLISH, "%.2f", cost)} د.ج", 480f, y + 15f, boldPaint)
                    
                    paint.color = Color.parseColor("#F1F5F9")
                    canvas.drawLine(30f, y + 22f, 565f, y + 22f, paint)
                    y += 25f
                }
                y += 10f
            }
            
            // If space runs out, create new page
            if (y > 620f) {
                pdfDocument.finishPage(page)
                pageNumber++
                pageInfo = PdfDocument.PageInfo.Builder(595, 842, pageNumber).create()
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                drawHeader(canvas, pageNumber)
                y = 120f
            }
            
            // --- Section 4: Totals & Billing ---
            y += 15f
            boldPaint.color = Color.parseColor("#1E3A8A")
            boldPaint.textSize = 14f
            canvas.drawText("4. ملخص الحساب المالي الكلي والضرائب", 40f, y, boldPaint)
            y += 15f
            
            val grossSubtotal = totalWallCost + totalCeilingCost + totalFlooringCost + tasks.sumOf { it.quantity * it.unitPrice }
            val discountAmount = grossSubtotal * (project.discount / 100.0)
            val subtotalAfterDiscount = grossSubtotal - discountAmount
            val taxAmount = subtotalAfterDiscount * (project.taxRate / 100.0)
            val grandTotal = subtotalAfterDiscount + taxAmount
            
            // Drawing the Invoice Block (Modern layout)
            paint.color = Color.parseColor("#F8FAFC")
            canvas.drawRect(250f, y, 565f, y + 120f, paint)
            
            paint.color = Color.parseColor("#334155")
            paint.textSize = 10f
            canvas.drawText("المجموع الفرعي للأعمال:", 270f, y + 20f, paint)
            canvas.drawText("خصم العرض (${project.discount}%):", 270f, y + 40f, paint)
            canvas.drawText("ضريبة القيمة المضافة (${project.taxRate}%):", 270f, y + 60f, paint)
            
            boldPaint.textSize = 10f
            boldPaint.color = Color.parseColor("#0F172A")
            canvas.drawText("${String.format(Locale.ENGLISH, "%.2f", grossSubtotal)} د.ج", 460f, y + 20f, boldPaint)
            canvas.drawText("- ${String.format(Locale.ENGLISH, "%.2f", discountAmount)} د.ج", 460f, y + 40f, boldPaint)
            canvas.drawText("+ ${String.format(Locale.ENGLISH, "%.2f", taxAmount)} د.ج", 460f, y + 60f, boldPaint)
            
            // Divider
            paint.color = Color.parseColor("#CBD5E1")
            canvas.drawLine(260f, y + 75f, 550f, y + 75f, paint)
            
            // Grand Total
            boldPaint.color = Color.parseColor("#1E3A8A")
            boldPaint.textSize = 13f
            canvas.drawText("الإجمالي الكلي المستحق:", 270f, y + 95f, boldPaint)
            canvas.drawText("${String.format(Locale.ENGLISH, "%.2f", grandTotal)} د.ج", 450f, y + 95f, boldPaint)
            
            // Terms and note on left
            paint.color = Color.parseColor("#64748B")
            paint.textSize = 8f
            canvas.drawText("ملاحظات وشروط عامة:", 40f, y + 20f, boldPaint)
            canvas.drawText("• يسري هذا العرض لمدة 30 يوماً من تاريخه.", 40f, y + 35f, paint)
            canvas.drawText("• لا يشمل العرض المواد إلا إذا نص البند على ذلك.", 40f, y + 48f, paint)
            canvas.drawText("• يتم دفع 50% دفعة مقدمة قبل مباشرة العمل.", 40f, y + 61f, paint)
            canvas.drawText("• الحسابات خاضعة للتدقيق الميداني النهائي.", 40f, y + 74f, paint)
            
            // Contractor details at the footer
            if (contractorPhone.isNotEmpty() || contractorAddress.isNotEmpty() || contractorTaxId.isNotEmpty()) {
                var rowY = y + 20f
                boldPaint.color = Color.parseColor("#0F172A")
                boldPaint.textSize = 8f
                canvas.drawText("منفّذ العمل والجهة المصدرة:", 270f, rowY, boldPaint)
                paint.textSize = 8f
                paint.color = Color.parseColor("#334155")
                if (contractorPhone.isNotEmpty()) {
                    rowY += 15f
                    canvas.drawText("• الهاتف: $contractorPhone", 270f, rowY, paint)
                }
                if (contractorAddress.isNotEmpty()) {
                    rowY += 15f
                    canvas.drawText("• المقر: $contractorAddress", 270f, rowY, paint)
                }
                if (contractorTaxId.isNotEmpty()) {
                    rowY += 15f
                    canvas.drawText("• سجل تجاري/رقم ضريبي: $contractorTaxId", 270f, rowY, paint)
                }
            }
            
            y += 140f
            
            // Footnote
            paint.color = Color.parseColor("#475569")
            paint.textSize = 9f
            canvas.drawText("شكرًا لتعاملكم معنا. تطبيق المقدّر الذكي للمحترفين.", 190f, y, paint)
            
            // Finish PDF Document
            pdfDocument.finishPage(page)
            
            // Save to Cache Directory
            val safeFileName = project.name.replace(" ", "_").replace("/", "_")
            val pdfFile = File(context.cacheDir, "Quotation_${safeFileName}_${project.id}.pdf")
            val outputStream = FileOutputStream(pdfFile)
            pdfDocument.writeTo(outputStream)
            
            pdfDocument.close()
            outputStream.close()
            
            // Share the file
            sharePdf(context, pdfFile)
            
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "خطأ في توليد ملف الـ PDF: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    private fun sharePdf(context: Context, file: File) {
        val authority = "${context.packageName}.fileprovider"
        val uri: Uri = FileProvider.getUriForFile(context, authority, file)
        
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "عرض سعر تقديري - المقدّر الذكي")
            putExtra(Intent.EXTRA_TEXT, "السلام عليكم، نرفق لكم عرض السعر التفصيلي وجدول كميات المواد المطلوبة بصيغة PDF.")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        
        context.startActivity(Intent.createChooser(intent, "مشاركة عرض السعر PDF"))
    }
}
