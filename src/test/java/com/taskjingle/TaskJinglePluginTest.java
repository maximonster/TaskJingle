package com.taskjingle;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class TaskJinglePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(TaskJinglePlugin.class);
		RuneLite.main(args);
	}
}