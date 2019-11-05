package ru.limeek.organizer.domain.entities.location

data class LocationSpinnerItem(val name: String,
                               val locationId : Long?){
    override fun toString(): String {
        return name
    }
}