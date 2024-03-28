package com.example.springboot;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import discord4j.rest.RestClient;
//import io.github.cdimascio.dotenv.Dotenv;

// import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Application {

    // @Value("${discord.bot.token}")
    // private static String discordBotToken;
    //Dotenv dotenv = Dotenv.configure().load();

	public static void main(String[] args) {
		 //Start spring application
        new SpringApplicationBuilder(Application.class)
            .build()
            .run(args);
	}
	
    // dotenv.get("BOT_TOKEN") REPLACE CREATE ARGUMENT WITH THIS DURING LOCAL TESTING
	@Bean
    public GatewayDiscordClient gatewayDiscordClient() {
        return DiscordClientBuilder.create(System.getenv("BOT_TOKEN")).build()
            .gateway()
            .setInitialPresence(ignore -> ClientPresence.online(ClientActivity.listening(" /commands")))
            .login()
            .block();
    }

    @Bean
    public RestClient discordRestClient(GatewayDiscordClient client) {
        return client.getRestClient();
    }
	
}
