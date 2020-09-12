package net.hashsploit.clank.server.medius.MediusPackets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
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
import net.hashsploit.clank.utils.Utils;

public class MediusAccountUpdateStats extends MediusPacket {
	
	private static final Logger logger = Logger.getLogger("");
    
    public MediusAccountUpdateStats() {
        super(MediusPacketType.AccountUpdateStats);
    }
    
    @Override
    public void process(Client client, ChannelHandlerContext ctx, byte[] packetData) { 
    	// Process the packet    	
    	ByteBuffer buf = ByteBuffer.wrap(packetData);
    	
    	//byte[] finalPayload = Utils.hexStringToByteArray("0a1e00019731000000000000000000000000000000000000000000000000000000");
    	byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
    	byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
    	byte[] stats = new byte[MediusConstants.ACCOUNTSTATS_MAXLEN.getValue()];
    	
    	
    	buf.get(messageID);
    	buf.get(sessionKey);
    	buf.get(stats);

    	byte[] statsResponse = Utils.buildByteArrayFromString("0000000", 7); // empty 7 byte array    	    	
    	
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write(MediusPacketType.AccountUpdateStatsResponse.getShortByte());
			outputStream.write(messageID);
			outputStream.write(statsResponse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Combine RT id and len
		byte[] data = outputStream.toByteArray();
		logger.fine("Data: " + Utils.bytesToHex(data));
		DataPacket packet = new DataPacket(RTPacketId.SERVER_APP, data);
		
		byte[] finalPayload = packet.toData().array();
		logger.fine("Final payload: " + Utils.bytesToHex(finalPayload));
        ByteBuf msg = Unpooled.copiedBuffer(finalPayload);
        ctx.write(msg); // (1)
        ctx.flush(); // (2)
    }

}
