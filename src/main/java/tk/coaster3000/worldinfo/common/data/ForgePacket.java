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

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import org.apache.logging.log4j.Logger;
import tk.coaster3000.worldinfo.WorldInfoMod;

public class ForgePacket extends CommonPacket implements IMessage {

	private static final Logger log = WorldInfoMod.logger;

	public ForgePacket(String channel, byte id, byte[] data) {
		super(channel, id, data);
	}

	public ForgePacket(CommonPacket packet) {
		id = packet.id;
		channel = packet.channel;
		data = packet.data;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		id = buf.readByte();
		int len = buf.readInt();
		data = new byte[len];
		buf.readBytes(data, 0, len);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		//FIXME: BREAKPOINT ME BITCH	buf.writeByte(id).writeInt(getDataLength()).writeBytes(data);
		log.debug("LOG00000:ByteBuf supplies value's by default: " + buf);
		log.debug("LOG00020:ByteBuf now reads as this: " + buf); //FIXME: BREAKPOINT ME BITCH
	}
}
