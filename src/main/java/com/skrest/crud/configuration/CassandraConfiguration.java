package com.skrest.crud.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.Objects;


@Configuration
@EnableCassandraRepositories(basePackages = "com.skrest.crud.repository")
@PropertySource(value = { "classpath:cassandra.properties" })
public class CassandraConfiguration extends AbstractCassandraConfiguration {
    /**
     * Constant String for Keyspace
     */
    private static final String KEYSPACE = "cassandra.keyspace";
    /**
     * Constant String for ContactPoints
     */
    private static final String CONTACT_POINTS = "cassandra.contactpoints";
    /**
     * Constant String for Port
     */
    private static final String PORT = "cassandra.port";

    @Autowired
    private Environment environment;

    /*
     * Provide a contact point to the configuration.
     */
    protected String getKeyspaceName() {
        return environment.getProperty(KEYSPACE);
    }

    /*
     * Provide a keyspace name to the configuration.
     */
    protected String getContactPoints() {
        return environment.getProperty(CONTACT_POINTS);
    }

    /*
     * Retrieves the cassandra port number.
     */
    protected int getPortNumber() {
        return Integer.parseInt(Objects.requireNonNull(environment.getProperty(PORT)));
    }
}
