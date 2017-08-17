package org.myan.web.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.myan.web.exceptions.ContextException;
import org.myan.web.exceptions.InitializeException;
import org.myan.web.ConfigHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by myan on 2017/8/8.
 * Intellij IDEA
 */
public final class DBUtil {
    private static final Logger LOG = LoggerFactory.getLogger(DBUtil.class);
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();
    private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<>();
    private static final ComboPooledDataSource DATA_SOURCE;

    static {
        String driver = ConfigHelper.getJdbcDriver();
        String url = ConfigHelper.getJdbcUrl();
        String user = ConfigHelper.getJdbcUser();
        String password = ConfigHelper.getJdbcPassword();

        DATA_SOURCE = new ComboPooledDataSource();
        try {
            DATA_SOURCE.setDriverClass(driver);
            DATA_SOURCE.setJdbcUrl(url);
            DATA_SOURCE.setUser(user);
            DATA_SOURCE.setPassword(password);
        } catch (PropertyVetoException e) {
            LOG.error("Failed to set property for c3p0 datasource.", e);
        }
    }

    private static Connection getConnection() {
        Connection connection = CONNECTION_HOLDER.get();
        if (connection == null) {
            try {
                connection = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOG.error("Failed to get connection.", e);
                throw new InitializeException(e);
            } finally {
                CONNECTION_HOLDER.set(connection);
            }
        }
        return connection;
    }

    private static void closeConnection() {
        Connection connection = CONNECTION_HOLDER.get();
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOG.error("Failed to close connection.", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList;
        try {
            Connection connection = getConnection();
            entityList = QUERY_RUNNER.query(connection, sql, new BeanListHandler<>(entityClass), params);
        } catch (SQLException e) {
            LOG.error("Failed to execute query.", e);
            throw new ContextException(e);
        } finally {
            closeConnection();
        }
        return entityList;
    }

    public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
        List<Map<String, Object>> result = null;
        try {
            Connection connection = getConnection();
            result = QUERY_RUNNER.query(connection, sql, new MapListHandler(), params);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return result;
    }

    public static int executeUpdate(String sql, Object... params) {
        int rows;
        try {
            Connection connection = getConnection();
            rows = QUERY_RUNNER.update(connection, sql, params);
        } catch (SQLException e) {
            LOG.error("Failed to execute query.", e);
            throw new ContextException(e);
        } finally {
            closeConnection();
        }
        return rows;
    }

    public static <T> boolean insert(Class<T> entityClass, Map<String, Object> fieldMap) {
        if (CollectionUtil.isEmpty(fieldMap)) {
            LOG.error("Failed to insert entity: field map is empty.");
            return false;
        }
        String sql = "INSERT INTO " + getTableName(entityClass);
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(",");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(","), columns.length(), ")");
        values.replace(values.lastIndexOf(","), values.length(), ")");

        Object[] params = fieldMap.values().toArray();
        return executeUpdate(sql, params) == 1;
    }

    public static <T> boolean update(Class<T> entityClass, long id, Map<String, Object> fieldMap) {
        if (CollectionUtil.isEmpty(fieldMap)) {
            LOG.error("Failed to insert entity: field map is empty.");
            return false;
        }
        String sql = "UPDATE " + getTableName(entityClass) + " SET";
        StringBuilder columns = new StringBuilder();
        for (String fieldName : fieldMap.keySet())
            columns.append(fieldName).append("=?,");
        sql += columns.substring(0, columns.lastIndexOf(",")) + " WHERE id=?";
        List<Object> paramList = new ArrayList<>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        return executeUpdate(sql, paramList.toArray()) == 1;
    }

    public static <T> boolean delete(Class<T> entityClass, long id) {
        String sql = "DELETE FROM " + getTableName(entityClass) + " WHERE id=?";
        return executeUpdate(sql, id) == 1;
    }

    public static void executeSqlFile(String fileName) {
        try (
                InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(input))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                DBUtil.executeUpdate(line);
            }
        } catch (IOException e) {
            LOG.error("Failed to execute sql file", e);
            throw new RuntimeException(e);
        }
    }

    private static String getTableName(Class<?> entityClass) {
        return entityClass.getSimpleName();
    }

    public static void beginTransaction() {
        Connection connection = getConnection();
        if(connection != null) {
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                LOG.error("Failed to start database transaction.", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(connection);
            }
        }
    }

    public static void commitTransaction() {
        Connection connection = getConnection();
        if(connection != null) {
            try {
                connection.commit();
                connection.close();
            } catch (SQLException e) {
                LOG.error("Failed to commit database transaction.", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    public static void rollbackTransaction() {
        Connection connection = getConnection();
        if(connection != null) {
            try {
                connection.rollback();
                connection.close();
            } catch (SQLException e) {
                LOG.error("Failed to rollback database transaction.", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

}
