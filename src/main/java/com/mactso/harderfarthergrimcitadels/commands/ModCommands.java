package com.mactso.harderfarthergrimcitadels.commands;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import com.mactso.harderfarthergrimcitadels.Main;
import com.mactso.harderfarthergrimcitadels.config.MyConfig;
import com.mactso.harderfarthergrimcitadels.managers.GrimCitadelManager;
import com.mactso.harderfarthergrimcitadels.network.Network;
import com.mactso.harderfarthergrimcitadels.network.SyncFogToClientsPacket;
import com.mactso.harderfarthergrimcitadels.utility.Utility;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class ModCommands {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal(Main.MODID).requires((source) -> {
			return source.hasPermission(3);
		}).then(Commands.literal("setDebugLevel")
				.then(Commands.argument("debugLevel", IntegerArgumentType.integer(0, 2)).executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					return setDebugLevel(p, IntegerArgumentType.getInteger(ctx, "debugLevel"));
				}))).then(Commands.literal("setGrimCitadelsRadius").then(
						Commands.argument("grimCitadelsRadius", IntegerArgumentType.integer(4, 11)).executes(ctx -> {
							ServerPlayer p = ctx.getSource().getPlayerOrException();
							return setGrimCitadelsRadius(p, IntegerArgumentType.getInteger(ctx, "grimCitadelsRadius"));
						})))
				.then(Commands.literal("setBonusRange")
						.then(Commands.argument("bonusRange", IntegerArgumentType.integer(500, 6000)).executes(ctx -> {
							ServerPlayer p = ctx.getSource().getPlayerOrException();
							return setBonusRange(p, IntegerArgumentType.getInteger(ctx, "bonusRange"));
						})))
				.then(Commands.literal("grimEffectsInfo").executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					printGrimEffectsInfo(p);
					return 1;
				})).then(Commands.literal("grimInfo").executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					printGrimInfo(p);
					return 1;
				})).then(Commands.literal("lifeHeart").executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					reportGrimLifeHeartPulseSeconds(p);
					return 1;
				})).then(Commands.literal("grimReport").executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					String report = "Grim Citadels at : " + GrimCitadelManager.getCitadelListAsString();
					Utility.sendChat(p, report, ChatFormatting.GREEN);
					return 1;
				}))
				.then(Commands.literal("musicInfo").executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					printGrimMusicInfo(p);
					return 1;
				})).then(Commands.literal("fogInfo").executes(ctx -> {
					ServerPlayer p = ctx.getSource().getPlayerOrException();
					printFogInfo(p);
					return 1;
				}))
				.then(Commands.literal("setFogColors")
						.then(Commands.argument("R", IntegerArgumentType.integer(0, 100))
								.then(Commands.argument("G", IntegerArgumentType.integer(0, 100)).then(
										Commands.argument("B", IntegerArgumentType.integer(0, 100)).executes(ctx -> {
											ServerPlayer p = ctx.getSource().getPlayerOrException();
											int r = IntegerArgumentType.getInteger(ctx, "R");
											int g = IntegerArgumentType.getInteger(ctx, "G");
											int b = IntegerArgumentType.getInteger(ctx, "B");
											return setFogColors(p, r, g, b);
										}))))
						));
	}

	private static int setFogColors(ServerPlayer p, int r, int g, int b) {
		MyConfig.setGrimFogRedPercent(r);
		MyConfig.setGrimFogGreenPercent(g);
		MyConfig.setGrimFogBluePercent(b);
		updateGCFogToAllClients((ServerLevel) p.level(), (double) r / 100, (double) g / 100, (double) b / 100);
		printFogInfo(p);
		return 1;
	}

	private static void updateGCFogToAllClients(ServerLevel level, double r, double g, double b) {
		List<ServerPlayer> allPlayers = level.getServer().getPlayerList().getPlayers();
		Iterator<ServerPlayer> apI = allPlayers.iterator();
		SyncFogToClientsPacket msg = new SyncFogToClientsPacket(r, g, b);
		while (apI.hasNext()) { // sends to all players online.
			Network.sendToClient(msg, apI.next());
		}
	}

	private static void printFogInfo(ServerPlayer p) {

		String chatMessage = "\nFog Color Current Values";
		Utility.sendBoldChat(p, chatMessage, ChatFormatting.DARK_GREEN);
		chatMessage = "Red   (" + MyConfig.getGrimFogRedPercent() + ")" + " Green (" + MyConfig.getGrimFogGreenPercent() + ")"
				+ " Blue  (" + MyConfig.getGrimFogBluePercent() + ")";
		Utility.sendChat(p, chatMessage, ChatFormatting.GREEN);

	}

	public static int setDebugLevel(ServerPlayer p, int newDebugLevel) {
		MyConfig.setDebugLevel(newDebugLevel);
		ChatFormatting color = ChatFormatting.GREEN;
		switch (newDebugLevel) {
		case 1:
			color = ChatFormatting.YELLOW;
			break;
		case 2:
			color = ChatFormatting.RED;
			break;
		}
		Utility.sendChat(p, "Debug Level set to " + newDebugLevel + ".", color);
		return 1;
	}

	private static void printGrimEffectsInfo(ServerPlayer p) {

		Utility.sendBoldChat(p, "\nGrim Effects Info", ChatFormatting.DARK_GREEN);

		String chatMessage = ("\n  Effect Villagers ..............................: " + MyConfig.isGrimEffectVillagers()
				+ "\n  Effect Trees .....................................: " + MyConfig.isGrimEffectTrees()
				+ "\n  Effect Animals ...................................: " + MyConfig.isGrimEffectAnimals()
				+ "\n  Effect Pigs ..........................................: " + MyConfig.isGrimEffectPigs());

		Utility.sendChat(p, chatMessage, ChatFormatting.GREEN);

	}

	private static void printGrimInfo(ServerPlayer p) {
		BlockPos pPos = p.blockPosition();

		Utility.sendBoldChat(p, "\nGrim Citadel Information", ChatFormatting.DARK_GREEN);
		String chatMessage = "   Nearest Grim Citadel ...........................: "
				+ (int) Math.sqrt(GrimCitadelManager.getClosestGrimCitadelDistanceSq(pPos)) + " blocks away."
				+ "\n     at .....................................................................: "
				+ GrimCitadelManager.getClosestGrimCitadelPos(pPos).toShortString()
				+ "\n   Aura Range .................................................: "
				+ MyConfig.getGrimCitadelBonusDistance() + " blocks."
				+ "\n   Player Curse Range ...........................: "
				+ MyConfig.getGrimCitadelPlayerCurseDistance() + " blocks."
				+ "\n   Maximum Grim Difficulty ......................: " + MyConfig.getGrimCitadelMaxBoostValue() +"%"
				+ "\n   Local Difficulty ........................................: " + genFormatedDifficultyString(p) 
				+ "\n   Minimum Number of Grim Citadels..: " + MyConfig.getGrimCitadelsCount()
				+ "\n   Citadel Radius  .........................................: "
				+ GrimCitadelManager.getGrimRadius();
		Utility.sendChat(p, chatMessage, ChatFormatting.GREEN);

	}

	private static void reportGrimLifeHeartPulseSeconds(ServerPlayer p) {
		Utility.sendChat(p, "  Life Heart Pulse Rate......: " + "roughly " + MyConfig.getGrimLifeheartPulseSeconds()
				+ " seconds +/- 50%.", ChatFormatting.YELLOW);
	}

	public static int setBonusRange(ServerPlayer p, int newRange) {
		MyConfig.setBonusRange(newRange);
		printGrimInfo(p);
		return 1;
	}

	private static String genFormatedDifficultyString(ServerPlayer p) {
		String difficultyMessage;
		float difficulty = 100 * Main.difficultyCallProxy.getDifficulty(p.blockPosition());
		DecimalFormat df = new DecimalFormat("##.##");
		String formatted = df.format(difficulty);

		difficultyMessage = formatted + "%" ;
		if (MyConfig.isUsingCore()) {
			difficultyMessage += " (per HF Core)";
		} else {
			difficultyMessage += " (per Grim Citadels)";
		}
		return difficultyMessage;
	}

	private static void printGrimMusicInfo(ServerPlayer p) {
		String chatMessage = "\nMusic Attribution";
		Utility.sendBoldChat(p, chatMessage, ChatFormatting.DARK_GREEN);
		chatMessage = "Attribution Tags for Ambient Music\n" + "\n"
				+ "Lake of Destiny by Darren Curtis | https://www.darrencurtismusic.com/\n"
				+ "Music promoted by https://www.chosic.com/free-music/all/\n"
				+ "Creative Commons Attribution 3.0 Unported License\n"
				+ "https://creativecommons.org/licenses/by/3.0/\n" + "\n"
				+ "Dusty Memories by Darren Curtis | https://www.darrencurtismusic.com/\n"
				+ "Music promoted by https://www.chosic.com/free-music/all/\n"
				+ "Creative Commons Attribution 3.0 Unported License\n"
				+ "https://creativecommons.org/licenses/by/3.0/\n" + " \n"
				+ "Labyrinth of Lost Dreams by Darren Curtis | https://www.darrencurtismusic.com/\n"
				+ "Music promoted on https://www.chosic.com/free-music/all/\n"
				+ "Creative Commons Attribution 3.0 Unported (CC BY 3.0)\n"
				+ "https://creativecommons.org/licenses/by/3.0/\n" + " \n" + "\n" + "";
		Utility.sendChat(p, chatMessage, ChatFormatting.GREEN);

	}

	private static int setGrimCitadelsRadius(ServerPlayer p, int radius) {
		MyConfig.setGrimCitadelsRadius(radius);
		printGrimInfo(p);
		return 0;
	}

}
