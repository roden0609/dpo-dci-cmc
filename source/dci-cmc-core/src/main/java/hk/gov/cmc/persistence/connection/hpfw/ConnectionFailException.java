package hk.gov.cmc.persistence.connection.hpfw;

public class ConnectionFailException extends java.sql.SQLException {
    private static final long serialVersionUID = 3779333604278878013L;

    public ConnectionFailException(String error) {
        super(error);
    }

    public ConnectionFailException() {
        super();
    }
}
