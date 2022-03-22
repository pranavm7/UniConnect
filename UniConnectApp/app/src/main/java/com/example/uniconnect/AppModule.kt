package com.example.uniconnect

import com.example.uniconnect.service.IUniversityService
import com.example.uniconnect.service.UniversityService
import org.koin.dsl.module

val appModule = module {
    single<IUniversityService> { UniversityService() }
}