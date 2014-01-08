package be.vdab;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
	public static void main(String[] args) throws IOException{
		//int portNumber = Integer.parseInt(args[0]);
		int portNumber = 8082;
		int numberOfPlayers = 1;
		int maxPlayers = 2;
		//Board board = new Board();
		//board.init();

		try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
			while (true) {
				if(numberOfPlayers <= maxPlayers){
					Socket client = serverSocket.accept();
					ClientHandler c = new ClientHandler(client, numberOfPlayers);
					System.out.println("Player " + numberOfPlayers + " has connected...");
					c.start();
					numberOfPlayers++;
				} else {
					System.out.println("Maximum players reached !");
					break;
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}