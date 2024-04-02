package com.example.springboot.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.player.event.TrackEndEvent;
import com.sedmelluq.discord.lavaplayer.player.event.TrackStartEvent;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class TrackScheduler implements AudioLoadResultHandler, AudioEventListener {

    private final AudioPlayer player;

    public TrackScheduler(final AudioPlayer player) {
        this.player = player;
    }

    @Override
    public void trackLoaded(final AudioTrack track) {
        // LavaPlayer found an audio source for us to play
        player.playTrack(track);
    }

    @Override
    public void playlistLoaded(final AudioPlaylist playlist) {
        // LavaPlayer found multiple AudioTracks from some playlist
    }

    @Override
    public void noMatches() {
        // LavaPlayer did not find any audio to extract
    }

    @Override
    public void loadFailed(final FriendlyException exception) {
        // LavaPlayer could not parse an audio source for some reason
    }

    @Override
    public void onEvent(AudioEvent event) {
        if (event instanceof TrackStartEvent) {
            TrackStartEvent startEvent = (TrackStartEvent) event;
            System.out.println("Track started: " + startEvent.track.getInfo().title);
        } else if (event instanceof TrackEndEvent) {
            TrackEndEvent endEvent = (TrackEndEvent) event;
            System.out.println("Track ended: " + endEvent.track.getInfo().title + " with reason: " + endEvent.endReason);
            // Here you can add code to disconnect from the channel if needed, or load next track, etc.
        }
        // Handle other event types (TrackExceptionEvent, TrackStuckEvent, etc.) as needed
    }
}