package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Registration frame.
 * @author Joshua
 */
public class Register extends ClientFrame {

	// Members
	private Button submitButton,  cancelButton;
	private JPanel bottom;
	private DialogPanel dialogPanel;

	/**
	 * Constructor.
	 */
	public Register() {
		super("Registration");
		
		submitButton = new Button("Submit");
		cancelButton = new Button("Cancel");
		bottom = new JPanel();

		bottom.add(submitButton);
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				submitButton_actionPerformed(e);
			}
		});

		bottom.add(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelButton_actionPerformed(e);
			}
		});

		bottom.setBorder(BorderFactory.createEtchedBorder());
		dialogPanel = new DialogPanel();
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(bottom, BorderLayout.SOUTH);
		getContentPane().add(dialogPanel, BorderLayout.CENTER);
		setSize(320, 200);

		setResizable(false);
		setVisible(true);
	}

	/**
	 * Show an error message.
	 * @param error The message to display.
	 */
	public void showRegistrationError(String error) {
		dialogPanel.setErrorLabelText(error);
		dialogPanel.setErrorLabelVisible(true);
	}

	/**
	 * Panel containing input boxes.
	 */
	private class DialogPanel extends JPanel {

		// Members
		private JLabel nameLabel, passwordLabel, confirmPasswordLabel;
		private JTextField nameField;
		private JPasswordField passwordField, confirmPasswordField;
		private JLabel errorLabel;

		/**
		 * Construtor.
		 */
		public DialogPanel() {
			nameLabel = new JLabel("Username:");
			nameField = new JTextField();
			nameField.setDocument(new LimitedDocument(8));
			passwordLabel = new JLabel("Password:");
			passwordField = new JPasswordField();
			passwordField.setDocument(new LimitedDocument(10));
			confirmPasswordLabel = new JLabel("Confirm Password:");
			confirmPasswordField = new JPasswordField();
			confirmPasswordField.setDocument(new LimitedDocument(10));

			errorLabel = new JLabel("Username already in use!");
			errorLabel.setVisible(false);

			add(nameLabel);
			add(nameField);
			add(passwordLabel);
			add(passwordField);
			add(confirmPasswordLabel);
			add(confirmPasswordField);
			add(errorLabel);
		}

		/**
		 * Layout the panel.
		 */
		@Override
		public void doLayout() {
			nameLabel.setBounds(10, 10, 90, 30);
			nameField.setBounds(150, 10, 150, 30);
			passwordLabel.setBounds(10, 40, 90, 30);
			passwordField.setBounds(150, 40, 150, 30);
			confirmPasswordLabel.setBounds(10, 70, 140, 30);
			confirmPasswordField.setBounds(150, 70, 150, 30);
			errorLabel.setBounds(10, 100, 250, 30);
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
		public String getPassword() { return new String(passwordField.getPassword()); }

		/**
		 * Get the confirmation password.
		 * @return The confirmation password.
		 */
		public String getConfirmPassword() { return new String(confirmPasswordField.getPassword()); }

		/**
		 * Set the error label hidden or visible.
		 * @param visible True to show the error label; false to hide it.
		 */
		public void setErrorLabelVisible(boolean visible) {
			errorLabel.setVisible(visible);
		}

		/**
		 * Set the text on the error label.
		 * @param text The text to show.
		 */
		public void setErrorLabelText(String text) {
			errorLabel.setText(text);
		}
	}

	/**
	 * Handle submit button clicks.
	 * @param evt Event object.
	 */
	public void submitButton_actionPerformed(ActionEvent evt) {
		dialogPanel.setErrorLabelVisible(false);
		client.Main.register(this, dialogPanel.getUsername(), dialogPanel.getPassword(), dialogPanel.getConfirmPassword());
	}

	/**
	 * Handle cancel button clicks.
	 * @param evt Event object.
	 */
	public void cancelButton_actionPerformed(ActionEvent evt) {
		client.Main.showLogin();
		dispose();
	}
}  

