# Strava Print Visualizer
An Android application which will leverage Strava API to generate aesthetic visualizations of athelete's workouts.

This will be a significant learning experience to understand working with API in Android.

General idea:

User connects with Strava

User selects which activities to visualize by choosing filters.
* FILTER by a certain type (run/cycle)
* FILTER by a certain distance
* FILTER by a certain time (last week, last month, last year, etc)
* FILTER by a certain gear
* Select manually whichever to export

User chooses activity coloring
* Color by a single color
* Color activities by distance (Red --> Green) or other coditional formatting gradients
* Color by gear
* Color by kudos

User adds text

User saves image of resolution appropriate for 18x24 printing


http://www.strava.com/oauth/authorize?client_id=75992&response_type=code&redirect_uri=http://localhost/exchange_token&approval_prompt=force&scope=read_all

## References
https://github.com/SweetzpotAS/StravaZpot-Android<br>
https://www.youtube.com/watch?v=sgscChKfGyg<br>
https://developers.strava.com/docs/authentication/<br>
https://www.youtube.com/watch?v=t6Sql3WMAnk "The Ultimate Retrofit Crash Course"<br>
