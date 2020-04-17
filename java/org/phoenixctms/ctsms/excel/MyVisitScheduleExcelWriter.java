package org.phoenixctms.ctsms.excel;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jxl.HeaderFooter;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;

import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.vo.ProbandAddressOutVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleExcelVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;
import org.phoenixctms.ctsms.excel.VisitScheduleExcelWriter.Styles;

public class MyVisitScheduleExcelWriter extends VisitScheduleExcelWriter {

	//public enum Styles {
	//	TRIAL_VISIT_SCHEDULE, PROBAND_VISIT_SCHEDULE, PROBAND_TRIAL_VISIT_SCHEDULE, TRAVEL_EXPENSES_VISIT_SCHEDULE,
	//}

	public static String getAliquotVisitReimbursementColumnName() {
		return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.ALIQUOT_VISIT_REIMBURSEMENT_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getEnrollmentStatusColumnName() {
		return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.ENROLLMENT_STATUS_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getEnrollmentStatusReasonColumnName() {
		return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.ENROLLMENT_STATUS_REASON_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getEnrollmentStatusTimestampColumnName() {
		return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.ENROLLMENT_STATUS_TIMESTAMP_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getEnrollmentStatusTypeIsCountColumnName() {
		return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.ENROLLMENT_STATUS_TYPE_IS_COUNT_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	public static String getFirstVisitReimbursementColumnName() {
		return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.FIRST_VISIT_REIMBURSEMENT_HEAD, ExcelUtil.DEFAULT_LABEL);
	}

	private VisitScheduleExcelVO excelVO;
	private TrialOutVO trial;
	private ProbandOutVO proband;
	private ProbandAddressOutVO address;
	private Styles style;
	private static final String VISIT_SCHEDULE_EXCEL_FILENAME_TRIAL = "trial_";
	private static final String VISIT_SCHEDULE_EXCEL_FILENAME_PROBAND = "proband_";
	public static final String RECENT_PROBAND_LIST_STATUS_ENTRY = "_RECENT_PROBAND_LIST_STATUS_ENTRY";

	public MyVisitScheduleExcelWriter(boolean omitFields, Styles style) {
		super();
		this.style = style;
		excelVO = new VisitScheduleExcelVO();
		getSpreadSheetWriters().add(createSpreadSheetWriter(omitFields));
	}

	protected void appendHeaderFooter(HeaderFooter header, HeaderFooter footer) throws Exception {
		String temp;
		header.getLeft().clear();
		temp = CommonUtil.trialOutVOToString(trial) + " (test)"; //modified for this customer renderer example ...
		if (!CommonUtil.isEmptyString(temp)) {
			header.getLeft().append(L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.TRIAL_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp));
		} else {
			header.getLeft().append(L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.ALL_TRIALS_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL));
		}
		header.getCentre().clear();
		temp = CommonUtil.probandOutVOToString(proband);
		switch (style) {
			case TRIAL_VISIT_SCHEDULE:
				if (!CommonUtil.isEmptyString(temp)) {
					temp = L10nUtil
							.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.TRIAL_VISIT_SCHEDULE_PROBAND_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp);
				} else {
					temp = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.TRIAL_VISIT_SCHEDULE_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL);
				}
				break;
			case PROBAND_VISIT_SCHEDULE:
				if (!CommonUtil.isEmptyString(temp)) {
					temp = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.PROBAND_VISIT_SCHEDULE_PROBAND_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL,
							temp);
				} else {
					temp = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.PROBAND_VISIT_SCHEDULE_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL);
				}
				break;
			case PROBAND_TRIAL_VISIT_SCHEDULE:
				if (!CommonUtil.isEmptyString(temp)) {
					temp = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.PROBAND_TRIAL_VISIT_SCHEDULE_PROBAND_HEADER_FOOTER,
							ExcelUtil.DEFAULT_LABEL, temp);
				} else {
					temp = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.PROBAND_TRIAL_VISIT_SCHEDULE_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL);
				}
				break;
			case TRAVEL_EXPENSES_VISIT_SCHEDULE:
				if (!CommonUtil.isEmptyString(temp)) {
					temp = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_PROBAND_HEADER_FOOTER,
							ExcelUtil.DEFAULT_LABEL, temp);
				} else {
					temp = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL);
				}
				break;
			default:
		}
		if (!CommonUtil.isEmptyString(temp)) {
			header.getCentre().append(temp);
		}
		header.getRight().clear();
		temp = address == null ? null : address.getName();
		if (!CommonUtil.isEmptyString(temp)) {
			header.getRight().append(L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.ADDRESS_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp));
		}
		footer.getLeft().clear();
		temp = excelVO.getFileName();
		if (!CommonUtil.isEmptyString(temp)) {
			footer.getLeft().append(L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.FILE_NAME_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp));
		}
		footer.getCentre().clear();
		footer.getCentre().append(L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.PAGE_NUMBER_HEADER_FOOTER_1, ExcelUtil.DEFAULT_LABEL));
		footer.getCentre().appendPageNumber();
		footer.getCentre().append(L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.PAGE_NUMBER_HEADER_FOOTER_2, ExcelUtil.DEFAULT_LABEL));
		footer.getCentre().appendTotalPages();
		footer.getRight().clear();
		temp = excelVO.getRequestingUser() != null ? CommonUtil.staffOutVOToString(excelVO.getRequestingUser().getIdentity()) : null;
		if (!CommonUtil.isEmptyString(temp)) {
			footer.getRight().append(
					L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.DATE_REQUESTING_USER_HEADER_FOOTER, ExcelUtil.DEFAULT_LABEL, temp,
							CommonUtil.formatDate(excelVO.getContentTimestamp() != null ? excelVO.getContentTimestamp() : now, ExcelUtil.EXCEL_DATE_PATTERN,
									L10nUtil.getLocale(Locales.USER))));
			// (new SimpleDateFormat(ExcelUtil.EXCEL_DATE_PATTERN)).format(excelVO.getContentTimestamp() != null ? excelVO.getContentTimestamp() : now)));
		}
	}

	@Override
	public void applySpreadsheetSettings(WritableSheet spreadSheet, int sheetIndex) throws Exception {
		Integer scaleFactor = null;
		switch (style) {
			case TRIAL_VISIT_SCHEDULE:
				scaleFactor = Settings.getIntNullable(MyVisitScheduleExcelSettingCodes.TRIAL_VISIT_SCHEDULE_SCALE_FACTOR, Bundle.VISIT_SCHEDULE_EXCEL,
						MyVisitScheduleExcelDefaultSettings.TRIAL_VISIT_SCHEDULE_SCALE_FACTOR);
				if (Settings.getBoolean(MyVisitScheduleExcelSettingCodes.TRIAL_VISIT_SCHEDULE_APPEND_HEADER_FOOTER, Bundle.VISIT_SCHEDULE_EXCEL,
						MyVisitScheduleExcelDefaultSettings.TRIAL_VISIT_SCHEDULE_APPEND_HEADER_FOOTER)) {
					appendHeaderFooter(spreadSheet.getSettings().getHeader(), spreadSheet.getSettings().getFooter());
				}
				break;
			case PROBAND_VISIT_SCHEDULE:
				scaleFactor = Settings.getIntNullable(MyVisitScheduleExcelSettingCodes.PROBAND_VISIT_SCHEDULE_SCALE_FACTOR, Bundle.VISIT_SCHEDULE_EXCEL,
						MyVisitScheduleExcelDefaultSettings.PROBAND_VISIT_SCHEDULE_SCALE_FACTOR);
				if (Settings.getBoolean(MyVisitScheduleExcelSettingCodes.PROBAND_VISIT_SCHEDULE_APPEND_HEADER_FOOTER, Bundle.VISIT_SCHEDULE_EXCEL,
						MyVisitScheduleExcelDefaultSettings.PROBAND_VISIT_SCHEDULE_APPEND_HEADER_FOOTER)) {
					appendHeaderFooter(spreadSheet.getSettings().getHeader(), spreadSheet.getSettings().getFooter());
				}
				break;
			case PROBAND_TRIAL_VISIT_SCHEDULE:
				scaleFactor = Settings.getIntNullable(MyVisitScheduleExcelSettingCodes.PROBAND_TRIAL_VISIT_SCHEDULE_SCALE_FACTOR, Bundle.VISIT_SCHEDULE_EXCEL,
						MyVisitScheduleExcelDefaultSettings.PROBAND_TRIAL_VISIT_SCHEDULE_SCALE_FACTOR);
				if (Settings.getBoolean(MyVisitScheduleExcelSettingCodes.PROBAND_TRIAL_VISIT_SCHEDULE_APPEND_HEADER_FOOTER, Bundle.VISIT_SCHEDULE_EXCEL,
						MyVisitScheduleExcelDefaultSettings.PROBAND_TRIAL_VISIT_SCHEDULE_APPEND_HEADER_FOOTER)) {
					appendHeaderFooter(spreadSheet.getSettings().getHeader(), spreadSheet.getSettings().getFooter());
				}
				break;
			case TRAVEL_EXPENSES_VISIT_SCHEDULE:
				scaleFactor = Settings.getIntNullable(MyVisitScheduleExcelSettingCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_SCALE_FACTOR, Bundle.VISIT_SCHEDULE_EXCEL,
						MyVisitScheduleExcelDefaultSettings.TRAVEL_EXPENSES_VISIT_SCHEDULE_SCALE_FACTOR);
				if (Settings.getBoolean(MyVisitScheduleExcelSettingCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_APPEND_HEADER_FOOTER, Bundle.VISIT_SCHEDULE_EXCEL,
						MyVisitScheduleExcelDefaultSettings.TRAVEL_EXPENSES_VISIT_SCHEDULE_APPEND_HEADER_FOOTER)) {
					appendHeaderFooter(spreadSheet.getSettings().getHeader(), spreadSheet.getSettings().getFooter());
				}
				break;
			default:
		}
		if (scaleFactor != null && scaleFactor.intValue() > 0) {
			spreadSheet.getSettings().setScaleFactor(scaleFactor);
		}
	}

	@Override
	protected void applyWorkbookSettings(WorkbookSettings settings) {
	}

	protected SpreadSheetWriter createSpreadSheetWriter(boolean omitFields) {
		switch (style) {
			case TRIAL_VISIT_SCHEDULE:
				return new SpreadSheetWriter(this,
						getColumnIndexMap(L10nUtil.getVisitScheduleExcelColumns(Locales.USER, MyVisitScheduleExcelLabelCodes.TRIAL_VISIT_SCHEDULE_VO_FIELD_COLUMNS,
								MyVisitScheduleExcelDefaultSettings.TRIAL_VISIT_SCHEDULE_VO_FIELD_COLUMNS)),
						Settings.getInt(MyVisitScheduleExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
						omitFields,
						Settings.getBoolean(MyVisitScheduleExcelSettingCodes.TRIAL_VISIT_SCHEDULE_AUTOSIZE, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.TRIAL_VISIT_SCHEDULE_AUTOSIZE),
						Settings.getBoolean(MyVisitScheduleExcelSettingCodes.TRIAL_VISIT_SCHEDULE_WRITEHEAD, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.TRIAL_VISIT_SCHEDULE_WRITEHEAD),
						Settings.getIntNullable(MyVisitScheduleExcelSettingCodes.TRIAL_VISIT_SCHEDULE_PAGE_BREAK_AT_ROW, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.TRIAL_VISIT_SCHEDULE_PAGE_BREAK_AT_ROW),
						Settings.getBoolean(MyVisitScheduleExcelSettingCodes.TRIAL_VISIT_SCHEDULE_ROW_COLORS, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.TRIAL_VISIT_SCHEDULE_ROW_COLORS),
						Settings.getExcelCellFormat(MyVisitScheduleExcelSettingCodes.TRIAL_VISIT_SCHEDULE_HEAD_FORMAT, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.TRIAL_VISIT_SCHEDULE_HEAD_FORMAT),
						Settings.getExcelCellFormat(MyVisitScheduleExcelSettingCodes.TRIAL_VISIT_SCHEDULE_ROW_FORMAT, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.TRIAL_VISIT_SCHEDULE_ROW_FORMAT));
			case PROBAND_VISIT_SCHEDULE:
				return new SpreadSheetWriter(this,
						getColumnIndexMap(L10nUtil.getVisitScheduleExcelColumns(Locales.USER, MyVisitScheduleExcelLabelCodes.PROBAND_VISIT_SCHEDULE_VO_FIELD_COLUMNS,
								MyVisitScheduleExcelDefaultSettings.PROBAND_VISIT_SCHEDULE_VO_FIELD_COLUMNS)),
						Settings.getInt(MyVisitScheduleExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
						omitFields,
						Settings.getBoolean(MyVisitScheduleExcelSettingCodes.PROBAND_VISIT_SCHEDULE_AUTOSIZE, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.PROBAND_VISIT_SCHEDULE_AUTOSIZE),
						Settings.getBoolean(MyVisitScheduleExcelSettingCodes.PROBAND_VISIT_SCHEDULE_WRITEHEAD, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.PROBAND_VISIT_SCHEDULE_WRITEHEAD),
						Settings.getIntNullable(MyVisitScheduleExcelSettingCodes.PROBAND_VISIT_SCHEDULE_PAGE_BREAK_AT_ROW, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.PROBAND_VISIT_SCHEDULE_PAGE_BREAK_AT_ROW),
						Settings.getBoolean(MyVisitScheduleExcelSettingCodes.PROBAND_VISIT_SCHEDULE_ROW_COLORS, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.PROBAND_VISIT_SCHEDULE_ROW_COLORS),
						Settings.getExcelCellFormat(MyVisitScheduleExcelSettingCodes.PROBAND_VISIT_SCHEDULE_HEAD_FORMAT, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.PROBAND_VISIT_SCHEDULE_HEAD_FORMAT),
						Settings.getExcelCellFormat(MyVisitScheduleExcelSettingCodes.PROBAND_VISIT_SCHEDULE_ROW_FORMAT, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.PROBAND_VISIT_SCHEDULE_ROW_FORMAT));
			case PROBAND_TRIAL_VISIT_SCHEDULE:
				return new SpreadSheetWriter(this,
						getColumnIndexMap(L10nUtil.getVisitScheduleExcelColumns(Locales.USER, MyVisitScheduleExcelLabelCodes.PROBAND_TRIAL_VISIT_SCHEDULE_VO_FIELD_COLUMNS,
								MyVisitScheduleExcelDefaultSettings.PROBAND_TRIAL_VISIT_SCHEDULE_VO_FIELD_COLUMNS)),
						Settings.getInt(MyVisitScheduleExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
						omitFields,
						Settings.getBoolean(MyVisitScheduleExcelSettingCodes.PROBAND_TRIAL_VISIT_SCHEDULE_AUTOSIZE, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.PROBAND_TRIAL_VISIT_SCHEDULE_AUTOSIZE),
						Settings.getBoolean(MyVisitScheduleExcelSettingCodes.PROBAND_TRIAL_VISIT_SCHEDULE_WRITEHEAD, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.PROBAND_TRIAL_VISIT_SCHEDULE_WRITEHEAD),
						Settings.getIntNullable(MyVisitScheduleExcelSettingCodes.PROBAND_TRIAL_VISIT_SCHEDULE_PAGE_BREAK_AT_ROW, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.PROBAND_TRIAL_VISIT_SCHEDULE_PAGE_BREAK_AT_ROW),
						Settings.getBoolean(MyVisitScheduleExcelSettingCodes.PROBAND_TRIAL_VISIT_SCHEDULE_ROW_COLORS, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.PROBAND_TRIAL_VISIT_SCHEDULE_ROW_COLORS),
						Settings.getExcelCellFormat(MyVisitScheduleExcelSettingCodes.PROBAND_TRIAL_VISIT_SCHEDULE_HEAD_FORMAT, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.PROBAND_TRIAL_VISIT_SCHEDULE_HEAD_FORMAT),
						Settings.getExcelCellFormat(MyVisitScheduleExcelSettingCodes.PROBAND_TRIAL_VISIT_SCHEDULE_ROW_FORMAT, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.PROBAND_TRIAL_VISIT_SCHEDULE_ROW_FORMAT));
			case TRAVEL_EXPENSES_VISIT_SCHEDULE:
				return new SpreadSheetWriter(this,
						getColumnIndexMap(L10nUtil.getVisitScheduleExcelColumns(Locales.USER, MyVisitScheduleExcelLabelCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_VO_FIELD_COLUMNS,
								MyVisitScheduleExcelDefaultSettings.TRAVEL_EXPENSES_VISIT_SCHEDULE_VO_FIELD_COLUMNS)),
						Settings.getInt(MyVisitScheduleExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
						omitFields,
						Settings.getBoolean(MyVisitScheduleExcelSettingCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_AUTOSIZE, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.TRAVEL_EXPENSES_VISIT_SCHEDULE_AUTOSIZE),
						Settings.getBoolean(MyVisitScheduleExcelSettingCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_WRITEHEAD, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.TRAVEL_EXPENSES_VISIT_SCHEDULE_WRITEHEAD),
						Settings.getIntNullable(MyVisitScheduleExcelSettingCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_PAGE_BREAK_AT_ROW, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.TRAVEL_EXPENSES_VISIT_SCHEDULE_PAGE_BREAK_AT_ROW),
						Settings.getBoolean(MyVisitScheduleExcelSettingCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_ROW_COLORS, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.TRAVEL_EXPENSES_VISIT_SCHEDULE_ROW_COLORS),
						Settings.getExcelCellFormat(MyVisitScheduleExcelSettingCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_HEAD_FORMAT, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.TRAVEL_EXPENSES_VISIT_SCHEDULE_HEAD_FORMAT),
						Settings.getExcelCellFormat(MyVisitScheduleExcelSettingCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_ROW_FORMAT, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.TRAVEL_EXPENSES_VISIT_SCHEDULE_ROW_FORMAT));
			default:
				return new SpreadSheetWriter(this,
						getColumnIndexMap(new ArrayList<String>()),
						Settings.getInt(MyVisitScheduleExcelSettingCodes.VO_GRAPH_RECURSION_DEPTH, Bundle.VISIT_SCHEDULE_EXCEL,
								MyVisitScheduleExcelDefaultSettings.VO_GRAPH_RECURSION_DEPTH),
						omitFields,
						false,
						true,
						null,
						true,
						ExcelCellFormat.getDefaultHeadFormat(),
						ExcelCellFormat.getDefaultRowFormat());
		}
	}

	public ProbandAddressOutVO getAddress() {
		return address;
	}

	@Override
	public String getColumnTitle(String l10nKey) {
		return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, l10nKey, ExcelUtil.DEFAULT_LABEL);
	}

	public ArrayList<String> getDistinctColumnNames() {
		return getSpreadSheetWriters().get(0).getDistinctColumnNames();
	}

	public HashMap<Long, HashMap<String, Object>> getDistinctFieldRows() {
		return getSpreadSheetWriters().get(0).getDistinctFieldRows();
	}

	public VisitScheduleExcelVO getExcelVO() {
		return excelVO;
	}

	public ProbandOutVO getProband() {
		return proband;
	}

	protected String getStyleName() {
		switch (style) {
			case TRIAL_VISIT_SCHEDULE:
				return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.TRIAL_VISIT_SCHEDULE_NAME, ExcelUtil.DEFAULT_LABEL);
			case PROBAND_VISIT_SCHEDULE:
				return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.PROBAND_VISIT_SCHEDULE_NAME, ExcelUtil.DEFAULT_LABEL);
			case PROBAND_TRIAL_VISIT_SCHEDULE:
				return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.PROBAND_TRIAL_VISIT_SCHEDULE_NAME, ExcelUtil.DEFAULT_LABEL);
			case TRAVEL_EXPENSES_VISIT_SCHEDULE:
				return L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_NAME, ExcelUtil.DEFAULT_LABEL);
			default:
		}
		return "";
	}

	@Override
	public String getTemplateFileName() throws Exception {
		switch (style) {
			case TRIAL_VISIT_SCHEDULE:
				return Settings.getString(MyVisitScheduleExcelSettingCodes.TRIAL_VISIT_SCHEDULE_TEMPLATE_FILE_NAME, Bundle.VISIT_SCHEDULE_EXCEL,
						MyVisitScheduleExcelDefaultSettings.TRIAL_VISIT_SCHEDULE_TEMPLATE_FILE_NAME);
			case PROBAND_VISIT_SCHEDULE:
				return Settings.getString(MyVisitScheduleExcelSettingCodes.PROBAND_VISIT_SCHEDULE_TEMPLATE_FILE_NAME, Bundle.VISIT_SCHEDULE_EXCEL,
						MyVisitScheduleExcelDefaultSettings.PROBAND_VISIT_SCHEDULE_TEMPLATE_FILE_NAME);
			case PROBAND_TRIAL_VISIT_SCHEDULE:
				return Settings.getString(MyVisitScheduleExcelSettingCodes.PROBAND_TRIAL_VISIT_SCHEDULE_TEMPLATE_FILE_NAME, Bundle.VISIT_SCHEDULE_EXCEL,
						MyVisitScheduleExcelDefaultSettings.PROBAND_TRIAL_VISIT_SCHEDULE_TEMPLATE_FILE_NAME);
			case TRAVEL_EXPENSES_VISIT_SCHEDULE:
				return Settings.getString(MyVisitScheduleExcelSettingCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_TEMPLATE_FILE_NAME, Bundle.VISIT_SCHEDULE_EXCEL,
						MyVisitScheduleExcelDefaultSettings.TRAVEL_EXPENSES_VISIT_SCHEDULE_TEMPLATE_FILE_NAME);
			default:
				return null;
		}
	}

	public TrialOutVO getTrial() {
		return trial;
	}

	public Collection getVOs() {
		return getSpreadSheetWriters().get(0).getVOs();
	}

	@Override
	public boolean save() throws Exception {
		byte[] documentData = buffer.toByteArray();
		excelVO.setMd5(CommonUtil.getHex(MessageDigest.getInstance("MD5").digest(documentData)));
		excelVO.setSize(documentData.length);
		excelVO.setDocumentDatas(documentData);
		return true;
	}

	public void setAddress(ProbandAddressOutVO address) {
		this.address = address;
	}

	public void setDistinctColumnNames(ArrayList<String> distinctColumnNames) {
		getSpreadSheetWriters().get(0).setDistinctColumnNames(distinctColumnNames);
	}

	public void setDistinctFieldRows(
			HashMap<Long, HashMap<String, Object>> distinctFieldRows) {
		getSpreadSheetWriters().get(0).setDistinctFieldRows(distinctFieldRows);
	}

	public void setProband(ProbandOutVO proband) {
		this.proband = proband;
		setSpreadSheetName(null);
	}

	@Override
	public void setSpreadSheetName(String spreadSheetName) {
		if (CommonUtil.isEmptyString(spreadSheetName)) {
			String templateSpreadSheetName = null;
			switch (style) {
				case TRIAL_VISIT_SCHEDULE:
					templateSpreadSheetName = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.TRIAL_VISIT_SCHEDULE_SPREADSHEET_NAME,
							ExcelUtil.DEFAULT_LABEL);
					break;
				case PROBAND_VISIT_SCHEDULE:
					templateSpreadSheetName = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.PROBAND_VISIT_SCHEDULE_SPREADSHEET_NAME,
							ExcelUtil.DEFAULT_LABEL);
					break;
				case PROBAND_TRIAL_VISIT_SCHEDULE:
					templateSpreadSheetName = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.PROBAND_TRIAL_VISIT_SCHEDULE_SPREADSHEET_NAME,
							ExcelUtil.DEFAULT_LABEL);
					break;
				case TRAVEL_EXPENSES_VISIT_SCHEDULE:
					templateSpreadSheetName = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_SPREADSHEET_NAME,
							ExcelUtil.DEFAULT_LABEL);
					break;
				default:
			}
			if (CommonUtil.isEmptyString(templateSpreadSheetName)) {
				if (trial != null && proband != null) {
					templateSpreadSheetName = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.TRIAL_PROBAND_SPREADSHEET_NAME,
							ExcelUtil.DEFAULT_LABEL, getStyleName(), trial.getId(), trial.getName(), proband.getId(), proband.getName());
				} else if (trial != null) {
					templateSpreadSheetName = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.TRIAL_SPREADSHEET_NAME, ExcelUtil.DEFAULT_LABEL,
							getStyleName(), trial.getId(), trial.getName());
				} else if (proband != null) {
					templateSpreadSheetName = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.PROBAND_SPREADSHEET_NAME, ExcelUtil.DEFAULT_LABEL,
							getStyleName(), proband.getId(), proband.getName());
				} else {
					templateSpreadSheetName = L10nUtil.getVisitScheduleExcelLabel(Locales.USER, MyVisitScheduleExcelLabelCodes.SPREADSHEET_NAME, ExcelUtil.DEFAULT_LABEL,
							getStyleName());
				}
			}
			getSpreadSheetWriters().get(0).setSpreadSheetName(templateSpreadSheetName);
		} else {
			getSpreadSheetWriters().get(0).setSpreadSheetName(spreadSheetName);
		}
	}

	public void setTrial(TrialOutVO trial) {
		this.trial = trial;
		setSpreadSheetName(null);
	}

	public void setVOs(Collection VOs) {
		getSpreadSheetWriters().get(0).setVOs(VOs);
	}

	@Override
	protected void updateExcelVO() {
		excelVO.setContentTimestamp(now);
		excelVO.setContentType(CoreUtil.getExcelMimeType());
		excelVO.setTrial(trial);
		excelVO.setProband(proband);
		excelVO.setRowCount(getVOs().size());
		StringBuilder fileName = new StringBuilder(style.toString());
		fileName.append("_");
		if (trial != null) {
			fileName.append(VISIT_SCHEDULE_EXCEL_FILENAME_TRIAL);
			fileName.append(trial.getId());
			fileName.append("_");
		}
		if (proband != null) {
			fileName.append(VISIT_SCHEDULE_EXCEL_FILENAME_PROBAND);
			fileName.append(proband.getId());
			fileName.append("_");
		}
		fileName.append(CommonUtil.formatDate(now, CommonUtil.DIGITS_ONLY_DATETIME_PATTERN));
		fileName.append(".");
		fileName.append(CoreUtil.EXCEL_FILENAME_EXTENSION);
		excelVO.setFileName(fileName.toString());
	}

	@Override
	public Color voToColor(Object vo) {
		if (vo instanceof VisitScheduleItemOutVO) {
			VisitScheduleItemOutVO visitScheduleItem = (VisitScheduleItemOutVO) vo;
			Object distinctVo = getDistinctFieldRows().get(visitScheduleItem.getId()).get(RECENT_PROBAND_LIST_STATUS_ENTRY);
			if (distinctVo instanceof ProbandListStatusEntryOutVO) {
				ProbandListStatusEntryOutVO statusEntry = (ProbandListStatusEntryOutVO) distinctVo;
				if (statusEntry.getStatus().isCount()) {
					return Settings.getColor(MyVisitScheduleExcelSettingCodes.ENROLLMENT_STATUS_IS_COUNT_COLOR, Bundle.VISIT_SCHEDULE_EXCEL,
							MyVisitScheduleExcelDefaultSettings.ENROLLMENT_STATUS_IS_COUNT_COLOR);
				} else {
					return Settings.getColor(MyVisitScheduleExcelSettingCodes.ENROLLMENT_STATUS_IS_NOT_COUNT_COLOR, Bundle.VISIT_SCHEDULE_EXCEL,
							MyVisitScheduleExcelDefaultSettings.ENROLLMENT_STATUS_IS_NOT_COUNT_COLOR);
				}
			}
		}
		return null;
	}
}
