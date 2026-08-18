package com.wealthmetric.app.model

data class AnnuityProviderInfo(
    val id: String,
    val providerName: String,
    val planName: String,
    val annuityRate: Double,
    val minCorpusRupees: Double,
    val highlights: String,
    val rating: String,
    val badgeTag: String? = null
)

object AnnuityProviderData {
    val topProviders = listOf(
        AnnuityProviderInfo(
            id = "hdfc_pension",
            providerName = "HDFC Life",
            planName = "Systematic Pension Plan",
            annuityRate = 7.15,
            minCorpusRupees = 500000.0,
            highlights = "Guaranteed lifelong income with return of purchase price to nominees.",
            rating = "4.9 ★",
            badgeTag = "Highest Yield"
        ),
        AnnuityProviderInfo(
            id = "icici_pru",
            providerName = "ICICI Prudential",
            planName = "Guaranteed Pension Plan",
            annuityRate = 7.10,
            minCorpusRupees = 250000.0,
            highlights = "Early rate lock-in options with increasing annuity payout choices.",
            rating = "4.8 ★",
            badgeTag = "Top Rated"
        ),
        AnnuityProviderInfo(
            id = "max_life",
            providerName = "Max Life Insurance",
            planName = "Guaranteed Lifetime Income",
            annuityRate = 7.05,
            minCorpusRupees = 300000.0,
            highlights = "Joint life cover options ensuring continuous pension for spouse.",
            rating = "4.7 ★",
            badgeTag = "Flexible Pay"
        ),
        AnnuityProviderInfo(
            id = "lic_india",
            providerName = "LIC of India",
            planName = "Jeevan Shanti / Jeevan Akshay",
            annuityRate = 6.95,
            minCorpusRupees = 150000.0,
            highlights = "Sovereign guarantee by Govt. of India for absolute capital safety.",
            rating = "4.9 ★",
            badgeTag = "Sovereign Trust"
        ),
        AnnuityProviderInfo(
            id = "sbi_life",
            providerName = "SBI Life",
            planName = "Smart Annuity Plan",
            annuityRate = 6.85,
            minCorpusRupees = 200000.0,
            highlights = "Seamless integration with SBI savings accounts and instant pension credit.",
            rating = "4.6 ★",
            badgeTag = "Bank Partner"
        )
    )
}
