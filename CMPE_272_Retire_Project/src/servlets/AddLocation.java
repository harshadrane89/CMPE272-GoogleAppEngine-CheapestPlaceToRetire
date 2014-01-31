package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.BPTR.db.CountryMaster;
import com.BPTR.db.LocationManager;
import com.BPTR.input.NewLocationObj;

public class AddLocation extends HttpServlet {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("THE DO GET METHOD IS INVOKED");
//		super.doGet(req, resp);
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
		// TODO Auto-generated method stub
//		super.doPost(req, resp);

		NewLocationObj location = new NewLocationObj();

		String[] country = (String[]) req.getParameterValues("country");
		
		
		
		if(country!=null){
		if (!country[0].equalsIgnoreCase("Select Country")) 
		{
			for(String count:country)
				System.out.println("In for loop"+count);
			location.setCountryName(country[0]);
		}}
		else 
		{	System.out.println((String)req.getParameter("CountryName"));
			location.setCountryName((String)req.getParameter("CountryName"));
		}
		
		location.setLocationName((String)req.getParameter("loc_name"));
		System.out.println();
		location.setLat((String)req.getParameter("lat"));
		location.setLang((String)req.getParameter("lang"));
		location.setaMax((String)req.getParameter("aMin"));
		location.setaMax((String)req.getParameter("aMax"));
		location.sethMin((String)req.getParameter("hMin"));
		location.sethMax((String)req.getParameter("hMax"));
		location.setGrocery((String)req.getParameter("gExp"));
		location.setUtility((String)req.getParameter("uExp"));
		location.setOtherExp((String)req.getParameter("oExp"));
		location.setMedicalExp((String)req.getParameter("mExp"));
		location.setCountryId(new CountryMaster().getCountryId(location.getCountryName()));
		location.setClimate((String)req.getParameter("climate"));
		location.setLocationType((String)req.getParameter("loc"));
		location.setLocInfo((String)req.getParameter("info"));
		String output=(new LocationManager()).populateLocationMaster(location);
		req.setAttribute("output",  output);
		req.getRequestDispatcher("./AddForm.jsp").forward(req, resp);
		
	}
}
