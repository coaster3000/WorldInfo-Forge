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
package tk.coaster3000.worldinfo.common.data;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import tk.coaster3000.worldinfo.WorldInfoMod;

public class ForgePlayer extends CommonPlayer<ForgeWorld> {
	private final EntityPlayer player;

	public ForgePlayer(EntityPlayer player) {
		this.player = player;
	}

	@Override
	public void sendMessage(String message) {
		player.addChatComponentMessage(new ChatComponentText(message));
	}

	@Override
	public void sendPacket(CommonPacket packet) {
		ForgePacket pack = WorldInfoMod.instance.wrapPacket(packet);
		if (WorldInfoMod.instance.getSide() == Side.CLIENT) WorldInfoMod.instance.getPacketChannel().sendToServer(pack);
		else if (player instanceof EntityPlayerMP)
			WorldInfoMod.instance.getPacketChannel().sendTo(pack, (EntityPlayerMP) player);
		else
			WorldInfoMod.logger.error("Mod initialized incorrectly. Side is server yet player object is not EntityPlayerMP. \n Please contact the "
					+ "mod author about this error.");
	}

	@Override
	public ForgeWorld getWorld() {
		return WorldInfoMod.instance.wrapWorld(player.getEntityWorld());
	}
}
