package com.example.springboot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
//import io.github.cdimascio.dotenv.Dotenv;

//import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

// import java.io.IOException;
// import java.net.URI;
// import java.net.http.HttpClient;
// import java.net.http.HttpRequest;
// import java.net.http.HttpResponse;
// import java.net.URLEncoder;
// import java.nio.charset.StandardCharsets;

// import org.json.*;


@Component
public class DefineCommand implements SlashCommand{

    // @Value("${Merriam.Webster.token}")
    // private static String MerriamWebstertoken;
    

    @Override
    public String getName() {
        return "Define";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        String wordToDefine = event.getOption("word") // The option name must match what you've defined in your command setup
            .flatMap(ApplicationCommandInteractionOption::getValue)
            .map(ApplicationCommandInteractionOptionValue::asString)
            .orElse("NULL");

        //System.out.println(wordToDefine);    
        String definition = APIHandler.callMerriamWebsterApi(wordToDefine);

        return event.reply()
        .withContent(definition);
    }

}
