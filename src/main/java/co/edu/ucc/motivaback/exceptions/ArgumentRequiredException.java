package co.edu.ucc.motivaback.exceptions;

/**
 * @author nagredo
 * @project motiva-back
 * @class ArgumentRequiredException
 */
public class ArgumentRequiredException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ArgumentRequiredException(String message) {
        super(message);
    }

}
