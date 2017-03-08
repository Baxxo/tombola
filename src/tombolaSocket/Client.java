package tombolaSocket;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class Client {

	protected Shell shell;
	private Table table;
	private TableItem tbItem;
	private Text text;
	private Label lblNome;
	private Button btnConnettiti;
	private Label lblAmbo;
	private Label lblTerna;
	private Label lblQuaterna;
	private Label lblCinquina;
	private Label lblTombola;
	private Label lblVincita;
	private Label lblSomma;

	ArrayList<TableItem> item = new ArrayList<TableItem>();
	ArrayList<Integer> numero = new ArrayList<Integer>();
	String output = "";
	int somma = 0;

	private PrintWriter out;
	private Socket s;
	private Text text_1;
	private int[] numeri; // numeri contenuti in questa cartella
	private boolean[] segnati = new boolean[15]; // true se il numero
													// corrispondente è segnato
	boolean ambo = false;
	boolean amboFatto;
	boolean terna = false;
	boolean ternaFatto;
	boolean quaterna = false;
	boolean quaternaFatto;
	boolean cinquina = false;
	boolean cinquinaFatto;
	boolean tombola = false;
	boolean tombolaFatto;

	boolean amboMio = false;
	boolean ternaMio = false;
	boolean quaternaMio = false;
	boolean cinquinaMio = false;
	boolean tombolaMio = false;

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

		for (int i = 0; i < 15; i++) {
			segnati[i] = false;
		}

		shell = new Shell();
		shell.setSize(641, 256);
		shell.setText("Concorrente");

		table = new Table(shell, SWT.BORDER);
		table.setLinesVisible(true);
		table.setBounds(21, 21, 382, 65);
		table.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				table.deselectAll();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				table.deselectAll();
			}
		});

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

					out.println("nome");
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
		text_1.setBounds(21, 158, 594, 50);

		generaNumeri();
		btnConnettiti.setEnabled(true);

		lblAmbo = new Label(shell, SWT.NONE);
		lblAmbo.setAlignment(SWT.CENTER);
		lblAmbo.setBounds(21, 110, 55, 15);
		lblAmbo.setText("Ambo");

		lblTerna = new Label(shell, SWT.NONE);
		lblTerna.setAlignment(SWT.CENTER);
		lblTerna.setBounds(94, 110, 55, 15);
		lblTerna.setText("Terna");

		lblQuaterna = new Label(shell, SWT.NONE);
		lblQuaterna.setAlignment(SWT.CENTER);
		lblQuaterna.setBounds(166, 110, 55, 15);
		lblQuaterna.setText("Quaterna");

		lblCinquina = new Label(shell, SWT.NONE);
		lblCinquina.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblCinquina.setAlignment(SWT.CENTER);
		lblCinquina.setBounds(255, 110, 55, 15);
		lblCinquina.setText("Cinquina");

		lblTombola = new Label(shell, SWT.NONE);
		lblTombola.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblTombola.setAlignment(SWT.CENTER);
		lblTombola.setBounds(336, 110, 67, 15);
		lblTombola.setText("TOMBOLA");

		lblVincita = new Label(shell, SWT.NONE);
		lblVincita.setAlignment(SWT.RIGHT);
		lblVincita.setBounds(462, 110, 55, 15);
		lblVincita.setText("Vincita: ");

		lblSomma = new Label(shell, SWT.NONE);
		lblSomma.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblSomma.setBounds(522, 110, 55, 15);
		lblSomma.setText(somma + "");
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

	public void numeroEstratto(int num, String ambo, String terna, String quaterna, String cinquina, String tombola) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				amboFatto = Boolean.parseBoolean(ambo);
				ternaFatto = Boolean.parseBoolean(terna);
				quaternaFatto = Boolean.parseBoolean(quaterna);
				cinquinaFatto = Boolean.parseBoolean(cinquina);
				tombolaFatto = Boolean.parseBoolean(tombola);
				if (amboFatto && amboMio) {
					lblAmbo.setBackground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
					lblAmbo.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
				if (ternaFatto && ternaMio) {
					lblTerna.setBackground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
					lblTerna.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
				if (quaternaFatto && quaternaMio) {
					lblQuaterna.setBackground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
					lblQuaterna.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
				if (cinquinaFatto && cinquinaMio) {
					lblCinquina.setBackground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
					lblCinquina.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
				if (tombolaFatto && tombolaMio) {
					lblTombola.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
					lblTombola.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
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
				// segno che ho trovato un numero
				for (int i = 0; i < numeri.length; i++) {
					if (num == numeri[i]) {
						segnati[i] = true;
					}
				}
				if (amboFatto == false) {
					checkAmbo();
				} else {
					if (ternaFatto == false) {
						checkTerna();
					} else {
						if (quaternaFatto == false) {
							checkQuaterna();

						} else {
							if (cinquinaFatto == false) {
								checkCiqnuina();
							} else {
								if (tombolaFatto == false) {
									checkTombola();
								}
							}
						}

					}

				}
			}
		});
	}

	public void checkAmbo() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				// controllo che ci sia un ambo
				for (int j = 0; j < item.size(); j++) {
					if (j == 0) {
						for (int i = 0; i < 4; i++) {
							if (segnati[i] == true && segnati[i + 1] == true) {
								ambo = true;
								break;
							}
						}
						if (ambo == true) {
							amboMio = true;
							amboFatto = true;
						} else {
							System.out.println();
						}
						System.out.println();
					}
					if (j == 1) {
						for (int i = 5; i < 9; i++) {
							if (segnati[i] == true && segnati[i + 1] == true) {
								ambo = true;
								break;
							}
						}
						if (ambo == true) {
							amboMio = true;
							amboFatto = true;
						} else {
							System.out.println();
						}
						System.out.println();
					}
					if (j == 2) {
						for (int i = 10; i < 14; i++) {
							if (segnati[i] == true && segnati[i + 1] == true) {
								ambo = true;
								break;
							}
						}
						if (ambo == true) {
							amboMio = true;
							amboFatto = true;
						} else {
							System.out.println();
						}
						System.out.println();
					}
				}
				if (amboFatto) {
					out.println("ambo");
					somma += 10;
				}
				lblSomma.setText(somma + "");
			}
		});

	}

	public void checkTerna() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				// controllo che ci sia una terna
				for (int j = 0; j < item.size(); j++) {
					if (j == 0) {
						for (int i = 0; i < 3; i++) {
							if (segnati[i] == true && segnati[i + 1] == true && segnati[i + 2] == true) {
								terna = true;
								break;
							}
						}
						if (terna) {
							ternaMio = true;
							ternaFatto = true;
						}
					}
					if (j == 1) {
						for (int i = 5; i < 8; i++) {
							if (segnati[i] == true && segnati[i + 1] == true && segnati[i + 2] == true) {
								terna = true;
								break;
							}
						}
						if (terna) {
							ternaMio = true;
							ternaFatto = true;
						}
					}
					if (j == 2) {
						for (int i = 10; i < 13; i++) {
							if (segnati[i] == true && segnati[i + 1] == true && segnati[i + 2] == true) {
								terna = true;
								break;
							}
						}
						if (terna) {
							ternaMio = true;
							ternaFatto = true;
						} else {
							System.out.println();
						}
						System.out.println();
					}
				}
				if (ternaFatto) {
					out.println("terna");
					somma += 20;
				}
				lblSomma.setText(somma + "");
			}
		});

	}

	public void checkQuaterna() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				// controllo che ci sia una quaterna
				for (int j = 0; j < item.size(); j++) {
					if (j == 0) {
						for (int i = 0; i < 2; i++) {
							if (segnati[i] == true && segnati[i + 1] == true && segnati[i + 2] == true
									&& segnati[i + 3] == true) {
								quaterna = true;
								break;
							}
						}
						if (quaterna) {
							quaternaMio = true;
							quaternaFatto = true;
						}
					}
					if (j == 1) {
						for (int i = 5; i < 7; i++) {
							if (segnati[i] == true && segnati[i + 1] == true && segnati[i + 2] == true
									&& segnati[i + 3] == true) {
								quaterna = true;
								break;
							}
						}
						if (quaterna) {
							quaternaMio = true;
							quaternaFatto = true;
						}
					}
					if (j == 2) {
						for (int i = 10; i < 12; i++) {
							if (segnati[i] == true && segnati[i + 1] == true && segnati[i + 2] == true
									&& segnati[i + 3] == true) {
								quaterna = true;
								break;
							}
						}
						if (quaterna) {
							quaternaMio = true;
							quaternaFatto = true;
						}
					}
				}
				if (quaterna) {
					out.println("quaterna");
					somma += 50;
				}
				lblSomma.setText(somma + "");
			}
		});
	}

	public void checkCiqnuina() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				// controllo che ci sia una cinquina
				for (int j = 0; j < item.size(); j++) {
					if (j == 0) {
						for (int i = 0; i < 1; i++) {
							if (segnati[i] == true && segnati[i + 1] == true && segnati[i + 2] == true
									&& segnati[i + 3] == true && segnati[i + 4] == true) {
								cinquina = true;
								break;
							}
						}
						if (cinquina) {
							System.out.println("CINQUINA");
							cinquinaMio = true;
							cinquinaFatto = true;
						}
					}
					if (j == 1) {
						for (int i = 5; i < 6; i++) {
							if (segnati[i] == true && segnati[i + 1] == true && segnati[i + 2] == true
									&& segnati[i + 3] == true && segnati[i + 4] == true) {
								quaterna = true;
								break;
							}
						}
						if (cinquina) {
							System.out.println("CINQUINA");
							cinquinaMio = true;
							cinquinaFatto = true;
						}
					}
					if (j == 2) {
						for (int i = 10; i < 11; i++) {
							if (segnati[i] == true && segnati[i + 1] == true && segnati[i + 2] == true
									&& segnati[i + 3] == true && segnati[i + 4] == true) {
								quaterna = true;
								break;
							}
						}
						if (cinquina) {
							if (!item.get(2).getChecked()) {
								cinquinaMio = true;
								cinquinaFatto = true;
							}
						} 
					}
				}
				if (cinquina) {
					out.println("cinquina");
					somma += 100;
				}
				lblSomma.setText(somma + "");
			}
		});
	}

	public void checkTombola() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				// controllo che ci sia una tombola
				for (int i = 0; i < segnati.length; i++) {
					if (segnati[i] == true) {
						tombola = true;
					} else {
						tombola = false;
						break;
					}
				}
				if (tombola && tombolaFatto == false) {
					System.out.println("TOMBOLA");
					tombolaMio = true;
					tombolaFatto = true;
				}
				if (tombola) {
					out.println("tombola/" + lblNome.getText());
					somma += 500;
				}
				lblSomma.setText(somma + "");
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

			// Se il numero casuale generato è già presente oppure se ci sono
			// già due
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
			if (i >= 10 && i < 20) {
				item.get(index).setText(1, i + "");
			}
			if (i >= 20 && i < 30) {
				item.get(index).setText(2, i + "");
			}
			if (i >= 30 && i < 40) {
				item.get(index).setText(3, i + "");
			}
			if (i >= 40 && i < 50) {
				item.get(index).setText(4, i + "");
			}
			if (i >= 50 && i < 60) {
				item.get(index).setText(5, i + "");
			}
			if (i >= 60 && i < 70) {
				item.get(index).setText(6, i + "");
			}
			if (i >= 70 && i < 80) {
				item.get(index).setText(7, i + "");
			}
			if (i >= 80) {
				item.get(index).setText(8, i + "");
			}
			lun++;
		}
	}
}