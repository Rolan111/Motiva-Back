package co.edu.ucc.motivaback.exceptions;

/**
 * @author nagredo
 * @project motiva-back
 * @class ModelNotFoundException
 */
public class ModelNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ModelNotFoundException(String message) {
        super(message);
    }
}
