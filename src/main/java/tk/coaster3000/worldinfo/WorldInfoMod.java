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

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.command.ICommand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import org.apache.logging.log4j.Logger;
import tk.coaster3000.worldinfo.client.DebugListener;
import tk.coaster3000.worldinfo.common.DebugCommand;
import tk.coaster3000.worldinfo.common.config.ForgeSettings;
import tk.coaster3000.worldinfo.common.data.ForgePacket;
import tk.coaster3000.worldinfo.common.data.ForgePlayer;
import tk.coaster3000.worldinfo.common.data.ForgeWorld;
import tk.coaster3000.worldinfo.common.data.ICommonPacket;

import java.util.HashMap;
import java.util.Map;

@Mod(modid = WorldInfoMod.MODID, name = WorldInfoMod.NAME, version = WorldInfoMod.VERSION, canBeDeactivated = true)
public final class WorldInfoMod extends WorldInfo<EntityPlayer, Object, World, ForgePlayer, ForgePacket, ForgeWorld> {

	public static final String MODID = "worldinfo";
	public static final String VERSION = "1.0.0";
	public static final String NAME = "WorldInfo";

	@Mod.Instance(MODID)
	public static WorldInfoMod instance;

	private Side side;

	private ForgeSettings settings;

	public static Logger logger;

	public static Item debugItem = null;

	private DebugListener debugListener;

	private NetworkHandler handler;

	private ForgeListener listener;
	private final Map<String, ICommand> commands = new HashMap<String, ICommand>();

	public ForgeListener getListener() {
		return listener;
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		WorldInfoMod.logger = event.getModLog();
		settings = new ForgeSettings(event.getSuggestedConfigurationFile());
		loadSettings();

		handler = new NetworkHandler();
	}

	@EventHandler
	public void onServerInit(FMLServerStartingEvent event) {
		DebugCommand cmd = new DebugCommand();

		commands.put(cmd.getCommandName(), cmd);
		event.registerServerCommand(cmd);
	}

	@EventHandler
	public void onInit(FMLInitializationEvent event) {
		this.handler = new NetworkHandler();
		this.side = event.getSide();

		WorldInfoMod.debugItem = new DebugItem().setTextureName(String.format("%s:debug", MODID));

		GameRegistry.registerItem(debugItem, debugItem.getUnlocalizedName());

		debugListener = new DebugListener();
		debugListener.register();

		listener = new ForgeListener();

		listener.register();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

	@Override
	public ForgeSettings getSettings() {
		return settings;
	}

	@Override
	public ForgePacket wrapPacket(Object packet) {
		if (packet instanceof ForgePacket)
			return (ForgePacket) packet;
		else if (packet instanceof ICommonPacket)
			return wrapPacket((ICommonPacket) packet);
		else
			throw new IllegalArgumentException("Not a valid packet object type to wrap!");
	}

	public ForgePacket wrapPacket(final ICommonPacket packet) {
		if (packet instanceof ForgePacket)
			return (ForgePacket) packet;

		return new ForgePacket(packet) { //TODO: Somehow make this actual class objects
			byte[] data = packet.getData();

			public void fromBytes(ByteBuf buf) {
				data = buf.array();
			}

			public byte[] getData() {
				return data;
			}
		};
	}

	@Override
	public ForgePlayer wrapPlayer(EntityPlayer player) {
		return new ForgePlayer(player);
	}

	@Override
	public ForgeWorld wrapWorld(World world) {
		return new ForgeWorld(world);
	}

	public NetworkHandler getNetworkHandler() {
		return handler;
	}

	public Side getSide() {
		return side;
	}
}
