package com.wealthmetric.app.calculator

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import com.wealthmetric.app.model.RetirementCalculationResult
import com.wealthmetric.app.model.RetirementInputState
import com.wealthmetric.app.model.RetirementStatus
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.pow

object ReportExporter {

    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply {
        maximumFractionDigits = 0
    }

    /**
     * Generates a PDF Report for the current retirement plan calculation
     */
    fun generatePdfReport(
        context: Context,
        inputState: RetirementInputState,
        resultState: RetirementCalculationResult,
        planTitle: String
    ): File {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 dimensions in points (595x842)
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val titlePaint = Paint().apply {
            color = Color.rgb(99, 102, 241) // Indigo Primary
            textSize = 22f
            isFakeBoldText = true
            isAntiAlias = true
        }

        val subtitlePaint = Paint().apply {
            color = Color.rgb(71, 85, 105) // Slate Secondary
            textSize = 12f
            isAntiAlias = true
        }

        val sectionPaint = Paint().apply {
            color = Color.rgb(15, 23, 42) // Slate Dark
            textSize = 14f
            isFakeBoldText = true
            isAntiAlias = true
        }

        val textPaint = Paint().apply {
            color = Color.rgb(30, 41, 59) // Dark Text
            textSize = 11f
            isAntiAlias = true
        }

        val boldTextPaint = Paint().apply {
            color = Color.rgb(30, 41, 59)
            textSize = 11f
            isFakeBoldText = true
            isAntiAlias = true
        }

        val linePaint = Paint().apply {
            color = Color.rgb(226, 232, 240) // Slate Border
            strokeWidth = 1f
        }

        var y = 40f

        // Header Title
        canvas.drawText("GrowMetric - Retirement Wealth Report", 30f, y, titlePaint)
        y += 18f
        canvas.drawText("Simulate. Plan. Prosper. | Plan: $planTitle", 30f, y, subtitlePaint)
        y += 15f

        canvas.drawLine(30f, y, 565f, y, linePaint)
        y += 25f

        // Status Summary Card Box
        val statusBgColor = when (resultState.status) {
            RetirementStatus.ACHIEVED, RetirementStatus.EXACT -> Color.rgb(220, 252, 231) // Light Green
            RetirementStatus.DEFICIT -> Color.rgb(254, 226, 226) // Light Red
        }
        val statusPaint = Paint().apply {
            color = statusBgColor
        }
        canvas.drawRect(30f, y, 565f, y + 45f, statusPaint)

        val statusTextPaint = Paint().apply {
            color = when (resultState.status) {
                RetirementStatus.ACHIEVED, RetirementStatus.EXACT -> Color.rgb(22, 101, 52)
                RetirementStatus.DEFICIT -> Color.rgb(153, 27, 27)
            }
            textSize = 13f
            isFakeBoldText = true
            isAntiAlias = true
        }

        val statusLabel = when (resultState.status) {
            RetirementStatus.ACHIEVED -> "GOAL ACHIEVED (+${currencyFormat.format(resultState.difference)} Surplus/Yr)"
            RetirementStatus.EXACT -> "GOAL EXACTLY ACHIEVED"
            RetirementStatus.DEFICIT -> "DEFICIT WARNING (-${currencyFormat.format(-resultState.difference)} Shortfall/Yr)"
        }
        canvas.drawText(statusLabel, 42f, y + 27f, statusTextPaint)
        y += 65f

        // Input Parameters Section
        canvas.drawText("Plan Assumptions & Inputs", 30f, y, sectionPaint)
        y += 18f

        fun drawRow(label: String, value: String) {
            canvas.drawText(label, 30f, y, textPaint)
            canvas.drawText(value, 350f, y, boldTextPaint)
            y += 18f
        }

        drawRow("Current Age / Target Retirement Age:", "${inputState.currentAge} Yrs / ${inputState.retirementAge} Yrs")
        drawRow("Accumulation Horizon:", "${resultState.yearsRemaining} Years")
        drawRow("Current Accumulated Corpus:", currencyFormat.format(inputState.currentCorpus))
        drawRow("Annual Savings Contribution:", currencyFormat.format(inputState.annualSavings) + "/yr")
        drawRow("Current Annual Expenditure:", currencyFormat.format(inputState.annualExpenditure) + "/yr")
        drawRow("Pre-Retirement Investment Yield:", "${inputState.annualInterest}% p.a.")
        drawRow("Expected Inflation Rate:", "${inputState.annualInflation}% p.a.")
        drawRow("Expected Annuity Rate:", "${inputState.expectedAnnuityRate}% p.a.")
        drawRow("Payout Frequency:", inputState.payoutFrequency.label)

        y += 10f
        canvas.drawLine(30f, y, 565f, y, linePaint)
        y += 25f

        // Calculation Results
        canvas.drawText("Financial Projection at Age ${inputState.retirementAge}", 30f, y, sectionPaint)
        y += 18f

        drawRow("Projected Corpus at Age ${inputState.retirementAge}:", currencyFormat.format(resultState.corpusAtRetirement))
        drawRow("Annual Income Required at Age ${inputState.retirementAge}:", currencyFormat.format(resultState.annualIncomeRequired))
        drawRow("Annual Pension Income from Annuity:", currencyFormat.format(resultState.annualIncomeFromAnnuity))
        drawRow("Net Difference (Annuity - Required):", currencyFormat.format(resultState.difference))

        y += 10f
        canvas.drawLine(30f, y, 565f, y, linePaint)
        y += 25f

        // Recommendations Section
        canvas.drawText("Smart Recommendation to Bridge Gap", 30f, y, sectionPaint)
        y += 18f

        if (resultState.status == RetirementStatus.DEFICIT) {
            val annualDeficit = -resultState.difference
            val annuityRate = inputState.expectedAnnuityRate / 100.0
            val additionalCorpusNeeded = if (annuityRate > 0.0) annualDeficit / annuityRate else 0.0
            val r = inputState.annualInterest / 100.0
            val n = resultState.yearsRemaining

            val compoundingFactor = when {
                n <= 0 -> 1.0
                r > 0.0 -> ((1.0 + r).pow(n.toDouble()) - 1.0) / r
                else -> n.toDouble()
            }

            val addAnnualSavings = if (compoundingFactor > 0.0) additionalCorpusNeeded / compoundingFactor else additionalCorpusNeeded
            val addMonthlySavings = addAnnualSavings / 12.0

            canvas.drawText("• Increase Monthly Savings by ${currencyFormat.format(addMonthlySavings)} (${currencyFormat.format(addAnnualSavings)}/yr)", 30f, y, textPaint)
            y += 16f
            canvas.drawText("  Building an extra ${currencyFormat.format(additionalCorpusNeeded)} corpus over $n years completely eliminates your deficit.", 30f, y, subtitlePaint)
        } else {
            canvas.drawText("• Your projected pension exceeds your inflated target expenditure! Consider lock-in guarantees.", 30f, y, textPaint)
        }

        y += 30f
        canvas.drawText("Report generated automatically by GrowMetric app.", 30f, y, subtitlePaint)

        pdfDocument.finishPage(page)

        val reportsDir = File(context.cacheDir, "reports").apply { mkdirs() }
        val pdfFile = File(reportsDir, "GrowMetric_Retirement_Report.pdf")
        FileOutputStream(pdfFile).use { out ->
            pdfDocument.writeTo(out)
        }
        pdfDocument.close()

        return pdfFile
    }

    /**
     * Generates a CSV Report of the year-by-year annuity schedule matrix
     */
    fun generateCsvReport(
        context: Context,
        resultState: RetirementCalculationResult,
        planTitle: String
    ): File {
        val sb = StringBuilder()
        sb.append("GrowMetric - Retirement Annuity Schedule Matrix\n")
        sb.append("Plan Title,$planTitle\n")
        sb.append("Total Projected Years,${resultState.scheduleRows.size}\n\n")

        sb.append("Year,Age,Phase,Starting Corpus (INR),Interest / Payout (INR),Annual Addition (INR),Ending Corpus (INR),Target Expenditure (INR)\n")

        resultState.scheduleRows.forEach { row ->
            val phase = if (row.isAccumulationPhase) "Accumulation" else "Pension Payout"
            sb.append("${row.year},")
            sb.append("${row.age},")
            sb.append("$phase,")
            sb.append("%.2f,".format(row.startingCorpus))
            sb.append("%.2f,".format(row.interestOrPayout))
            sb.append("%.2f,".format(row.annualAddition))
            sb.append("%.2f,".format(row.endingCorpus))
            sb.append("%.2f\n".format(row.targetExpenditure))
        }

        val reportsDir = File(context.cacheDir, "reports").apply { mkdirs() }
        val csvFile = File(reportsDir, "GrowMetric_Annuity_Schedule.csv")
        csvFile.writeText(sb.toString())

        return csvFile
    }

    /**
     * Triggers Android OS share sheet for the generated file
     */
    fun shareFile(
        context: Context,
        file: File,
        mimeType: String,
        chooserTitle: String
    ) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(intent, chooserTitle).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
    }
}
