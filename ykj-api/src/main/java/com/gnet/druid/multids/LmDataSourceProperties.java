package com.gnet.druid.multids;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.datasource2")
public class LmDataSourceProperties {

	/**
	 * Name of the datasource.
	 */
	private String name = "testdb";

	/**
	 * Fully qualified name of the connection pool implementation to use. By default, it
	 * is auto-detected from the classpath.
	 */
	private Class<? extends DataSource> type;

	/**
	 * Fully qualified name of the JDBC driver. Auto-detected based on the URL by default.
	 */
	private String driverClassName;

	/**
	 * JDBC url of the database.
	 */
	private String url;

	/**
	 * Login user of the database.
	 */
	private String username;

	/**
	 * Login password of the database.
	 */
	private String password;

	/**
	 * JNDI location of the datasource. Class, url, username & password are ignored when
	 * set.
	 */
	private String jndiName;

	/**
	 * Populate the database using 'data.sql'.
	 */
	private boolean initialize = true;

	/**
	 * Platform to use in the schema resource (schema-${platform}.sql).
	 */
	private String platform = "all";

	/**
	 * Schema (DDL) script resource reference.
	 */
	private String schema;

	/**
	 * User of the database to execute DDL scripts (if different).
	 */
	private String schemaUsername;

	/**
	 * Password of the database to execute DDL scripts (if different).
	 */
	private String schemaPassword;

	/**
	 * Data (DML) script resource reference.
	 */
	private String data;

	/**
	 * User of the database to execute DML scripts.
	 */
	private String dataUsername;

	/**
	 * Password of the database to execute DML scripts.
	 */
	private String dataPassword;

	/**
	 * Do not stop if an error occurs while initializing the database.
	 */
	private boolean continueOnError = false;

	/**
	 * Statement separator in SQL initialization scripts.
	 */
	private String separator = ";";


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<? extends DataSource> getType() {
		return this.type;
	}

	public void setType(Class<? extends DataSource> type) {
		this.type = type;
	}

	/**
	 * Return the configured driver or {@code null} if none was configured.
	 * @return the configured driver
	 * @see #determineDriverClassName()
	 */
	public String getDriverClassName() {
		return this.driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	/**
	 * Return the configured url or {@code null} if none was configured.
	 * @return the configured url
	 * @see #determineUrl()
	 */
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Return the configured username or {@code null} if none was configured.
	 * @return the configured username
	 * @see #determineUsername()
	 */
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Return the configured password or {@code null} if none was configured.
	 * @return the configured password
	 * @see #determinePassword()
	 */
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getJndiName() {
		return this.jndiName;
	}

	/**
	 * Allows the DataSource to be managed by the container and obtained via JNDI. The
	 * {@code URL}, {@code driverClassName}, {@code username} and {@code password} fields
	 * will be ignored when using JNDI lookups.
	 * @param jndiName the JNDI name
	 */
	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

	public boolean isInitialize() {
		return this.initialize;
	}

	public void setInitialize(boolean initialize) {
		this.initialize = initialize;
	}

	public String getPlatform() {
		return this.platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getSchema() {
		return this.schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getSchemaUsername() {
		return this.schemaUsername;
	}

	public void setSchemaUsername(String schemaUsername) {
		this.schemaUsername = schemaUsername;
	}

	public String getSchemaPassword() {
		return this.schemaPassword;
	}

	public void setSchemaPassword(String schemaPassword) {
		this.schemaPassword = schemaPassword;
	}

	public String getData() {
		return this.data;
	}

	public void setData(String script) {
		this.data = script;
	}

	public String getDataUsername() {
		return this.dataUsername;
	}

	public void setDataUsername(String dataUsername) {
		this.dataUsername = dataUsername;
	}

	public String getDataPassword() {
		return this.dataPassword;
	}

	public void setDataPassword(String dataPassword) {
		this.dataPassword = dataPassword;
	}

	public boolean isContinueOnError() {
		return this.continueOnError;
	}

	public void setContinueOnError(boolean continueOnError) {
		this.continueOnError = continueOnError;
	}

	public String getSeparator() {
		return this.separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

}
