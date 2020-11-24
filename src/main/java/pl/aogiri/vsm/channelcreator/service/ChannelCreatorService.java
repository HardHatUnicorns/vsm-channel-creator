package pl.aogiri.vsm.channelcreator.service;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.aogiri.vsm.channelcreator.dao.TeamspeakDAO;
import pl.aogiri.vsm.channelcreator.dto.ChannelRequestDTO;
import pl.aogiri.vsm.channelcreator.exception.UserNotUniqueException;

import java.util.Optional;

@Service
public class ChannelCreatorService {

    @Autowired
    TeamspeakDAO dao;

    public void create(ChannelRequestDTO dto) throws UserNotUniqueException {
        String data = dto.getName() == null ? dto.getUID() : dto.getName();
        try{
            dao.connectToServer();
            Optional<Client> clientOptional = dao.findUser(data);
            clientOptional.ifPresent(client -> dao.createNewPrivateChannel(client));
        } finally {
            dao.disconnectFromServer();
        }
    }

}
