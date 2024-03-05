package GradProject.RentFinder.Exception;

public class Exceptions extends RuntimeException{
    private final AllExceptions allExceptions;

    public Exceptions(AllExceptions allExceptions) {
        super(allExceptions.getDescription());
        this.allExceptions = allExceptions;
    }

    public AllExceptions getAllExceptions() {
        return allExceptions;
    }
}
