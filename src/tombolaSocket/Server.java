package tombolaSocket;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.wb.swt.SWTResourceManager;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Text;

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
		ClientReciver client = new ClientReciver(this);
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
		shell.setSize(500, 508);
		shell.setText("Tombola");

		table = new Table(shell, SWT.BORDER);
		table.setHeaderVisible(true);
		table.setBounds(27, 36, 424, 200);
		table.setLinesVisible(true);

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

		Label lblTabellone = new Label(shell, SWT.NONE);
		lblTabellone.setBounds(177, 10, 55, 15);
		lblTabellone.setText("Tabellone");

		btnNumero = new Button(shell, SWT.NONE);
		btnNumero.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int num = coloraNumero();
				sThread.mandaNumero(num, ambo, terna, quaterna, cinquina, tombo);
			}
		});
		btnNumero.setBounds(199, 269, 75, 25);
		btnNumero.setText("Numero");

		list = new List(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		list.setBounds(306, 308, 145, 124);

		Label lblClient = new Label(shell, SWT.NONE);
		lblClient.setAlignment(SWT.CENTER);
		lblClient.setBounds(306, 274, 137, 15);
		lblClient.setText("Client");

		lblAmbo = new Label(shell, SWT.NONE);
		lblAmbo.setAlignment(SWT.CENTER);
		lblAmbo.setBounds(27, 312, 55, 15);
		lblAmbo.setText("Ambo");

		lblTerna = new Label(shell, SWT.NONE);
		lblTerna.setAlignment(SWT.CENTER);
		lblTerna.setBounds(142, 312, 55, 15);
		lblTerna.setText("Terna");

		lblQuaterna = new Label(shell, SWT.NONE);
		lblQuaterna.setAlignment(SWT.CENTER);
		lblQuaterna.setBounds(27, 349, 55, 15);
		lblQuaterna.setText("Quaterna");

		lblCinquina = new Label(shell, SWT.NONE);
		lblCinquina.setBounds(142, 349, 55, 15);
		lblCinquina.setText("Cinquina");

		lblTombola = new Label(shell, SWT.BORDER | SWT.CENTER);
		lblTombola.setAlignment(SWT.CENTER);
		lblTombola.setBounds(27, 395, 170, 15);
		lblTombola.setText("TOMBOLA");

	}

	public void setVin(boolean ambo, boolean terna, boolean quaterna, boolean cinquina, boolean tombola) {
		this.ambo = ambo;
		this.terna = terna;
		this.quaterna = quaterna;
		this.cinquina = cinquina;
		this.tombo = tombola;
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				if (ambo) {
					lblAmbo.setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
					lblAmbo.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
				if (terna) {
					lblTerna.setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
					lblTerna.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
				if (quaterna) {
					lblQuaterna.setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
					lblQuaterna.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
				if (cinquina) {
					lblCinquina.setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
					lblCinquina.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
				if (tombola) {
					lblTombola.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
					lblTombola.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
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
					item.get(0).setBackground(num - 1, SWTResourceManager.getColor(SWT.COLOR_RED));
				}
				if (numInt > 10 && numInt <= 20) {
					item.get(1).setBackground(num - 1, SWTResourceManager.getColor(SWT.COLOR_RED));
				}
				if (numInt > 20 && numInt <= 30) {
					item.get(2).setBackground(num - 1, SWTResourceManager.getColor(SWT.COLOR_RED));
				}
				if (numInt > 30 && numInt <= 40) {
					item.get(3).setBackground(num - 1, SWTResourceManager.getColor(SWT.COLOR_RED));
				}
				if (numInt > 40 && numInt <= 50) {
					item.get(4).setBackground(num - 1, SWTResourceManager.getColor(SWT.COLOR_RED));
				}
				if (numInt > 50 && numInt <= 60) {
					item.get(5).setBackground(num - 1, SWTResourceManager.getColor(SWT.COLOR_RED));
				}
				if (numInt > 60 && numInt <= 70) {
					item.get(6).setBackground(num - 1, SWTResourceManager.getColor(SWT.COLOR_RED));
				}
				if (numInt > 70 && numInt <= 80) {
					item.get(7).setBackground(num - 1, SWTResourceManager.getColor(SWT.COLOR_RED));
				}
				if (numInt > 80 && numInt <= 90) {
					item.get(8).setBackground(num - 1, SWTResourceManager.getColor(SWT.COLOR_RED));
				}
			} else {
				if (numInt == 10) {
					item.get(0).setBackground(9, SWTResourceManager.getColor(SWT.COLOR_RED));
				}
				if (numInt == 20) {
					item.get(1).setBackground(9, SWTResourceManager.getColor(SWT.COLOR_RED));
				}
				if (numInt == 30) {
					item.get(2).setBackground(9, SWTResourceManager.getColor(SWT.COLOR_RED));
				}
				if (numInt == 40) {
					item.get(3).setBackground(9, SWTResourceManager.getColor(SWT.COLOR_RED));
				}
				if (numInt == 50) {
					item.get(4).setBackground(9, SWTResourceManager.getColor(SWT.COLOR_RED));
				}
				if (numInt == 60) {
					item.get(5).setBackground(9, SWTResourceManager.getColor(SWT.COLOR_RED));
				}
				if (numInt == 70) {
					item.get(6).setBackground(9, SWTResourceManager.getColor(SWT.COLOR_RED));
				}
				if (numInt == 80) {
					item.get(7).setBackground(9, SWTResourceManager.getColor(SWT.COLOR_RED));
				}
				if (numInt == 90) {
					item.get(8).setBackground(9, SWTResourceManager.getColor(SWT.COLOR_RED));
				}
			}
		}
		return numInt;

	}
}
