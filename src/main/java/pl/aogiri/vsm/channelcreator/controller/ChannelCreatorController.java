package pl.aogiri.vsm.channelcreator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.aogiri.vsm.channelcreator.dto.ChannelRequestDTO;
import pl.aogiri.vsm.channelcreator.exception.UserNotUniqueException;
import pl.aogiri.vsm.channelcreator.service.ChannelCreatorService;

@RestController
public class ChannelCreatorController {

    @Autowired
    ChannelCreatorService service;

    @PostMapping("/create")
    public ResponseEntity start(@RequestBody ChannelRequestDTO channelRequestDTO){
        if (channelRequestDTO.getUID() == null & channelRequestDTO.getUID() == null)
            return ResponseEntity.badRequest().body("Specify user by uid or name.");
        try {
            service.create(channelRequestDTO);
        } catch (UserNotUniqueException e) {
            return ResponseEntity.badRequest().body("User is not unique.");
        }
        return ResponseEntity.ok().build();
    }


    @GetMapping("/health")
    public ResponseEntity<Object> getHealthService(){
        return ResponseEntity.ok().build();
    }
}
