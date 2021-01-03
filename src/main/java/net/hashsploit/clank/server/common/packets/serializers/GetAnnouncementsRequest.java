package net.hashsploit.clank.server.common.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;

public class GetAnnouncementsRequest extends MediusMessage {

	private byte[] messageId = new byte[MediusConstants.MESSAGEID_MAXLEN.value];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.value];
	private int appId;
	
	public GetAnnouncementsRequest(byte[] data) {
		super(MediusMessageType.GetAnnouncements, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(messageId);
		buf.get(sessionKey);
		buf.get(new byte[2]);
		buf.get(appId);
	}
	
	public byte[] getMessageId() {
		return messageId;
	}
	
}
