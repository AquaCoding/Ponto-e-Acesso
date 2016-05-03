package aquacoding.utils;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class MaskField {
	public static void moneyMask(TextField field) {
		field.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				TextField tf = (TextField) event.getSource();
				if(!isMoney(tf.getText() + event.getCharacter())) {
					event.consume();
				}
			}
		});
	}

	public static void doubleMask(TextField field) {
		field.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				TextField tf = (TextField) event.getSource();
				if(!isDouble(tf.getText() + event.getCharacter())) {
					event.consume();
				}
			}
		});
	}

	public static void intMask(TextField field) {
		field.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(!event.getCharacter().matches("\\d")) {
					event.consume();
				}
			}
		});
	}

	public static void cpfMaks(TextField field) {
		field.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCharacter().matches("\\d")) {
					TextField tf = (TextField) event.getSource();
					String newValue = tf.getText() + event.getCharacter();

					if(newValue.length() <= 11) {
						event.consume();
						tf.setText(newValue);
					} else {
						event.consume();
					}

					tf.positionCaret(tf.getText().length());
				} else {
					event.consume();
				}
			}
		});
	}

	public static void rgMaks(TextField field) {
		field.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCharacter().matches("\\d")) {
					TextField tf = (TextField) event.getSource();
					String newValue = tf.getText() + event.getCharacter();

					if(newValue.length() <= 9) {
						event.consume();
						tf.setText(newValue);
					} else {
						event.consume();
					}

					tf.positionCaret(tf.getText().length());
				} else {
					event.consume();
				}
			}
		});
	}

	public static void phoneMask(TextField field) {
		field.addEventHandler(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent event) {

				if(event.getCharacter().matches("\\d")) {
					TextField tf = (TextField) event.getSource();
					String newValue = tf.getText() + event.getCharacter();

					if(newValue.length() == 1) {
						event.consume();
						tf.setText("("+newValue);
					} else if (newValue.length() == 3) {
						event.consume();
						tf.setText(newValue+") ");
					}else if(newValue.length() == 9) {
						event.consume();
						tf.setText(newValue+"-");
					} else if(newValue.length() <= 15){
						event.consume();
						tf.setText(newValue);
					} else {
						event.consume();
					}

					tf.positionCaret(tf.getText().length());
				} else {
					event.consume();
				}
			}
		});

		field.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				KeyCode a = event.getCode();
				if(a.getName().equals("Backspace")){
					TextField tf = (TextField) event.getSource();

					if(tf.getText().length() == 2) {
						event.consume();
						tf.setText("");
					} else if(tf.getText().length() == 5) {
						event.consume();
						tf.setText(tf.getText(0, 2));
					} else if(tf.getText().length() == 10) {
						event.consume();
						tf.setText(tf.getText(0, 8));
					}

					tf.positionCaret(tf.getText().length());
				} else {
					if(!a.getName().equals("Tab"))
						event.consume();
				}
			}
		});
	}

	private static boolean isMoney(String value) {
		if(value.matches("^[\\d]{1,}[.]{0,1}[\\d]{0,2}")) {
			int dots = 0;

			for(char a : value.toCharArray()) {
				if(a == '.')
					dots++;
			}

			if(dots <= 1)
				return true;

			return false;
		} else {
			return false;
		}
	}

	private static boolean isDouble(String value) {
		if(value.matches("^[\\d]{1,}[.]{0,1}[\\d]{0,2}")) {
			int dots = 0;

			for(char a : value.toCharArray()) {
				if(a == '.')
					dots++;
			}

			if(dots <= 1)
				return true;

			return false;
		} else {
			return false;
		}
	}
}
