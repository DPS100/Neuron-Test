package src.network;

public class InvalidCircuitParameter extends Exception {

    enum ERROR {

        CONSTRUCT("While constructing: "),
        PROCESS("While processing: ");

        String message;

        ERROR(String message) {
            this.message = message;
        }

    }

    public InvalidCircuitParameter(ERROR eType, String errorMessage) {
        super(eType.message + errorMessage);
    }
}
