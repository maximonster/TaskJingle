package com.taskjingle;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

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
}
