package game.net.channelobj;

import java.net.SocketAddress;

import game.net.message.AbstractMessage;
import io.netty.channel.ChannelFuture;

public interface IRemoteNode {

	SocketAddress getAddress();

	void setAddress(SocketAddress address);

	Object getChannel();

	void close();

	boolean isConnected();

	void writeAndClose(AbstractMessage msg);

	ChannelFuture writeAndFlush(AbstractMessage msg);

	void write(AbstractMessage msg);

	void flush();

	void setDownSequenceId(int newSeqId);

	Integer getDownSequenceId();

	void setUpSequenceId(int newSeqId);

	Integer getUpSequenceId();

	void addActiveCount(int num);

	void setActiveCount(int activeCount);

	int getActiveCount();

	void setUserId(long id);

	long getUserId();

	void setAttachement(Object attachement);

	Object getAttachement();

	long getId();


}
