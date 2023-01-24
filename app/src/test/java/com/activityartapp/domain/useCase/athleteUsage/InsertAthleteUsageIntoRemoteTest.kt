package com.activityartapp.domain.useCase.athleteUsage

import com.activityartapp.data.repository.FakeAthleteUsageRepository
import com.activityartapp.util.Response
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class InsertAthleteUsageIntoRemoteTest {

    companion object {
        private const val ATHLETE_ID = 0L
    }

    private lateinit var getAthleteUsageFromRemote: GetAthleteUsageFromRemote
    private lateinit var insertAthleteUsageIntoRemote: InsertAthleteUsageIntoRemote
    private lateinit var fakeAthleteUsageRepository: FakeAthleteUsageRepository

    @Before
    fun setUp() {
        fakeAthleteUsageRepository = FakeAthleteUsageRepository()
        getAthleteUsageFromRemote = GetAthleteUsageFromRemote(fakeAthleteUsageRepository)
        insertAthleteUsageIntoRemote = InsertAthleteUsageIntoRemote(fakeAthleteUsageRepository)
    }

    @Test
    fun `Inserting usage of 5, 7, 8, and 9 sequentially results in 9 for the same athlete`() = runBlocking {

        val firstValue = 5
        val lastValue = 9

        (firstValue..lastValue).forEach {
            insertAthleteUsageIntoRemote(ATHLETE_ID, it)
        }

        val usage = (getAthleteUsageFromRemote(ATHLETE_ID) as? Response.Success)!!.data
        assert(usage == lastValue)
    }

    @Test
    fun `Inserting usage of 5, 7, 8, and 9 should set usage to  5, 7, 8, and finally 9`() = runBlocking {

        val firstValue = 5
        val lastValue = 9

        (firstValue..lastValue).forEach {
            insertAthleteUsageIntoRemote(ATHLETE_ID, it)
            val usage = (getAthleteUsageFromRemote(ATHLETE_ID) as? Response.Success)!!.data
            assert(usage == it)
        }
    }
}