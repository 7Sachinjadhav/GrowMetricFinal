package com.wealthmetric.app.model

enum class RetirementPlanType {
    IMMEDIATE_PENSION,
    GUARANTEED_FUTURE_PENSION,
    RETIREMENT_WEALTH_BUILDER,
    FLEXIBLE_PENSION
}

data class RetirementPlanInfo(
    val type: RetirementPlanType,
    val title: String,
    val shortDescription: String,
    val bestFor: String,
    val about: String,
    val howItWorks: String,
    val benefits: List<String>,
    val defaultAnnuityRate: Double
)

object RetirementPlanData {
    val plans = listOf(
        RetirementPlanInfo(
            type = RetirementPlanType.IMMEDIATE_PENSION,
            title = "Immediate Pension Plan",
            shortDescription = "Invest your retirement savings once in Rupees (₹) and start receiving a regular monthly/annual pension immediately.",
            bestFor = "People who have already retired or have a retirement corpus ready in Rupees (₹).",
            about = "The Immediate Pension Plan allows you to convert a lump-sum corpus in Rupees (₹) into an immediate lifetime pension stream. Once initialized, annuity payouts begin immediately without any waiting period.",
            howItWorks = "You invest your accumulated retirement savings as a single lump sum in Rupees (₹). The annuity provider calculates your regular pension based on the prevailing rate (default 6.8%) and starts disbursing payments right away.",
            benefits = listOf(
                "Immediate financial security with guaranteed regular cash flows in Rupees (₹).",
                "Zero accumulation waiting period – income starts right after setup.",
                "Hassle-free management with fixed or escalating pension options."
            ),
            defaultAnnuityRate = 6.8
        ),
        RetirementPlanInfo(
            type = RetirementPlanType.GUARANTEED_FUTURE_PENSION,
            title = "Guaranteed Future Pension Plan",
            shortDescription = "Invest a lump sum in Rupees (₹) today. Your money grows until retirement and then starts providing a regular pension.",
            bestFor = "People who already have savings and are planning for retirement.",
            about = "Designed for individuals with existing savings in Rupees (₹) who wish to lock in future pension income. Your principal grows compound interest until your chosen retirement age, after which annuity payments trigger automatically.",
            howItWorks = "Your lump-sum investment compounds annually in Rupees (₹) until retirement. At retirement, the entire accumulated corpus converts into a lifetime annuity generating a regular annual pension (default rate 6.6%).",
            benefits = listOf(
                "Guaranteed compound growth on your existing savings until retirement.",
                "Protects your future lifestyle against rising living expenses (inflation).",
                "Offers clarity and confidence in setting retirement targets in Rupees (₹)."
            ),
            defaultAnnuityRate = 6.6
        ),
        RetirementPlanInfo(
            type = RetirementPlanType.RETIREMENT_WEALTH_BUILDER,
            title = "Retirement Wealth Builder",
            shortDescription = "Start with your existing savings in Rupees (₹) and continue investing every year until retirement. The total corpus is then used to generate pension.",
            bestFor = "Working professionals planning long-term retirement.",
            about = "Ideal for active earners looking to systematically build a formidable retirement nest egg over time in Rupees (₹). Combines existing capital with compounded annual savings up to your target retirement age.",
            howItWorks = "Your existing corpus and ongoing annual contributions grow at the expected return rate until retirement age. Upon reaching retirement, the final corpus is deployed into an annuity yielding predictable yearly income in Rupees (₹) (default rate 6.7%).",
            benefits = listOf(
                "Disciplined wealth creation strategy for mid-career professionals.",
                "Maximizes compounding benefits over remaining working years.",
                "Generates a robust pension stream to match future living expenses in Rupees (₹)."
            ),
            defaultAnnuityRate = 6.7
        ),
        RetirementPlanInfo(
            type = RetirementPlanType.FLEXIBLE_PENSION,
            title = "Flexible Pension Plan",
            shortDescription = "Build your retirement corpus in Rupees (₹) and choose how you want to receive your pension after retirement (monthly, quarterly, half-yearly or yearly).",
            bestFor = "People who want flexibility in retirement income.",
            about = "Provides maximum customization over how and when you receive payouts after retirement. Adjust payment frequency to match your personal lifestyle and recurring bill schedules.",
            howItWorks = "Accumulate savings until retirement age. Post-retirement, your corpus generates pension at a baseline rate (default 6.5%), and you choose monthly, quarterly, half-yearly, or annual disbursement in Rupees (₹).",
            benefits = listOf(
                "Customizable payout frequencies: Monthly, Quarterly, Half-Yearly, or Yearly in Rupees (₹).",
                "Adaptable to changing post-retirement liquidity needs.",
                "Combines safety of guaranteed returns with flexible cash flow schedules."
            ),
            defaultAnnuityRate = 6.5
        )
    )

    fun getPlan(type: RetirementPlanType): RetirementPlanInfo {
        return plans.first { it.type == type }
    }

    fun createMutualFundPlanInfo(fund: MutualFundData): RetirementPlanInfo {
        return RetirementPlanInfo(
            type = RetirementPlanType.RETIREMENT_WEALTH_BUILDER,
            title = "${fund.fundName} (${fund.cagr5Year}% p.a.)",
            shortDescription = "Invest systematically or lump-sum in ${fund.fundName} with a 5-Year historical CAGR of ${fund.cagr5Year}% p.a.",
            bestFor = "Investors seeking market-linked wealth compounding under ${fund.category} (${fund.amcProvider.displayName}).",
            about = "${fund.aboutText} Historical 5-Year CAGR return is ${fund.cagr5Year}% p.a. with 1-Year return of ${fund.cagr1Year}% p.a.",
            howItWorks = "Your capital and annual contributions compound at the fund's 5-Year historical yield rate of ${fund.cagr5Year}% p.a. until your target retirement age. At retirement, the final accumulated corpus is deployed into an annuity producing regular income in Rupees (₹).",
            benefits = listOf(
                "High return potential with 5-Year historical CAGR of ${fund.cagr5Year}% p.a.",
                "Managed by ${fund.amcProvider.displayName} under ${fund.category} regulations.",
                "Full integration with retirement goal planning, current corpus, and annual expenditure calculations."
            ),
            defaultAnnuityRate = fund.cagr5Year
        )
    }
}

enum class PayoutFrequency(val label: String, val perYearMultiplier: Int) {
    MONTHLY("Monthly", 12),
    QUARTERLY("Quarterly", 4),
    HALF_YEARLY("Half-Yearly", 2),
    YEARLY("Yearly", 1)
}

data class RetirementInputState(
    val currentAge: Int = 35,
    val retirementAge: Int = 60,
    val currentCorpus: Double = 2500000.0, // ₹ 25,00,000 default corpus
    val annualSavings: Double = 100000.0, // ₹ 1,00,000 / year additional annual contribution
    val annualExpenditure: Double = 600000.0, // ₹ 6,00,000 default annual expenditure
    val annualInterest: Double = 7.0,
    val annualInflation: Double = 6.0,
    val expectedAnnuityRate: Double = 6.8,
    val payoutFrequency: PayoutFrequency = PayoutFrequency.YEARLY
)

enum class RetirementStatus {
    ACHIEVED,
    EXACT,
    DEFICIT
}

data class AnnuityScheduleRow(
    val year: Int,
    val age: Int,
    val isAccumulationPhase: Boolean,
    val startingCorpus: Double,
    val interestOrPayout: Double,
    val annualAddition: Double,
    val endingCorpus: Double,
    val targetExpenditure: Double
)

data class RetirementCalculationResult(
    val yearsRemaining: Int,
    val corpusAtRetirement: Double,
    val annualIncomeRequired: Double,
    val annualIncomeFromAnnuity: Double,
    val difference: Double,
    val status: RetirementStatus,
    val periodPensionAmount: Double,
    val inflationMultiplier: Double,
    val scheduleRows: List<AnnuityScheduleRow> = emptyList()
)
