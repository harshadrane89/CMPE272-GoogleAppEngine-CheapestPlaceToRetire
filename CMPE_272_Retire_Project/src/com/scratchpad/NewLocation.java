package com.scratchpad;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.BPTR.db.CountryMaster;

import com.BPTR.db.LocationManager;

import com.BPTR.input.NewLocationObj;

public class NewLocation extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// super.doGet(req, resp);
		try {
			System.out.println("reached get");
			// while(true)
			// {
			// System.out.println("REACHED HERE");
			// }
			doPost(req, resp);
			// req.getRequestDispatcher("/Output.jsp").forward(req, resp);
			// return;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			// log(e.printStackTrace());
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);

		NewLocationObj location = new NewLocationObj();

		String country = (String) req.getAttribute("country");

		if (!country.equalsIgnoreCase("Select Country")) {
			location.setCountryName(country);
		} else {
			location.setCountryName((String) req.getAttribute("CountryName"));
		}

		location.setLocationName((String) req.getAttribute("loc_name"));
		location.setLat((String) req.getAttribute("lat"));
		location.setLang((String) req.getAttribute("lang"));
		location.setaMax((String) req.getAttribute("aMin"));
		location.setaMax((String) req.getAttribute("aMax"));
		location.sethMin((String) req.getAttribute("hMin"));
		location.sethMax((String) req.getAttribute("hMax"));
		location.setGrocery((String) req.getAttribute("gExp"));
		location.setUtility((String) req.getAttribute("uExp"));
		location.setOtherExp((String) req.getAttribute("oExp"));
		location.setMedicalExp((String) req.getAttribute("mExp"));
		location.setCountryId(new CountryMaster().getCountryId(location
				.getCountryName()));
		location.setClimate((String) req.getAttribute("climate"));
		location.setLocationType((String) req.getAttribute("loc"));
		location.setLocInfo((String) req.getAttribute("info"));
		String output = (new LocationManager())
				.populateLocationMaster(location);
		req.setAttribute("output", output);
		req.getRequestDispatcher("./AddForm.jsp").forward(req, resp);

	}

}
