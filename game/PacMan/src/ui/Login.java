package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Login frame.
 * @author Joshua
 */
public class Login extends ClientFrame {

	// Members
	private JButton loginButton;
	private JPanel bottom;
	private DialogPanel dialogPanel;
	
	/**
	 * Constructor.
	 */
	public Login() {
		super("Log In");

		loginButton = new JButton("Login");
		bottom = new JPanel();
		
		bottom.add(loginButton);
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginButton_actionPerformed(e);
			}
		});

		bottom.setBorder(BorderFactory.createEtchedBorder());
		dialogPanel = new DialogPanel();

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(bottom, BorderLayout.SOUTH);
		getContentPane().add(dialogPanel, BorderLayout.CENTER);
		
		setSize(300, 200);
		setVisible(true);
	}

	/**
	 * Handle login button clicks.
	 * @param evt Event object.
	 */
	public void loginButton_actionPerformed(ActionEvent evt) {
		dialogPanel.setErrorLabelVisible(false);
		client.Main.login(this, dialogPanel.getUsername(), dialogPanel.getPassword());
	}

	/**
	 * Show the login error message.
	 */
	public void showLoginError() {
		dialogPanel.setErrorLabelVisible(true);
	}

	/**
	 * Panel containing input boxes and associated labels.
	 */
	private class DialogPanel extends JPanel {

		private JLabel nameLabel;
		private JTextField nameField;
		private JLabel errorLabel;

		/**
		 * Constructor.
		 */
		public DialogPanel() {
			nameLabel = new JLabel("Username:");
			nameField = new JTextField();
			nameField.setDocument(new LimitedDocument(8));
			errorLabel = new JLabel("Incorrect username!");
			errorLabel.setVisible(false);

			add(nameLabel);
			add(nameField);
			add(errorLabel);
		}

		/**
		 * Layout the panel.
		 */
		@Override
		public void doLayout() {
			nameLabel.setBounds(10, 10, 90, 30);
			nameField.setBounds(110, 10, 150, 30);
			errorLabel.setBounds(30, 100, 250, 30);
		}

		/**
		 * Get the username.
		 * @return The username.
		 */
		public String getUsername() { return nameField.getText(); }

		/**
		 * Get the password.
		 * @return The password.
		 */
		public String getPassword() { return nameField.getText(); }

		/**
		 * Make the error label visible/hidden.
		 * @param visible True to show the error label.
		 */
		public void setErrorLabelVisible(boolean visible) {
			errorLabel.setVisible(visible);
		}
	}
}
