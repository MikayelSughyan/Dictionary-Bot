package com.example.springboot.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import ch.qos.logback.core.util.DelayStrategy;
//import ch.qos.logback.core.util.Duration;
import java.time.Duration;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.core.spec.VoiceChannelJoinSpec;
import discord4j.voice.VoiceConnection;
import reactor.core.publisher.Mono;

public class PronounceCommand implements SlashCommand {
    
    @Override
    public String getName() {
        return "pronounce";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return event.reply().withContent("Pronounce command called");
        // return Mono.just(event)
        // .flatMap(evt -> {
        //     String wordToPronounce = event.getOption("word")
        //         .flatMap(ApplicationCommandInteractionOption::getValue)
        //         .map(ApplicationCommandInteractionOptionValue::asString)
        //         .orElse("NULL");
            
        //     String audioURL = APIHandler.getAudioJSONForWord(wordToPronounce);

        //     return evt.getInteraction().getGuild()
        //         .flatMap(guild -> guild.getVoiceStates()
        //             .filter(voiceState -> voiceState.getUserId().equals(event.getInteraction().getUser().getId()))
        //             .next()
        //             .flatMap(voiceState -> voiceState.getChannel())
        //         )
        //         .flatMap(channel -> joinAndPlay(channel, audioURL))
        //         .then(event.reply("Playing pronunciation for " + wordToPronounce));
        // })
        // .then();

        // String wordToPronounce = event.getOption("word")
        //     .flatMap(ApplicationCommandInteractionOption::getValue)
        //     .map(ApplicationCommandInteractionOptionValue::asString)
        //     .orElse("NULL");
        
        // String audioURL = APIHandler.getAudioJSONForWord(wordToPronounce);
        
        
        // return event.getInteraction().getGuild()
        //     .flatMap(guild -> guild.getVoiceStates()
        //         .filter(voiceState -> voiceState.getUserId().equals(event.getInteraction().getUser().getId()))
        //         .next()
        //         .flatMap(voiceState -> voiceState.getChannel())
        //     )
        //     .flatMap(channel -> joinAndPlay(channel, audioURL))
        //     .then(event.reply("Playing pronunciation for " + wordToPronounce));
    }

    private Mono<Void> joinAndPlay(VoiceChannel channel, String audioURL) {
        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioPlayer player = playerManager.createPlayer();

        TrackScheduler scheduler = new TrackScheduler(player);
        player.addListener(scheduler);

        VoiceChannelJoinSpec spec = VoiceChannelJoinSpec.create()
                                    .withSelfDeaf(true)
                                    .withProvider(new LavaPlayerAudioProvider(player))
                                    .withTimeout(Duration.ofMinutes(1));

        return channel.getVoiceConnection()
            .flatMap(voiceConnection -> voiceConnection.disconnect())
            .then(channel.join(spec))
            .flatMap(voiceConnection -> Mono.fromCallable(() -> playerManager.loadItem(audioURL, scheduler)))
            .then();
            //.flatMap(voiceConnection -> playerManager.loadItem(audioURL, scheduler))
            
        // return channel.getGuild()
        //     .flatMap(guild -> guild.getVoiceConnection())
        //     .then(channel.join(spec))
        //     .flatMap(voiceConnection -> playerManager.loadItem(audioURL,scheduler))
        //     .then();
                
    }
    //spec -> spec.setProvider(new LavaPlayerAudioProvider(player)   
}
