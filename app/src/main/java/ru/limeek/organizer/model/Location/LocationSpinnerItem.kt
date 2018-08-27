package ru.limeek.organizer.model.Location

data class LocationSpinnerItem(val name: String,
                               val locationId : Long?){
    override fun toString(): String {
        return name
    }
}