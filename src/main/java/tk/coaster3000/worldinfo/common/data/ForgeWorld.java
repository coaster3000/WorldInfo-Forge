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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import tk.coaster3000.worldinfo.WorldInfoMod;

import java.io.File;

public class ForgeWorld extends CommonWorld<ForgePlayer> {
	private final World world;
	private final File worldFolder;
	private final File uidFile;

	public File getUidFile() {
		return uidFile;
	}

	protected void handleException(Exception ex) {
		WorldInfoMod.logger.error(ex); //TODO: Implement stack trace filtering...
	}

	public ForgeWorld(World world) {
		this.world = world;
		worldFolder = world.getSaveHandler().getWorldDirectory();
		uidFile = new File(worldFolder, "uid.dat");
		name = world.getSaveHandler().getWorldDirectoryName();
		load();
	}

	@Override
	public File getWorldFolder() {
		return worldFolder;
	}

	public long getSeed() {
		return world.getSeed();
	}

	public ForgePlayer[] getPlayers() {
		EntityPlayer[] players = (EntityPlayer[]) world.playerEntities.toArray();

		ForgePlayer[] forgePlayers = new ForgePlayer[players.length];

		for (int i = 0; i < players.length; i++)
			forgePlayers[i] = new ForgePlayer(players[i]);

		return forgePlayers;
	}

	public boolean hasPlayer(ForgePlayer player) {
		return world.getPlayerEntityByName(player.getName()) != null;
	}
}
