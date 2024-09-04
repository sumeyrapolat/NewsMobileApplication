package com.example.newsmobileapplication.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

//application colors
val Pink =Color(0xFFED7299)
val PastelYellow = Color(0xFFF1FDB0)
val Orange =  Color(0xFFF6CA52)
val LightYellow = Color(0xFFFFFFDF)
val White = Color(0xFFFFFFFF)

val SoftPink = Color(0xFFFFB8E1)


val cardColor = Color(0xFFFFF8F9)
val SoftBlue = Color(0xFFD1D6FF)
val LightPink = Color(0xFFFFF5FA)
val LightOrange = Color(0xFFFFEEA3)
val SoftGreen = Color(0xFFF1FDB0)

val LightPastelPink = Color(0xFFFDC9EC)
val PastelPink = Color(0xFFFDB5E5)
val MediumPink = Color(0xFFF59AE9)
val DarkerPastelPink = Color(0xFFFF8CD9)

val LightPurple = Color(0xFFF6DDFF)
val MediumPurple = Color(0xFFDD9AF5)
val DarkerLightPurple = Color(0xFFA573CA)
val DarkerPurple = Color(0xFFA573CA)


val LightBlue = Color(0xFFB8CEFF)
val DarkerSoftBlue = Color(0xFFB7CDFF)
val MediumBlue = Color(0xFFA0F1EA)


val categoryGradientColor = Brush.linearGradient(
    colors = listOf(
        MediumPurple,
        MediumBlue,
    ),
    start = Offset(0f, 0f),
    end = Offset( Float.POSITIVE_INFINITY,0f),
    tileMode = TileMode.Clamp
)

val cardGradientColor = Brush.linearGradient(
    colors = listOf(
        MediumPurple,
        MediumPink,
        MediumBlue,
        MediumPink,
        MediumPurple
    ),
    start = Offset(0f, 0f),
    end = Offset(0f,Float.POSITIVE_INFINITY),
    tileMode = TileMode.Clamp
)

val wordCardGradientColor = Brush.linearGradient(
    colors = listOf(
        MediumPurple,
        PastelPink,
        MediumBlue,
        PastelPink,
        MediumPurple
    ),
    start = Offset(0f, 0f),
    end = Offset(0f,Float.POSITIVE_INFINITY),
    tileMode = TileMode.Clamp
)

val backCarGradientColor = Brush.linearGradient(
    colors = listOf(
        MediumPurple.copy(0.7f),
        PastelPink.copy(0.7f),
        MediumBlue.copy(0.7f),
        PastelPink.copy(0.7f),
        MediumPurple.copy(0.7f)
    ),
    start = Offset(0f, 0f),
    end = Offset(0f,Float.POSITIVE_INFINITY),
    tileMode = TileMode.Clamp
)

val bottomGradient = Brush.linearGradient(
    colors = listOf(
        MediumPurple,
        PastelPink,
        PastelPink,
        MediumPurple
    ),
    start = Offset(0f, 0f),
    end = Offset( Float.POSITIVE_INFINITY,0f),
    tileMode = TileMode.Clamp
)


val drawerGradientColor = Brush.linearGradient(
    colors = listOf(
        LightPurple,
        PastelPink,
        LightYellow,
        PastelPink,
        LightPurple
    ),
    start = Offset(0f, 0f),
    end = Offset(0f, Float.POSITIVE_INFINITY),
    tileMode = TileMode.Clamp
)


val addStoryCardColor = Brush.linearGradient(
    colors = listOf(
        LightPurple,
        PastelPink,
        LightYellow,
        PastelPink,
        LightPurple
    ),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, 0f),
    tileMode = TileMode.Clamp
)

val addStoryButtonColor = Brush.linearGradient(
    colors = listOf(
        MediumPurple,
        PastelPink,
        PastelPink,
        MediumPurple
    ),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, 0f),
    tileMode = TileMode.Clamp
)