package br.com.empirelands.mysql.interfaces;

import br.com.empirelands.exception.InvalidInput;

import java.util.List;

public interface DaoUtils {

    boolean add(List<Object> objectMap) throws InvalidInput;

    boolean delete(Object id);

    boolean update(String column, Object id, Object value);
}
