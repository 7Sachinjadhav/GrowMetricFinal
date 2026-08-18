package com.wealthmetric.app

import com.wealthmetric.app.model.AmcProvider
import com.wealthmetric.app.model.PreconfiguredMutualFunds
import com.wealthmetric.app.repository.MutualFundRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MutualFundRepositoryTest {

    @Test
    fun testPreconfiguredFundsList() {
        val sbiFunds = PreconfiguredMutualFunds.getFundsForAmc(AmcProvider.SBI)
        assertEquals(5, sbiFunds.size)

        val iciciFunds = PreconfiguredMutualFunds.getFundsForAmc(AmcProvider.ICICI)
        assertEquals(5, iciciFunds.size)

        val licFunds = PreconfiguredMutualFunds.getFundsForAmc(AmcProvider.LIC)
        assertEquals(5, licFunds.size)
    }

    @Test
    fun testIciciEvifFundCodeLookup() {
        val evifFund = PreconfiguredMutualFunds.getFundByCode("EVIF")
        assertNotNull(evifFund)
        assertEquals("ICICI Prudential Bluechip Fund Direct Growth", evifFund?.fundName)
        assertEquals(AmcProvider.ICICI, evifFund?.amcProvider)
    }

    @Test
    fun testFetchFundWithNavHistoryFallback() = runBlocking {
        val fund = PreconfiguredMutualFunds.allFunds.first()
        val fetchedFund = MutualFundRepository.fetchFundWith5YearNavHistory(fund)

        assertNotNull(fetchedFund)
        assertTrue(fetchedFund.currentNav > 0)
        assertTrue(fetchedFund.navHistory5Y.isNotEmpty())
    }
}
