package com.taskjingle;


import com.google.inject.Provides;
import jaco.mp3.player.MP3Player;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.client.chat.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.io.File;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@PluginDescriptor(
	name = "TaskJingle"
)
public class TaskJinglePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private ChatCommandManager chatCommandManager;
	@Inject
	private TaskJingleConfig config;
	private static final Pattern CHAT_COMPLETE_MESSAGE = Pattern.compile("You've completed (?:at least )?(?<tasks>[\\d,]+) (?:Wilderness )?tasks?(?: and received \\d+ points, giving you a total of (?<points>[\\d,]+)| and reached the maximum amount of Slayer points \\((?<points2>[\\d,]+)\\))?");
	private MP3Player trackPlayer = new MP3Player(getClass().getClassLoader().getResource("task-jingle.mp3"));
	private MP3Player tracklPlayer = new MP3Player(getClass().getClassLoader().getResource("lvlup.mp3"));

	private MP3Player customtrack = new MP3Player(getClass().getClassLoader().getResource("task-jingle.mp3"));
	private MP3Player customltrack = new MP3Player(getClass().getClassLoader().getResource("lvlup.mp3"));

	private static final String TESTJINGLE_COMMAND_STRING = "testjingle";
	private static final String TESTLVLJINGLE_COMMAND_STRING = "testlvljingle";
	private final Map<Skill, Integer> CurrentLevelsMap = new EnumMap<>(Skill.class);

	@Override
	protected void startUp() throws Exception
	{
		log.info("Task jingle started!");
	}

	@Override
	protected void shutDown() throws Exception{
		log.info("Task jingle stopped!");
		CurrentLevelsMap.clear();
	}
	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		trackPlayer.setVolume(config.volume());
		String chatMsg = Text.removeTags(event.getMessage()); //remove color and linebreaks
		if (chatMsg.contains("Welcome")){
		Loadcurrentlevels();
		}
		if ((chatMsg.startsWith("You've completed") && (chatMsg.contains("Slayer master") || chatMsg.contains("Slayer Master")))&& config.taskjingleon())
		{
			Matcher mComplete = CHAT_COMPLETE_MESSAGE.matcher(chatMsg);

			if (mComplete.find()) {
				if (config.customjingle()){
					try {


						if (config.custompath().endsWith(".mp3") || config.custompath().endsWith(".MP3")) {
							if (config.custompath().startsWith("http://") || config.custompath().startsWith("https://")) {
								customtrack = new MP3Player(new URL(config.custompath()));
							} else {
								customtrack = new MP3Player(new File(config.custompath()));
							}

						} else {
							String chatMessage = new ChatMessageBuilder()
									.append(ChatColorType.HIGHLIGHT)
									.append("Your custom track is not an MP3 file. Default track loaded")
									.build();

							chatMessageManager.queue(QueuedMessage.builder()
									.type(ChatMessageType.CONSOLE)
									.runeLiteFormattedMessage(chatMessage)
									.build());

							customtrack = new MP3Player(getClass().getClassLoader().getResource("task-jingle.mp3"));
						}
						customtrack.setVolume(config.volume());
						customtrack.play();
					} catch (Exception e) {
						String chatMessage = new ChatMessageBuilder()
								.append(ChatColorType.HIGHLIGHT)
								.append("Your URL or filepath is incorrect")
								.build();

						chatMessageManager.queue(QueuedMessage.builder()
								.type(ChatMessageType.CONSOLE)
								.runeLiteFormattedMessage(chatMessage)
								.build());
                    }
                }
				else {
					trackPlayer.setVolume(config.volume());
					trackPlayer.play();
				}
			}
		}
	}
	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGING_IN)
		{
			CurrentLevelsMap.clear();
		}
	}


	@Subscribe
	public void onStatChanged(StatChanged statChanged)
	{
		final Skill skill = statChanged.getSkill();
		int skillLevelBefore = CurrentLevelsMap.getOrDefault(skill, -1);
		if (skillLevelBefore ==-1 || skillLevelBefore == 0){
			Loadcurrentlevels();
		}
		else if (statChanged.getLevel() > skillLevelBefore && config.lvlupjingleon()) {

			CurrentLevelsMap.replace(skill, statChanged.getLevel());
			if (config.customjinglel()) {
				try {


					if (config.custompathl().endsWith(".mp3") || config.custompathl().endsWith(".MP3")) {
						if (config.custompathl().startsWith("http://") || config.custompathl().startsWith("https://")) {
							customltrack = new MP3Player(new URL(config.custompathl()));
						} else {
							customltrack = new MP3Player(new File(config.custompathl()));
						}

					} else {
						String chatMessage = new ChatMessageBuilder()
								.append(ChatColorType.HIGHLIGHT)
								.append("Your custom track is not an MP3 file. Default track loaded")
								.build();

						chatMessageManager.queue(QueuedMessage.builder()
								.type(ChatMessageType.CONSOLE)
								.runeLiteFormattedMessage(chatMessage)
								.build());

						customltrack = new MP3Player(getClass().getClassLoader().getResource("lvlup.mp3"));
					}
					customltrack.setVolume(config.volume());
					customltrack.play();
				} catch (Exception e) {
					String chatMessage = new ChatMessageBuilder()
							.append(ChatColorType.HIGHLIGHT)
							.append("Your URL or filepath is incorrect")
							.build();

					chatMessageManager.queue(QueuedMessage.builder()
							.type(ChatMessageType.CONSOLE)
							.runeLiteFormattedMessage(chatMessage)
							.build());
				}
			} else{
				tracklPlayer.setVolume(config.volume());
				tracklPlayer.play();
			}
		}
	}
	@Subscribe
	public void onCommandExecuted(CommandExecuted commandExecuted)
	{
		if (commandExecuted.getCommand().equals(TESTJINGLE_COMMAND_STRING))
		{
			testcustomtrack();
		}
		if (commandExecuted.getCommand().equals(TESTLVLJINGLE_COMMAND_STRING))
		{
			testcustomltrack();
		}
	}
	public void testcustomtrack()
	{
		if (config.custompath().endsWith(".mp3")||config.custompath().endsWith(".MP3") )
		{
			if (config.custompath().startsWith("http://") || config.custompath().startsWith("https://") ){
                try {
                    customtrack = new MP3Player(new URL(config.custompath()));
                } catch (Exception e) {
					sendChatMessage("URL could not be loaded. Please check URL");
					return;
                }
            }
			else
			{
				try {
								customtrack = new MP3Player(new File(config.custompath()));

				}catch (Exception e) {
						sendChatMessage("File path seems to be incorrect. Please check file path");
					return;
				}
			}
					sendChatMessage("Your custom track is loaded successfully. It should be playing now.");
		}
		else
		{
			sendChatMessage("Your custom track is not an MP3 file. Default track loaded");
			customtrack = new MP3Player(getClass().getClassLoader().getResource("task-jingle.mp3"));
		}
		customtrack.setVolume(config.volume());
		customtrack.play();
	}
	public void testcustomltrack()
	{
		if (config.custompathl().endsWith(".mp3")||config.custompathl().endsWith(".MP3") )
		{
			if (config.custompathl().startsWith("http://") || config.custompathl().startsWith("https://") ){
				try {
					customltrack = new MP3Player(new URL(config.custompathl()));
				} catch (Exception e) {
					sendChatMessage("URL could not be loaded. Please check URL");
					return;
				}
			}
			else
			{
				try {
					customltrack = new MP3Player(new File(config.custompathl()));

				}catch (Exception e) {
					sendChatMessage("File path seems incorrect. Please check file path");
					return;
				}
			}
			sendChatMessage("Your custom track is loaded successfully. It should be playing now.");
		}
		else
		{
			sendChatMessage("Your custom track is not an MP3 file. Default track loaded");
			customltrack = new MP3Player(getClass().getClassLoader().getResource("lvlup.mp3"));
		}
		customltrack.setVolume(config.volume());
		customltrack.play();
	}
	private void Loadcurrentlevels()
	{
		for (final Skill skill : Skill.values())
		{
			if (skill.getName() != "Overall")
			{
			CurrentLevelsMap.put(skill, client.getRealSkillLevel(skill));
			}
		}
	}
	private void sendChatMessage(String chatMessage)
	{
		final String message = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(chatMessage)
				.build();

		chatMessageManager.queue(
				QueuedMessage.builder()
						.type(ChatMessageType.CONSOLE)
						.runeLiteFormattedMessage(message)
						.build());
	}
	@Provides
	TaskJingleConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TaskJingleConfig.class);
	}
}
