package pl.aogiri.vsm.channelcreator.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecretsDAO {
    public static String HOST = System.getenv("SERVER_HOST");
    public static String LOGIN = System.getenv("SERVER_LOGIN");
    public static String PASSWORD = System.getenv("SERVER_PASSWORD");
}
