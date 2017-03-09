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
	PrintWriter out;
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
						String c = in.readLine();
						server.vincitore = c;
						server.setVin(con);
					}
				}

			} catch (

			IOException e) {
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
				out = new PrintWriter(s.getOutputStream(), true);
				clientList.add(out);
				// per ogni connessione crea un socket e un thread che lo
				// gestisca
				ServerThread st = new ServerThread(s, server);
				out.println("Connesso");
				int[] numeriClient = server.generaNumeri();
				out.println(numeriClient.length);
				for (int i : numeriClient) {
					out.println(i);
				}
				st.start();
				// ritorna in ascolto/attesa
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void tomb() {
		for (PrintWriter printWriter : clientList) {
			printWriter.println("fai");
		}
	}

	public void vincite(String nameAmbo, String nameTerna, String nameQuaterna, String nameCinquina,
			String nameTombola) {
		for (PrintWriter printWriter : clientList) {
			printWriter.println("vincite");
			printWriter.println(nameAmbo);
			printWriter.println(nameTerna);
			printWriter.println(nameQuaterna);
			printWriter.println(nameCinquina);
			printWriter.println(nameTombola);
		}

	}

	boolean amboFatto = false;
	boolean ternaFatto = false;
	boolean quaternaFatto = false;
	boolean ciqnuinaFatto = false;
	boolean tombolaFatto = false;

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
