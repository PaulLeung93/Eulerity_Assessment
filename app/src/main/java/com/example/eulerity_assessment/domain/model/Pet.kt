package com.example.eulerity_assessment.domain.model

import java.io.Serializable

data class Pet(
    val title: String,
    val description: String,
    val url: String,
    val created: String
) : Serializable