package connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import config.Configuration;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *  @description Class for creating a connection to a DB using DriverManager class.
 *  Reads the information from a property file (mysql-properties.xml)
 */

@Singleton
public class PoolConnection {
	private final Configuration config;
	private final DataSource hikariDataSource;

	@Inject
	public PoolConnection(Configuration config) {
		this.config = config;
		this.hikariDataSource = getHikariPool();
	}

	public DataSource getHikariPool() {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setJdbcUrl(config.getProperty("urlDB"));
		hikariConfig.setUsername(config.getProperty("user_name"));
		hikariConfig.setPassword(config.getProperty("password"));
		hikariConfig.setDriverClassName(config.getProperty("driver"));

		hikariConfig.addDataSourceProperty("cachePrepStmts", true);
		hikariConfig.addDataSourceProperty("prepStmtCacheSize", 250);
		hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);

		return new HikariDataSource(hikariConfig);
	}

	public DataSource getDataSource() {
		return hikariDataSource;
	}

	public Connection getConnection() {
		Connection con = null;

		try {
			con = hikariDataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return con;
	}

	@PreDestroy
	public void closePool() {
		((HikariDataSource) hikariDataSource).close();
	}
}
