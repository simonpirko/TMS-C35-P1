package by.tms.tmsc35p1;

public record Account(Integer id, String username, String password) {
    @Override
    public Integer id() {
        return id;
    }
}
