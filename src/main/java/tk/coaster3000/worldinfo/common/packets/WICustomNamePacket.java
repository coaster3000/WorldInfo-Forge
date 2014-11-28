package tk.coaster3000.worldinfo.common.packets;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import tk.coaster3000.worldinfo.WorldInfoMod;
import tk.coaster3000.worldinfo.common.data.ForgePacket;

import static tk.coaster3000.worldinfo.WorldInfo.PLUGIN_CHANNEL;
import static tk.coaster3000.worldinfo.util.LogicUtil.fixNull;

public class WICustomNamePacket extends ForgePacket {
	public static final byte discriminatorID = 3;

	private String message = "";

	public WICustomNamePacket() {
		this("");
	}

	public WICustomNamePacket(String msg) {
		super(PLUGIN_CHANNEL, discriminatorID);
		message = fixNull(msg, "");

		ByteBuf buffer = new UnpooledByteBufAllocator(false).buffer(message.length());
		ByteBufUtils.writeUTF8String(buffer, message);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			message = ByteBufUtils.readUTF8String(buf);
		} catch (Throwable thrown) {
			WorldInfoMod.logger.error(String.format("Error occurred while decoding %s", getClass().getSimpleName()), thrown);
		}
	}

	public String getMessage() {
		return message;
	}

	public byte[] getData() {
		ByteBuf buffer = new UnpooledByteBufAllocator(false).buffer();

		ByteBufUtils.writeUTF8String(buffer, getMessage());

		return buffer.array();
	}
}
