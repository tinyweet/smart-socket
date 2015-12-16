package net.vinote.smart.socket.service.filter.impl;

import java.nio.ByteBuffer;

import net.vinote.smart.socket.service.filter.SmartFilter;
import net.vinote.smart.socket.service.filter.SmartFilterChain;
import net.vinote.smart.socket.service.process.ProtocolDataReceiver;
import net.vinote.smart.socket.transport.TransportSession;

/**
 * 业务层消息预处理器
 *
 * @author Seer
 * @version SmartFilterChainImpl.java, v 0.1 2015年8月26日 下午5:08:31 Seer Exp.
 */
public class SmartFilterChainImpl<T> implements SmartFilterChain<T> {
	private ProtocolDataReceiver<T> receiver;
	private SmartFilter<T>[] handlers = null;

	public SmartFilterChainImpl(ProtocolDataReceiver<T> receiver, SmartFilter<T>[] handlers) {
		this.receiver = receiver;
		this.handlers = handlers;
	}

	public void doReadFilter(TransportSession<T> session, T dataEntry) {
		if (dataEntry == null) {
			return;
		}
		// 接收到的消息进行预处理
		if (handlers != null && handlers.length > 0) {
			for (SmartFilter<T> h : handlers) {
				h.readFilter(session, dataEntry);
			}
		}
		boolean succ = receiver.receive(session, dataEntry);
		// 未能成功接受消息
		if (!succ && handlers != null && handlers.length > 0) {
			for (SmartFilter<T> h : handlers) {
				h.receiveFailHandler(session, dataEntry);
			}
		}

	}

	public void doWriteFilter(TransportSession<T> session, ByteBuffer buffer) {
		if (handlers != null && handlers.length > 0) {
			for (SmartFilter<T> h : handlers) {
				h.writeFilter(session, buffer);
			}
		}
	}
}
