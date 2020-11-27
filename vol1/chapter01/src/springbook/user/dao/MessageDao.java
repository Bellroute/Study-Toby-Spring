package springbook.user.dao;

public class MessageDao {
    private ConnectionMaker connectionMater;

    public MessageDao(ConnectionMaker connectionMaker) {
        this.connectionMater = connectionMaker;
    }
}
