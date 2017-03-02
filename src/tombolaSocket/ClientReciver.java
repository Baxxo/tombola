package tombolaSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
				if(con.equalsIgnoreCase("connesso")){
					c.setCon(con);	
				}else{
					c.numeroEstratto(Integer.parseInt(con));
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}