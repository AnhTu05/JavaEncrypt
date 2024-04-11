package pack;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLDocument.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ListIterator;
import java.awt.event.ActionEvent;

public class Main extends JFrame {

	private JPanel contentPane;
	private static JTextField textField;
	private static JPasswordField passwordField;
	private JSONArray accountList = new JSONArray();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		textField.setBounds(160, 71, 232, 31);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Username:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblNewLabel.setBounds(30, 71, 111, 31);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Login & Register Form");
		lblNewLabel_1.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(0, 0, 434, 45);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblPassword.setBounds(30, 127, 111, 31);
		contentPane.add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Segoe UI", Font.BOLD, 18));
		passwordField.setBounds(160, 129, 232, 31);
		contentPane.add(passwordField);
		
		JCheckBox chbPassword = new JCheckBox("See the password");
		chbPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chbPassword.isSelected()) {
					passwordField.setEchoChar((char)0);
				}
				else {
					passwordField.setEchoChar('●');
				}
			}
		});
		chbPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		chbPassword.setBounds(259, 165, 133, 23);
		contentPane.add(chbPassword);
		
		JButton btnNewButton = new JButton("Login");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JSONParser jsonParser = new JSONParser();
				try (FileReader reader = new FileReader("output.json")) {
					Object obj = jsonParser.parse(reader);
					JSONArray account = (JSONArray) obj;	
					ListIterator it = account.listIterator();
					boolean isLogin = false;
					while (it.hasNext()) {
						JSONObject accountObject = (JSONObject) it.next();
						//System.out.println(accountObject);
						JSONObject acc = (JSONObject) accountObject.get("account");
						String password = (String) acc.get("password");
						String username = (String) acc.get("username");
						//System.out.println(password + '\n' + username);
						if (textField.getText().equals(username) &&
							MD5.getMd5(passwordField.getText()).equals(password)) {
								Welcome w = new Welcome();
								w.setVisible(true);
								w.setLocationRelativeTo(null);
								setVisible(false);
								isLogin = true;
						}
					}
					if (!isLogin) {
						JOptionPane.showMessageDialog(null, "Invalid username or password!");
					}
				}
				catch (IOException ex) {
					ex.printStackTrace();
				} catch (ParseException e1) {
					
				}
			}
		});
		btnNewButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
		btnNewButton.setBounds(83, 201, 110, 30);
		contentPane.add(btnNewButton);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean isAlready = true;
				JSONParser jsonParser = new JSONParser();
				try (FileReader reader = new FileReader("output.json")) {
					Object obj = jsonParser.parse(reader);
					JSONArray account = (JSONArray) obj;	
					ListIterator it = account.listIterator();
					while (it.hasNext()) {
						JSONObject accountObject = (JSONObject) it.next();
						JSONObject acc = (JSONObject) accountObject.get("account");
						String username = (String) acc.get("username");
						if (textField.getText().equals(username)) {
							JOptionPane.showMessageDialog(null, "Username are already used!");
							isAlready = false;
						}
						accountList.add(accountObject);
					}
				}
				catch (IOException ex) {
					ex.printStackTrace();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				if (isAlready) {
					JSONObject file = new JSONObject();
					file.put("username", textField.getText());
					String pw_encrypt = MD5.getMd5(passwordField.getText());
					file.put("password", pw_encrypt);

					JSONObject empList = new JSONObject();
					empList.put("account", file);
					accountList.add(empList);
					try {
						FileWriter f = new FileWriter("output.json");
						f.write(accountList.toJSONString());
						f.flush();
						f.close();
						JOptionPane.showMessageDialog(null, "Done!");
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 16));
		btnRegister.setBounds(245, 201, 110, 30);
		contentPane.add(btnRegister);
	}
}
