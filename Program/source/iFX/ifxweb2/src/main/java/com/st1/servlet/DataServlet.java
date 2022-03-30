package com.st1.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DataServlet
 */
// @WebServlet("/DataServlet")
public class DataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DataServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String city = request.getParameter("cityName");
		String report = getWeather(city);
		response.setContentType("text/xml");
		PrintWriter out = response.getWriter();
		out.println("<weather><report>" + report + "</report></weather>");
		out.flush();
		out.close();
	}

	private String getWeather(String city) {
		String report;

		if (city.toLowerCase(Locale.TAIWAN).equals("trivandrum"))
			report = "Currently it is not raining in Trivandrum. Average temperature is 20";
		else if (city.toLowerCase(Locale.TAIWAN).equals("chennai"))
			report = "It��s a rainy season in Chennai now. Better get a umbrella before going out.";
		else if (city.toLowerCase(Locale.TAIWAN).equals("bangalore"))
			report = "It��s mostly cloudy in Bangalore. Good weather for a cricket match.";
		else
			report = "The City you have entered is not present in our system. May be it has been destroyed " + "in last World War or not yet built by the mankind";
		return report;
	}

}
