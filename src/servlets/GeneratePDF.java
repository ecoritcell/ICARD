package servlets;

import java.awt.Color;
import java.awt.TextArea;
import java.awt.desktop.AboutHandler;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.function.FailableLongToDoubleFunction;

import javax.lang.model.element.Element;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.PdfCanvasConstants;
import com.itextpdf.kernel.pdf.canvas.parser.listener.TextChunk;
import com.itextpdf.kernel.pdf.filters.IFilterHandler;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.Border.Side;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.BorderCollapsePropertyValue;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.Leading;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

import communication.DBConnect;
import communication.Family;

import com.google.zxing.StringsResourceTranslator;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.counter.ContextManager;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.renderer.IRenderer;
/**
 * Servlet implementation class GeneratePDF
 */
@WebServlet("/GeneratePDF")
public class GeneratePDF extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final boolean isLive = false;
	private String photoSignFilePath = "";
	private String qrCodeFilePath = "";	
	private String baseURL = "";
	private String appid = "";
	
	private String errorMsgString = "Unable to generate ICRD please contact administrator.";
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private DateFormat tf = new SimpleDateFormat("dd-MM-YY");
	private DateFormat tfdob = new SimpleDateFormat("dd-MM-YYYY");
	private DateFormat validUptoDateFormat = new SimpleDateFormat("MMM-yyyy");

	//Different color
	com.itextpdf.kernel.colors.Color firstRowColor = new DeviceRgb(72, 209, 204);
	com.itextpdf.kernel.colors.Color secondRowColor = new DeviceRgb(3,63,99);
	com.itextpdf.kernel.colors.Color yellowColor = new DeviceRgb(255, 170, 51);
	com.itextpdf.kernel.colors.Color redColor = new DeviceRgb(255, 0, 0);
	com.itextpdf.kernel.colors.Color blueColor = new DeviceRgb(0, 0, 209);


	
	//Different font size
	private float firstPageHeadingFontSize = 9f;
	private float firstPageFontsize = 6f;
	private float firstPageFontsize7 = 7f;
	private float secondPageHeadingFontSize = 10f;
	private float secondPageFontsize = 7f;
	private float signImgHeight = 17f;
	private float hindiTitleFontSize = 15f;

	
	//Different font
	PdfFont hinNormalFont;
	PdfFont hinBoldFont;
	PdfFont hinBoldFont4;
	PdfFont hinBoldFont5;
	PdfFont hinBoldFont6;
	PdfFont hindiDevnewFont;
	PdfFont boldFont;
	PdfFont boldFontTime;
	PdfFont normalFont;
	PdfFont normalFontTimes;
	

	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GeneratePDF() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		System.out.println("Servlet Called----------------------------");
		String isGAZ = request.getParameter("isGAZ");
		appid = request.getParameter("appid");

//		String contextpath = request.getServletContext().getContextPath();
//		String realpath = request.getServletContext().getRealPath("/");
//		String realpath2 = request.getServletContext().getRealPath("");
//		String realpath1 = request.getServletContext().getRealPath("/index.jsp");
//		Set<String> resourcePaths = request.getServletContext().getResourcePaths("/");
		
		

		
		String url = request.getRequestURL().toString();
		System.out.println("url = "+url);				
		String rootURL = url.substring(0, url.length() - request.getRequestURI().length()) ;
		System.out.println("rootURL = "+rootURL);	
		baseURL = rootURL + request.getContextPath();
		System.out.println("baseURL = "+baseURL);	
		
		
		String hindiNormalFontPath = baseURL + "/fonts/KRDEV010.TTF";
		String hindiBoldFontPath = baseURL + "/fonts/Kruti-Dev-031.ttf";
		String hindiBoldFontPath4 = baseURL + "/fonts/KRDEV040-BOLD.ttf";
		String hindiBoldFontPath5 = baseURL + "/fonts/KRDEV050-BOLD.TTF";
		String hindiBoldFontPath6 = baseURL + "/fonts/KRDEV060-BOLD.TTF";

		
		PdfFontFactory.register(hindiNormalFontPath);
		PdfFontFactory.register(hindiBoldFontPath);
		PdfFontFactory.register(hindiBoldFontPath4);
		PdfFontFactory.register(hindiBoldFontPath5);
		PdfFontFactory.register(hindiBoldFontPath6);


		hinNormalFont = PdfFontFactory.createFont(hindiNormalFontPath, true);
		hinBoldFont = PdfFontFactory.createFont(hindiBoldFontPath, true);
		hinBoldFont4 = PdfFontFactory.createFont(hindiBoldFontPath4, true);
		hinBoldFont5 = PdfFontFactory.createFont(hindiBoldFontPath5, true);
		hinBoldFont6 = PdfFontFactory.createFont(hindiBoldFontPath6, true);

		boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
		boldFontTime = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
		normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
		normalFontTimes = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
		
		if(isLive) {
			
			photoSignFilePath = rootURL + "/ICARD_Attachments/";
			qrCodeFilePath = rootURL + "/ICARD_QR_codes/";			
		}
		else {
			//photoSignFilePath = "E:\\ICARD_Attachments\\";
			//qrCodeFilePath = "E:\\ICARD_QR_codes\\";
			photoSignFilePath = "D:\\Tomcat\\webapps\\ICARD_Attachments\\";
			qrCodeFilePath = "D:\\Tomcat\\webapps\\ICARD_QR_codes\\";
		}
		
		String pdfName = "RlyIcard_"+appid+".pdf";
		ServletOutputStream os = response.getOutputStream();
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + pdfName + "\"");

		try {
			
			PdfWriter writer = new PdfWriter(os);
			// Initialize PDF document
			PdfDocument pdf = new PdfDocument(writer);
			// As the measurement unit in PDF is the user unit, and as 1 inch corresponds
			// with 72 user units,
			PageSize pageSize = new PageSize(243, 153);
			pdf.setDefaultPageSize(pageSize);

			// Initialize document
			Document document = new Document(pdf);
			document.setMargins(0, 0, 0, 0);
			document.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 1.0f));

			if (isGAZ.equals("Y")) {
				designGazIcard(document, pdf, request);
			} else if (isGAZ.equals("N")){
				designNonGazIcard(document, pdf, request);
			}
			else {
				
				Paragraph errorParagraph = new Paragraph("Undefined employee type");
				document.add(errorParagraph);
			}

			document.close();
			System.out.println("PDF Generated successfully");
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception occured while creating PDF");
			System.out.println(e);
		}
		

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		System.out.println("+++++++++++++++++++++++++++");
	}

	public void designNonGazIcard(Document document, PdfDocument pdfDocument, HttpServletRequest request) {

		

		ResultSet rs = null;
		Statement st = null;
		ResultSet rsFamily = null;
		Statement stFamily = null;

		try {

			Connection con = DBConnect.getConnection();
			st = con.createStatement();
			String query = "SELECT ID_NO,EMPNO,EMPNAME,DESIGNATION,DOB,DEPARTMENT,DEPT_NAME,STATION,RES_ADDRESS,DEPT_SLNO,"
					+ "EMERGENCY_CONTACT_NO,PHOTO,SIGNATURE,QR_CODE "
					+ "from non_gaz_master ngm,department d where d.dept_code=ngm.department and id_no='" + appid
					+ "' order by id_no";
			rs = st.executeQuery(query);
			if (rs.next()) {

				String idNoString = rs.getString("ID_NO");
				String empNoString = rs.getString("EMPNO");
				String empNameString = rs.getString("EMPNAME");
				if(empNameString != null)
					empNameString = empNameString.toUpperCase();
				
				String empDesigString = rs.getString("DESIGNATION");				
				if(empDesigString != null)
					empDesigString = empDesigString.toUpperCase();
				
				String empDobString = rs.getString("DOB");
				empDobString = tfdob.format(df.parse(empDobString));
				
				String empDepartmentCodeString = rs.getString("DEPARTMENT");
				String empDepartmentNameString = rs.getString("DEPT_NAME");
				if(empDepartmentNameString != null)
					empDepartmentNameString = empDepartmentNameString.toUpperCase();
	
				String deptSlno = rs.getString("DEPT_SLNO");
				deptSlno = getNonGazDeptSlno(deptSlno);
				
				
				String empStationtString = rs.getString("STATION");
				if(empStationtString != null)
					empStationtString = empStationtString.toUpperCase();
				
				
				String empResAddress = rs.getString("RES_ADDRESS");
				//empResAddress = capitalizeWords(empResAddress);
				
				String empEmgContactNo = rs.getString("EMERGENCY_CONTACT_NO");
				String empPhotoFileName = rs.getString("PHOTO");
				String empSignFileName = rs.getString("SIGNATURE");
				String empQrcodeFileName = rs.getString("QR_CODE");

				
				

				// पूर्व तट रेलवे
				Paragraph title = new Paragraph();
				title.add("iwoZ rV jsyos");
				title.setFont(hinBoldFont4);
				//title.setFontSize(25);
				title.setFontSize(22);
				title.setMargin(0);
				title.setPaddingTop(7);
				//title.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 0.8f));
				title.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 0.4f));
				title.setVerticalAlignment(VerticalAlignment.BOTTOM);


				
				
				//East Coast Railway
				Paragraph ecor = new Paragraph();
				ecor.add("East Coast Railway");
				ecor.setFont(boldFont);
				ecor.setFontSize(15);
				ecor.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 0.9f));


				String imageFilePath = baseURL + "/images/irlogo.png";
				Image img = null;
				ImageData data = null;
				try {
					
					data = ImageDataFactory.create(imageFilePath);
					if(data !=null) {
						img = new Image(data);
						img.setWidth(40f);
						img.setHeight(40f);
						img.setHorizontalAlignment(HorizontalAlignment.CENTER);
						img.setPadding(0);
					}
					else {
						System.out.println("Logo image did't found = "+imageFilePath);
					}
					


				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e);
				}

				if(isLive)
					empPhotoFileName = encodeURLPathComponent(empPhotoFileName);
				String employeePhotoFilePath = photoSignFilePath + empPhotoFileName;
				System.out.println("employeePhotoFilePath =" + employeePhotoFilePath);

				ImageData empImgData = null;
				Image empImg = null;
				try {
										
					empImgData = ImageDataFactory.create(employeePhotoFilePath);
					if(empImgData != null) {
						
						empImg = new Image(empImgData);
						empImg.setBorder(new SolidBorder(1f));
						//empImg.setWidth(35f);
						//empImg.setHeight(42f);
						empImg.setWidth(40f);
						empImg.setHeight(49f);
						empImg.setHorizontalAlignment(HorizontalAlignment.CENTER);
					}
					else {
						System.out.println("Employee image did't found = "+employeePhotoFilePath);
					}
					
					
					
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e);
				}

				if(isLive)
					empSignFileName = encodeURLPathComponent(empSignFileName);
				String employeeSignFilePath = photoSignFilePath + empSignFileName;
				System.out.println("employeeSignFilePath =" + employeeSignFilePath);

				ImageData signImgdata = null;
				Image signImg = null;
				try {
										
					signImgdata = ImageDataFactory.create(employeeSignFilePath);
					if(signImgdata != null) {
						signImg = new Image(signImgdata);
						signImg.setMaxWidth(50f);
						signImg.setMaxHeight(signImgHeight);
						signImg.setHorizontalAlignment(HorizontalAlignment.CENTER);
					}else {
						
						System.out.println("Employee sign did't found = "+employeeSignFilePath);
					}

				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e);
				}

				// Officer signature file path
				String officerSignString = baseURL + "/images/officer_sign.png";
				Image officerSignImage = null;
				ImageData officerSignData = null;
				try {
					
					officerSignData = ImageDataFactory.create(officerSignString);
					if(officerSignData != null) {
						officerSignImage = new Image(officerSignData);
						officerSignImage.setMaxWidth(50f);
						officerSignImage.setMaxHeight(signImgHeight);
						officerSignImage.setHorizontalAlignment(HorizontalAlignment.CENTER);
					}else {
						
						System.out.println("Officer sign did't found = "+officerSignString);
					}

				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e);
				}

				//Qrcode Image
				// String employeeSignFilePath = baseURL + empSignFileName;
				if(isLive)
					empQrcodeFileName = encodeURLPathComponent(empQrcodeFileName);
				String qrcodeFilePath = qrCodeFilePath + empQrcodeFileName;
				System.out.println("qrcodeFilePath =" + qrcodeFilePath);

				ImageData qrcodeImgdata = null;
				Image qrcodeImg = null;
				try {
					
					qrcodeImgdata = ImageDataFactory.create(qrcodeFilePath);
					if(qrcodeImgdata != null) {
						qrcodeImg = new Image(qrcodeImgdata);
						//qrcodeImg.setWidth(50f);
						//qrcodeImg.setHeight(50f);
						qrcodeImg.setWidth(65f);
						qrcodeImg.setHeight(65f);
						//qrcodeImg.setPaddingTop(-10);
						qrcodeImg.setPaddings(0, 0, 0, 0);
						qrcodeImg.setMargins(0, 0, 0, 0);
						qrcodeImg.setHorizontalAlignment(HorizontalAlignment.CENTER);
					}else {
						System.out.println("qrcodeFilePath  did't found = " + qrcodeFilePath);
					}

				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e);
				}
				
				
				
				float columnWidth[] = { 20, 60, 20 };
				Table headTable = new Table(UnitValue.createPercentArray(columnWidth)).useAllAvailableWidth()
						.setBorder(Border.NO_BORDER).setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE)
						.setMargins(4, 0, 1, 0).setPadding(0);
				if (img != null)
					//headTable.addCell(new Cell(2, 1).add(img).setVerticalAlignment(VerticalAlignment.TOP).setMargin(0).setPaddings(2, 0, 2, 2));
					headTable.addCell(new Cell(2, 1).add(img).setVerticalAlignment(VerticalAlignment.TOP).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER));
				else
					headTable.addCell(new Cell(2, 1).add(new Paragraph("")).setVerticalAlignment(VerticalAlignment.TOP).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER));

				headTable.addCell(new Cell().add(title).setMargin(0).setPadding(0)
						.setVerticalAlignment(VerticalAlignment.BOTTOM).setTextAlignment(TextAlignment.CENTER).setPadding(0).setMargin(0).setBorder(Border.NO_BORDER));
				headTable.addCell(new Cell(2, 1).add(new Paragraph("")).setMargin(0).setPadding(0)
						.setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));

				headTable.addCell(new Cell().add(ecor).setMargin(0).setPadding(0).setVerticalAlignment(VerticalAlignment.TOP)
						.setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));

				// Department Hindi				
				Paragraph bivagParagraph = new Paragraph("foHkkx");
				bivagParagraph.setFont(hinBoldFont4);
				bivagParagraph.setFontSize(8);
				bivagParagraph.setPaddingTop(2);
				bivagParagraph.setMargin(0);
				bivagParagraph.setMultipliedLeading(0.7f);


				Text dept = new Text("DEPARTMENT");
				dept.setFont(boldFont);
				dept.setFontSize(firstPageFontsize);
				
				
				// Org Department Hindi				
				Paragraph orgDeptHindi = new Paragraph(getDepartmentWiseHindiText(empDepartmentCodeString));
				orgDeptHindi.setFont(hinBoldFont4);
				orgDeptHindi.setFontSize(8);
				orgDeptHindi.setPaddingTop(2);
				orgDeptHindi.setMargin(0);
				orgDeptHindi.setMultipliedLeading(0.7f);


				Text accts = new Text(empDepartmentNameString);
				accts.setFont(boldFont);
				accts.setFontSize(firstPageFontsize);

				com.itextpdf.kernel.colors.Color whiteColor = new DeviceRgb(255, 255, 255);
				
				
				// Org Department Hindi				
				Paragraph pechanpatra = new Paragraph("igpku i=");
				pechanpatra.setFont(hinBoldFont4);
				pechanpatra.setFontSize(8);
				pechanpatra.setPaddingTop(2);
				pechanpatra.setMargin(0);
				pechanpatra.setMultipliedLeading(0.7f);
				pechanpatra.setFontColor(whiteColor);

				Text icardtext = new Text("IDENTITY CARD");
				icardtext.setFont(boldFont);
				icardtext.setFontSize(firstPageFontsize);
				icardtext.setFontColor(whiteColor);
				
				
				Paragraph hqhindi = new Paragraph("ç-dk");
				hqhindi.setFont(hinBoldFont4);
				hqhindi.setFontSize(8);
				hqhindi.setPaddingTop(2);
				hqhindi.setMargin(0);
				hqhindi.setMultipliedLeading(0.7f);
				hqhindi.setFontColor(whiteColor);

				String idcardNoString = "H.Q. SI.No. " + empDepartmentNameString + "-" + deptSlno;
				Text slno = new Text(idcardNoString);
				slno.setFont(boldFont);
				slno.setFontSize(firstPageFontsize);
				slno.setFontColor(whiteColor);

				float deptTableColumnWidth[] = { 20, 20, 15, 45 };
				Table deptTable = new Table(UnitValue.createPercentArray(deptTableColumnWidth)).useAllAvailableWidth()
						.setBorder(Border.NO_BORDER).setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE)
						.setMargin(0).setPadding(0);

				deptTable.addCell(new Cell().add(bivagParagraph).setMargin(0).setPadding(0).setBackgroundColor(firstRowColor).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
				deptTable.addCell(new Cell().add(new Paragraph(dept)).setMargin(0).setPadding(0)
						.setBackgroundColor(firstRowColor).setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.MIDDLE));
				deptTable.addCell(
						new Cell().add(orgDeptHindi).setMargin(0).setPadding(0).setBackgroundColor(firstRowColor)
								.setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.MIDDLE));
				deptTable.addCell(new Cell().add(new Paragraph(accts)).setMargin(0).setPaddings(0, 0, 0, 5)
						.setBackgroundColor(firstRowColor).setBorder(Border.NO_BORDER)
						.setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

				//Second Row
				deptTable.addCell(new Cell().add(pechanpatra).setMargin(0).setPaddings(0, 0, 0, 0)
						.setBackgroundColor(secondRowColor).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));
				deptTable.addCell(new Cell().add(new Paragraph(icardtext)).setMargin(0).setPadding(0)
						.setBackgroundColor(secondRowColor).setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.MIDDLE));
				deptTable.addCell(
						new Cell().add(hqhindi).setMargin(0).setPadding(0).setBackgroundColor(secondRowColor)
								.setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.MIDDLE));
				deptTable.addCell(
						new Cell().add(new Paragraph(slno)).setMargin(0).setPadding(0).setBackgroundColor(secondRowColor)
								.setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

				
				float detailFontSize = 11.0f;//8.0f				
				float gapbetweenLinesFloat = 0.7f;//0.9f
				
				//Name Hindi
				Paragraph namehindi =  new Paragraph("uke");
				namehindi.setFont(hinBoldFont4);
				namehindi.setFontSize(detailFontSize);
				namehindi.setMultipliedLeading(gapbetweenLinesFloat);
				
				
				//Pad Naam Hindi
				Paragraph padnaam =  new Paragraph("in uke");
				padnaam.setFont(hinBoldFont4);
				padnaam.setFontSize(detailFontSize);
				padnaam.setMultipliedLeading(gapbetweenLinesFloat);
				
				//PF NO. Hindi
				Paragraph pfnohindi =  new Paragraph("ih-,Q-ua");
				pfnohindi.setFont(hinBoldFont4);
				pfnohindi.setFontSize(detailFontSize);
				pfnohindi.setMultipliedLeading(gapbetweenLinesFloat);
				
				//Station Hindi
				Paragraph stationhindi =  new Paragraph("LVs'ku");
				stationhindi.setFont(hinBoldFont4);
				stationhindi.setFontSize(detailFontSize);
				stationhindi.setMultipliedLeading(gapbetweenLinesFloat);
				
				//DOB Hindi
				Paragraph dobhindi =  new Paragraph("tUe rkjh[k");
				dobhindi.setFont(hinBoldFont4);
				dobhindi.setFontSize(detailFontSize);
				dobhindi.setMultipliedLeading(gapbetweenLinesFloat);
				

				
				
				
				float detailTableColumnWidth[] = { 20, 20, 12, 48 };
				Table detailTable = new Table(UnitValue.createPercentArray(detailTableColumnWidth))
						.useAllAvailableWidth().setBorder(Border.NO_BORDER)
						.setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE).setMarginTop(2).setPadding(0)
						.setFont(normalFont).setFontSize(firstPageFontsize);

			
				
				if (empImg != null)
					detailTable.addCell(new Cell(5, 1).add(empImg).setMargin(0).setPadding(0).setVerticalAlignment(VerticalAlignment.TOP)
							.setBorder(Border.NO_BORDER));
				else
					detailTable.addCell(new Cell(5, 1).add(new Paragraph("")).setMargin(0).setPadding(0)
							.setVerticalAlignment(VerticalAlignment.TOP).setBorder(Border.NO_BORDER));

				
				detailTable.addCell(new Cell().add(namehindi).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER));
				detailTable.addCell(new Cell().add(new Paragraph("Name")).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER).setFont(normalFont).setFontSize(firstPageFontsize7));
				detailTable.addCell(new Cell().add(new Paragraph(": " + empNameString)).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER).setMaxHeight(8).setFont(boldFont));

				detailTable.addCell(new Cell().add(padnaam).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER));
				detailTable.addCell(new Cell().add(new Paragraph("Desig")).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER).setFont(normalFont).setFontSize(firstPageFontsize7));
				detailTable.addCell(new Cell().add(new Paragraph(": " + empDesigString)).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER).setMaxHeight(8).setFont(boldFont));

				detailTable.addCell(new Cell().add(pfnohindi).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER));
				detailTable.addCell(new Cell().add(new Paragraph("P.F.No.")).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER).setFont(normalFont).setFontSize(firstPageFontsize7));
				detailTable.addCell(new Cell().add(new Paragraph(": " + empNoString)).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER).setFont(boldFont));

				detailTable.addCell(new Cell().add(stationhindi).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER));
				detailTable.addCell(new Cell().add(new Paragraph("Station")).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER).setFont(normalFont).setFontSize(firstPageFontsize7));
				detailTable.addCell(new Cell().add(new Paragraph(": " + empStationtString)).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER).setFont(boldFont));

				detailTable.addCell(new Cell().add(dobhindi).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER));
				detailTable.addCell(new Cell().add(new Paragraph("D.O.B")).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER).setFont(normalFont).setFontSize(firstPageFontsize7));
				detailTable.addCell(new Cell().add(new Paragraph(": " + empDobString)).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER).setFont(boldFont));

				
				
				//Hindi signature of the card holder
				//Paragraph hindiSignOfTheCardHolder =  new Paragraph("deZpkjh ds gLrk{kj ;k vaxwBs dk fu'kku");
				Paragraph hindiSignOfTheCardHolder =  new Paragraph("dkMZ/kkjd dk gLrk{kj");
				hindiSignOfTheCardHolder.setFont(hinBoldFont4);
				hindiSignOfTheCardHolder.setWordSpacing(0.0f);
				//hindiSignOfTheCardHolder.setFontSize(5);
				hindiSignOfTheCardHolder.setFontSize(8);
				//hindiSignOfTheCardHolder.setMultipliedLeading(0.7f);
				hindiSignOfTheCardHolder.setMultipliedLeading(0.5f);
				hindiSignOfTheCardHolder.setPaddings(2, 0, 0, 0);
				
				//Hindi signature of the issuing authority
				//Paragraph hindiSignOfIssuingAuthority =  new Paragraph("tkjh djus okys çkf/kdj.k dk gLrk{kj");
				Paragraph hindiSignOfIssuingAuthority =  new Paragraph("tkjhdrkZ çkf/kdkjh dk gLrk{kj");
				hindiSignOfIssuingAuthority.setFont(hinBoldFont4);
				//hindiSignOfIssuingAuthority.setFontSize(5);
				hindiSignOfIssuingAuthority.setFontSize(8);
				//hindiSignOfIssuingAuthority.setMultipliedLeading(0.7f);
				hindiSignOfIssuingAuthority.setMultipliedLeading(0.5f);
				hindiSignOfIssuingAuthority.setPaddings(2, 0, 0, 0);
				
				//signature of the card holder
				Paragraph signOfTheCardHolder =  new Paragraph("Signature of the Card Holder");
				signOfTheCardHolder.setFont(normalFont);
				signOfTheCardHolder.setFontSize(6);
				signOfTheCardHolder.setMultipliedLeading(0.8f);
				
				//signature of the issuing authority
				Paragraph signOfIssuingAuthority =  new Paragraph("Signature of Issuing Authority");
				signOfIssuingAuthority.setFont(normalFont);
				signOfIssuingAuthority.setFontSize(6);
				signOfIssuingAuthority.setMultipliedLeading(0.8f);
				
				
				
				
				float signTableColumnWidth[] = { 40, 15, 45 };
				Table signTable = new Table(UnitValue.createPercentArray(signTableColumnWidth)).useAllAvailableWidth()
						.setBorder(Border.NO_BORDER).setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE)
						.setMargin(0).setPadding(0).setFont(normalFont).setFontSize(firstPageFontsize);

				if (signImg != null)
					signTable.addCell(
							new Cell().add(signImg).setMargin(0).setPadding(0).setVerticalAlignment(VerticalAlignment.BOTTOM)
									.setBorder(Border.NO_BORDER).setMinHeight(signImgHeight).setMaxHeight(signImgHeight));
				else
					signTable.addCell(new Cell().add(new Paragraph("")).setMargin(0).setPadding(0)
							.setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setMinHeight(signImgHeight)
							.setMaxHeight(signImgHeight));

				signTable.addCell(new Cell().add(new Paragraph("")).setMargin(0).setPadding(0)
						.setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));

				if (officerSignImage != null)
					signTable.addCell(new Cell().add(officerSignImage).setMargin(0).setPadding(0)
							.setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setMinHeight(signImgHeight)
							.setMaxHeight(signImgHeight));
				else
					signTable.addCell(new Cell().add(new Paragraph("")).setMargin(0).setPadding(0)
							.setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setMinHeight(signImgHeight)
							.setMaxHeight(signImgHeight));

				signTable.addCell(new Cell().add(hindiSignOfTheCardHolder).setMargin(0).setPadding(0).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
				signTable.addCell(new Cell().add(new Paragraph("")).setMargin(0).setPadding(0).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
				signTable.addCell(new Cell().add(hindiSignOfIssuingAuthority).setMargin(0).setPadding(0).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));

				signTable.addCell(new Cell().add(signOfTheCardHolder).setMargin(0).setPadding(0).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
				signTable.addCell(new Cell().add(new Paragraph("")).setMargin(0).setPadding(0).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
				signTable.addCell(new Cell().add(signOfIssuingAuthority).setMargin(0).setPadding(0).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));

				document.add(headTable);
				document.add(deptTable);
				document.add(detailTable);
				document.add(signTable);

				try {
					
					
					// Second page
					document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

					Text hindiFamilyDetail = new Text("ifjokj dk fooj.k");
					hindiFamilyDetail.setFont(hinBoldFont4);
					hindiFamilyDetail.setFontSize(11.0f);


					Text familyDetail = new Text("/Details of the family");
					familyDetail.setFont(normalFont);
					familyDetail.setFontSize(9.0f);

					Paragraph headingParagraph = new Paragraph();
					headingParagraph.add(hindiFamilyDetail);
					headingParagraph.add(familyDetail);
					headingParagraph.setMultipliedLeading(0.5f);

					float familyTableColumnWidth[] = { 30, 11, 12, 9, 38 };

					Table familyTable = new Table(UnitValue.createPercentArray(familyTableColumnWidth))
							.useAllAvailableWidth().setBorder(Border.NO_BORDER)
							.setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE).setMargins(5, 2, 0, 5).setPadding(0)
							.setFont(normalFont).setFontSize(secondPageFontsize).setMaxHeight(100.0f);

					familyTable.addCell(new Cell(1, 5).add(headingParagraph).setMargin(0).setPaddings(3, 0, 3, 0)
							.setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));

					stFamily = con.createStatement();
					String familyQuery = "select FNAME,BLOOD_GROUP,RELATIONSHIP,DOB,ID_MARKS,SLNO from non_gaz_family  where ID_NO='"
							+ appid + "'order by slno";
					rsFamily = stFamily.executeQuery(familyQuery);

					List<Family> familyList = new ArrayList<>();

					while (rsFamily.next()) {

						String nameString = rsFamily.getString("FNAME").toLowerCase();
						nameString = capitalizeWords(nameString);
						//if(nameString != null)
						//	nameString = nameString.toUpperCase(); 
						String relationString = rsFamily.getString("RELATIONSHIP").toLowerCase();
						relationString = capitalizeWords(relationString);
						String dobString = rsFamily.getString("DOB");
						String bgString = rsFamily.getString("BLOOD_GROUP").toLowerCase();
						bgString = capitalizeWords(bgString);
						String idmarkString = rsFamily.getString("ID_MARKS").toLowerCase();
						idmarkString = capitalize(idmarkString);
						dobString = tf.format(df.parse(dobString));
						
						
						Family fam = new Family();
						fam.name = nameString;
						fam.relation = relationString;
						fam.dob = dobString;
						fam.bg = bgString;
						fam.idmark = idmarkString;
						familyList.add(fam);


					}
					
					
					float idmarkFontSize = getFontSizeForIdmark(pdfDocument, document, familyList);					
					float nameFontSize = getFontSizeForName(pdfDocument, document, familyList);
					float otherFontSize = 6.0f;
//					if(idmarkFontSize<=5.0f)
//						otherFontSize = 5.0f;
					
					for(Family fam : familyList) {
						
						familyTable.addCell(new Cell().add(new Paragraph(fam.name)).setMargin(0).setPadding(0)
								.setTextAlignment(TextAlignment.LEFT).setFontSize(nameFontSize).setVerticalAlignment(VerticalAlignment.MIDDLE).setMaxHeight(16.0f).setBorder(Border.NO_BORDER));
						familyTable.addCell(new Cell().add(new Paragraph(fam.relation)).setMargin(0).setPadding(0)
								.setTextAlignment(TextAlignment.LEFT).setFontSize(otherFontSize).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
						familyTable.addCell(new Cell().add(new Paragraph(fam.dob)).setMargin(0).setPadding(0)
								.setTextAlignment(TextAlignment.LEFT).setFontSize(otherFontSize).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
						familyTable.addCell(new Cell().add(new Paragraph(fam.bg)).setMargin(0).setPadding(0)
								.setTextAlignment(TextAlignment.LEFT).setFontSize(otherFontSize).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
						//familyTable.addCell(new Cell().add(new Paragraph(idmarkString)).setMargin(0).setPadding(0)
							//	.setTextAlignment(TextAlignment.LEFT).setFontSize(secondPageFontsize).setVerticalAlignment(VerticalAlignment.MIDDLE).setMaxHeight(16.0f));
						
						
												
						Paragraph idmarkParagraph =  new Paragraph(fam.idmark);
						idmarkParagraph.setMargin(0);
						idmarkParagraph.setPadding(0);
						idmarkParagraph.setFont(normalFont);
						idmarkParagraph.setFontSize(idmarkFontSize);		

						familyTable.addCell(new Cell().add(idmarkParagraph).setMargin(0).setPadding(0)
									.setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE).setMaxHeight(16.0f).setBorder(Border.NO_BORDER));

					}
					
					
					
//					int i =0;
//					while (i<6) {
//
//						String nameString = "Jhsjkdfh dfsdklf skldfj skdfhhd skjy ksldhfiudsf ds";
//						String relationString =  "Daughter";
//						String dobString = "10-06-1986";
//						String bgString = "AB+ve";
//						String idmarkString = "kjahdsf aklsjdfh au lka shdfaksdf askjldfh kadfh askdfhasdfiu kj asjkdfhaisudf ";
//
//						familyTable.addCell(new Cell().add(new Paragraph(nameString)).setMargin(0).setPadding(0)
//								.setTextAlignment(TextAlignment.LEFT).setFontSize(secondPageFontsize).setVerticalAlignment(VerticalAlignment.MIDDLE).setMaxHeight(16.0f));
//						familyTable.addCell(new Cell().add(new Paragraph(relationString)).setMargin(0).setPadding(0)
//								.setTextAlignment(TextAlignment.LEFT).setFontSize(6.0f).setVerticalAlignment(VerticalAlignment.MIDDLE));
//						familyTable.addCell(new Cell().add(new Paragraph(dobString)).setMargin(0).setPadding(0)
//								.setTextAlignment(TextAlignment.LEFT).setFontSize(6.0f).setVerticalAlignment(VerticalAlignment.MIDDLE));
//						familyTable.addCell(new Cell().add(new Paragraph(bgString)).setMargin(0).setPadding(0)
//								.setTextAlignment(TextAlignment.LEFT).setFontSize(6.0f).setVerticalAlignment(VerticalAlignment.MIDDLE));
//						familyTable.addCell(new Cell().add(new Paragraph(idmarkString)).setMargin(0).setPadding(0)
//								.setTextAlignment(TextAlignment.LEFT).setFontSize(secondPageFontsize).setVerticalAlignment(VerticalAlignment.MIDDLE).setMaxHeight(16.0f));
//
//						 i++;
//					}

					document.add(familyTable);

					// Bottom address part

					Paragraph hindiIfFound = new Paragraph(";fn ;g dkMZ feys rks —i;k fudVre iksLV c‚Dl esa Mky nsaA");
					//hindiIfFound.setFont(hinBoldFont4);
					hindiIfFound.setFont(hinBoldFont);
					hindiIfFound.setFontSize(8.0f);
					hindiIfFound.setPaddings(2, 0, 0, 0);
					hindiIfFound.setMultipliedLeading(0.6f);
					

					Paragraph ifFound = new Paragraph("If found please drop it in the nearest Post Box");
					ifFound.setFont(normalFont);
					ifFound.setFontSize(7.0f);

					Text hindiAddress = new Text("?kj dk irk");
					hindiAddress.setFont(hinBoldFont4);
					hindiAddress.setFontSize(8.0f);

					Text engAddress = new Text("/Res.Address: ");
					engAddress.setFont(normalFont);
					engAddress.setFontSize(7.0f);

					Text orgAddress = new Text(empResAddress);
					orgAddress.setFont(normalFont);
					orgAddress.setFontSize(7.0f);

					Paragraph addParagraph = new Paragraph();
					addParagraph.add(hindiAddress);
					addParagraph.add(engAddress);
					addParagraph.add(orgAddress);
					addParagraph.setMargin(0);
					addParagraph.setPadding(0);
					addParagraph.setMultipliedLeading(0.7f);

					
					
					
					//signature of the card holder
					String emergencyContactNo = "Emergency Contact No. : " + empEmgContactNo;
					Paragraph emergencyContactNoPara =  new Paragraph(emergencyContactNo);
					emergencyContactNoPara.setFont(boldFont);
					emergencyContactNoPara.setFontSize(7.0f);
					//emergencyContactNoPara.setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.FILL_STROKE);
					//emergencyContactNoPara.setStrokeWidth(0.05f);
					//emergencyContactNoPara.setfo;
					
					

					float addressTableColumnWidth[] = { 78, 22 };
					Table addressTable = new Table(UnitValue.createPercentArray(addressTableColumnWidth)).useAllAvailableWidth().setBorder(Border.NO_BORDER)
							.setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE).setMargins(0, 1, 1, 5).setPadding(0)
							.setFont(normalFont).setFontSize(secondPageFontsize)
							.setVerticalAlignment(VerticalAlignment.BOTTOM);

					addressTable.addCell(new Cell().add(emergencyContactNoPara).setPaddings(0, 0, 5, 0).setMargin(0).setBorder(Border.NO_BORDER));
					//addressTable.addCell(new Cell(4,1).add(qrcodeImg).setPadding(0).setMargin(0).setVerticalAlignment(VerticalAlignment.TOP).setPaddings(-7, 0, 0, 0));
					//addressTable.addCell(new Cell(4,1).add(qrcodeImg).setPadding(0).setMargin(0).setPaddings(-9, -9, -9, -9).setBorder(Border.NO_BORDER));

					if(qrcodeImg != null)
						addressTable.addCell(new Cell(4,1).add(qrcodeImg).setPadding(0).setMargin(0).setPaddings(-9,0,-9,-9).setBorder(Border.NO_BORDER));
					else 
						addressTable.addCell(new Cell(4,1).add(new Paragraph("")).setPadding(0).setMargin(0).setVerticalAlignment(VerticalAlignment.TOP).setPaddings(-7, 0, 0, 0).setBorder(Border.NO_BORDER));
					
					addressTable.addCell(new Cell().add(addParagraph).setPaddings(0, 0, 5, 0).setMargin(0).setMaxHeight(18.0f).setBorder(Border.NO_BORDER));										
					
					addressTable.addCell(new Cell().add(hindiIfFound).setPaddings(0, 0, 2, 0).setMargin(0).setBorder(Border.NO_BORDER));
					addressTable.addCell(new Cell().add(ifFound).setPadding(0).setMargin(0).setBorder(Border.NO_BORDER));

					// How to calculate table height
					PageSize pdfSize = pdfDocument.getDefaultPageSize();
					IRenderer tableRenderer = familyTable.createRendererSubTree().setParent(document.getRenderer());
					LayoutResult tableLayoutResult = tableRenderer.layout(new LayoutContext(new LayoutArea(0, new Rectangle(pdfSize.getWidth(), 1000))));
					float familyTableHeight = tableLayoutResult.getOccupiedArea().getBBox().getHeight();

					System.out.println("familyTableHeight :" + familyTableHeight);
					
					tableRenderer = addressTable.createRendererSubTree().setParent(document.getRenderer());
					tableLayoutResult = tableRenderer.layout(new LayoutContext(new LayoutArea(0, new Rectangle(pdfSize.getWidth(), 1000))));
					float addressTableHeight = tableLayoutResult.getOccupiedArea().getBBox().getHeight();
					
					System.out.println("addressTableHeight :" + addressTableHeight);
					
					float gapBaetweenTables = 0f;
					if(familyTableHeight <= 70)
						gapBaetweenTables = 20.0f;
					else if(familyTableHeight <= 85)
						gapBaetweenTables = 10.0f;
					else
						gapBaetweenTables = 0.0f;
					
					
					//LEFT,BOTTOM,WIDTH
					addressTable.setFixedPosition(5, pdfSize.getHeight()-familyTableHeight - addressTableHeight -gapBaetweenTables , pdfSize.getWidth() - 5);
					//addressTable.setFixedPosition(5, 5, pdfSize.getWidth() - 5);

					document.add(addressTable);
					
					
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("Unable get family details.");
					System.out.println(e.toString());

				}
			} else {

				Paragraph errorParagraph = new Paragraph(errorMsgString);
				document.add(errorParagraph);

			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			Paragraph errorParagraph = new Paragraph(errorMsgString);
			document.add(errorParagraph);

		}
	}

	public void designGazIcard(Document document, PdfDocument pdfDocument, HttpServletRequest request) {


		ResultSet rs = null;
		Statement st = null;
		ResultSet rsFamily = null;
		Statement stFamily = null;

		try {

			Connection con = DBConnect.getConnection();
			st = con.createStatement();			
			String query="select ID_NO,RUID_NO,EMPNAME,DESIGNATION,DOB,STATION,DEPT_SLNO,NO_STARS,VALIDITY_UPTO,RES_ADDRESS,"
					+ "RLY_NUMBER,MOBILE_NUMBER,EMERGENCY_CONTACT_NAME,EMERGENCY_CONTACT_NO,PHOTO,SIGNATURE,HINDI_NAME,"
					+ "HINDI_DESIG,QR_CODE from gaz_master ngm,department d where d.dept_code=ngm.department  and id_no='"+appid+"' order by id_no";

			rs = st.executeQuery(query);
			if (rs.next()) {

				String idNoString = rs.getString("ID_NO");
				String ruidString = rs.getString("RUID_NO");
				String empNameString = rs.getString("EMPNAME");
				if(empNameString != null)
					empNameString = empNameString.toUpperCase();				
				
				
				String empDesigString = rs.getString("DESIGNATION");
				if(empDesigString != null)
					empDesigString = empDesigString.toUpperCase();	
				
				String deptSlno = rs.getString("DEPT_SLNO");
				if(deptSlno == null)
					deptSlno = "";
				System.out.println("deptSlno = "+deptSlno);
				String noOfStars = rs.getString("NO_STARS");
				if(noOfStars == null)
					noOfStars = "";
				else 
					noOfStars = noOfStars.trim();
				
				System.out.println("noOfStars = "+noOfStars);
				String validityUpto = rs.getString("VALIDITY_UPTO");
				if(validityUpto != null)				
					validityUpto = validUptoDateFormat.format(df.parse(validityUpto));
				else
					validityUpto = "";
				validityUpto = validityUpto.toUpperCase();
				System.out.println("validityUpto = "+validityUpto);
				String empResAddress = rs.getString("RES_ADDRESS");
				//empResAddress = capitalizeWords(empResAddress);
				System.out.println("empResAddress = "+empResAddress);
				String empEmgContactNo = rs.getString("EMERGENCY_CONTACT_NO");
				String empPhotoFileName = rs.getString("PHOTO");
				String empSignFileName = rs.getString("SIGNATURE");		
				String empQrcodeFileName = rs.getString("QR_CODE");
				String empHindiName = rs.getString("HINDI_NAME");
				String empHindiDesig = rs.getString("HINDI_DESIG");
				String empDob = rs.getString("DOB");
				empDob = tfdob.format(df.parse(empDob));
				String empStation = rs.getString("STATION");

				

				
				//भारत सरकार				
				Paragraph bharatSarkar = new Paragraph();
				bharatSarkar.add("Hkkjr ljdkj");
				bharatSarkar.setFont(hinBoldFont4);				
				bharatSarkar.setFontSize(hindiTitleFontSize);
				//bharatSarkar.setPaddingTop(4);
				//bharatSarkar.setPaddings(4, 0, 0, 0);
				bharatSarkar.setPadding(0);
				bharatSarkar.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 0.4f));
				bharatSarkar.setVerticalAlignment(VerticalAlignment.BOTTOM);

				
				
				//रेल मंत्रालय				
				Paragraph railMantralay = new Paragraph();
				railMantralay.add("jsy ea=ky;");
				railMantralay.setFont(hinBoldFont4);
				railMantralay.setFontSize(hindiTitleFontSize);
				//railMantralay.setPaddingTop(4);
				railMantralay.setPaddings(4, 0, 0, 0);
				railMantralay.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 0.4f));
				railMantralay.setVerticalAlignment(VerticalAlignment.BOTTOM);
				
				//Government of Indian		
				Paragraph govIndia = new Paragraph();
				govIndia.add("Government of India");
				govIndia.setFont(boldFont);
				govIndia.setFontSize(firstPageHeadingFontSize);
				govIndia.setPadding(0);
				govIndia.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 0.7f));

				
				//Ministry of Railways		
				Paragraph minRailway = new Paragraph();
				minRailway.add("Ministry of Railways");
				minRailway.setFont(boldFont);
				minRailway.setFontSize(firstPageHeadingFontSize);
				minRailway.setPadding(0);
				minRailway.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 0.7f));
				
				
				//पहचान पत्र
				Text pechanpatra = new Text("igpku i=");
				pechanpatra.setFont(hinBoldFont4);
				pechanpatra.setFontSize(hindiTitleFontSize);
				pechanpatra.setFontColor(redColor);
				pechanpatra.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 0.6f));
				
				Paragraph pechanpatraParagraph = new Paragraph();
				pechanpatraParagraph.add(pechanpatra);
				pechanpatraParagraph.setPaddings(3, 0, 0, 0);
				pechanpatraParagraph.setVerticalAlignment(VerticalAlignment.BOTTOM);
				pechanpatraParagraph.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 0.7f));
				
				
				//Identity Card
				Text icardtext = new Text("IDENTITY CARD");
				icardtext.setFont(boldFont);
				icardtext.setFontSize(10);
				icardtext.setFontColor(redColor);
				//icardtext.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 0.7f));
				icardtext.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 0.8f));
				
				
				Paragraph icardtextParagraph = new Paragraph();
				icardtextParagraph.add(icardtext);
				icardtextParagraph.setVerticalAlignment(VerticalAlignment.BOTTOM);
				icardtextParagraph.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 0.7f));
				
				//Identity Card Paragraph	
//				Paragraph icardParagraph = new Paragraph();
//				icardParagraph.add(pechanpatra);
//				icardParagraph.add(icardtext);
//				icardParagraph.setVerticalAlignment(VerticalAlignment.MIDDLE);
//				icardParagraph.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 0.6f));

				
				
				//पूर्व तट रेलवे, भुबनेश्वर
				Paragraph purbatatrailway = new Paragraph();
				purbatatrailway.add("iwoZ rV jsyos] Hkqcus'oj");
				purbatatrailway.setFont(hinBoldFont4);
				//purbatatrailway.setFontSize(8);
				purbatatrailway.setFontSize(7);
				//purbatatrailway.setPaddingTop(2);
				purbatatrailway.setPaddings(2, 0, 0, 0);
				purbatatrailway.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 0.7f));

				
				//Ministry of Railways		
				Paragraph ecorBbsr = new Paragraph();
				ecorBbsr.add("East Coast Railway, Bhubaneswar");
				ecorBbsr.setFont(boldFont);
				ecorBbsr.setFontSize(firstPageFontsize7);
				ecorBbsr.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 0.7f));

				
				
				String ashokstambaImgFilePath = baseURL + "/images/ashok_stamba.png";
				Image ashokStambaImg = null;
				ImageData ashokStambaData = null;
				try {
					ashokStambaData = ImageDataFactory.create(ashokstambaImgFilePath);
					ashokStambaImg = new Image(ashokStambaData);
					ashokStambaImg.setWidth(18f);
					//ashokStambaImg.setHeight(25f);
					ashokStambaImg.setHeight(30.0f);
					ashokStambaImg.setHorizontalAlignment(HorizontalAlignment.CENTER);

				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e);
				}


				if(isLive)
					empPhotoFileName = encodeURLPathComponent(empPhotoFileName);
				String employeePhotoFilePath = photoSignFilePath + empPhotoFileName;
				System.out.println("employeePhotoFilePath =" + employeePhotoFilePath);
				
				ImageData empImgData = null;
				Image empImg = null;
				try {
					
					empImgData = ImageDataFactory.create(employeePhotoFilePath);
					if(empImgData != null) {
						empImg = new Image(empImgData);
						empImg.setBorder(new SolidBorder(1f));
						empImg.setWidth(40f);
						empImg.setHeight(49f);
						empImg.setHorizontalAlignment(HorizontalAlignment.CENTER);
					}else {
						
						System.out.println("Employee image did't found = " + employeePhotoFilePath);
					}
					
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e);
				}

				if(isLive)
					empSignFileName = encodeURLPathComponent(empSignFileName);
				String employeeSignFilePath = photoSignFilePath + empSignFileName;
				System.out.println("employeeSignFilePath =" + employeeSignFilePath);

				ImageData signImgdata = null;
				Image signImg = null;
				try {
					
					signImgdata = ImageDataFactory.create(employeeSignFilePath);
					if(signImgdata != null) {
						signImg = new Image(signImgdata);
						signImg.setMaxWidth(50f);
						signImg.setMaxHeight(signImgHeight);
						signImg.setHorizontalAlignment(HorizontalAlignment.CENTER);
					}else {
						System.out.println("Employee sign did't found = "+employeeSignFilePath);
					}
						

				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e);
				}

				// Officer signature file path
				String officerSignString = baseURL + "/images/officer_sign.png";
				Image officerSignImage = null;
				ImageData officerSignData = null;
				try {
					officerSignData = ImageDataFactory.create(officerSignString);
					if(officerSignData != null) {
						officerSignImage = new Image(officerSignData);
						officerSignImage.setMaxWidth(50f);
						officerSignImage.setMaxHeight(signImgHeight);
						officerSignImage.setHorizontalAlignment(HorizontalAlignment.CENTER);
					}else {
						
						System.out.println("Officer sign did't found = "+officerSignString);
					}
					

				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e.getMessage());
				}
				
				//Qrcode Image
				if(isLive)
					empQrcodeFileName = encodeURLPathComponent(empQrcodeFileName);
				String qrcodeFilePath = qrCodeFilePath + empQrcodeFileName;
				System.out.println("qrcodeFilePath =" + qrcodeFilePath);

				ImageData qrcodeImgdata = null;
				Image qrcodeImg = null;
				try {
					
					qrcodeImgdata = ImageDataFactory.create(qrcodeFilePath);
					if(qrcodeImgdata != null) {
						qrcodeImg = new Image(qrcodeImgdata);
						//qrcodeImg.setBackgroundColor(redColor);
						qrcodeImg.setWidth(65f);
						qrcodeImg.setHeight(65f);
						//qrcodeImg.setPaddingTop(-10);
						qrcodeImg.setPaddings(0, 0, 0, 0);
						qrcodeImg.setMargins(0, 0, 0, 0);
						qrcodeImg.setHorizontalAlignment(HorizontalAlignment.CENTER);
					}else {
						System.out.println("qrcodeFilePath  did't found = " + qrcodeFilePath);
					}

				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e);
				}
				
							
				//float columnWidth[] = { 37, 10, 37,16 };
				float columnWidth[] = { 38, 9, 37,16 };
				Table headTable = new Table(UnitValue.createPercentArray(columnWidth)).useAllAvailableWidth()
						.setBorder(Border.NO_BORDER).setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE)
						.setMargins(6, 0, 0, 5).setPadding(0);

				//First Row
				headTable.addCell(new Cell().add(bharatSarkar).setMargin(0).setPadding(0).setVerticalAlignment(VerticalAlignment.BOTTOM).setTextAlignment(TextAlignment.CENTER).setPadding(0).setMargin(0).setBorder(Border.NO_BORDER));				
				if (ashokStambaImg != null)
					headTable.addCell(new Cell(3, 1).add(ashokStambaImg).setMargin(0).setPadding(0).setVerticalAlignment(VerticalAlignment.TOP).setBorder(Border.NO_BORDER));
				else
					headTable.addCell(new Cell(2, 1).add(new Paragraph("")).setVerticalAlignment(VerticalAlignment.TOP).setBorder(Border.NO_BORDER));

				headTable.addCell(new Cell().add(railMantralay).setMargin(0).setPadding(0).setVerticalAlignment(VerticalAlignment.BOTTOM).setTextAlignment(TextAlignment.CENTER).setPadding(0).setMargin(0).setBorder(Border.NO_BORDER));
				headTable.addCell(new Cell(3, 1).add(new Paragraph("")).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(Border.NO_BORDER));

				//Second Row
				headTable.addCell(new Cell().add(govIndia).setMargin(0).setPadding(0).setVerticalAlignment(VerticalAlignment.TOP).setTextAlignment(TextAlignment.CENTER).setPadding(0).setMargin(0).setBorder(Border.NO_BORDER));				
				headTable.addCell(new Cell().add(minRailway).setMargin(0).setPadding(0).setVerticalAlignment(VerticalAlignment.TOP).setTextAlignment(TextAlignment.CENTER).setPadding(0).setMargin(0).setBorder(Border.NO_BORDER));

				//Third Row
				//headTable.addCell(new Cell(1,4).add(icardParagraph).setMargin(0).setPadding(0).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER).setPadding(0).setMargin(0));
				headTable.addCell(new Cell().add(pechanpatraParagraph).setMargin(0).setPadding(0).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
				//headTable.addCell(new Cell().add(new Paragraph("")).setMargin(0).setPadding(0).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER).setPadding(0).setMargin(0).setBorder(Border.NO_BORDER));
				headTable.addCell(new Cell().add(icardtextParagraph).setMargin(0).setPadding(0).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
				//headTable.addCell(new Cell().add(new Paragraph("")).setMargin(0).setPadding(0).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER).setPadding(0).setMargin(0).setBorder(Border.NO_BORDER));

				
				float ecorTablecolumnWidth[] = { 40,60 };
				Table ecorTable = new Table(UnitValue.createPercentArray(ecorTablecolumnWidth)).useAllAvailableWidth()
						.setBorder(Border.NO_BORDER).setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE)
						.setMargins(0, 5, 0, 5).setPadding(0);
				ecorTable.addCell(new Cell().add(purbatatrailway).setMargin(0).setPadding(0).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.LEFT).setPaddingLeft(8).setMargin(0).setBackgroundColor(yellowColor).setBorderRight(Border.NO_BORDER).setBorderRight(Border.NO_BORDER));
				ecorTable.addCell(new Cell().add(ecorBbsr).setMargin(0).setPadding(0).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.RIGHT).setPaddingRight(5).setMargin(0).setBackgroundColor(yellowColor).setBorderLeft(Border.NO_BORDER).setBorderLeft(Border.NO_BORDER));

				
				
				
				
				
				
				Paragraph star = new Paragraph();
				star.add(noOfStars);
				star.setFont(boldFont);
				star.setFontSize(15);
				star.setCharacterSpacing(1.5f);
				//star.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 0.1f));
				star.setMultipliedLeading(0.1f);
				star.setVerticalAlignment(VerticalAlignment.BOTTOM);
				star.setHorizontalAlignment(HorizontalAlignment.CENTER);
				//star.setPaddingTop(6.0f);
				star.setPaddings(6.0f, 0, 0, 0);
				star.setMargin(0);
				star.setFontColor(redColor);

				
				//वैध तिथि
				Paragraph baidhTithi = new Paragraph();
				baidhTithi.add("oS/k frfFk");
				//baidhTithi.setFont(hinNormalFont);
				baidhTithi.setFont(hinBoldFont4);
				baidhTithi.setFontSize(8);
				//baidhTithi.setPaddingTop(4.0f);
				baidhTithi.setPaddings(3, 0, 0, 0);
				baidhTithi.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 0.4f));
				baidhTithi.setFontColor(redColor);

				
				//Valid Upto
				Paragraph validUpto = new Paragraph();
				validUpto.add("VALID UPTO");
				validUpto.setFont(normalFont);
				validUpto.setFontSize(firstPageFontsize);
				validUpto.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 0.5f));
				validUpto.setFontColor(redColor);

				
				//Valid Date
				Paragraph validDate = new Paragraph();
				validDate.add(validityUpto);
				validDate.setFont(normalFont);
				validDate.setFontSize(firstPageFontsize);
				validDate.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 0.5f));
				validDate.setFontColor(redColor);



				//Star Table
				float starTablecolumnWidth[] = { 20,35, 12, 18,15 };
				Table starTable = new Table(UnitValue.createPercentArray(starTablecolumnWidth)).useAllAvailableWidth()
						.setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE)
						.setMargins(0, 5, 0, 5).setPadding(0);
				
				starTable.addCell(new Cell().add(star).setPadding(0).setMargin(0).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
				starTable.addCell(new Cell().add(new Paragraph("")).setPadding(0).setMargin(0).setBorder(Border.NO_BORDER));
				starTable.addCell(new Cell().add(baidhTithi).setMargin(0).setPadding(0).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER).setPadding(0).setMargin(0).setBorderTop(Border.NO_BORDER));
				starTable.addCell(new Cell().add(validUpto).setMargin(0).setPadding(0).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER).setPadding(0).setMargin(0).setBorderTop(Border.NO_BORDER));
				starTable.addCell(new Cell().add(validDate).setMargin(0).setPadding(0).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER).setPadding(0).setMargin(0).setBorderTop(Border.NO_BORDER));
				
				
				float detailFontSize = 11.0f;//8.0f				
				float gapbetweenLinesFloat = 0.7f;//0.9f
				//Name Hindi
				Paragraph namehindi =  new Paragraph("uke");
				namehindi.setFont(hinBoldFont4);
				namehindi.setFontSize(detailFontSize);
				namehindi.setMultipliedLeading(gapbetweenLinesFloat);
				
				
				//Pad Naam Hindi
				Paragraph padnaam =  new Paragraph("in uke");
				padnaam.setFont(hinBoldFont4);
				padnaam.setFontSize(detailFontSize);
				padnaam.setMultipliedLeading(gapbetweenLinesFloat);
				
				//PF NO. Hindi  
				//Paragraph pfnohindi =  new Paragraph("ih-,Q-ua");
				Paragraph pfnohindi =  new Paragraph("vkbZ-Mh-ua");
				pfnohindi.setFont(hinBoldFont4);
				pfnohindi.setFontSize(detailFontSize);
				pfnohindi.setMultipliedLeading(gapbetweenLinesFloat);
				
				//Station Hindi
				Paragraph stationhindi =  new Paragraph("LVs'ku");
				stationhindi.setFont(hinBoldFont4);
				stationhindi.setFontSize(detailFontSize);
				stationhindi.setMultipliedLeading(gapbetweenLinesFloat);
				
				//DOB Hindi
				Paragraph dobhindi =  new Paragraph("tUe rkjh[k");
				dobhindi.setFont(hinBoldFont4);
				dobhindi.setFontSize(detailFontSize);
				dobhindi.setMultipliedLeading(gapbetweenLinesFloat);
				

				//float detailTableColumnWidth[] = { 20, 20, 14, 46 };
				float detailTableColumnWidth[] = { 19, 20, 14, 47 };
				Table detailTable = new Table(UnitValue.createPercentArray(detailTableColumnWidth))
						.useAllAvailableWidth().setBorder(Border.NO_BORDER)
						.setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE).setMarginTop(2).setPadding(0)
						.setFont(normalFont).setFontSize(firstPageFontsize7)
						.setMargins(0, 2, 0, 5);

			
				
				if (empImg != null)
					detailTable.addCell(new Cell(5, 1).add(empImg).setMargin(0).setPadding(0).setVerticalAlignment(VerticalAlignment.TOP).setBorder(Border.NO_BORDER));
				else
					detailTable.addCell(new Cell(5, 1).add(new Paragraph("")).setMargin(0).setPadding(0).setVerticalAlignment(VerticalAlignment.TOP).setBorder(Border.NO_BORDER));


				//1st ROW
				detailTable.addCell(new Cell().add(namehindi).setMargin(0).setPaddings(2, 0, 0, 0).setBorder(Border.NO_BORDER));
				detailTable.addCell(new Cell().add(new Paragraph("Name")).setMargin(0).setPaddings(2, 0, 0, 0).setBorder(Border.NO_BORDER).setFont(boldFont).setFontSize(firstPageFontsize7));	
				
				String empName = ": " + empNameString;
				float empNameFontsize = getFontSizeForNameAndDesig(pdfDocument, document, empName);
				detailTable.addCell(new Cell().add(new Paragraph(empName)).setMargin(0).setPaddings(2, 0, 0, 0).setBorder(Border.NO_BORDER).setMaxHeight(8).setFont(boldFont).setFontSize(empNameFontsize));

				//2nd ROW
				detailTable.addCell(new Cell().add(padnaam).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER));
				detailTable.addCell(new Cell().add(new Paragraph("Desig")).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER).setFont(boldFont).setFontSize(firstPageFontsize7));
				String empDesig = ": " + empDesigString;
				float empDesigFontsize = getFontSizeForNameAndDesig(pdfDocument, document, empDesig);
				detailTable.addCell(new Cell().add(new Paragraph(empDesig)).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER).setMaxHeight(8).setFont(boldFont).setFontSize(empDesigFontsize));

				detailTable.addCell(new Cell().add(pfnohindi).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER));
				detailTable.addCell(new Cell().add(new Paragraph("RUID No.")).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER).setFont(boldFont).setFontSize(firstPageFontsize7));
				detailTable.addCell(new Cell().add(new Paragraph(": " + ruidString)).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER).setFont(boldFont));

				detailTable.addCell(new Cell().add(stationhindi).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER));
				detailTable.addCell(new Cell().add(new Paragraph("Station")).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER).setFont(boldFont).setFontSize(firstPageFontsize7));
				detailTable.addCell(new Cell().add(new Paragraph(": " + empStation)).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER).setFont(boldFont));

				detailTable.addCell(new Cell().add(dobhindi).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER));
				detailTable.addCell(new Cell().add(new Paragraph("D.O.B")).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER).setFont(boldFont).setFontSize(firstPageFontsize7));
				detailTable.addCell(new Cell().add(new Paragraph(": " + empDob)).setMargin(0).setPadding(0).setBorder(Border.NO_BORDER).setFont(boldFont));
				
				//सं
				Text numberHindi = new Text("la");
				//numberHindi.setFont(hinNormalFont);
				//numberHindi.setFontSize(firstPageFontsize);
				numberHindi.setFont(hinBoldFont);
				numberHindi.setFontSize(7.0f);
				numberHindi.setFontColor(blueColor);

				String cardnoString = "/No." + deptSlno;
				Text gzicardno = new Text(cardnoString);
				gzicardno.setFont(boldFont);
				gzicardno.setFontSize(5);
				gzicardno.setFontColor(blueColor);
				
				//Identity Card Paragraph	
				Paragraph cardNoParagraph = new Paragraph();
				cardNoParagraph.add(numberHindi);
				cardNoParagraph.add(gzicardno);
				cardNoParagraph.setMultipliedLeading(0.7f);

				

				//Hindi signature of the card holder
				//Paragraph hindiSignOfTheCardHolder =  new Paragraph("deZpkjh ds gLrk{kj ;k vaxwBs dk fu'kku");
				Paragraph hindiSignOfTheCardHolder =  new Paragraph("dkMZ/kkjd dk gLrk{kj");
				hindiSignOfTheCardHolder.setFont(hinBoldFont4);
				hindiSignOfTheCardHolder.setWordSpacing(0.0f);
				//hindiSignOfTheCardHolder.setFontSize(5);
				hindiSignOfTheCardHolder.setFontSize(8);
				//hindiSignOfTheCardHolder.setMultipliedLeading(0.7f);
				hindiSignOfTheCardHolder.setMultipliedLeading(0.5f);
				hindiSignOfTheCardHolder.setPaddings(2, 0, 0, 0);
				
				//Hindi signature of the issuing authority
				//Paragraph hindiSignOfIssuingAuthority =  new Paragraph("tkjh djus okys çkf/kdj.k dk gLrk{kj");
				Paragraph hindiSignOfIssuingAuthority =  new Paragraph("tkjhdrkZ çkf/kdkjh dk gLrk{kj");
				hindiSignOfIssuingAuthority.setFont(hinBoldFont4);
				//hindiSignOfIssuingAuthority.setFontSize(5);
				hindiSignOfIssuingAuthority.setFontSize(8);
				//hindiSignOfIssuingAuthority.setMultipliedLeading(0.7f);
				hindiSignOfIssuingAuthority.setMultipliedLeading(0.5f);
				hindiSignOfIssuingAuthority.setPaddings(2, 0, 0, 0);
				
				//signature of the card holder
				Paragraph signOfTheCardHolder =  new Paragraph("Signature of the Card Holder");
				signOfTheCardHolder.setFont(normalFont);
				signOfTheCardHolder.setFontSize(6);
				signOfTheCardHolder.setMultipliedLeading(0.8f);
				
				//signature of the issuing authority
				Paragraph signOfIssuingAuthority =  new Paragraph("Signature of Issuing Authority");
				signOfIssuingAuthority.setFont(normalFont);
				signOfIssuingAuthority.setFontSize(6);
				signOfIssuingAuthority.setMultipliedLeading(0.8f);
				
				
				
				//float signTableColumnWidth[] = { 35, 26, 39 };
				float signTableColumnWidth[] = { 34, 28, 38 };
				Table signTable = new Table(UnitValue.createPercentArray(signTableColumnWidth)).useAllAvailableWidth()
						.setBorder(Border.NO_BORDER).setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE)
						.setMargins(0, 0, 0, 0) .setPadding(0);

				//First Row
				if (signImg != null)
					signTable.addCell(
							new Cell().add(signImg).setMargin(0).setPadding(0).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
				else
					signTable.addCell(new Cell().add(new Paragraph("")).setMargin(0).setPadding(0).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));

				signTable.addCell(new Cell().add(new Paragraph("")).setMargin(0).setPadding(0).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));

				if (officerSignImage != null)
					signTable.addCell(new Cell().add(officerSignImage).setMargin(0).setPadding(0).setTextAlignment(TextAlignment.CENTER).setMaxHeight(signImgHeight).setBorder(Border.NO_BORDER));
				else
					signTable.addCell(new Cell().add(new Paragraph("")).setMargin(0).setPadding(0).setTextAlignment(TextAlignment.CENTER).setMinHeight(signImgHeight).setBorder(Border.NO_BORDER));

				//Second Row
				
				//कार्डधारक का हस्ताक्षर
				signTable.addCell(new Cell().add(hindiSignOfTheCardHolder).setMargin(0).setPaddings(0, 0, 0, 2).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
				signTable.addCell(new Cell().add(new Paragraph("")).setMargin(0).setPadding(0).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));				
				signTable.addCell(new Cell().add(hindiSignOfIssuingAuthority).setMargin(0).setPadding(0).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER));

				//Third Row
				signTable.addCell(new Cell().add(signOfTheCardHolder).setMargin(0).setPadding(0).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
				signTable.addCell(new Cell().add(cardNoParagraph).setMargin(0).setPadding(0).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
				signTable.addCell(new Cell().add(signOfIssuingAuthority).setMargin(0).setPaddings(0, 0, 0, 3).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER));

				document.add(headTable);
				document.add(ecorTable);
				document.add(starTable);
				document.add(detailTable);
				document.add(signTable);

				try {

					// Second page
					document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

					Text hindiFamilyDetail = new Text("ifjokj dk fooj.k");
					hindiFamilyDetail.setFont(hinBoldFont4);
					hindiFamilyDetail.setFontSize(11.0f);


					Text familyDetail = new Text("/Details of the family");
					familyDetail.setFont(normalFont);
					familyDetail.setFontSize(9.0f);

					Paragraph headingParagraph = new Paragraph();
					headingParagraph.add(hindiFamilyDetail);
					headingParagraph.add(familyDetail);
					headingParagraph.setMultipliedLeading(0.5f);

					float familyTableColumnWidth[] = { 30, 11, 12, 9, 38 };

					Table familyTable = new Table(UnitValue.createPercentArray(familyTableColumnWidth))
							.useAllAvailableWidth().setBorder(Border.NO_BORDER)
							.setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE).setMargins(5, 2, 0, 5).setPadding(0)
							.setFont(normalFont).setFontSize(secondPageFontsize).setMaxHeight(100.0f);

					familyTable.addCell(new Cell(1, 5).add(headingParagraph).setMargin(0).setPaddings(3, 0, 3, 0)
							.setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));

					stFamily = con.createStatement();
					String familyQuery = "select FNAME,BLOOD_GROUP,RELATIONSHIP,DOB,ID_MARKS,SLNO from gaz_family  where ID_NO='"
							+ appid + "'order by slno";
					rsFamily = stFamily.executeQuery(familyQuery);

					List<Family> familyList = new ArrayList<>();

					while (rsFamily.next()) {

						String nameString = rsFamily.getString("FNAME").toLowerCase();
						nameString = capitalizeWords(nameString);
						//if(nameString !=null)
							//nameString = nameString.toUpperCase();
						String relationString = rsFamily.getString("RELATIONSHIP").toLowerCase();
						relationString = capitalizeWords(relationString);
						String dobString = rsFamily.getString("DOB");
						String bgString = rsFamily.getString("BLOOD_GROUP").toLowerCase();
						bgString = capitalizeWords(bgString);
						String idmarkString = rsFamily.getString("ID_MARKS").toLowerCase();
						idmarkString = capitalize(idmarkString);
						dobString = tf.format(df.parse(dobString));
						
						
						Family fam = new Family();
						fam.name = nameString;
						fam.relation = relationString;
						fam.dob = dobString;
						fam.bg = bgString;
						fam.idmark = idmarkString;
						familyList.add(fam);


					}
					
					
					float idmarkFontSize = getFontSizeForIdmark(pdfDocument, document, familyList);					
					float nameFontSize = getFontSizeForName(pdfDocument, document, familyList);
					float otherFontSize = 6.0f;
//					if(idmarkFontSize<=5.0f)
//						otherFontSize = 5.0f;
					
					for(Family fam : familyList) {
						
						familyTable.addCell(new Cell().add(new Paragraph(fam.name)).setMargin(0).setPaddings(0, 0, 1, 0)
								.setTextAlignment(TextAlignment.LEFT).setFontSize(nameFontSize).setVerticalAlignment(VerticalAlignment.MIDDLE).setMaxHeight(16.0f).setBorder(Border.NO_BORDER));
						familyTable.addCell(new Cell().add(new Paragraph(fam.relation)).setMargin(0).setPadding(0)
								.setTextAlignment(TextAlignment.LEFT).setFontSize(otherFontSize).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
						familyTable.addCell(new Cell().add(new Paragraph(fam.dob)).setMargin(0).setPadding(0)
								.setTextAlignment(TextAlignment.LEFT).setFontSize(otherFontSize).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
						familyTable.addCell(new Cell().add(new Paragraph(fam.bg)).setMargin(0).setPadding(0)
								.setTextAlignment(TextAlignment.LEFT).setFontSize(otherFontSize).setVerticalAlignment(VerticalAlignment.MIDDLE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
						//familyTable.addCell(new Cell().add(new Paragraph(idmarkString)).setMargin(0).setPadding(0)
							//	.setTextAlignment(TextAlignment.LEFT).setFontSize(secondPageFontsize).setVerticalAlignment(VerticalAlignment.MIDDLE).setMaxHeight(16.0f));
						
						
												
						Paragraph idmarkParagraph =  new Paragraph(fam.idmark);
						idmarkParagraph.setMargin(0);
						idmarkParagraph.setPadding(0);
						idmarkParagraph.setFont(normalFont);
						idmarkParagraph.setFontSize(idmarkFontSize);		

						familyTable.addCell(new Cell().add(idmarkParagraph).setMargin(0).setPadding(0)
									.setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE).setMaxHeight(16.0f).setBorder(Border.NO_BORDER));

					}
					
					
					
//					int i =0;
//					while (i<6) {
//
//						String nameString = "Jhsjkdfh dfsdklf skldfj skdfhhd skjy ksldhfiudsf ds";
//						String relationString =  "Daughter";
//						String dobString = "10-06-1986";
//						String bgString = "AB+ve";
//						String idmarkString = "kjahdsf aklsjdfh au lka shdfaksdf askjldfh kadfh askdfhasdfiu kj asjkdfhaisudf ";
//
//						familyTable.addCell(new Cell().add(new Paragraph(nameString)).setMargin(0).setPadding(0)
//								.setTextAlignment(TextAlignment.LEFT).setFontSize(secondPageFontsize).setVerticalAlignment(VerticalAlignment.MIDDLE).setMaxHeight(16.0f));
//						familyTable.addCell(new Cell().add(new Paragraph(relationString)).setMargin(0).setPadding(0)
//								.setTextAlignment(TextAlignment.LEFT).setFontSize(6.0f).setVerticalAlignment(VerticalAlignment.MIDDLE));
//						familyTable.addCell(new Cell().add(new Paragraph(dobString)).setMargin(0).setPadding(0)
//								.setTextAlignment(TextAlignment.LEFT).setFontSize(6.0f).setVerticalAlignment(VerticalAlignment.MIDDLE));
//						familyTable.addCell(new Cell().add(new Paragraph(bgString)).setMargin(0).setPadding(0)
//								.setTextAlignment(TextAlignment.LEFT).setFontSize(6.0f).setVerticalAlignment(VerticalAlignment.MIDDLE));
//						familyTable.addCell(new Cell().add(new Paragraph(idmarkString)).setMargin(0).setPadding(0)
//								.setTextAlignment(TextAlignment.LEFT).setFontSize(secondPageFontsize).setVerticalAlignment(VerticalAlignment.MIDDLE).setMaxHeight(16.0f));
//
//						 i++;
//					}

					document.add(familyTable);

					// Bottom address part

					Paragraph hindiIfFound = new Paragraph(";fn ;g dkMZ feys rks —i;k fudVre iksLV c‚Dl esa Mky nsaA");
					//hindiIfFound.setFont(hinBoldFont4);
					hindiIfFound.setFont(hinBoldFont);
					hindiIfFound.setFontSize(8.0f);
					hindiIfFound.setPaddings(2, 0, 0, 0);
					hindiIfFound.setMultipliedLeading(0.6f);
					

					Paragraph ifFound = new Paragraph("If found please drop it in the nearest Post Box");
					ifFound.setFont(normalFont);
					ifFound.setFontSize(7.0f);

					Text hindiAddress = new Text("?kj dk irk");
					hindiAddress.setFont(hinBoldFont4);
					hindiAddress.setFontSize(8.0f);

					Text engAddress = new Text("/Res.Address: ");
					engAddress.setFont(normalFont);
					engAddress.setFontSize(7.0f);

					Text orgAddress = new Text(empResAddress);
					orgAddress.setFont(normalFont);
					orgAddress.setFontSize(7.0f);

					Paragraph addParagraph = new Paragraph();
					addParagraph.add(hindiAddress);
					addParagraph.add(engAddress);
					addParagraph.add(orgAddress);
					addParagraph.setMargin(0);
					addParagraph.setPadding(0);
					addParagraph.setMultipliedLeading(0.7f);

					
					
					
					//signature of the card holder
					String emergencyContactNo = "Emergency Contact No. : " + empEmgContactNo;
					Paragraph emergencyContactNoPara =  new Paragraph(emergencyContactNo);
					emergencyContactNoPara.setFont(boldFont);
					emergencyContactNoPara.setFontSize(7.0f);
					//emergencyContactNoPara.setTextRenderingMode(PdfCanvasConstants.TextRenderingMode.FILL_STROKE);
					//emergencyContactNoPara.setStrokeWidth(0.05f);
					//emergencyContactNoPara.setfo;
					
					

					float addressTableColumnWidth[] = { 78, 22 };
					Table addressTable = new Table(UnitValue.createPercentArray(addressTableColumnWidth)).useAllAvailableWidth().setBorder(Border.NO_BORDER)
							.setBorderCollapse(BorderCollapsePropertyValue.COLLAPSE).setMargins(0, 1, 1, 5).setPadding(0)
							.setFont(normalFont).setFontSize(secondPageFontsize)
							.setVerticalAlignment(VerticalAlignment.BOTTOM);

					addressTable.addCell(new Cell().add(emergencyContactNoPara).setPaddings(0, 0, 5, 0).setMargin(0).setBorder(Border.NO_BORDER));
					if(qrcodeImg != null)
						//addressTable.addCell(new Cell(4,1).add(qrcodeImg).setPadding(0).setMargin(0).setVerticalAlignment(VerticalAlignment.TOP).setPaddings(-7, 0, 0, 0));
						addressTable.addCell(new Cell(4,1).add(qrcodeImg).setPadding(0).setMargin(0).setPaddings(-9, -9, -9, -9).setBorder(Border.NO_BORDER));
					else 
						addressTable.addCell(new Cell(4,1).add(new Paragraph("")).setPadding(0).setMargin(0).setVerticalAlignment(VerticalAlignment.TOP).setPaddings(-7, 0, 0, 0).setBorder(Border.NO_BORDER));
					
					addressTable.addCell(new Cell().add(addParagraph).setPaddings(0, 0, 5, 0).setMargin(0).setMaxHeight(18.0f).setBorder(Border.NO_BORDER));										
					
					addressTable.addCell(new Cell().add(hindiIfFound).setPaddings(0, 0, 2, 0).setMargin(0).setBorder(Border.NO_BORDER));
					addressTable.addCell(new Cell().add(ifFound).setPadding(0).setMargin(0).setBorder(Border.NO_BORDER));

					// How to calculate table height
					PageSize pdfSize = pdfDocument.getDefaultPageSize();
					IRenderer tableRenderer = familyTable.createRendererSubTree().setParent(document.getRenderer());
					LayoutResult tableLayoutResult = tableRenderer.layout(new LayoutContext(new LayoutArea(0, new Rectangle(pdfSize.getWidth(), 1000))));
					float familyTableHeight = tableLayoutResult.getOccupiedArea().getBBox().getHeight();

					System.out.println("familyTableHeight :" + familyTableHeight);
					
					tableRenderer = addressTable.createRendererSubTree().setParent(document.getRenderer());
					tableLayoutResult = tableRenderer.layout(new LayoutContext(new LayoutArea(0, new Rectangle(pdfSize.getWidth(), 1000))));
					float addressTableHeight = tableLayoutResult.getOccupiedArea().getBBox().getHeight();
					
					System.out.println("addressTableHeight :" + addressTableHeight);
					
					float gapBaetweenTables = 0f;
					if(familyTableHeight <= 70)
						gapBaetweenTables = 20.0f;
					else if(familyTableHeight <= 85)
						gapBaetweenTables = 10.0f;
					else
						gapBaetweenTables = 0.0f;
					
					
					//LEFT,BOTTOM,WIDTH
					addressTable.setFixedPosition(5, pdfSize.getHeight()-familyTableHeight - addressTableHeight -gapBaetweenTables , pdfSize.getWidth() - 5);
					//addressTable.setFixedPosition(5, 5, pdfSize.getWidth() - 5);

					document.add(addressTable);

				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("Unable get family details.");
					System.out.println(e.toString());

				}
			} else {

				Paragraph errorParagraph = new Paragraph(errorMsgString);
				document.add(errorParagraph);

			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			Paragraph errorParagraph = new Paragraph(errorMsgString);
			document.add(errorParagraph);

		}
	}
	
	public float getFontSizeForNameAndDesig(PdfDocument pdfDocument,Document document, String textToDisplay) {
		
		float fontSize = 7.0f;
		
		Cell tempcell = new Cell();
		tempcell.add(new Paragraph(textToDisplay));
		tempcell.setMargin(0);
		tempcell.setPadding(0);
		tempcell.setBorder(Border.NO_BORDER);
		tempcell.setFont(boldFont);
		
		PageSize pdfSize = pdfDocument.getDefaultPageSize();
		IRenderer tableRenderer = null;
		float pdfWidth = pdfSize.getWidth();
		pdfWidth = pdfWidth *0.47f; //Cell width set as 47%


		do {		
			System.out.println("For Text - "+textToDisplay + " Font Size - "+fontSize);
			tempcell.setFontSize(fontSize);			
			tableRenderer = tempcell.createRendererSubTree().setParent(document.getRenderer());				
			LayoutResult tableLayoutResult1 = tableRenderer.layout(new LayoutContext(new LayoutArea(0, new Rectangle(pdfWidth, 1000))));
			float cellheight = tableLayoutResult1.getOccupiedArea().getBBox().getHeight();
			System.out.println("cellheight : " +cellheight);
			if(cellheight <=8)
				break;
			
			fontSize -=1.0;

			
		}while(fontSize>=6);
				
		return fontSize;
	}
	
	public float getFontSizeForIdmark(PdfDocument pdfDocument,Document document, List<Family> famList) {

		float fontSize = 7.0f;
		float leastFontSize = 7.0f;

		PageSize pdfSize = pdfDocument.getDefaultPageSize();
		IRenderer tableRenderer = null;
		float pdfWidth = pdfSize.getWidth();
		pdfWidth = pdfWidth *0.30f; //Cell width set as 38%
		Cell cell = null;


		for (Family family : famList) {

			fontSize = 7.0f;
			cell = new Cell();
			cell.add(new Paragraph(family.idmark));
			cell.setMargin(0);
			cell.setPadding(0);
			cell.setBorder(Border.NO_BORDER);
			cell.setFont(normalFont);


			do {		
				System.out.println("For Text - "+family.idmark + " Font Size - "+fontSize);
				cell.setFontSize(fontSize);			
				tableRenderer = cell.createRendererSubTree().setParent(document.getRenderer());				
				LayoutResult tableLayoutResult1 = tableRenderer.layout(new LayoutContext(new LayoutArea(0, new Rectangle(pdfWidth, 1000))));
				float cellheight = tableLayoutResult1.getOccupiedArea().getBBox().getHeight();
				System.out.println("cellheight : " +cellheight);
				if(cellheight <=16)
					break;
				
				fontSize -=1.0;

			}while(fontSize>=6);
			
			if(leastFontSize>fontSize)
				leastFontSize = fontSize;
			
			if(leastFontSize == 5.0f)
				break;
		}

		System.out.println("Finanl font size for idmark = " +leastFontSize);
		return leastFontSize;
	}
	

	public float getFontSizeForName(PdfDocument pdfDocument,Document document, List<Family> famList) {

		float fontSize = 7.0f;
		float leastFontSize = 7.0f;

		PageSize pdfSize = pdfDocument.getDefaultPageSize();
		IRenderer tableRenderer = null;
		float pdfWidth = pdfSize.getWidth();
		pdfWidth = pdfWidth *0.30f; //Cell width set as 38%
		Cell cell = null;


		for (Family family : famList) {

			fontSize = 7.0f;
			cell = new Cell();
			cell.add(new Paragraph(family.name));
			cell.setMargin(0);
			cell.setPadding(0);
			cell.setBorder(Border.NO_BORDER);
			cell.setFont(normalFont);


			do {		
				System.out.println("For Text - "+family.name + " Font Size - "+fontSize);
				cell.setFontSize(fontSize);			
				tableRenderer = cell.createRendererSubTree().setParent(document.getRenderer());				
				LayoutResult tableLayoutResult1 = tableRenderer.layout(new LayoutContext(new LayoutArea(0, new Rectangle(pdfWidth, 1000))));
				float cellheight = tableLayoutResult1.getOccupiedArea().getBBox().getHeight();
				System.out.println("cellheight : " +cellheight);
				if(cellheight <=16)
					break;
				
				fontSize -=1.0;

			}while(fontSize>=6);
			
			if(leastFontSize>fontSize)
				leastFontSize = fontSize;
			
			if(leastFontSize == 5.0f)
				break;
		}

		System.out.println("Finanl font size for name = " +leastFontSize);

		return leastFontSize;
	}


	public String getDepartmentWiseHindiText(String strDeptCode) {

		String deptHindiTextString = "ys[kk";

		if (strDeptCode.toUpperCase().equals("001")) // ACCOUNTS
			deptHindiTextString = "ys[kk";
		else if (strDeptCode.toUpperCase().equals("002")) // COMMERCIAL
			deptHindiTextString = "O;kolkf;d";
		else if (strDeptCode.toUpperCase().equals("004"))// ELECTRICAL
			deptHindiTextString = "fo|qrh;";
		else if (strDeptCode.toUpperCase().equals("003"))// ENGINEERING
			deptHindiTextString = "vfHk;kaf=dh";
		else if (strDeptCode.toUpperCase().equals("010"))// GA
			deptHindiTextString = "th-,";
		else if (strDeptCode.toUpperCase().equals("009"))// MECHANICAL
			deptHindiTextString = ";kaf=d";
		else if (strDeptCode.toUpperCase().equals("013"))// MEDICAL
			deptHindiTextString = "fpfdRlk";
		else if (strDeptCode.toUpperCase().equals("005"))// OPERATING
			deptHindiTextString = "ifjpkyu";
		else if (strDeptCode.toUpperCase().equals("006"))// PERSONNEL
			deptHindiTextString = "dkfeZd";
		else if (strDeptCode.toUpperCase().equals("014"))// RRB
			deptHindiTextString = "vkjvkjch";
		else if (strDeptCode.toUpperCase().equals("007"))// S&T
			deptHindiTextString = ",l rFkk Vh";
		else if (strDeptCode.toUpperCase().equals("011"))// SAFETY
			deptHindiTextString = "lqj{kk";
		else if (strDeptCode.toUpperCase().equals("012"))// SECURITY
			deptHindiTextString = "lqj{kk";
		else if (strDeptCode.toUpperCase().equals("008"))// STORES
			deptHindiTextString = "HkaMkj";

		return deptHindiTextString;
	}

	public String capitalizeWords(String input) {

		if (input == null || input.length() == 0)
		{
			input = "";
			return input;
		}
			

		input = input.toLowerCase();
		// split the input string into an array of words
		String[] words = input.split("\\s");

		// StringBuilder to store the result
		StringBuilder result = new StringBuilder();

		// iterate through each word
		for (String word : words) {
			// capitalize the first letter, append the rest of the word, and add a space
			if(word.length()>0)
				result.append(Character.toTitleCase(word.charAt(0))).append(word.substring(1)).append(" ");
		}

		// convert StringBuilder to String and trim leading/trailing spaces
		return result.toString().trim();
	}

	public String capitalize(String str) {
		if (str == null || str.length() == 0) {
			str = "";
			return str;
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	
	public String getNonGazDeptSlno(String deptslno) {
		if (deptslno == null || deptslno.length() == 0) {
			deptslno = "";
			return deptslno;
		}
		
		String[] deptslnoarr = deptslno.split("-");
		System.out.println("deptslnoarr length = " + deptslnoarr.length);
		if(deptslnoarr.length >0)
			deptslno = deptslnoarr[deptslnoarr.length -1];
		return deptslno;
	}
	
	public boolean checkFileExist(String filePath) {
		
		Path path = Paths.get(filePath);
		if (Files.exists(path))
		  return true;		
		return false;
		
	}
	
	
	public static String encodeURLPathComponent(String path) {
	    try {
	    	
	    	path = new URI(null, null, path, null).toASCIIString();
	    	System.out.println("path = "+path);
	    } catch (Exception e) {
	        // do some error handling
	    	path = "";
			System.out.println(e.getMessage());
	    }
	    return path;
	}


}
