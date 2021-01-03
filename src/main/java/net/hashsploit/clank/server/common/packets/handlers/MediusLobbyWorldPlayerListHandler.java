package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusConnectionType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.objects.MediusPlayerStatus;
import net.hashsploit.clank.server.common.packets.serializers.LobbyWorldPlayerListRequest;
import net.hashsploit.clank.server.common.packets.serializers.LobbyWorldPlayerListResponse;

public class MediusLobbyWorldPlayerListHandler extends MediusPacketHandler {

	private LobbyWorldPlayerListRequest reqPacket;
	private LobbyWorldPlayerListResponse respPacket;
	
	public MediusLobbyWorldPlayerListHandler() {
		super(MediusMessageType.AccountLogin, MediusMessageType.AccountLoginResponse);
	}
	
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new LobbyWorldPlayerListRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public void write(MediusClient client) {
		
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.SUCCESS;
		MediusPlayerStatus playerStatus = MediusPlayerStatus.MEDIUS_PLAYER_IN_CHAT_WORLD;
		int accountId = 21;
		String accountName = "Aeq";
		byte[] stats = new byte[MediusConstants.ACCOUNTSTATS_MAXLEN.value];
		MediusConnectionType connectionType = MediusConnectionType.ETHERNET;
		boolean endOfList = true;
		
		
		respPacket = new LobbyWorldPlayerListResponse(reqPacket.getMessageId(), callbackStatus, playerStatus, accountId, accountName, stats, connectionType, endOfList);
		
       	client.sendMediusMessage(respPacket);
	}


}