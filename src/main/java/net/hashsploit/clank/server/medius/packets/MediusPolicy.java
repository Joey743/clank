package net.hashsploit.clank.server.medius.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.Client;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class MediusPolicy extends MediusPacket {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];

	// WORKING !!!

	public MediusPolicy() {
		super(MediusPacketType.Policy,MediusPacketType.PolicyResponse);
	}
	
	@Override
	public void read(MediusMessage mm) {
		// Process the packet
		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(messageID);
		buf.get(sessionKey);
	}

	@Override
	public MediusMessage write(Client client) {

		byte[] policy = Utils.buildByteArrayFromString("POLICY TEST", MediusConstants.POLICY_MAXLEN.getValue());
		byte[] endOfList = Utils.hexStringToByteArray("01000000");

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000"));
			outputStream.write(Utils.intToBytes(MediusCallbackStatus.MediusSuccess.getValue()));
			outputStream.write(policy);
			outputStream.write(endOfList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new MediusMessage(responseType, outputStream.toByteArray());	    
	}
}
