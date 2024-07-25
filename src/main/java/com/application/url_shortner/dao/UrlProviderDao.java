package com.application.url_shortner.dao;

import com.application.url_shortner.exceptions.DbNotReachableException;
import com.application.url_shortner.models.ShortenedUrl;
import com.application.url_shortner.util.ConnectionPool;
import com.application.url_shortner.util.RedisConnectionPool;
import redis.clients.jedis.Jedis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UrlProviderDao {
    public static Connection getConnectionFromConnectionPool() throws SQLException, DbNotReachableException, ClassNotFoundException, DbNotReachableException {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPoolInstance();
        return connectionPool.getConnection();
    }

    public static void removeConnectionFromConnectionPoolInstance(Connection connection) throws SQLException, DbNotReachableException, ClassNotFoundException {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPoolInstance();
        connectionPool.removeConnection(connection);
    }

    public static Jedis getJedisConnection() {
        RedisConnectionPool redisConnectionPool = RedisConnectionPool.getInstance();
        return redisConnectionPool.getConnection();
    }

    public static void saveUrl(String originalUrl, String shortenedUrl) {
        String sql = "insert into url_shortened(original_url, shortened_url, count) values (?, ?, 0)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try (Jedis jedis = getJedisConnection()) {
            connection = getConnectionFromConnectionPool();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, originalUrl);
            preparedStatement.setString(2, shortenedUrl);
            preparedStatement.executeUpdate();
            jedis.set(shortenedUrl, originalUrl);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                removeConnectionFromConnectionPoolInstance(connection);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static ShortenedUrl getUrl(String shortenedUrl) {
        String sql = "select * from url_shortened where shortened_url = ?";
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        ShortenedUrl shortenedUrl1 = new ShortenedUrl();
        try (Jedis jedis = getJedisConnection()) {
            if (jedis.get(shortenedUrl) != null) {
                shortenedUrl1 = new ShortenedUrl(shortenedUrl, jedis.get(shortenedUrl));
            } else {
                Connection connection = null;
                try {
                    connection = getConnectionFromConnectionPool();
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, shortenedUrl);
                    resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        shortenedUrl1 = new ShortenedUrl(resultSet.getString("original_string"), resultSet.getInt("count"));
                        jedis.set(shortenedUrl, shortenedUrl1.getOriginalUrl());
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                } finally {
                    try {
                        if (resultSet != null) {
                            resultSet.close();
                            preparedStatement.close();
                        }
                        removeConnectionFromConnectionPoolInstance(connection);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        return shortenedUrl1;
    }

    public static void updateAccessCount(String shortUrl) {
        String sql = "update url_shortened set access_count = access_count+1 where shortened_url = ?";
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = getConnectionFromConnectionPool();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, shortUrl);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
