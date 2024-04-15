package com.taskjingle;

import jaco.mp3.player.MP3Player;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;
import com.taskjingle.TaskJinglePlugin;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

@ConfigGroup("taskjingle")
public interface TaskJingleConfig extends Config
{

    @Range(
			max = 100
	)
	@ConfigItem(
			keyName = "volume",
			name = "Volume",
			description = "Specify the volume.",
			position = 1
	)
	default int volume()
	{
		return 100;
	}
	@ConfigItem(
			keyName = "Taskjingle",
			name = "Task Jingle",
			description = "Turns on jingles for slayer tasks",
			position = 2
	)
	default boolean taskjingleon()
	{
		return true;
	}
	@ConfigItem(
			keyName = "customjingle",
			name = "Custom Task Jingle",
			description = "Toggles whether a custom jingle is used",
			position = 3
	)
	default boolean customjingle()
	{
		return false;
	}
	@ConfigItem(
			keyName = "custompath",
			name = "Custom Task jingle path/link",
			description = "Specify file path or URL(must contain http:// or https://) to custom MP3",
			position = 4
	)
	default String custompath()
	{
		return "";
	}
	@ConfigItem(
			keyName = "lvlupjingle",
			name = "Level up music",
			description = "Turns on music for level ups",
			position = 5
	)
	default boolean lvlupjingleon()
	{
		return false;
	}
	@ConfigItem(
			keyName = "customlevelupmusic",
			name = "Custom Level up music",
			description = "Toggles whether a custom level up music is used",
			position = 6
	)
	default boolean customjinglel()
	{
		return false;
	}
	@ConfigItem(
			keyName = "customleveluppath",
			name = "Custom level up music path/link",
			description = "Specify file path or URL(must contain http:// or https://) to custom MP3",
			position = 7
	)
	default String custompathl()
	{
		return "";
	}

}
