package com.wealthmetric.app.model

enum class AmcProvider(val displayName: String, val shortName: String) {
    SBI("SBI Mutual Fund", "SBI"),
    ICICI("ICICI Prudential MF", "ICICI"),
    LIC("LIC Mutual Fund", "LIC")
}

data class NavPoint(
    val date: String,
    val nav: Double
)

data class AssetAllocation(
    val equityPercent: Double,
    val debtPercent: Double = 0.0,
    val cashPercent: Double = 0.0
)

data class MutualFundData(
    val schemeCode: String,
    val sfinCode: String,
    val amcProvider: AmcProvider,
    val fundName: String,
    val category: String,
    val benchmarkIndex: String,
    val launchDate: String = "01-Jan-2013",
    val inceptionNav: Double = 10.0,
    val currentNav: Double,
    val navDate: String,
    val dailyChangePercent: Double,
    val cagr1Year: Double,
    val cagr3Year: Double,
    val cagr5Year: Double,
    val sinceInceptionReturn: Double,
    val assetAllocation: AssetAllocation,
    val aboutText: String,
    val navHistory5Y: List<NavPoint> = emptyList()
)

object PreconfiguredMutualFunds {

    val allFunds: List<MutualFundData> = listOf(
        // ==================== SBI MUTUAL FUND TOP 5 ====================
        MutualFundData(
            schemeCode = "119551",
            sfinCode = "SBI-BCF-DG",
            amcProvider = AmcProvider.SBI,
            fundName = "SBI Bluechip Fund Direct Growth",
            category = "Large Cap Fund",
            benchmarkIndex = "Nifty 50 TRI",
            launchDate = "01-Jan-2013",
            currentNav = 84.62,
            navDate = "Latest",
            dailyChangePercent = +0.42,
            cagr1Year = 18.5,
            cagr3Year = 14.8,
            cagr5Year = 15.2,
            sinceInceptionReturn = 15.8,
            assetAllocation = AssetAllocation(equityPercent = 94.8, cashPercent = 5.2),
            aboutText = "Invests primarily in top 100 large-cap bluechip stocks in India, focusing on steady capital growth and bluechip market leaders."
        ),
        MutualFundData(
            schemeCode = "125497",
            sfinCode = "SBI-SCF-DG",
            amcProvider = AmcProvider.SBI,
            fundName = "SBI Small Cap Fund Direct Growth",
            category = "Small Cap Fund",
            benchmarkIndex = "BSE 250 SmallCap TRI",
            launchDate = "01-Jan-2013",
            currentNav = 168.45,
            navDate = "Latest",
            dailyChangePercent = +0.85,
            cagr1Year = 28.2,
            cagr3Year = 22.4,
            cagr5Year = 26.4,
            sinceInceptionReturn = 24.1,
            assetAllocation = AssetAllocation(equityPercent = 91.5, cashPercent = 8.5),
            aboutText = "A high-growth small-cap fund seeking long-term capital appreciation by investing in emerging future leaders."
        ),
        MutualFundData(
            schemeCode = "119579",
            sfinCode = "SBI-FEF-DG",
            amcProvider = AmcProvider.SBI,
            fundName = "SBI Focused Equity Fund Direct Growth",
            category = "Focused Fund (Max 30 Stocks)",
            benchmarkIndex = "BSE 500 TRI",
            launchDate = "01-Jan-2013",
            currentNav = 312.18,
            navDate = "Latest",
            dailyChangePercent = -0.15,
            cagr1Year = 19.4,
            cagr3Year = 15.6,
            cagr5Year = 16.8,
            sinceInceptionReturn = 17.2,
            assetAllocation = AssetAllocation(equityPercent = 95.8, cashPercent = 4.2),
            aboutText = "A conviction-driven portfolio limited to maximum 30 high-potential growth stocks across sectors."
        ),
        MutualFundData(
            schemeCode = "119598",
            sfinCode = "SBI-CTF-DG",
            amcProvider = AmcProvider.SBI,
            fundName = "SBI Contra Fund Direct Growth",
            category = "Value / Contra Fund",
            benchmarkIndex = "BSE 500 TRI",
            launchDate = "01-Jan-2013",
            currentNav = 364.92,
            navDate = "Latest",
            dailyChangePercent = +1.12,
            cagr1Year = 38.6,
            cagr3Year = 27.5,
            cagr5Year = 24.1,
            sinceInceptionReturn = 18.5,
            assetAllocation = AssetAllocation(equityPercent = 88.6, cashPercent = 11.4),
            aboutText = "Follows a contrarian investment strategy, investing in undervalued out-of-favor companies poised for multi-year turnaround."
        ),
        MutualFundData(
            schemeCode = "119565",
            sfinCode = "SBI-ELSS-DG",
            amcProvider = AmcProvider.SBI,
            fundName = "SBI Long Term Equity Fund (ELSS) Direct Growth",
            category = "Tax Saver ELSS (3Y Lock-in)",
            benchmarkIndex = "BSE 500 TRI",
            launchDate = "01-Jan-2013",
            currentNav = 398.20,
            navDate = "Latest",
            dailyChangePercent = +0.55,
            cagr1Year = 24.5,
            cagr3Year = 18.2,
            cagr5Year = 17.5,
            sinceInceptionReturn = 16.9,
            assetAllocation = AssetAllocation(equityPercent = 96.5, cashPercent = 3.5),
            aboutText = "Combines dual benefits of tax savings under Section 80C with long-term equity wealth generation."
        ),

        // ==================== ICICI PRUDENTIAL MF TOP 5 ====================
        MutualFundData(
            schemeCode = "120586",
            sfinCode = "EVIF",
            amcProvider = AmcProvider.ICICI,
            fundName = "ICICI Prudential Bluechip Fund Direct Growth",
            category = "Large Cap Fund",
            benchmarkIndex = "Nifty 50 TRI",
            launchDate = "01-Jan-2013",
            currentNav = 112.34,
            navDate = "Latest",
            dailyChangePercent = +0.68,
            cagr1Year = 22.4,
            cagr3Year = 17.8,
            cagr5Year = 16.5,
            sinceInceptionReturn = 16.2,
            assetAllocation = AssetAllocation(equityPercent = 93.8, cashPercent = 6.2),
            aboutText = "ICICI Prudential flagship large-cap equity fund investing in market leaders with strong corporate governance and cash flows."
        ),
        MutualFundData(
            schemeCode = "120594",
            sfinCode = "ICICI-EDF-DG",
            amcProvider = AmcProvider.ICICI,
            fundName = "ICICI Prudential Equity & Debt Fund Direct Growth",
            category = "Aggressive Hybrid Fund",
            benchmarkIndex = "Nifty 50 Hybrid Composite 65:35",
            launchDate = "01-Jan-2013",
            currentNav = 345.80,
            navDate = "Latest",
            dailyChangePercent = +0.32,
            cagr1Year = 26.5,
            cagr3Year = 20.8,
            cagr5Year = 18.9,
            sinceInceptionReturn = 17.4,
            assetAllocation = AssetAllocation(equityPercent = 68.2, debtPercent = 26.5, cashPercent = 5.3),
            aboutText = "Maintains a balanced blend of 65-75% equities for growth and 25-35% high-grade fixed income bonds for stability."
        ),
        MutualFundData(
            schemeCode = "120621",
            sfinCode = "ICICI-VDF-DG",
            amcProvider = AmcProvider.ICICI,
            fundName = "ICICI Prudential Value Discovery Fund Direct Growth",
            category = "Value Fund",
            benchmarkIndex = "Nifty 500 TRI",
            launchDate = "01-Jan-2013",
            currentNav = 432.10,
            navDate = "Latest",
            dailyChangePercent = +0.95,
            cagr1Year = 29.8,
            cagr3Year = 23.4,
            cagr5Year = 21.4,
            sinceInceptionReturn = 19.1,
            assetAllocation = AssetAllocation(equityPercent = 87.5, debtPercent = 4.2, cashPercent = 8.3),
            aboutText = "Identifies high quality businesses available at significant discount to their intrinsic value for superior long-term returns."
        ),
        MutualFundData(
            schemeCode = "120597",
            sfinCode = "ICICI-LMF-DG",
            amcProvider = AmcProvider.ICICI,
            fundName = "ICICI Prudential Large & Mid Cap Fund Direct Growth",
            category = "Large & Mid Cap Fund",
            benchmarkIndex = "Nifty LargeMidcap 250 TRI",
            launchDate = "01-Jan-2013",
            currentNav = 85.90,
            navDate = "Latest",
            dailyChangePercent = +0.45,
            cagr1Year = 25.1,
            cagr3Year = 19.6,
            cagr5Year = 19.2,
            sinceInceptionReturn = 17.8,
            assetAllocation = AssetAllocation(equityPercent = 94.6, cashPercent = 5.4),
            aboutText = "Combines stability of large-cap leaders (35%+) with dynamic upside growth potential of mid-cap companies (35%+)."
        ),
        MutualFundData(
            schemeCode = "120614",
            sfinCode = "ICICI-SCF-DG",
            amcProvider = AmcProvider.ICICI,
            fundName = "ICICI Prudential Smallcap Fund Direct Growth",
            category = "Small Cap Fund",
            benchmarkIndex = "Nifty Smallcap 250 TRI",
            launchDate = "01-Jan-2013",
            currentNav = 92.40,
            navDate = "Latest",
            dailyChangePercent = +0.72,
            cagr1Year = 27.6,
            cagr3Year = 21.9,
            cagr5Year = 24.8,
            sinceInceptionReturn = 20.5,
            assetAllocation = AssetAllocation(equityPercent = 90.8, cashPercent = 9.2),
            aboutText = "Invests in high-potential small businesses with strong market share expansion and earnings acceleration."
        ),

        // ==================== LIC MUTUAL FUND TOP 5 ====================
        MutualFundData(
            schemeCode = "119717",
            sfinCode = "LIC-LCF-DG",
            amcProvider = AmcProvider.LIC,
            fundName = "LIC MF Large Cap Fund Direct Growth",
            category = "Large Cap Fund",
            benchmarkIndex = "Nifty 50 TRI",
            launchDate = "01-Jan-2013",
            currentNav = 52.80,
            navDate = "Latest",
            dailyChangePercent = +0.28,
            cagr1Year = 16.4,
            cagr3Year = 13.2,
            cagr5Year = 13.8,
            sinceInceptionReturn = 13.5,
            assetAllocation = AssetAllocation(equityPercent = 95.5, cashPercent = 4.5),
            aboutText = "Backed by LIC heritage, focusing on steady capital preservation and long-term capital growth in India's top 100 bluechips."
        ),
        MutualFundData(
            schemeCode = "119728",
            sfinCode = "LIC-FCF-DG",
            amcProvider = AmcProvider.LIC,
            fundName = "LIC MF Flexi Cap Fund Direct Growth",
            category = "Flexi Cap Fund",
            benchmarkIndex = "Nifty 500 TRI",
            launchDate = "01-Jan-2013",
            currentNav = 89.60,
            navDate = "Latest",
            dailyChangePercent = +0.50,
            cagr1Year = 18.2,
            cagr3Year = 14.1,
            cagr5Year = 14.9,
            sinceInceptionReturn = 14.2,
            assetAllocation = AssetAllocation(equityPercent = 92.8, cashPercent = 7.2),
            aboutText = "Dynamically shifts investments across large, mid, and small-cap opportunities based on evolving macro market trends."
        ),
        MutualFundData(
            schemeCode = "119736",
            sfinCode = "LIC-INF-DG",
            amcProvider = AmcProvider.LIC,
            fundName = "LIC MF Infrastructure Fund Direct Growth",
            category = "Sectoral / Thematic Infrastructure",
            benchmarkIndex = "Nifty Infrastructure TRI",
            launchDate = "01-Jan-2013",
            currentNav = 48.90,
            navDate = "Latest",
            dailyChangePercent = +1.25,
            cagr1Year = 39.5,
            cagr3Year = 25.8,
            cagr5Year = 22.1,
            sinceInceptionReturn = 15.6,
            assetAllocation = AssetAllocation(equityPercent = 91.2, cashPercent = 8.8),
            aboutText = "Captures long-term growth opportunities in India's nation-building sectors including capital goods, energy, power, and logistics."
        ),
        MutualFundData(
            schemeCode = "119725",
            sfinCode = "LIC-LMF-DG",
            amcProvider = AmcProvider.LIC,
            fundName = "LIC MF Large & Mid Cap Fund Direct Growth",
            category = "Large & Mid Cap Fund",
            benchmarkIndex = "Nifty LargeMidcap 250 TRI",
            launchDate = "01-Jan-2013",
            currentNav = 38.40,
            navDate = "Latest",
            dailyChangePercent = +0.38,
            cagr1Year = 21.0,
            cagr3Year = 15.9,
            cagr5Year = 16.2,
            sinceInceptionReturn = 14.8,
            assetAllocation = AssetAllocation(equityPercent = 93.9, cashPercent = 6.1),
            aboutText = "Maintains disciplined allocation across market-leading large caps and agile mid-caps for balanced risk-adjusted growth."
        ),
        MutualFundData(
            schemeCode = "119711",
            sfinCode = "LIC-ELSS-DG",
            amcProvider = AmcProvider.LIC,
            fundName = "LIC MF Tax Saver Fund Direct Growth",
            category = "Tax Saver ELSS (3Y Lock-in)",
            benchmarkIndex = "Nifty 500 TRI",
            launchDate = "01-Jan-2013",
            currentNav = 145.20,
            navDate = "Latest",
            dailyChangePercent = +0.40,
            cagr1Year = 17.5,
            cagr3Year = 13.0,
            cagr5Year = 13.5,
            sinceInceptionReturn = 13.9,
            assetAllocation = AssetAllocation(equityPercent = 97.2, cashPercent = 2.8),
            aboutText = "Combines 80C income tax deduction benefits with wealth building through equity market participation."
        )
    )

    fun getFundsForAmc(amc: AmcProvider): List<MutualFundData> {
        return allFunds.filter { it.amcProvider == amc }
    }

    fun getFundByCode(code: String): MutualFundData? {
        return allFunds.firstOrNull { it.schemeCode == code || it.sfinCode == code }
    }
}
