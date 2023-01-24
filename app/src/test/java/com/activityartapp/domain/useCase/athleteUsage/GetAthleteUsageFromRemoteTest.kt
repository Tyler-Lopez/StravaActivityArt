package com.activityartapp.domain.useCase.athleteUsage

import com.activityartapp.data.repository.FakeAthleteUsageRepository
import com.activityartapp.domain.repository.AthleteUsageRepository
import com.activityartapp.util.Response
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetAthleteUsageFromRemoteTest {

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
    fun `Getting usage when value is null returns the NO_ATHLETE_USAGE constant`() = runBlocking {
        val usage = (getAthleteUsageFromRemote(ATHLETE_ID) as? Response.Success)!!.data
        assert(usage == AthleteUsageRepository.NO_USAGE_FOUND)
    }

    @Test
    fun `Getting usage after inserting 5 returns 5`() = runBlocking {
        val newUsage = 5
        insertAthleteUsageIntoRemote(ATHLETE_ID, newUsage)
        val usage = (getAthleteUsageFromRemote(ATHLETE_ID) as? Response.Success)!!.data
        assert(usage == newUsage)
    }
}