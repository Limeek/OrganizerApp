package ru.limeek.organizer.data.model.location

data class LocationSpinnerItem(val name: String,
                               val locationId : Long?){
    override fun toString(): String {
        return name
    }
}