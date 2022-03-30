package com.st1.dwr;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.WebContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatServer {
	static final Logger logger = LoggerFactory.getLogger(ChatServer.class);

	private MessageSender sender = null;

	public ChatServer() {
		sender = new MessageSender(this);
		Thread senderThread = new Thread(sender, "Message Sender");
		senderThread.setDaemon(true);
		senderThread.start();
	}

	public void join(final String name) {
		if (name != null && name.trim().length() > 0) {
			ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
			String sessionId = scriptSession.getId();

			System.out.println("new member:" + name);
			if (!members.containsKey(name)) {
				members.put(name, sessionId);
				Browser.withSession(sessionId, new Runnable() {
					@Override
					public void run() {
						ScriptSessions.addFunctionCall("signon");
					}
				});
				Browser.withCurrentPage(new Runnable() {
					public void run() {
						ScriptSessions.addFunctionCall("members", new ArrayList<String>(members.keySet()));
					}
				});
			} else {

				Browser.withSession(scriptSession.getId(), new Runnable() {
					@Override
					public void run() {
						ScriptSessions.addFunctionCall("showError", name + " already exists!");

					}
				});
			}
		}
	}

	/**
	 * @param text The new message text to add
	 */
	public void addMessage(Message message) {
		if (message.text != null && message.text.trim().length() > 0) {
			final String t = message.from + ":" + message.text;
			System.out.println("addMessage:" + t);
			if (message.to.toUpperCase(Locale.TAIWAN).equals("ALL")) {
				sender.addMessage(t);
			} else {
				System.out.println("private message, from :" + message.from + ", to:" + message.to);
				String sessionId = members.get(message.to);
				Browser.withSession(sessionId, new Runnable() {
					@Override
					public void run() {
						ScriptSessions.addFunctionCall("receiveMessages", Arrays.asList("*" + t));
					}
				});
			}

		}
	}

	public void broadcast(final ArrayList<String> messages) {

		Browser.withAllSessions(new Runnable() {
			@Override
			public void run() {
				System.out.println("sending messages");
				ScriptSessions.addFunctionCall("receiveMessages", messages);
				System.out.println("sent");
			}
		});
	}

	/**
	 * The current set of messages
	 */
	private final HashMap<String, String> members = new HashMap<String, String>();
	private static Object lockObject = new Object();

	private class MessageSender implements Runnable {
		private boolean running = true;
		private boolean done = false;
		protected final ArrayList<String> messages = new ArrayList<String>();
		private final ArrayList<String> pendingMessages = new ArrayList<String>();

		private ChatServer server;

		public MessageSender(ChatServer server) {
			this.server = server;
		}

		public void addMessage(String t) {
			System.out.println("add message:" + t);
			synchronized (messages) {
				messages.add(t);
				// notify();
				messages.notify();
				System.out.println("notifyed");
			}
		}

		@Override
		public void run() {
			while (running) {

				if (messages.size() == 0) {
					try {
						System.out.println("sneder: no message, wait");
						synchronized (messages) {
							messages.wait();
						}
					} catch (InterruptedException e) {
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						logger.error(errors.toString());
					}
				}

				synchronized (messages) {
					pendingMessages.clear();
					pendingMessages.addAll(messages);
					messages.clear();
				}

				Runnable r = new Runnable() {
					@Override
					public void run() {
						System.out.println("sending messages");
						ScriptSessions.addFunctionCall("receiveMessages", pendingMessages);
						System.out.println("sent");
						done = true;
						synchronized (this) {
							notifyAll();
						}
						System.out.println("sent/notify");
					}
				};

				try {
					Browser.withAllSessions(r);
					System.out.println("started");
					if (!done) {
						synchronized (r) {
							r.wait();

						}
						System.out.println("wake up");
					} else {
						System.out.println("done");
					}
				} catch (Exception ex) {
					StringWriter errors = new StringWriter();
					ex.printStackTrace(new PrintWriter(errors));
					logger.error(errors.toString());
				}

			}

		}
	}

}
