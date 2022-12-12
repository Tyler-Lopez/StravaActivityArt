package com.company.activityart.util

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

data class NavArgSpecification(
        val name: String,
        val navType: NavType<*>
    ) {

        val navArg: NamedNavArgument = navArgument(name = name) {
            this.type = navType
        }

        val key: String = navArg.name
        val route: String = "$key={$key}"
    }

