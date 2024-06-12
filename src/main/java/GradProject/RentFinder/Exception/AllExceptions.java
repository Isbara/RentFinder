package GradProject.RentFinder.Exception;


public enum AllExceptions {
    PROPERTY_ID_NOT_FOUND("Property id couldn't be found"),
    PROPERTY_TABLE_EMPTY("There is no property in the database at the moment"),
    ADDRESS_LENGTH("Adress length must be longer than 4"), // Input specificatıonları burdan da alabiliriz frontendden de- konuşalım
    EMAIL_EXISTS("Email already exists"),
    USER_ID_NOT_FOUND("User id couldn't be found"),
    TOKEN_EXPIRED("Token is expired"),
    RESERVATION_ID_NOT_FOUND("Reservation id couldn't be found"),
    INTERNAL_SERVER_ERROR("An error has occured"),
    WRONG_CREDENTIALS("Email or password is wrong"),
    DECISION_ERROR("An error has occured while trying to update decision"),
    NULL_ERROR("One of the values is null"),
    LOW_KARMA("User karma is too low! Can not write reviews!"),
    ALREADY_RESERVED("There is a reservation in between given dates!"),
    SELF_RESERVATION("You can not reserve your own property!"),
    MULTIPLE_CLOSE_RESERVATIONS("You can not make reservations in 7 days intervals!");



    private final String exception_description;

    AllExceptions(String exception_description) {
        this.exception_description = exception_description;
    }



    public String getDescription() {
        return exception_description;
    }
}
