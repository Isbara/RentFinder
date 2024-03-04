package GradProject.RentFinder.Exception;


public enum AllExceptions {
    PROPERTY_ID_NOT_FOUND("Property id couldn't be found"),
    PROPERTY_TABLE_EMPTY("There is no property in the database at the moment");



    private final String exception_description;

    AllExceptions(String exception_description) {
        this.exception_description = exception_description;
    }



    public String getDescription() {
        return exception_description;
    }
}
