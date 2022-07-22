package com.taskjingle;


import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.RuneLite;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import jaco.mp3.player.MP3Player;

@Slf4j
@PluginDescriptor(
	name = "TaskJingle"
)
public class TaskJinglePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private TaskJingleConfig config;
	private static final Pattern CHAT_COMPLETE_MESSAGE = Pattern.compile("You've completed (?:at least )?(?<tasks>[\\d,]+) (?:Wilderness )?tasks?(?: and received \\d+ points, giving you a total of (?<points>[\\d,]+)| and reached the maximum amount of Slayer points \\((?<points2>[\\d,]+)\\))?");
	private MP3Player trackPlayer = new MP3Player(getClass().getClassLoader().getResource("task-jingle.mp3"));
	private MP3Player customtrack = new MP3Player(getClass().getClassLoader().getResource("task-jingle.mp3"));


	@Subscribe
	public void onChatMessage(ChatMessage event) throws Exception {
		trackPlayer.setVolume(config.volume());
		String chatMsg = Text.removeTags(event.getMessage()); //remove color and linebreaks
		if (chatMsg.startsWith("You've completed") && (chatMsg.contains("Slayer master") || chatMsg.contains("Slayer Master"))) {
			Matcher mComplete = CHAT_COMPLETE_MESSAGE.matcher(chatMsg);

			if (mComplete.find()) {
				if (config.customjingle()){
					if (config.custompath().endsWith(".mp3")||config.custompath().endsWith(".MP3") )
					{
					if (config.custompath().startsWith("http://") || config.custompath().startsWith("https://") ){
						customtrack = new MP3Player(new URL(config.custompath()));
					}

					{
					customtrack = new MP3Player(new File(config.custompath()));
					}
					}
					else{
						customtrack = new MP3Player(getClass().getClassLoader().getResource("task-jingle.mp3"));
					}
					customtrack.play();
				}
				else
				trackPlayer.play();
			}
		}
	}


	@Provides
	TaskJingleConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TaskJingleConfig.class);
	}
}
