package tombolaSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ClientReciver extends Thread {
	private Socket s;
	private Client c;
	Server server;

	// deve essere inizializzato con il socket e con la classe grafica
	public ClientReciver(Socket s, Client c) {
		this.s = s;
		this.c = c;

	}

	public ClientReciver(Server server) {
		this.server = server;
	}

	@Override
	public void run() {
		super.run();

		// all' infinito resta in ascolto i nuovi messaggi nel scoket
		while (true) {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				// quando arriva un nuovo messaggio
				// legge il messaggio

				String con = in.readLine();
				if (con.equalsIgnoreCase("connesso")) {
					c.setCon(con);
				} else {
					if (con.equals("fai")) {
						c.coloraTombola();
					} else {
						int num = Integer.parseInt(con);
						String ambo = in.readLine();
						String terna = in.readLine();
						String quaterna = in.readLine();
						String cinquina = in.readLine();
						String tombola = in.readLine();
						c.numeroEstratto(num, ambo, terna, quaterna, cinquina, tombola);
					}
				}

			} catch (IOException e) {
				JPanel panel = new JPanel();
				JOptionPane.showMessageDialog(panel, "Attenzione!\npartita già iniziata!", "Client",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
		}

	}

}