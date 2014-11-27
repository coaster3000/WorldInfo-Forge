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
package tk.coaster3000.worldinfo.common;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import tk.coaster3000.worldinfo.WorldInfoMod;
import tk.coaster3000.worldinfo.common.data.ForgePlayer;
import tk.coaster3000.worldinfo.common.packets.WINamePacket;
import tk.coaster3000.worldinfo.common.packets.WISeedPacket;
import tk.coaster3000.worldinfo.common.packets.WIUidPacket;

public class DebugCommand extends CommandBase {

	public String getCommandName() {
		return "wi-debug";
	}

	public String getCommandUsage(ICommandSender sender) {
		return "/wi-debug [id|name|seed]";
	}

	public void processCommand(ICommandSender sender, String[] args) {
		ForgePlayer player = WorldInfoMod.instance.wrapPlayer(getCommandSenderAsPlayer(sender));

		player.sendPacket(new WINamePacket(player.getWorld().getName()));
		player.sendPacket(new WISeedPacket(player.getWorld().getSeed()));
		player.sendPacket(new WIUidPacket(player.getWorld().getUID().toString()));
	}
}
