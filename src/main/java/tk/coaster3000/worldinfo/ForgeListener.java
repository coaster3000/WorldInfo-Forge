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
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import tk.coaster3000.worldinfo.common.CommonListener;
import tk.coaster3000.worldinfo.common.packets.WINamePacket;
import tk.coaster3000.worldinfo.common.packets.WISeedPacket;
import tk.coaster3000.worldinfo.common.packets.WIUidPacket;

public class ForgeListener extends CommonListener {

	private final SimpleNetworkWrapper handle;

	public ForgeListener() {
		this.handle = WorldInfoMod.instance.getNetworkHandler().getChannel();
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		Minecraft mc = FMLClientHandler.instance().getClient();

		if (!mc.isSingleplayer() && mc.thePlayer != null) {
			requestWorlduid();
			requestWorldSeed();
			requestName();
		}
	}
	@SideOnly(Side.CLIENT)
	private void requestName() {
		handle.sendToServer(new WINamePacket());
	}

	@SideOnly(Side.CLIENT)
	private void requestWorldSeed() {
		handle.sendToServer(new WISeedPacket());
	}

	@SideOnly(Side.CLIENT)
	private void requestWorlduid() {
		handle.sendToServer(new WIUidPacket());
	}

	public void register() {
		MinecraftForge.EVENT_BUS.register(this);
		WorldInfoMod.logger.info("Events registered");
	}

	public void unregister() {
		MinecraftForge.EVENT_BUS.unregister(this);
		WorldInfoMod.logger.info("Events unregistered");
	}
}
