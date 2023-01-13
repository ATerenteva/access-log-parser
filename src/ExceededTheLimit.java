public class ExceededTheLimit extends RuntimeException{

    public ExceededTheLimit() {
        super();
    }

    public ExceededTheLimit(String message) {
        super(message);
    }

    public ExceededTheLimit(String message, Throwable cause) {
        super(message, cause);
    }

    public ExceededTheLimit(Throwable cause) {
        super(cause);
    }
}
