package be.vdab;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;

public class SocketServer {
	public static void main(String[] args) throws IOException {
		// int portNumber = Integer.parseInt(args[0]);
		int portNumber = 7777;
		int numberOfPlayers = 1;
		boolean firstPlayer = true;

		try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
			while (true) {

				Socket client = serverSocket.accept();
				ClientHandler c = new ClientHandler(client, numberOfPlayers, firstPlayer);
				System.out.println("Player " + numberOfPlayers + " has connected...");
				c.start();
				numberOfPlayers++;
				firstPlayer = false;
				if (((numberOfPlayers % 2) != 0) && numberOfPlayers != 1) {
					firstPlayer = true;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}