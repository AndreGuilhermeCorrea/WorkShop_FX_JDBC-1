package gui.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class Utils {
	// classe responsável em retornar o palco Stage atual peritindo que se abra uma
	// janela apartir de um botão

	// função retorna stage o palco atual retornando o evento que o botao recebeu
	public static Stage currrentStage(ActionEvent event) {
		// implementação para acessar o stage apartir do objeto de evento com
		// downcasting NODE
		// acessando portanto o palco e a janela(superclasse do stage) também com
		// downcasting para poder ser convertida para stage
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}

	// método para converter o valor para numero inteiro
	public static Integer tryParseToInt(String str) {
		// como pode gerar uma exceção, caso ocorra será lançado valor nulo
		try {
			return Integer.parseInt(str);
		} // retornando valor nulo caso ocorra exceção
		catch (NumberFormatException e) {
			return null;
		}

	}
	
	
	// método para converter o valor double para numero inteiro
	public static Double tryParseToDouble(String str) {
		// como pode gerar uma exceção, caso ocorra será lançado valor nulo
		try {
			return Double.parseDouble(str);
		} // retornando valor nulo caso ocorra exceção
		catch (NumberFormatException e) {
			return null;
		}

	}

	public static <T> void formatTableColumnDate(TableColumn<T, Date> tableColumn, String format) {
		tableColumn.setCellFactory(column -> {
			TableCell<T, Date> cell = new TableCell<T, Date>() {
				private SimpleDateFormat sdf = new SimpleDateFormat(format);

				@Override
				protected void updateItem(Date item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						setText(sdf.format(item));
					}
				}
			};
			return cell;
		});
	}

	public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) {
		tableColumn.setCellFactory(column -> {
			TableCell<T, Double> cell = new TableCell<T, Double>() {
				@Override
				protected void updateItem(Double item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						Locale.setDefault(Locale.US);
						setText(String.format("%." + decimalPlaces + "f", item));
					}
				}
			};
			return cell;
		});
	}

	public static void formatDatePicker(DatePicker datePicker, String format) {
		datePicker.setConverter(new StringConverter<LocalDate>() {

			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
			{
				datePicker.setPromptText(format.toLowerCase());
			}

			@Override
			public String toString(LocalDate date) {
				if (date != null) {
					return dateFormatter.format(date);
				} else {
					return "";
				}
			}

			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				} else {
					return null;
				}
			}
		});
	}

}
