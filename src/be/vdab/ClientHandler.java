package be.vdab;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientHandler extends Thread {
	protected int player;
	protected boolean firstPlayerPlaysFirst = true;
	protected Socket socket;
	protected Scanner input;
	protected PrintWriter output;
	protected boolean firstPlayer;

	public ClientHandler(Socket socket, int player, boolean firstPlayer) throws IOException {
		this.player = player;
		this.socket = socket;
		this.firstPlayer = firstPlayer;
		input = new Scanner(socket.getInputStream());
		output = new PrintWriter(socket.getOutputStream());
	}

	protected static List<ClientHandler> handlers = new ArrayList<>();

	public void run() {
		if (firstPlayer) {
			
			this.output.println(1);
			this.output.flush();
			firstPlayer = false;
		} else {
			this.output.println(0);
			this.output.flush();
		}
		try {
			handlers.add(this);
			while (true) {
				int line = input.nextInt();
				arrangeBoard((player * 100) + (line));
				System.out.println("incoming message: " + line);
			}
		} finally {
			handlers.remove(this);
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void arrangeBoard(int line) {
		synchronized (handlers) {
			for (ClientHandler clientHandler : handlers) {
				synchronized (clientHandler.output) {
					System.out.println("arrangeboard with: " + line);
					clientHandler.output.println(line);
				}
				clientHandler.output.flush();
			}
		}
	}
}
