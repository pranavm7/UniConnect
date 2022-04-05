package com.example.uniconnect

import com.example.uniconnect.dao.IUniversityService
import com.example.uniconnect.service.UniversityService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { MainViewModel(get())}
    single<IUniversityService> { UniversityService() }
}