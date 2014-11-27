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
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.apache.logging.log4j.Logger;
import tk.coaster3000.worldinfo.common.config.ForgeSettings;
import tk.coaster3000.worldinfo.common.data.CommonPacket;
import tk.coaster3000.worldinfo.common.data.ForgePacket;
import tk.coaster3000.worldinfo.common.data.ForgePlayer;
import tk.coaster3000.worldinfo.common.data.ForgeWorld;

@Mod(modid = WorldInfoMod.MODID, name = WorldInfoMod.NAME, version = WorldInfoMod.VERSION, canBeDeactivated = true)
public class WorldInfoMod extends WorldInfo<EntityPlayer, Object, World> {

	public static final String MODID = "worldinfo";
	public static final String VERSION = "1.0";
	public static final String NAME = "WorldInfo";

	@Mod.Instance(MODID)
	public static WorldInfoMod instance;

	private Side side;

	private ForgeSettings settings;

	public static Logger logger;


	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		settings = new ForgeSettings(event.getSuggestedConfigurationFile());
		loadSettings();
	}

	@EventHandler
	public void onInit(FMLInitializationEvent event) {
		this.side = event.getSide();

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
		return null;
	}

	public ForgePacket wrapPacket(CommonPacket packet) {
		return new ForgePacket(packet);
	}

	@Override
	public ForgePlayer wrapPlayer(EntityPlayer player) {
		return new ForgePlayer(player);
	}

	@Override
	public ForgeWorld wrapWorld(World world) {
		return new ForgeWorld(world);
	}

	public Side getSide() {
		return side;
	}
}
