package wbdsoap.daos;

import org.hibernate.SessionFactory;
import wbdsoap.classes.GenericDAO;
import wbdsoap.models.LogEntity;

public class LogDAO extends GenericDAO<LogEntity> {
    public LogDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
