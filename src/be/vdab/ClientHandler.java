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

	public ClientHandler(Socket socket, int player) throws IOException {
		this.player = player;
		this.socket = socket;
		input = new Scanner(socket.getInputStream());
		output = new PrintWriter(socket.getOutputStream());
	}
	
	protected static List<ClientHandler> handlers = new ArrayList<>();

	public void run() {
		try {
			handlers.add(this);
			while (true) {
				int line = input.nextInt();
				int turn = 0;
				if(line % 10 == 1) {turn = 0;} else {turn = 1;}
				arrangeBoard((player*100) + (line) + turn);
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
	
	protected void arrangeBoard(int line){
		synchronized (handlers) {
			for(ClientHandler clientHandler : handlers){
				synchronized (clientHandler.output) {
					System.out.println("arrangeboard with: " + line);
					clientHandler.output.println(line);
				}
				clientHandler.output.flush();
			}
		}
	}
}
