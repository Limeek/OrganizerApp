package ru.limeek.organizer.usecases

interface UseCase<T,R>{
    suspend fun execute(params: T): R
}