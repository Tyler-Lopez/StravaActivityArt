package com.company.activityart.domain.use_case.athlete

import com.company.activityart.domain.models.Athlete
import com.company.activityart.domain.models.dataExpired
import com.company.activityart.domain.use_case.authentication.GetAccessTokenUseCase
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
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val getAthleteFromLocalUseCase: GetAthleteFromLocalUseCase,
    private val getAthleteFromRemoteUseCase: GetAthleteFromRemoteUseCase,
    private val insertAthleteUseCase: InsertAthleteUseCase,
) {
    suspend operator fun invoke(): Resource<Athlete> {
        val accessTokenResponse = getAccessTokenUseCase()
        return (accessTokenResponse as? Success)?.data?.let { localAuth ->
            getAthleteFromLocalUseCase(localAuth.athleteId).run {
                when {
                    this == null -> getAthleteFromRemoteUseCase(localAuth.accessToken)
                        .also { if (it is Success) insertAthleteUseCase(it.data) }
                    else -> Success(this)
                }
            }
        } ?: Resource.Error(message = "Bad Auth") // Todo, improve
    }
}