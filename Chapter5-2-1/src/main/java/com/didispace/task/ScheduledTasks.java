package com.didispace.task;

import com.didispace.rabbit.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTasks {
    @Autowired
    private Sender sender;

    @Scheduled(fixedRate = 1000)
    public void reportCurrentTime() {
        System.out.println("send msg : " + sender.send());
    }

}
