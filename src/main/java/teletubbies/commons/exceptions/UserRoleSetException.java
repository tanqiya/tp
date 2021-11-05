package teletubbies.commons.exceptions;

public class UserRoleSetException extends Exception {
    private static final String EXCEPTION_MESSAGE = "User profile has already been set "
            + "and cannot be modified.";

    public UserRoleSetException() {
        super(EXCEPTION_MESSAGE);
    }
}
