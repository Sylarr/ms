package com.sinosoft.ms.db;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.sinosoft.ms.cache.SystemCache;

/**
 * 数据库管理器，内实现连接池机制。
 * 
 * @author HanYan
 * @date 2014-09-10
 */
public class DBManager {

	private final static Logger log = Logger.getLogger(DBManager.class);
	private final static String CONFIG_NAME = "db.properties";
    private final static ThreadLocal<Connection> conns = new ThreadLocal<Connection>();
    private static DataSource dataSource;
    private static boolean show_sql = false;
    
    static {
        initDataSource(null);
    }

    /**
     * 初始化连接池
     * @param props
     * @param show_sql
     */
    private final static void initDataSource(Properties dbProperties) {
        try {
            if(dbProperties == null){
                dbProperties = new Properties();
                String path;
//                	path = "D:\\application\\ms\\WEB-INF" + File.separator + CONFIG_NAME;
//                	path = "D:\\workspace\\ms\\ms\\WEB-INF" + File.separator + CONFIG_NAME;//用来本地调试调用的
                	path = SystemCache.getCache("webinfo.path") + File.separator + CONFIG_NAME;
                dbProperties.load(new FileInputStream(new File(path)));
            }
            Properties cp_props = new Properties();
            for(Object key : dbProperties.keySet()) {
                String skey = (String)key;
                if(skey.startsWith("jdbc.")){
                    String name = skey.substring(5);
                    cp_props.put(name, dbProperties.getProperty(skey));
                    if("show_sql".equalsIgnoreCase(name)){
                        show_sql = "true".equalsIgnoreCase(dbProperties.getProperty(skey));
                    }
                }
            }
        	
            dataSource = (DataSource)Class.forName(cp_props.getProperty("datasource")).newInstance();
            //dataSource = new ComboPooledDataSource();
            if(dataSource.getClass().getName().indexOf("c3p0")>0){
                //Disable JMX in C3P0
                System.setProperty("com.mchange.v2.c3p0.management.ManagementCoordinator", 
                        "com.mchange.v2.c3p0.management.NullManagementCoordinator");
            }
            log.info("Using DataSource : " + dataSource.getClass().getName());
            BeanUtils.populate(dataSource, cp_props);

            Connection conn = getConnection();
            DatabaseMetaData mdm = conn.getMetaData();
            log.info("Connected to " + mdm.getDatabaseProductName() + " " + mdm.getDatabaseProductVersion());
            closeConnection();
        } catch (Exception e) {
        	log.error("数据库管理器初始化失败!", e);
        }
    }
    
    /**
     * 断开连接池
     */
    public final static void closeDataSource(){
        try {
            dataSource.getClass().getMethod("close").invoke(dataSource);
        } catch (NoSuchMethodException e){
        } catch (Exception e) {
            log.error("Unabled to destroy DataSource!!! ", e);
        }
    }

    public final static Connection getConnection() throws SQLException {
        Connection conn = conns.get();
        if(conn ==null || conn.isClosed()){
            conn = dataSource.getConnection();
            conns.set(conn);
        }
        return (show_sql && !Proxy.isProxyClass(conn.getClass()))?
                      new _DebugConnection(conn).getConnection():conn;
    }
    
    /**
     * 关闭连接
     */
    public final static void closeConnection() {
        Connection conn = conns.get();
        try {
            if(conn != null && !conn.isClosed()){
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            log.error("Unabled to close connection!!! ", e);
        }
        conns.set(null);
    }
 
    /**
     * 用于跟踪执行的SQL语句
     * @author HanYan
     */
    static class _DebugConnection implements InvocationHandler {
         
    	private final static Logger log = Logger.getLogger(_DebugConnection.class);
         
        private Connection conn = null;
 
        public _DebugConnection(Connection conn) {
            this.conn = conn;
        }

        /**
         * Returns the conn.
         * @return Connection
         */
        public Connection getConnection() {
            return (Connection) Proxy.newProxyInstance(conn.getClass().getClassLoader(), 
                             conn.getClass().getInterfaces(), this);
        }
        
        public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
            try {
                String method = m.getName();
                if("prepareStatement".equals(method) || "createStatement".equals(method))
                    log.info("[SQL] >>> " + args[0]);              
                return m.invoke(conn, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
    }
}
