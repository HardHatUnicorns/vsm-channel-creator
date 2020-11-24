package pl.aogiri.vsm.channelcreator.dao;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.ClientProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import pl.aogiri.vsm.channelcreator.exception.LastChannelNotFoundException;
import pl.aogiri.vsm.channelcreator.exception.UserNotUniqueException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
public class TeamspeakDAO {
    private TS3Query query;
    private TS3Api api;

    public Optional<Client> findUser(String data) throws UserNotUniqueException {
        List<Client> collect = api.getClients().stream().filter(client -> client.getNickname().equals(data) || client.getUniqueIdentifier().equals(data)).collect(Collectors.toList());
        if (collect.size() > 1)
            throw new UserNotUniqueException();
        return collect.stream().findFirst();
    }

    public void createNewPrivateChannel(Client client) {
        ChannelCreated last;
        try {
            last = getLastPrivateChannel();
            String newNumber = String.valueOf(last.number + 1);
            AtomicInteger lastId = new AtomicInteger(last.cid);

            int mainId = api.createChannel(newNumber+"." + " Name", prepareChannel(last.cid, 0));
            Arrays.asList("╔", "╠", "╚").forEach(prefix -> {
                lastId.set(api.createChannel(prefix + " Name", prepareChannel(0, mainId)));
            });
            api.setClientChannelGroup(getOwnerId(), mainId, client.getDatabaseId());
        } catch (LastChannelNotFoundException e) {
           System.out.println("Last channel not found");
        }
    }

    private int getOwnerId(){
        return api.getChannelGroups().stream().filter(c -> c.getName().equals("Właściciel")).findFirst().orElseThrow().getId();
    }

    private Map<ChannelProperty, String> prepareChannel(int upperId, int parentId){
        final Map<ChannelProperty, String> properties = new HashMap<>();
        properties.put(ChannelProperty.CHANNEL_FLAG_PERMANENT, "1");
        if (upperId != 0)
            properties.put(ChannelProperty.CHANNEL_ORDER, String.valueOf(upperId));
        if (parentId != 0)
            properties.put(ChannelProperty.CPID, String.valueOf(parentId));
        return properties;
    }

    private ChannelCreated getLastPrivateChannel() throws LastChannelNotFoundException {
        Pattern pattern = Pattern.compile("^[0-9]+\\.");
        Channel c = api.getChannels().stream()
                .dropWhile(channel -> !channel.getName().equals("[cspacer455466545645466]「 Prywatne 」"))
                .filter(channel -> pattern.matcher(channel.getName()).find())
                .max(Comparator.comparingInt(channel -> Integer.parseInt(channel.getName().split("\\.")[0])))
                .orElseThrow(LastChannelNotFoundException::new);
        return ChannelCreated.builder()
                .number(Integer.parseInt(c.getName().split("\\.")[0]))
                .cid(c.getId())
                .build();

    }

    private void setupQuery(){
        TS3Config config = new TS3Config();
        config.setHost(SecretsDAO.HOST);
        query = new TS3Query(config);
    }

    public void connectToServer(){
        if(query == null)
            setupQuery();
        if (!query.isConnected()){
            query.connect();
            api = query.getApi();
            api.selectVirtualServerById(1);
            api.login(SecretsDAO.LOGIN, SecretsDAO.PASSWORD);
            api.setNickname("Stefan_2");
        }
    }

    public void disconnectFromServer(){
        if (query != null && query.isConnected())
            query.exit();;
    }

    @Builder
    private static class ChannelCreated{
        private int number;
        private int cid;
    }
}
