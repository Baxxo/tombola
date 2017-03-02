package tombolaSocket;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Client {

	protected Shell shell;
	private Table table;
	private TableItem tbItem;
	ArrayList<TableItem> item = new ArrayList<TableItem>();
	// ArrayList<Integer> numeri = new ArrayList<Integer>();
	ArrayList<Integer> numero = new ArrayList<Integer>();
	private Text text;
	Button btnConnettiti;
	Label lblNome;
	Socket s;
	private PrintWriter out;
	private BufferedReader in;
	private Text text_1;
	private int[] numeri; // numeri contenuti in questa cartella
	private boolean[] segnati = new boolean[15]; // true se il numero
													// corrispondente � segnato

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Client window = new Client();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(641, 213);
		shell.setText("Concorrente");

		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(21, 10, 382, 87);

		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(42);
		tableColumn.setResizable(false);

		TableColumn tableColumn_1 = new TableColumn(table, SWT.NONE);
		tableColumn_1.setWidth(42);
		tableColumn_1.setResizable(false);

		TableColumn tableColumn_2 = new TableColumn(table, SWT.NONE);
		tableColumn_2.setWidth(42);
		tableColumn_2.setResizable(false);

		TableColumn tableColumn_3 = new TableColumn(table, SWT.NONE);
		tableColumn_3.setWidth(42);
		tableColumn_3.setResizable(false);

		TableColumn tableColumn_4 = new TableColumn(table, SWT.NONE);
		tableColumn_4.setWidth(42);
		tableColumn_4.setResizable(false);

		TableColumn tableColumn_5 = new TableColumn(table, SWT.NONE);
		tableColumn_5.setWidth(42);
		tableColumn_5.setResizable(false);

		TableColumn tableColumn_6 = new TableColumn(table, SWT.NONE);
		tableColumn_6.setWidth(42);
		tableColumn_6.setResizable(false);

		TableColumn tableColumn_7 = new TableColumn(table, SWT.NONE);
		tableColumn_7.setWidth(42);
		tableColumn_7.setResizable(false);

		TableColumn tableColumn_8 = new TableColumn(table, SWT.NONE);
		tableColumn_8.setWidth(42);
		tableColumn_8.setResizable(false);

		lblNome = new Label(shell, SWT.NONE);
		lblNome.setAlignment(SWT.RIGHT);
		lblNome.setBounds(462, 10, 55, 15);
		lblNome.setText("Nome:");

		text = new Text(shell, SWT.BORDER);
		text.setBounds(522, 10, 76, 21);

		btnConnettiti = new Button(shell, SWT.NONE);
		btnConnettiti.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String nome = text.getText();
				btnConnettiti.setText(nome);
				try {
					s = new Socket("localhost", 9999);
					out = new PrintWriter(s.getOutputStream(), true);

					ClientReciver cr = new ClientReciver(s, Client.this);
					cr.start();

					out.println(nome);
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnConnettiti.setBounds(495, 54, 75, 25);
		btnConnettiti.setText("Connettiti");
		btnConnettiti.setEnabled(false);

		text_1 = new Text(shell, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.CANCEL);
		text_1.setBounds(21, 114, 594, 50);

		generaNumeri();
		btnConnettiti.setEnabled(true);
	}

	public void setCon(String c) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				btnConnettiti.setText(c);
				if (c.equalsIgnoreCase("connesso")) {
					btnConnettiti.setEnabled(false);
				}
			}
		});
	}

	public void numeroEstratto(int num) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				text_1.append(num + "  ");
				for (int i = 0; i < numeri.length; i++) {
					if (numeri[i] == num) {
						for (int j = 0; j < item.size(); j++) {
							for (int l = 0; l < 9; l++) {
								if (!item.get(j).getText(l).equals("")) {
									if (Integer.parseInt(item.get(j).getText(l)) == num) {
										item.get(j).setBackground(l, SWTResourceManager.getColor(SWT.COLOR_GREEN));
									}

								}

							}
						}
					}
				}
				for (int i = 0; i < numeri.length; i++) {
					if (num == numeri[i]) {
						segnati[i] = true;
					}
				}
				String[] n = new String[5];
				int e = 0;
				for (int i = 0; i < 9; i++) {
					if (!item.get(0).getText(i).equals("")) {
						n[e] = item.get(0).getText(i);
						e++;
					}
				}
				e = 0;
				boolean ambo = false;
				boolean amboFatto = false;
				for (String string : n) {
					System.out.print(numeri[e] + " ");
					System.out.print(segnati[e]);
					System.out.println(" " + string);
					if(e>0){
						if (segnati[e] == true && segnati[e - 1] == true && amboFatto == false) {
							ambo = true;
							amboFatto=true;
							break;
						} else {
							ambo = false;
						}						
					}
					e++;
				}
				if (ambo == true) {
					System.out.println("AMBO");
				} else {
					System.out.println();
				}

			}
		});
	}

	public void generaNumeri() {

		numeri = new int[15];
		// Riempio il vettore con 15 numeri casuali che rispettino le regole:
		// 1. no numeri ripetuti
		// 2. max 2 numeri con la stessa decina
		final int[] decine = new int[10]; // indica quanti numeri per ogni
											// decina
		for (int i = 0; i < 15; i++) {
			// Genero un numero casuale tra 1 e 90
			final int n = Utility.generaCasuale(1, 90);
			// Decina attuale
			final int d = n == 90 ? 8 : n / 10; // il 90 va nella colonna degli
												// 80

			// Se il numero casuale generato � gi� presente oppure se ci sono
			// gi� due
			// numeri con la stessa decina, ripeto il calcolo dell'elemento
			// i-esimo
			if (decine[d] >= 2 || Utility.indexOf(n, numeri, i) >= 0) {
				i--;
				continue;
			} else {
				numeri[i] = n;
				decine[d]++;
			}
		}

		// Ordina il vettore finale
		Arrays.sort(numeri);

		// Permuta per ottenere le righe finali (un elemento ogni tre nel
		// vettore ordinato)
		int tmp = numeri[1];
		numeri[1] = numeri[3];
		numeri[3] = numeri[9];
		numeri[9] = numeri[13];
		numeri[13] = numeri[11];
		numeri[11] = numeri[5];
		numeri[5] = numeri[2];
		numeri[2] = numeri[6];
		numeri[6] = numeri[4];
		numeri[4] = numeri[12];
		numeri[12] = numeri[8];
		numeri[8] = numeri[10];
		numeri[10] = numeri[5];
		numeri[5] = tmp;

		// Scambia (in verticale) i numeri della stessa colonna se non sono in
		// ordine tra loro
		for (int i = 0; i < 15; i++) {
			final int n = numeri[i];
			final int d = n / 10;
			for (int j = i; j < 15; j++) {
				final int n2 = numeri[j];
				final int d2 = n2 / 10;

				if (d == d2 && n > n2) { // d==d2: stessa colonna, n>n2 ordine
											// invertito
					final int temp = numeri[i];
					numeri[i] = numeri[j];
					numeri[j] = temp;
				}
			}
		}
		stampa();
	}

	// Rappresentazione testuale
	public void stampa() {
		final String spacer = "   ";
		for (int r = 0; r < 3; r++) {
			String output = "";
			int d = 0;
			for (int c = 0; c < 5; c++) {
				int index = r * 5 + c;
				int num = numeri[index];

				// spazi per i numeri mancanti (per incolonnare i numeri nella
				// giusta decina)
				int _d = (int) ((double) num / 10.0);
				if (num == 90) // il 90 va nella colonna degli 80
					_d = 8;
				for (int i = 1; i < _d - d + (c == 0 ? 1 : 0); i++)
					output += spacer + "   ";
				d = _d;
			}

		}
		for (int i = 0; i < 3; i++) {
			tbItem = new TableItem(table, SWT.NONE);
			item.add(tbItem);
		}
		int index = 0;
		int lun = 0;
		for (int i : numeri) {
			if (lun > 4) {
				index++;
				lun = 0;
			}
			if (i < 10) {
				item.get(index).setText(0, i + "");
			}
			if (i > 10 && i <= 20) {
				item.get(index).setText(1, i + "");
			}
			if (i > 20 && i <= 30) {
				item.get(index).setText(2, i + "");
			}
			if (i > 30 && i <= 40) {
				item.get(index).setText(3, i + "");
			}
			if (i > 40 && i <= 50) {
				item.get(index).setText(4, i + "");
			}
			if (i > 50 && i <= 60) {
				item.get(index).setText(5, i + "");
			}
			if (i > 60 && i <= 70) {
				item.get(index).setText(6, i + "");
			}
			if (i > 70 && i <= 80) {
				item.get(index).setText(7, i + "");
			}
			if (i > 80 && i <= 90) {
				item.get(index).setText(8, i + "");
			}
			lun++;
		}
	}
}