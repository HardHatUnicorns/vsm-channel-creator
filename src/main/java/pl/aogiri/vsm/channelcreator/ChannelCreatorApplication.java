package pl.aogiri.vsm.channelcreator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pl.aogiri.vsm.channelcreator.dto.ChannelRequestDTO;
import pl.aogiri.vsm.channelcreator.exception.UserNotUniqueException;
import pl.aogiri.vsm.channelcreator.service.ChannelCreatorService;

@SpringBootApplication
public class ChannelCreatorApplication {

    public static void main(String... args) {
        SpringApplication.run(ChannelCreatorApplication.class, args);
    }
}
