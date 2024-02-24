package com.example.projekt_licencjacki.Dane

data class User (val uid: String? = null,
                 val name: String? = null,
                 val surname: String? = null,
                 val email: String? = null,
                 val reservations: List<String>? = null
)
