package GradProject.RentFinder.Exception;

public class Exception extends RuntimeException{
    private final AllExceptions allExceptions;

    public Exception(AllExceptions allExceptions) {
        super(allExceptions.getDescription());
        this.allExceptions = allExceptions;
    }

    public AllExceptions getAllExceptions() {
        return allExceptions;
    }
}
