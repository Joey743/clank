package net.hashsploit.clank.server.common.packets.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.MediusConfig;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RTMessageId;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.packets.serializers.GetAnnouncementsRequest;
import net.hashsploit.clank.server.common.packets.serializers.GetAnnouncementsResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusGetAnnouncementsHandler extends MediusPacketHandler {

	private GetAnnouncementsRequest reqPacket;
	private GetAnnouncementsResponse respPacket;

	public MediusGetAnnouncementsHandler() {
		super(MediusMessageType.GetAnnouncements, MediusMessageType.GetAnnouncementsResponse);
	}

	@Override
	public void read(MediusMessage mm) {
		reqPacket = new GetAnnouncementsRequest(mm.getPayload());
	}

	@Override
	public void write(MediusClient client) {

		final List<String> announcements = ((MediusConfig) Clank.getInstance().getConfig()).getAnnouncements();

		if (announcements != null && announcements.size() > 0) {
			for (int i = 0; i < announcements.size(); i++) {
				String announcementString = announcements.get(i);

				final int maxSegmentedAnnouncementLength = MediusConstants.ANNOUNCEMENT_MAXLEN.value - 1; // leave one byte of room for the null terminator

				final List<String> segmentedAnnouncementStrings = new ArrayList<String>();

				if (announcementString.length() > maxSegmentedAnnouncementLength) {
					for (int j = 0; j < announcementString.length(); j += maxSegmentedAnnouncementLength) {
						int remaining = j + maxSegmentedAnnouncementLength;
						if (remaining > announcementString.length()) {
							remaining = announcementString.length();
						}

						String segment = announcementString.substring(j, remaining);

						segmentedAnnouncementStrings.add(segment);
					}
				} else {
					segmentedAnnouncementStrings.add(announcementString);
				}

				// TODO: when queues are added, create a for-loop to iterate through 64-byte
				// length chunks of the policy set in the config.
				// enqueue each of the chunks respectively.
				final List<MediusMessage> mediusMessages = new ArrayList<MediusMessage>();
				for (int j = 0; j < segmentedAnnouncementStrings.size(); j++) {

					final MediusCallbackStatus callbackStatus = MediusCallbackStatus.SUCCESS;
					final int announcementId = i;
					final byte[] announcement = Utils.buildByteArrayFromString(segmentedAnnouncementStrings.get(j), maxSegmentedAnnouncementLength + 1);
					final boolean endOfList = announcements.size() - 1 == i;

					final GetAnnouncementsResponse announcementResponse = new GetAnnouncementsResponse(reqPacket.getMessageId(), callbackStatus, announcementId, announcement, endOfList);

					logger.finest(announcementResponse.getDebugString());
					mediusMessages.add(announcementResponse);
				}

				// FIXME: bad practice, this should be enqueued and the pipeline should be
				// rewritten.
				for (final MediusMessage mediusMessage : mediusMessages) {
					client.sendMessage(new RTMessage(RTMessageId.SERVER_APP, mediusMessage.toBytes()));
				}

			}
		} else {
			final MediusCallbackStatus callbackStatus = MediusCallbackStatus.NO_RESULT;
			final int announcementId = 1;
			final byte[] announcement = Utils.padByteArray(new byte[0], MediusConstants.ANNOUNCEMENT_MAXLEN.value);
			final boolean endOfList = true;

			final GetAnnouncementsResponse announcementResponse = new GetAnnouncementsResponse(reqPacket.getMessageId(), callbackStatus, announcementId, announcement, endOfList);
			
			client.sendMessage(new RTMessage(RTMessageId.SERVER_APP, announcementResponse.toBytes()));
		}
	}

}