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
package tk.coaster3000.worldinfo.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import tk.coaster3000.worldinfo.WorldInfoMod;
import tk.coaster3000.worldinfo.common.CommonListener;
import tk.coaster3000.worldinfo.common.packets.WIPrimaryIDPacket;

public final class DebugListener extends CommonListener {

	private static final int VERSION = 1;

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onInteract(PlayerInteractEvent event) {
		if (event.entityPlayer.getHeldItem().getItem() != WorldInfoMod.debugItem) return;

		final SimpleNetworkWrapper channel = WorldInfoMod.instance.getNetworkHandler().getChannel();

		channel.sendToServer(new WIPrimaryIDPacket());

		WorldInfoMod.logger.info(String.format("%nExecuted debug code Version: %d%n", VERSION));
	}

	public void register() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	public void unregister() {
		MinecraftForge.EVENT_BUS.unregister(this);
	}
}
