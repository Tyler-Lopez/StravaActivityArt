package com.company.activityart.domain.use_case.athlete

import com.company.activityart.domain.models.Athlete
import com.company.activityart.domain.models.dataExpired
import com.company.activityart.util.Resource
import com.company.activityart.util.Resource.Success
import javax.inject.Inject

/**
 * Use-case to receive the currently authenticated [Athlete] from either
 * local or remote repositories.
 *
 * If an athlete is fetched from the remote repository, they will be
 * inserted into the local repository for future access.
 *
 * @return The currently authenticated [Athlete].
 */
class GetAthleteUseCase @Inject constructor(
    private val getAthleteFromLocalUseCase: GetAthleteFromLocalUseCase,
    private val getAthleteFromRemoteUseCase: GetAthleteFromRemoteUseCase,
    private val insertAthleteUseCase: InsertAthleteUseCase,
) {
    suspend operator fun invoke(
        athleteId: Long,
        code: String
    ): Resource<Athlete> {
        getAthleteFromLocalUseCase(athleteId).apply {
            return when {
                this == null -> getAthleteFromRemoteUseCase(code)
                    .also { if (it is Success) insertAthleteUseCase(it.data) }
                else -> Success(this)
            }
        }
    }
}