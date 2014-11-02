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

import net.minecraft.world.World;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class ForgeWorld extends CommonWorld {
	protected World world;
	private File worldFolder;
	private File uidFile;

	public ForgeWorld(World world) {
		this.world = world;
		worldFolder = world.getSaveHandler().getWorldDirectory();
		uidFile = new File(worldFolder, "uid");
	}

	@Override
	protected void generateUID() {
		this.uid = UUID.randomUUID();
		save();
	}

	@Override
	public File getWorldFolder() {
		return worldFolder;
	}

	public void load() {
		try {
			this.uid = UUID.fromString(FileUtils.readFileToString(uidFile));
		} catch (FileNotFoundException e) {
			generateUID();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			FileUtils.writeStringToFile(uidFile, this.uid.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
