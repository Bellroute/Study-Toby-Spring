package springbook.user.dao;

public class AccountDao {
    private ConnectionMaker connectionMater;

    public AccountDao(ConnectionMaker connectionMaker) {
        this.connectionMater = connectionMaker;
    }
}
