/**
 * This file is part of WorldInfo, licensed under the MIT License (MIT).
 *
 * Copyright (c) WorldInfo
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package tk.coaster3000.worldinfo;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import tk.coaster3000.worldinfo.common.config.BasicSettings;
import tk.coaster3000.worldinfo.common.config.Mode;
import tk.coaster3000.worldinfo.common.data.ForgePacket;
import tk.coaster3000.worldinfo.common.data.ForgePlayer;
import tk.coaster3000.worldinfo.common.packets.WICustomNamePacket;
import tk.coaster3000.worldinfo.common.packets.WINamePacket;
import tk.coaster3000.worldinfo.common.packets.WIPrimaryIDPacket;
import tk.coaster3000.worldinfo.common.packets.WISeedPacket;
import tk.coaster3000.worldinfo.common.packets.WIUidPacket;

public final class NetworkHandler {

	final SimpleNetworkWrapper channel;

	public SimpleNetworkWrapper getChannel() {
		return channel;
	}

	public NetworkHandler() {
		channel = NetworkRegistry.INSTANCE.newSimpleChannel("world_info");

		//Primary Packet
		channel.registerMessage(PrimaryIDHandler.class, WIPrimaryIDPacket.class, WIPrimaryIDPacket.discriminatorID, Side.SERVER);

		//UID packets
		if (WorldInfoMod.instance.getSettings().getProperty(BasicSettings.UIDPacketEnabled)) {
			channel.registerMessage(UIDHandler.class, WIUidPacket.class, WIUidPacket.discriminatorID, Side.SERVER);
			channel.registerMessage(UIDHandler.class, WIUidPacket.class, WIUidPacket.discriminatorID, Side.CLIENT);
		}
		//Name Packets
		if (WorldInfoMod.instance.getSettings().getProperty(BasicSettings.worldNamePacketEnabled)) {
			channel.registerMessage(NameHandler.class, WINamePacket.class, WINamePacket.discriminatorID, Side.SERVER);
			channel.registerMessage(NameHandler.class, WINamePacket.class, WINamePacket.discriminatorID, Side.CLIENT);
		}

		//Custom Name Packets
		if (WorldInfoMod.instance.getSettings().getProperty(BasicSettings.customNamePacketEnabled)) {
			channel.registerMessage(CustomNameHandler.class, WICustomNamePacket.class, WICustomNamePacket.discriminatorID, Side.SERVER);
			channel.registerMessage(CustomNameHandler.class, WICustomNamePacket.class, WICustomNamePacket.discriminatorID, Side.CLIENT);
		}

		//Seed packets
		if (WorldInfoMod.instance.getSettings().getProperty(BasicSettings.seedPacketEnabled)) {
			channel.registerMessage(SeedHandler.class, WISeedPacket.class, WISeedPacket.discriminatorID, Side.CLIENT);
			channel.registerMessage(SeedHandler.class, WISeedPacket.class, WISeedPacket.discriminatorID, Side.SERVER);
		}
	}

	public static class PrimaryIDHandler implements IMessageHandler<WIPrimaryIDPacket, ForgePacket> {
		@Override
		public ForgePacket onMessage(WIPrimaryIDPacket message, MessageContext ctx) {
			ForgePlayer player;

			if (WorldInfoMod.instance.getSide() == Side.CLIENT)
				player = grabClientPlayer();
			else
				player = grabServerPlayer(ctx);

			WorldInfoMod.logger.info(String.format("Request Received from \"%s\" and will now respond accordingly...", player.getName()));

			String id;
			try {
				Mode m = WorldInfoMod.instance.getSettings().getSpecialProperty(BasicSettings.primaryMode);
				switch (m) {
					case UUID:
						return new WIUidPacket(player.getWorld().getUID().toString());
					case CustomName:
						return new WICustomNamePacket(player.getWorld().getCustomName());
					case Name:
						return new WINamePacket(player.getWorld().getName());
					case Seed:
						return new WISeedPacket(player.getWorld().getSeed());
					default:
						throw new Exception(String.format("Missing case clause for an the primary mode. The missing case is %s", m.name()));
				}
			} catch (Throwable t) {
				t.printStackTrace();
				id = "";
			}
			return new WIPrimaryIDPacket(id);
		}
	}

	public static class UIDHandler implements IMessageHandler<WIUidPacket, WIUidPacket> {

		@Override
		public WIUidPacket onMessage(WIUidPacket message, MessageContext ctx) {
			ForgePlayer player;
			if (WorldInfoMod.instance.getSide() == Side.CLIENT)
				player = grabClientPlayer();
			else
				player = grabServerPlayer(ctx);

			switch (ctx.side) {
				case CLIENT:
					player.sendMessage(String.format("World UID: %s", message.getMessage()));
					return null;
				case SERVER:
					try {
						return new WIUidPacket(player.getWorld().getUID().toString());
					} catch (Exception e) {
						WorldInfoMod.logger.error("An error occurred while generating the world UID packet information for player...", e);
						return null;
					}
				default:
					WorldInfoMod.logger.error("Side case was not caught. It fell through!", new Exception());
			}

			return null;
		}
	}

	public static class SeedHandler implements IMessageHandler<WISeedPacket, WISeedPacket> {
		@Override
		public WISeedPacket onMessage(WISeedPacket message, MessageContext ctx) {
			ForgePlayer player;
			if (WorldInfoMod.instance.getSide() == Side.CLIENT)
				player = grabClientPlayer();
			else
				player = grabServerPlayer(ctx);

			switch (ctx.side) {
				case CLIENT:
					player.sendMessage(String.format("World Seed: %d", message.getSeed()));
					return null;
				case SERVER:

					try {
						return new WISeedPacket(player.getWorld().getSeed());
					} catch (Exception e) {
						WorldInfoMod.logger.error("An error occurred while generating the world UID packet information for player...", e);
						return null;
					}
				default:
					WorldInfoMod.logger.error("Side case was not caught. It fell through!", new Exception());
			}

			return null;
		}
	}

	public static class NameHandler implements IMessageHandler<WINamePacket, WINamePacket> {
		@Override
		public WINamePacket onMessage(WINamePacket message, MessageContext ctx) {
			ForgePlayer player;
			if (WorldInfoMod.instance.getSide() == Side.CLIENT)
				player = grabClientPlayer();
			else
				player = grabServerPlayer(ctx);

			switch (ctx.side) {
				case CLIENT:
					player.sendMessage(String.format("World Name: %s", message.getMessage()));
					return null;
				case SERVER:

					try {
						return new WINamePacket(player.getWorld().getName());
					} catch (Exception e) {
						WorldInfoMod.logger.error("An error occurred while generating the world UID packet information for player...", e);
						return null;
					}
				default:
					WorldInfoMod.logger.error("Side case was not caught. It fell through!", new Exception());
			}

			return null;
		}
	}


	@SideOnly(Side.CLIENT)
	static ForgePlayer grabClientPlayer() {
		return WorldInfoMod.instance.wrapPlayer(FMLClientHandler.instance().getClientPlayerEntity());
	}

	@SideOnly(Side.SERVER)
	static ForgePlayer grabServerPlayer(MessageContext ctx) {
		return WorldInfoMod.instance.wrapPlayer(ctx.getServerHandler().playerEntity);
	}

	public static class CustomNameHandler implements IMessageHandler<WICustomNamePacket, WICustomNamePacket> {
		@Override
		public WICustomNamePacket onMessage(WICustomNamePacket message, MessageContext ctx) {
			ForgePlayer player;
			if (WorldInfoMod.instance.getSide() == Side.CLIENT)
				player = grabClientPlayer();
			else
				player = grabServerPlayer(ctx);

			switch (ctx.side) {
				case CLIENT:
					player.sendMessage(String.format("World Name: %s", message.getMessage()));
					return null;
				case SERVER:

					try {
						return new WICustomNamePacket(player.getWorld().getCustomName());
					} catch (Exception e) {
						WorldInfoMod.logger.error("An error occurred while generating the world UID packet information for player...", e);
						return null;
					}
				default:
					WorldInfoMod.logger.error("Side case was not caught. It fell through!", new Exception());
			}

			return null;
		}
	}
}
