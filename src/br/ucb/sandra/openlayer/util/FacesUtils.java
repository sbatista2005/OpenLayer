package br.ucb.sandra.openlayer.util;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

public class FacesUtils {

	public static void addInfoMessage(String msg) {
		addInfoMessage(msg);
	}

	public static void goToPage(String page) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext
				.getExternalContext().getResponse();
		HttpServletRequest request = (HttpServletRequest) facesContext
				.getExternalContext().getRequest();

		String url = request.getContextPath();

		try {
			response.sendRedirect(url + page);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void renderPage(String page) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		UIViewRoot root = facesContext.getApplication().getViewHandler()
				.createView(facesContext, page);
		facesContext.setViewRoot(root);
		facesContext.renderResponse();
	}

	

	public static void addWarn(String field, String key) {
		addMessage(field, key, key, FacesMessage.SEVERITY_WARN);
	}

	public static void addWarn(String field, String key, String... arguments) {
		addMessage(field, key, key, FacesMessage.SEVERITY_WARN, arguments);
	}

	public static void addSucessMessage(String field, String key) {
		addMessage(field, key, key, FacesMessage.SEVERITY_INFO);
	}

	public static void addSucessMessage(String field, String key,
			String... arguments) {
		addMessage(field, key, key, FacesMessage.SEVERITY_INFO, arguments);
	}

	public static void addError(String field, String key) {
		addMessage(field, key, key, FacesMessage.SEVERITY_ERROR);
	}

	public static void addError(String field, String key, String... arguments) {

		addMessage(field, key, key, FacesMessage.SEVERITY_ERROR, arguments);
	}

	public static void addMessage(String field, String key, Severity severity) {
		addMessage(field, key, key, severity);
	}

	/**
	 * Add Message with i18n resources in detail and summary (messages) and its
	 * format arguments. e.g: The {0}, {1} arguments
	 * 
	 * @param field
	 * @param summaryKey
	 *            = key of resource bundle for summary messages
	 * @param detailKey
	 *            = key of resource bundle for detail messages
	 * @param arguments
	 *            = arguments for i18n and format in summary and detail messages
	 */
	public static void addMessage(String field, String summaryKey,
			String detailKey, Severity severity, String... arguments) {
		FacesContext facesContext = FacesContext.getCurrentInstance();

		FacesMessage message = new FacesMessage();
		message.setSeverity(severity);

		if (arguments != null) {
			List<String> argumentKeys = new ArrayList<String>();
			for (String argument : arguments) {
				argumentKeys.add(getMessageByKey(argument));
			}

			message.setSummary(MessageFormat.format(
					getMessageByKey(summaryKey), argumentKeys));
			message.setDetail(MessageFormat.format(getMessageByKey(detailKey),
					argumentKeys));
		} else {
			message.setSummary(summaryKey);
			message.setDetail(detailKey);

		}

		facesContext.addMessage(field == null || field.equals("") ? null
				: field, message);

	}

	public static void addMessage(String field, String summaryKey,
			String detailKey, Severity severity) {
		FacesContext facesContext = FacesContext.getCurrentInstance();

		FacesMessage message = new FacesMessage();
		message.setSeverity(severity);

		message.setSummary(getMessageByKey(summaryKey));
		message.setDetail(getMessageByKey(detailKey));

		facesContext.addMessage(field == null || field.equals("") ? null
				: field, message);
	}

	// String s = MessageFormat.format("{2}salvou {0} com sucesso{1} as {3,
	// date, dd/MM/yyyy}","usuario", "!", "Msg: ", new Date());
	// System.out.println("Depois: " + s);

	public static String getMessageByKey(String key) {
		String messageBundleName = FacesContext.getCurrentInstance()
				.getApplication().getMessageBundle();
		Locale locale = FacesContext.getCurrentInstance().getApplication()
				.getDefaultLocale();
		ResourceBundle resourceBundle = (locale == null) ? ResourceBundle
				.getBundle(messageBundleName) : ResourceBundle.getBundle(
				messageBundleName, locale);
		try {
			return resourceBundle.getString(key);
		} catch (Exception e) {
			return key;
		}
	}

	@SuppressWarnings("rawtypes")
	/**
	 * Este metodo recebe uma classe de enum e um valor key e retorna o valor
	 * I18n.
	 */
	public static String internationalizeEnum(Class clazz, Enum key) {
		return FacesUtils.getMessageByKey(clazz.getSimpleName() + "." + key);
	}

	/**
	 * Este metodo recebe uma classe de enum e um valor I18n e retorna o enum
	 * correspondente. Funciona ao contrario do metodo internacionalizeEnum.
	 * Sera util quando o combo foi populado com o metodo
	 * enumSelectItemsWithI18nValue.
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public static Enum unInternationalizeEnum(Class<? extends Enum> type,
			String I18nValue) {

		for (Enum e : type.getEnumConstants()) {
			if (I18nValue.equals(FacesUtils.getMessageByKey(type
					.getSimpleName()
					+ "." + e.name()))) {
				return e;
			}
		}

		return null;
	}

	@SuppressWarnings("rawtypes")
	/**
	 * Cria uma lista de selectItens onde o value = valor do enum, e o label =
	 * valor I18n do enum
	 */
	public static List<SelectItem> enumSelectItems(Class<? extends Enum> type) {
		ArrayList<SelectItem> result = new ArrayList<SelectItem>();
		for (Enum e : type.getEnumConstants()) {
			result.add(new SelectItem(e, FacesUtils.getMessageByKey(type
					.getSimpleName()
					+ "." + e.name())));
		}
		return result;
	}

	/**
	 * Cria uma lista de selectItens onde o value = valor do enum, e o label =
	 * valor I18n do enum, ignorando uma chave
	 */
	@SuppressWarnings("rawtypes")
	public static List<SelectItem> enumSelectItems(Class<? extends Enum> type,
			Enum ignoreKey) {
		ArrayList<SelectItem> result = new ArrayList<SelectItem>();
		for (Enum e : type.getEnumConstants()) {
			if (!e.equals(ignoreKey)) {
				result.add(new SelectItem(e, FacesUtils.getMessageByKey(type
						.getSimpleName()
						+ "." + e.name())));
			}
		}
		return result;
	}

	/**
	 * Cria uma lista de selectItens onde o value = valor I18n do enum, e o
	 * label = valor I18n do enum
	 */
	@SuppressWarnings("rawtypes")
	public static List<SelectItem> enumSelectItemsWithI18nValue(
			Class<? extends Enum> type) {
		ArrayList<SelectItem> result = new ArrayList<SelectItem>();
		for (Enum e : type.getEnumConstants()) {
			result.add(new SelectItem(FacesUtils.getMessageByKey(type
					.getSimpleName()
					+ "." + e.name()), FacesUtils.getMessageByKey(type
					.getSimpleName()
					+ "." + e.name())));
		}
		return result;
	}

	/**
	 * Recebe uma lista de beans como parametro e devolve uma lista de
	 * selectItem do JSF
	 * 
	 * @param lista
	 *            = Uma lista de beans qualquer.
	 * @param fieldLabel
	 *            = O atributo dos beans que serao exibidos no label do
	 *            selectItem
	 * @return Retorna uma lista de SelectItem para ser usado em paginas JSF
	 */
	public static List<SelectItem> createSelectItemList(
			List<? extends Object> lista, String... fieldLabel) {

		return createSelectItemListwithSeparator(lista, " ", fieldLabel);

	}

	/**
	 * Recebe uma lista de beans como parametro e devolve uma lista de
	 * selectItem do JSF
	 * 
	 * @param lista
	 *            = Uma lista de beans qualquer.
	 * @param fieldLabel
	 *            = O atributo dos beans que serao exibidos no label do
	 *            selectItem
	 * @param separator
	 *            = O caracter de separacao entre os atributos exibidos,
	 *            exemplos: ' - ' ou ' : ' ou ' / '
	 * 
	 * @return Retorna uma lista de SelectItem para ser usado em paginas JSF
	 */
	public static List<SelectItem> createSelectItemListwithSeparator(
			List<? extends Object> lista, String separator,
			String... fieldLabel) {

		List<SelectItem> retorno = new ArrayList<SelectItem>();
		Iterator<? extends Object> iter = lista.iterator();

		while (iter.hasNext()) {
			Object element = iter.next();

			try {
				SelectItem selectItem = null;
				List<String> fields = new ArrayList<String>();
				for (String field : fieldLabel) {
					BeanUtils.getNestedProperty(element, field);
					Object label = BeanUtils.getNestedProperty(element, field);
					if (label != null) {
						fields.add(String.valueOf(label));
					} else {
						fields.add("");
					}

				}
				StringBuffer concat = new StringBuffer();
				for (String field : fields) {
					concat.append(field + separator);
				}
				String pronta = concat.substring(0, concat.length()
						- separator.length());
				if (element != null) {
					selectItem = new SelectItem(element, pronta);
				} else {
					selectItem = new SelectItem("", "");
				}

				retorno.add(selectItem);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return retorno;
		/*
		 * List<SelectItem> retorno = new ArrayList<SelectItem>(); Iterator<?
		 * extends Object> iter = lista.iterator();
		 * 
		 * while (iter.hasNext()) { Object element = iter.next();
		 * 
		 * try {
		 * 
		 * BeanUtils.getNestedProperty(element, fieldLabel); Object label =
		 * BeanUtils.getNestedProperty(element, fieldLabel);
		 * 
		 * SelectItem selectItem = new SelectItem(element,
		 * String.valueOf(label)); retorno.add(selectItem); } catch (Exception
		 * e) { e.printStackTrace(); } } return retorno;
		 */

	}

	/**
	 * Recebe uma lista de beans como parametro e devolve uma lista de
	 * selectItem do JSF.
	 * 
	 * @param lista
	 *            = Uma lista de beans qualquer.
	 * @param fieldValue
	 *            = O atributo dos beans que serao inseridos no value dos
	 *            elementos de selecao.
	 * @param fieldLabel
	 *            = O atributo dos beans que serao exibidos no label do
	 *            selectItem
	 * @return Retorna uma lista de SelectItem para ser usado em paginas JSF
	 */
	public static List<SelectItem> createSelectItemListWithValue(
			List<? extends Object> lista, String fieldValue, String fieldLabel) {

		List<SelectItem> retorno = new ArrayList<SelectItem>();
		Iterator<? extends Object> iter = lista.iterator();

		while (iter.hasNext()) {
			Object element = iter.next();

			try {

				BeanUtils.getNestedProperty(element, fieldLabel);

				Object label = BeanUtils.getNestedProperty(element, fieldLabel);
				Object value = BeanUtils.getNestedProperty(element, fieldValue);

				if (value instanceof Integer || value instanceof Long) {
					value = String.valueOf(value);
				}

				SelectItem selectItem = new SelectItem(value, String
						.valueOf(label));
				retorno.add(selectItem);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return retorno;
	}

	/**
	 * Recebe uma lista de beans como parametro e devolve uma lista de
	 * selectItem do JSF
	 * 
	 * @param lista
	 *            = Uma lista de beans qualquer.
	 * @param fieldValue
	 *            = O atributo dos beans que serao utilizados como value no
	 *            selectItem
	 * @param fieldLabel
	 *            = O atributo dos beans que serao exibidos no label do
	 *            selectItem
	 * @param separator
	 *            = O caracter de separacao entre os atributos exibidos,
	 *            exemplos: ' - ' ou ' : ' ou ' / '
	 * 
	 * @return Retorna uma lista de SelectItem para ser usado em paginas JSF
	 */
	public static List<SelectItem> createSelectItemListwithValueAndSeparator(
			List<? extends Object> lista, String fieldValue, String separator,
			String... fieldLabel) {

		List<SelectItem> retorno = new ArrayList<SelectItem>();
		Iterator<? extends Object> iter = lista.iterator();

		while (iter.hasNext()) {
			Object element = iter.next();

			try {
				SelectItem selectItem = null;
				List<String> fields = new ArrayList<String>();
				for (String field : fieldLabel) {
					BeanUtils.getNestedProperty(element, field);
					Object label = BeanUtils.getNestedProperty(element, field);

					fields.add(String.valueOf(label));
				}
				StringBuffer concat = new StringBuffer();
				for (String field : fields) {
					concat.append(field + separator);
				}
				String pronta = concat.substring(0, concat.length()
						- separator.length());

				Object value = BeanUtils.getNestedProperty(element, fieldValue);

				selectItem = new SelectItem(value, pronta);

				retorno.add(selectItem);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return retorno;
	}
}
