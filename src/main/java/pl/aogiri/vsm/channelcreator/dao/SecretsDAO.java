package pl.aogiri.vsm.channelcreator.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecretsDAO {
    @Value("${SERVER_HOST}")
    public static String HOST = System.getenv("SERVER_HOST");
    @Value("${SERVER_LOGIN}")
    public static String LOGIN = System.getenv("SERVER_LOGIN");
    @Value("${SERVER_PASSWORD}")
    public static String PASSWORD = System.getenv("SERVER_PASSWORD");
}
