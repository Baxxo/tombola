package tombolaSocket;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.MouseEvent;

public class Server {

	protected Shell shell;
	private Table table;
	private TableItem tbItem;
	private Button btnNumero;
	private List list;
	Label lblAmbo;
	boolean ambo = false;
	Label lblTerna;
	boolean terna = false;
	Label lblQuaterna;
	boolean quaterna = false;
	Label lblCinquina;
	boolean cinquina = false;
	Label lblTombola;
	boolean tombo = false;
	ArrayList<TableItem> item = new ArrayList<TableItem>();
	ArrayList<Integer> numeriEstratti = new ArrayList<Integer>();
	ArrayList<Integer> numeri = new ArrayList<Integer>();
	static ArrayList<PrintWriter> clientList = new ArrayList<PrintWriter>();
	int index = 0;
	int indexEstratti = 0;
	int numInt = 0;
	int col = 0;
	int row = 0;
	int num = 0;
	int tombola[][] = new int[9][10];
	Thread tServer;
	serverThread sThread;
	String vincitore = "";
	private Label lblClient;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Server window = new Server();
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
		new ClientReciver(this);
		sThread = new serverThread();
		sThread.setServer(Server.this);
		Thread t = new Thread(sThread);
		t.start();
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

		for (int i = 1; i < 91; i++) {
			numeri.add(i);
		}

		Collections.shuffle(numeri);

		int k = 0;

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 10; j++) {
				tombola[i][j] = numeri.get(k);
				k++;
			}
		}

		shell = new Shell();
		shell.setBackground(SWTResourceManager.getColor(153, 204, 255));
		shell.setSize(500, 508);
		shell.setText("Tombola");

		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		table.setForeground(SWTResourceManager.getColor(0, 0, 0));
		table.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		table.setBounds(27, 43, 424, 175);
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

		TableColumn tableColumn_9 = new TableColumn(table, SWT.NONE);
		tableColumn_9.setWidth(42);
		tableColumn_9.setResizable(false);

		for (int i = 0; i < 9; i++) {
			tbItem = new TableItem(table, SWT.NONE);
			item.add(tbItem);
		}

		int l = 0;

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 10; j++) {
				l++;
				item.get(i).setText(j, l + "");
			}
		}

		Label lblTabellone = new Label(shell, SWT.BORDER);
		lblTabellone.setFont(SWTResourceManager.getFont("Adobe Gothic Std B", 12, SWT.BOLD));
		lblTabellone.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		lblTabellone.setAlignment(SWT.CENTER);
		lblTabellone.setBounds(27, 10, 424, 25);
		lblTabellone.setText("TOMBOLONE");

		btnNumero = new Button(shell, SWT.NONE);
		btnNumero.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		btnNumero.setBackground(SWTResourceManager.getColor(153, 204, 255));

		btnNumero.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (list.getItemCount() == 0) {
					JPanel panel = new JPanel();
					JOptionPane.showMessageDialog(panel, "Nessun client è connesso", "Server",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				int num = coloraNumero();
				sThread.mandaNumero(num, ambo, terna, quaterna, cinquina, tombo);
				sThread.setCon(false);
			}
		});
		btnNumero.setBounds(188, 238, 75, 25);
		btnNumero.setText("Numero");

		list = new List(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		list.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		list.setForeground(SWTResourceManager.getColor(0, 255, 102));
		list.setBounds(306, 308, 145, 124);

		lblClient = new Label(shell, SWT.BORDER);
		lblClient.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblClient.setForeground(SWTResourceManager.getColor(0, 255, 102));
		lblClient.setAlignment(SWT.CENTER);
		lblClient.setBounds(306, 274, 137, 15);
		lblClient.setText("CLIENT");

		lblAmbo = new Label(shell, SWT.BORDER);
		lblAmbo.setBackground(SWTResourceManager.getColor(240, 240, 240));
		lblAmbo.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblAmbo.setForeground(SWTResourceManager.getColor(30, 144, 255));
		lblAmbo.setAlignment(SWT.CENTER);
		lblAmbo.setBounds(27, 289, 75, 25);
		lblAmbo.setText("Ambo");

		lblTerna = new Label(shell, SWT.BORDER);
		lblTerna.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblTerna.setForeground(SWTResourceManager.getColor(30, 144, 255));
		lblTerna.setAlignment(SWT.CENTER);
		lblTerna.setBounds(122, 289, 75, 25);
		lblTerna.setText("Terna");

		lblQuaterna = new Label(shell, SWT.BORDER);
		lblQuaterna.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD | SWT.ITALIC));
		lblQuaterna.setForeground(SWTResourceManager.getColor(30, 144, 255));
		lblQuaterna.setAlignment(SWT.CENTER);
		lblQuaterna.setBounds(27, 328, 75, 25);
		lblQuaterna.setText("Quaterna");

		lblCinquina = new Label(shell, SWT.BORDER);
		lblCinquina.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD | SWT.ITALIC));
		lblCinquina.setForeground(SWTResourceManager.getColor(30, 144, 255));
		lblCinquina.setAlignment(SWT.CENTER);
		lblCinquina.setBounds(122, 328, 75, 25);
		lblCinquina.setText("Cinquina");

		lblTombola = new Label(shell, SWT.BORDER | SWT.CENTER);
		lblTombola.setBackground(SWTResourceManager.getColor(240, 240, 240));
		lblTombola.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD | SWT.ITALIC));
		lblTombola.setForeground(SWTResourceManager.getColor(0, 51, 255));
		lblTombola.setAlignment(SWT.CENTER);
		lblTombola.setBounds(27, 372, 170, 25);
		lblTombola.setText("TOMBOLA");

	}

	public void setVin(String vinto) {
		if (vinto.equals("ambo")) {
			ambo = true;
		}
		if (vinto.equals("terna")) {
			terna = true;
		}
		if (vinto.equals("quaterna")) {
			quaterna = true;
		}
		if (vinto.equals("cinquina")) {
			cinquina = true;
		}
		if (vinto.equals("tombola")) {
			tombo = true;

		}
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				if (ambo) {
					lblAmbo.setBackground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
					lblAmbo.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
				if (terna) {
					lblTerna.setBackground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
					lblTerna.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
				if (quaterna) {
					lblQuaterna.setBackground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
					lblQuaterna.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
				if (cinquina) {
					lblCinquina.setBackground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
					lblCinquina.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
				if (tombo) {
					sThread.tomb();
					lblTombola.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
					lblTombola.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
					btnNumero.setEnabled(false);
					JPanel panel = new JPanel();
					JOptionPane.showMessageDialog(panel, "Il giocatore " + vincitore + " ha fatto tombola!", "Server",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
	}

	public void aggiugiClient(String client) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				list.add(client);
			}
		});
	}

	public int coloraNumero() {

		if (index == 90) {
			btnNumero.setEnabled(false);
		} else {
			numInt = numeri.get(index);
			index++;

			if (col == 9) {
				row++;
				col = 0;
			} else {
				col++;
			}

			if (numInt != 10 && numInt != 20 && numInt != 30 && numInt != 40 && numInt != 50 && numInt != 60
					&& numInt != 70 && numInt != 80 && numInt != 90) {

				// prendo la seconda cifra del numero
				String n = numInt + "";

				if (n.length() == 1) {
					n = "0" + n;
				}

				// la cifra la trasformo in int
				num = Character.getNumericValue(n.charAt(1));

				// cerco il numero

				if (numInt <= 10) {
					item.get(0).setBackground(num - 1, SWTResourceManager.getColor(SWT.COLOR_GREEN));
					item.get(0).setForeground(num - 1, SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
				if (numInt > 10 && numInt <= 20) {
					item.get(1).setBackground(num - 1, SWTResourceManager.getColor(SWT.COLOR_GREEN));
					item.get(1).setForeground(num - 1, SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
				if (numInt > 20 && numInt <= 30) {
					item.get(2).setBackground(num - 1, SWTResourceManager.getColor(SWT.COLOR_GREEN));
					item.get(2).setForeground(num - 1, SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
				if (numInt > 30 && numInt <= 40) {
					item.get(3).setBackground(num - 1, SWTResourceManager.getColor(SWT.COLOR_GREEN));
					item.get(3).setForeground(num - 1, SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
				if (numInt > 40 && numInt <= 50) {
					item.get(4).setBackground(num - 1, SWTResourceManager.getColor(SWT.COLOR_GREEN));
					item.get(4).setForeground(num - 1, SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
				if (numInt > 50 && numInt <= 60) {
					item.get(5).setBackground(num - 1, SWTResourceManager.getColor(SWT.COLOR_GREEN));
					item.get(5).setForeground(num - 1, SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
				if (numInt > 60 && numInt <= 70) {
					item.get(6).setBackground(num - 1, SWTResourceManager.getColor(SWT.COLOR_GREEN));
					item.get(6).setForeground(num - 1, SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
				if (numInt > 70 && numInt <= 80) {
					item.get(7).setBackground(num - 1, SWTResourceManager.getColor(SWT.COLOR_GREEN));
					item.get(7).setForeground(num - 1, SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
				if (numInt > 80 && numInt <= 90) {
					item.get(8).setBackground(num - 1, SWTResourceManager.getColor(SWT.COLOR_GREEN));
					item.get(8).setForeground(num - 1, SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
			} else {
				int col = (numInt / 10) - 1;
				item.get(col).setBackground(9, SWTResourceManager.getColor(SWT.COLOR_GREEN));
				item.get(col).setForeground(9, SWTResourceManager.getColor(SWT.COLOR_WHITE));
			}
		}
		return numInt;

	}
}
