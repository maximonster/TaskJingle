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
			keyName = "customjingle",
			name = "Custom Jingle",
			description = "Toggles whether a custom jingle is used",
			position = 2
	)
	default boolean customjingle()
	{
		return false;
	}
	@ConfigItem(
			keyName = "custompath",
			name = "Custom path",
			description = "Specify file path or URL(must contain http:// or https://) to custom MP3",
			position = 3
	)
	default String custompath()
	{
		return "";
	}

}
