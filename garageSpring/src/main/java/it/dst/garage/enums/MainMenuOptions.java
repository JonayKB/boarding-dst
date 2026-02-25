package it.dst.garage.enums;

public enum MainMenuOptions {
    SHOW_CARS("Show all cars info"),
    SHOW_CAR("Show a car by id"),
    SHOW_CARS_CONSTAINS_BRAND("Show all cars that contains a brand"),
    ADD_CAR("Add a new car"),
    UPDATE_CAR("Updates a created car"),
    REMOVE_CAR("Removes a car"),
    SEED_DATABASE("Add testing information about cars"),
    EXIT("Leave Program");

    public final String label;

    private MainMenuOptions(String label) {
        this.label = label;
    }

}
