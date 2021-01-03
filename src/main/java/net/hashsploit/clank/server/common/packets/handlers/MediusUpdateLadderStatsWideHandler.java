package net.hashsploit.clank.server.common.packets.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RTMessageId;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class MediusUpdateLadderStatsWideHandler extends MediusPacketHandler {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.value];
	private byte[] ladderType = new byte[4];
	private byte[] stats = new byte[MediusConstants.LADDERSTATSWIDE_MAXLEN.value];
	
	public MediusUpdateLadderStatsWideHandler() {
		super(MediusMessageType.UpdateLadderStatsWide,MediusMessageType.UpdateLadderStatsWideResponse);
	}
	
	@Override
	public void read(MediusMessage mm) {
		// Process the packet
		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(messageID);
		buf.get(ladderType);
		buf.get(stats);
	}

	@Override
	public void write(MediusClient client) {
		byte[] statsResponse = Utils.buildByteArrayFromString("0000000", 7); // empty 7 byte array
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageID);
			outputStream.write(statsResponse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		client.sendMediusMessage(new MediusMessage(responseType, outputStream.toByteArray()));
	}

}
