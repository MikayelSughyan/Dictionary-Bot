package com.example.springboot.commands;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// import discord4j.common.util.Snowflake;
// import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
//import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.MessageChannel;
//import com.example.springboot.commands.DefineCommand;

@Component
public class RandomWord {

    private final GatewayDiscordClient client;
    private final String filePath = "/Users/msughyan/Desktop/app/gs-spring-boot/initial/src/main/resources/words_alpha.txt";
    private final String indexFilePath = "/Users/msughyan/Desktop/app/gs-spring-boot/initial/src/main/resources/index.txt";

    @Autowired
    public RandomWord(GatewayDiscordClient client) {
        // Initialize your Discord client here
        this.client = client;
    }

    @Scheduled(cron = "0 0 12 * * *") // Example: every day at 6 pm
    public void sendDailyMessage() {

        String word = getRandomWordUsingIndex(filePath, indexFilePath);
        String definition = APIHandler.callMerriamWebsterApi(word);
        // Long channelId = Long.parseLong("1214811758464598029"); // Replace with your channel ID
        // client.getChannelById(Snowflake.of(channelId))
        //     .ofType(MessageChannel.class)
        //     .flatMap(channel -> channel.createMessage("Word of the day: " + word + '\n' + definition))
        //     .subscribe();

        client.getGuilds()
                .flatMap(guild -> guild.getChannels())
                .ofType(MessageChannel.class)
                .next()
                .flatMap(channel -> channel.createMessage("Word of the Day:\n" + definition))
                .subscribe();
    
        // client.getGuilds().flatMap(guild -> guild.getChannels())
        //         .filter(channel -> channel.getType() == Channel.Type.GUILD_TEXT)
        //         .filter(channel -> channel.getName().equalsIgnoreCase("general"))
        //         .switchIfEmpty(client.getGuilds()
        //                         .flatMap(guild -> guild.getChannels())
        //                         .filter(channel -> channel.getType() == Channel.Type.GUILD_TEXT)
        //                         .next())
        //         .ofType(MessageChannel.class)
        //         .flatMap(channel -> channel.createMessage("Word of the day: " + word + '\n' + definition))
        //         .subscribe();      
    }

    public static String getRandomWordUsingIndex(String filePath, String indexFilePath) {
    long lineCount;
    try (BufferedReader indexReader = new BufferedReader(new FileReader(indexFilePath))) {
        lineCount = indexReader.lines().count();
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }

    Random rand = new Random();
    long randomLine = Math.abs(rand.nextLong()) % lineCount;

    try (RandomAccessFile file = new RandomAccessFile(filePath, "r");
         BufferedReader indexReader = new BufferedReader(new FileReader(indexFilePath))) {
        for (long i = 0; i < randomLine; ++i) {
            indexReader.readLine();
        }
        long offset = Long.parseLong(indexReader.readLine().trim());
        file.seek(offset);
        return file.readLine().trim();
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
}

}
