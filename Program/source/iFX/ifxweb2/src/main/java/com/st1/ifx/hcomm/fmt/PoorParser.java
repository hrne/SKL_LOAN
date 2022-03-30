package com.st1.ifx.hcomm.fmt;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class PoorParser extends JPanel {
	private static final long serialVersionUID = -747050784995202173L;

	JPanel mainPanel;
	JTextField tita;
	JTextField titaTxCode;
	JTextField tota;
	JTextField totaTxCode;
	JTextArea titaParsedArea;
	JTextArea totaParsedArea;

	public PoorParser() {

		createPanels();
	}

	private void createPanels() {
		setLayout(new GridLayout(2, 1));
		add(createTitaPanel());
		add(createTotaPanel());
	}

	private Component createTitaPanel() {

		JButton btn = new JButton("parse");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				titaParsedArea.setText(parseTita(titaTxCode.getText(), tita.getText()));
				titaParsedArea.setCaretPosition(0);
			}

		});
		titaTxCode = new JTextField(5);
		titaTxCode.setText("I1000");
		titaTxCode.setCaretPosition(0);

		tita = new JTextField(50);
		String t = "IU505002611191 2039100000000000000000000000000100010313034820026100000000000000000000000000000000000000000000000000000000000000000000                 5050026100000000IU00I1000     50501ABAB7600000    0000026100000000100000000010000505261      000T000                    2011010350500000000000000000000000000000000001505083           12000000    000005050                                                                                                123             00000000000000201101030000000001000000000000010000           TAIPEI                                                                                                                                      20110103           AAA                                TAIPEI                             TAIWAN                                                                                                                                                                                                                                                                                                                                       N/A                                                                                                                                                010010000000000000000000000000                                                                                                                                                                                                                                                                                                                                                                                      100000000002500                                    TW$76                                  USDTAIWAN            ICBCUS33   0                                           00000000000000                                                                                                                                                                                                                           ";
		tita.setText(t);
		tita.setCaretPosition(0);
		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		JPanel p1 = new JPanel();
		p1.add(btn);
		p1.add(titaTxCode);
		p1.add(tita);
		p1.setBorder(new CompoundBorder(new TitledBorder(new EtchedBorder(), "Text"), new EmptyBorder(3, 5, 3, 5)));

		titaParsedArea = new JTextArea("", 7, 50);
		// titaParsedArea.setLineWrap(true);
		// titaParsedArea.setWrapStyleWord(true);
		JPanel p2 = new JPanel(new BorderLayout());
		p2.add(new JScrollPane(titaParsedArea));
		p2.setBorder(new CompoundBorder(new TitledBorder(new EtchedBorder(), "Result"), new EmptyBorder(3, 5, 3, 5)));

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		p.add(p1, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 40;
		p.add(p2, c);

		p.setBorder(new CompoundBorder(new TitledBorder(new EtchedBorder(), "Tita"), new EmptyBorder(3, 5, 3, 5)));

		return p;
	}

	private Component createTotaPanel() {
		JButton btn = new JButton("parse");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				totaParsedArea.setText(tota.getText());
			}
		});
		totaTxCode = new JTextField(5);
		tota = new JTextField(50);

		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		JPanel p1 = new JPanel();
		p1.add(btn);
		p1.add(totaTxCode);
		p1.add(tota);
		p1.setBorder(new CompoundBorder(new TitledBorder(new EtchedBorder(), "Text"), new EmptyBorder(3, 5, 3, 5)));

		totaParsedArea = new JTextArea("", 7, 50);
		// totaParsedArea.setLineWrap(true);
		// totaParsedArea.setWrapStyleWord(true);
		JPanel p2 = new JPanel(new BorderLayout());
		p2.add(new JScrollPane(totaParsedArea));
		p2.setBorder(new CompoundBorder(new TitledBorder(new EtchedBorder(), "Result"), new EmptyBorder(3, 5, 3, 5)));

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		p.add(p1, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 40;
		p.add(p2, c);

		p.setBorder(new CompoundBorder(new TitledBorder(new EtchedBorder(), "Tota"), new EmptyBorder(3, 5, 3, 5)));

		return p;

	}

	private String parseTita(String text, String text2) {

		return text + "\n" + text2;
	}

	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("Tita/Tota unFormatter");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add content to the window.
		frame.add(new PoorParser());

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				UIManager.put("TextArea.margin", new Insets(5, 5, 5, 5));
				createAndShowGUI();
			}
		});

	}

}
