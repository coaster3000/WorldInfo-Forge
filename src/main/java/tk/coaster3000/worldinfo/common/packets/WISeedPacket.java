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
package tk.coaster3000.worldinfo.common.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import tk.coaster3000.worldinfo.WorldInfoMod;
import tk.coaster3000.worldinfo.common.data.ForgePacket;

import static tk.coaster3000.worldinfo.WorldInfo.PLUGIN_CHANNEL;

public class WISeedPacket extends ForgePacket {
	public static final byte discriminatorID = 3;

	private long seed = 0;

	public WISeedPacket() {
		this(0);
	}

	public WISeedPacket(long seed) {
		super(PLUGIN_CHANNEL, discriminatorID);
		this.seed = seed;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			seed = buf.readLong();
		} catch (Throwable thrown) {
			WorldInfoMod.logger.error(String.format("Error occurred while decoding %s", getClass().getSimpleName()), thrown);
		}
	}

	public long getSeed() {
		return seed;
	}

	public byte[] getData() {
		ByteBuf buffer = new UnpooledByteBufAllocator(false).buffer();

		buffer.writeLong(getSeed());

		return buffer.array();
	}
}
