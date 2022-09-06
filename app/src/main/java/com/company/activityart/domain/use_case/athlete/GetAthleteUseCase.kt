package com.company.activityart.domain.use_case.athlete

import com.company.activityart.domain.models.Athlete
import com.company.activityart.domain.models.dataExpired
import com.company.activityart.domain.use_case.authentication.ClearAccessTokenUseCase
import com.company.activityart.domain.use_case.authentication.GetAccessTokenUseCase
import com.company.activityart.util.Resource
import com.company.activityart.util.Resource.Success
import javax.inject.Inject

/**
 * Use-case to receive the currently authenticated [Athlete] from either
 * local or remote repositories.
 *
 * Fetched athlete data will be inserted into the local repository as cache.
 *
 * If an error occurs accessing the authentication repository, an [Error]
 * will be returned and the existing authentication entry will be removed
 * from the repository.
 *
 * @return The currently authenticated [Athlete], wrapped either as [Success] or [Error].
 */
class GetAthleteUseCase @Inject constructor(
    private val getAthleteFromLocalUseCase: GetAthleteFromLocalUseCase,
    private val getAthleteFromRemoteUseCase: GetAthleteFromRemoteUseCase,
    private val insertAthleteUseCase: InsertAthleteUseCase,
    private val clearAccessTokenUseCase: ClearAccessTokenUseCase
) {
    suspend operator fun invoke(
        athleteId: Long,
        accessToken: String
    ): Resource<Athlete> {
        return getAthleteFromLocalUseCase(athleteId).run {
            when {
                this == null -> getAthleteFromRemoteUseCase(accessToken)
                else -> Success(this)
            }
        }
            .doOnSuccess {
                insertAthleteUseCase(data)
            }
            .doOnError {
                clearAccessTokenUseCase()
            }
    }
}

