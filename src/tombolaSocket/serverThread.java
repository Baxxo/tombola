package tombolaSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class serverThread extends Thread {

	// Array di PrintWriter
	private boolean isStarted = true;
	Socket s;
	ServerSocket ss;
	static ArrayList<PrintWriter> clientList = new ArrayList<PrintWriter>();
	private Server server;

	private static class ServerThread extends Thread {
		Socket s;
		private Server server;

		// il costruttore deve ricevere il socket su cui lavorare
		public ServerThread(Socket s, Server server) {
			this.server = server;
			this.s = s;
		}

		@Override
		public void run() {
			super.run();
			// resta in attesa dei messaggi del client
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				// quando riceve un messaggio
				while (true) {
					String con = in.readLine();
					if (con.equalsIgnoreCase("nome")) {
						String nome = in.readLine();
						server.aggiugiClient(nome);
					} else {
						if (con.equals("tombola")) {
							String c = in.readLine();
							System.out.println("ciao " + c);
							server.vincitore = c;
							server.setVin(con);
						} else {
							server.setVin(con);
						}

					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		// crea un server spocket in ascolto
		try {
			ss = new ServerSocket(9999);
			while (true) {
				s = ss.accept();
				if (!isCon()) {
					s.close();
				}
				// aggiunge ad un vettore di client il nuovo client
				PrintWriter out = new PrintWriter(s.getOutputStream(), true);
				clientList.add(out);
				// per ogni connessione crea un socket e un thread che lo
				// gestisca
				ServerThread st = new ServerThread(s, server);
				out.println("Connesso");
				st.start();
				// ritorna in ascolto/attesa
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void mandaNumero(int numero, boolean ambo, boolean terna, boolean quaterna, boolean cinquina,
			boolean tombola) {
		for (PrintWriter printWriter : clientList) {
			printWriter.println(numero + "");
			printWriter.println(ambo + "");
			printWriter.println(terna + "");
			printWriter.println(quaterna + "");
			printWriter.println(cinquina + "");
			printWriter.println(tombola + "");
		}
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public boolean isCon() {
		return isStarted;
	}

	public void setCon(boolean isStarted) {
		this.isStarted = isStarted;
	}

}
