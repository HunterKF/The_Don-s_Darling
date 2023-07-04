package com.example.thedonsdarling.domain.use_cases

import com.example.thedonsdarling.domain.repository.FireStoreRepository

class GetUid(
    private val repository: FireStoreRepository,
) {
    operator fun invoke(): String? {
        return repository.returnUid()
    }

}