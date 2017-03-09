package tombolaSocket;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
	Label lblAmbofatto;
	Label lblTernafatto;
	Label lblQuaternafatto;
	Label lblCinquinafatto;
	Label lblTombolafatto;
	private TableItem tbItem;

	ArrayList<TableItem> item = new ArrayList<TableItem>();
	ArrayList<Integer> numero = new ArrayList<Integer>();
	int somma = 0;
	private int[] numeriClient; // numeri contenuti in questa cartella
	String output = "";

	private PrintWriter out;
	private Socket s;
	private Text text_1;
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

	Thread t;

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
		shell.setBackground(SWTResourceManager.getColor(255, 204, 153));
		shell.setSize(641, 256);
		shell.setText("Concorrente");

		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table.setForeground(SWTResourceManager.getColor(0, 0, 0));
		table.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
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
		lblNome.setBackground(SWTResourceManager.getColor(255, 255, 255));
		lblNome.setAlignment(SWT.RIGHT);
		lblNome.setBounds(462, 10, 55, 15);
		lblNome.setText("Nome:");
		lblNome.setBackground(SWTResourceManager.getColor(255, 204, 153));

		text = new Text(shell, SWT.BORDER);
		text.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		text.setForeground(SWTResourceManager.getColor(255, 0, 0));
		text.setBounds(522, 10, 76, 21);

		btnConnettiti = new Button(shell, SWT.BORDER | SWT.CENTER);
		btnConnettiti.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		btnConnettiti.setBackground(SWTResourceManager.getColor(255, 204, 153));
		btnConnettiti.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String nome = text.getText();
				if (nome.equals("")) {
					JPanel panel = new JPanel();
					JOptionPane.showMessageDialog(panel, "Inserisci il tuo nome!", "Client",
							JOptionPane.WARNING_MESSAGE);
				}
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
					JPanel panel = new JPanel();
					JOptionPane.showMessageDialog(panel, "Nessun server acceso!", "Client",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnConnettiti.setBounds(495, 54, 75, 25);
		btnConnettiti.setText("Connettiti");

		text_1 = new Text(shell, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.CANCEL);
		text_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		text_1.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		text_1.setBounds(21, 158, 594, 50);

		lblAmbo = new Label(shell, SWT.NONE);
		lblAmbo.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblAmbo.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		lblAmbo.setAlignment(SWT.CENTER);
		lblAmbo.setBounds(21, 92, 65, 21);
		lblAmbo.setText("Ambo");
		lblAmbo.setBackground(SWTResourceManager.getColor(255, 102, 102));

		lblTerna = new Label(shell, SWT.NONE);
		lblTerna.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblTerna.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		lblTerna.setAlignment(SWT.CENTER);
		lblTerna.setBounds(80, 92, 65, 21);
		lblTerna.setText("Terna");
		lblTerna.setBackground(SWTResourceManager.getColor(255, 102, 102));

		lblQuaterna = new Label(shell, SWT.NONE);
		lblQuaterna.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		lblQuaterna.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		lblQuaterna.setAlignment(SWT.CENTER);
		lblQuaterna.setBounds(145, 92, 94, 21);
		lblQuaterna.setText("Quaterna");
		lblQuaterna.setBackground(SWTResourceManager.getColor(255, 102, 102));

		lblCinquina = new Label(shell, SWT.NONE);
		lblCinquina.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		lblCinquina.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD | SWT.ITALIC));
		lblCinquina.setAlignment(SWT.CENTER);
		lblCinquina.setBounds(239, 92, 87, 21);
		lblCinquina.setText("Cinquina");
		lblCinquina.setBackground(SWTResourceManager.getColor(255, 102, 102));

		lblTombola = new Label(shell, SWT.NONE);
		lblTombola.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		lblTombola.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD | SWT.ITALIC));
		lblTombola.setAlignment(SWT.CENTER);
		lblTombola.setBounds(326, 92, 76, 21);
		lblTombola.setText("TOMBOLA");
		lblTombola.setBackground(SWTResourceManager.getColor(255, 102, 102));

		lblAmbofatto = new Label(shell, SWT.NONE);
		lblAmbofatto.setBounds(21, 134, 65, 18);
		lblAmbofatto.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblAmbofatto.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		lblAmbofatto.setAlignment(SWT.CENTER);
		lblAmbofatto.setBackground(SWTResourceManager.getColor(255, 204, 153));

		lblTernafatto = new Label(shell, SWT.NONE);
		lblTernafatto.setBounds(86, 134, 65, 18);
		lblTernafatto.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblTernafatto.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		lblTernafatto.setAlignment(SWT.CENTER);
		lblTernafatto.setBackground(SWTResourceManager.getColor(255, 204, 153));

		lblQuaternafatto = new Label(shell, SWT.NONE);
		lblQuaternafatto.setBounds(151, 134, 94, 18);
		lblQuaternafatto.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		lblQuaternafatto.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		lblQuaternafatto.setAlignment(SWT.CENTER);
		lblQuaternafatto.setBackground(SWTResourceManager.getColor(255, 204, 153));

		lblCinquinafatto = new Label(shell, SWT.NONE);
		lblCinquinafatto.setBounds(245, 134, 87, 18);
		lblCinquinafatto.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		lblCinquinafatto.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD | SWT.ITALIC));
		lblCinquinafatto.setAlignment(SWT.CENTER);
		lblCinquinafatto.setBackground(SWTResourceManager.getColor(255, 204, 153));

		lblTombolafatto = new Label(shell, SWT.NONE);
		lblTombolafatto.setBounds(332, 134, 76, 18);
		lblTombolafatto.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		lblTombolafatto.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD | SWT.ITALIC));
		lblTombolafatto.setAlignment(SWT.CENTER);
		lblTombolafatto.setBackground(SWTResourceManager.getColor(255, 204, 153));

		lblVincita = new Label(shell, SWT.NONE);
		lblVincita.setBackground(SWTResourceManager.getColor(255, 255, 255));
		lblVincita.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblVincita.setAlignment(SWT.RIGHT);
		lblVincita.setBounds(462, 110, 55, 15);
		lblVincita.setText("Vincita: ");
		lblVincita.setBackground(SWTResourceManager.getColor(255, 204, 153));

		lblSomma = new Label(shell, SWT.NONE);
		lblSomma.setBackground(SWTResourceManager.getColor(255, 255, 255));
		lblSomma.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		lblSomma.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblSomma.setBounds(522, 110, 55, 15);
		lblSomma.setText(somma + "€");
		lblSomma.setBackground(SWTResourceManager.getColor(255, 204, 153));

		Label lblGiocatori = new Label(shell, SWT.BORDER);
		lblGiocatori.setAlignment(SWT.CENTER);
		lblGiocatori.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD | SWT.ITALIC));
		lblGiocatori.setBounds(21, 113, 382, 20);
		lblGiocatori.setText("GIOCATORI");
		lblGiocatori.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		lblGiocatori.setBackground(SWTResourceManager.getColor(255, 204, 153));
	}

	public void setCon(String c) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				btnConnettiti.setText(c);
				if (c.equalsIgnoreCase("connesso")) {
					btnConnettiti.setEnabled(false);
					text.setEditable(false);
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
				for (int i = 0; i < numeriClient.length; i++) {
					if (numeriClient[i] == num) {
						for (int j = 0; j < item.size(); j++) {
							for (int l = 0; l < 9; l++) {
								if (!item.get(j).getText(l).equals("")) {
									if (Integer.parseInt(item.get(j).getText(l)) == num) {
										item.get(j).setBackground(l, SWTResourceManager.getColor(SWT.COLOR_GREEN));
										item.get(j).setForeground(l, SWTResourceManager.getColor(SWT.COLOR_WHITE));
									}

								}

							}
						}
					}
				}
				// segno che ho trovato un numero
				for (int i = 0; i < numeriClient.length; i++) {
					if (num == numeriClient[i]) {
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
						}
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
						}
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
						}
					}
				}
				if (amboFatto) {
					out.println("ambo");
					out.println(text.getText());
					somma += 10;
				}
				lblSomma.setText(somma + "€");
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
						}
					}
				}
				if (ternaFatto) {
					out.println("terna");
					out.println(text.getText());
					somma += 20;
				}
				lblSomma.setText(somma + "€");
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
					out.println(text.getText());
					somma += 50;
				}
				lblSomma.setText(somma + "€");
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
					out.println(text.getText());
					somma += 100;
				}
				lblSomma.setText(somma + "€");
			}
		});
	}

	public void setLblAmbofatto(String Ambofatto) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				lblAmbofatto.setText(Ambofatto);
			}
		});
	}

	public void setLblTernafatto(String Ternafatto) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				lblTernafatto.setText(Ternafatto);
			}
		});
	}

	public void setLblQuaternafatto(String Quaternafatto) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				lblQuaternafatto.setText(Quaternafatto);

			}
		});
	}

	public void setLblCinquinafatto(String Cinquinafatto) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				lblCinquinafatto.setText(Cinquinafatto);
			}
		});
	}

	public void setLblTombolafatto(String Tombolafatto) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				lblTombolafatto.setText(Tombolafatto);
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
					tombolaMio = true;
					tombolaFatto = true;
				}
				if (tombola) {
					out.println("tombola");
					out.println(text.getText());
					somma += 500;
				}
				lblSomma.setText(somma + "€");
			}
		});
	}

	public void coloraTombola() {
		if (tombolaMio && tombolaFatto) {
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					lblTombola.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
					lblTombola.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
			});
		}
	}

	// Rappresentazione testuale
	public void stampa(int[] numer) {

		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				numeriClient = numer;
				final String spacer = "   ";
				for (int r = 0; r < 3; r++) {
					int d = 0;
					for (int c = 0; c < 5; c++) {
						int index = r * 5 + c;
						int num = numeriClient[index];

						// spazi per i numeri mancanti (per incolonnare i numeri
						// nella
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
				for (int i : numeriClient) {
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
		});
	}
}
