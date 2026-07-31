package com.taskjingle;

import org.junit.Test;

import java.util.regex.Matcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ChatCompleteMessageRegexTest
{
	@Test
	public void matchesPlainTaskCompletion()
	{
		Matcher m = TaskJinglePlugin.CHAT_COMPLETE_MESSAGE.matcher(
				"You've completed 164 tasks and received 610 points, giving you a total of 118,220");
		assertTrue(m.find());
		assertEquals("164", m.group("tasks"));
		assertEquals("118,220", m.group("points"));
	}

	@Test
	public void matchesWildernessTaskCompletion()
	{
		Matcher m = TaskJinglePlugin.CHAT_COMPLETE_MESSAGE.matcher(
				"You've completed 164 Wilderness tasks and received 610 points, giving you a total of 118,220");
		assertTrue(m.find());
		assertEquals("164", m.group("tasks"));
		assertEquals("118,220", m.group("points"));
	}

	@Test
	public void matchesMaxWildernessPointsCompletion()
	{
		Matcher m = TaskJinglePlugin.CHAT_COMPLETE_MESSAGE.matcher(
				"You've completed at least 100 Wilderness tasks and reached the maximum amount of Slayer points (100,000)");
		assertTrue(m.find());
		assertEquals("100", m.group("tasks"));
		assertEquals("100,000", m.group("points2"));
	}

	@Test
	public void matchesNewSlayerMasterTaskCompletion()
	{
		Matcher m = TaskJinglePlugin.CHAT_COMPLETE_MESSAGE.matcher(
				"You've completed 4 Mortimer tasks; return to a Slayer master.");
		assertTrue(m.find());
		assertEquals("4", m.group("tasks"));
	}

	@Test
	public void doesNotMatchUnrelatedMessage()
	{
		Matcher m = TaskJinglePlugin.CHAT_COMPLETE_MESSAGE.matcher(
				"You have completed your task! You killed 164 Jellies. You gained 22,890 xp.");
		assertFalse(m.find());
	}
}
