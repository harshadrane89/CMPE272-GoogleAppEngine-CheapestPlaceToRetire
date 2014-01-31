package servlets;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.log.Log;

import com.BPTR.db.BPTR_Algo;
import com.BPTR.db.DataSource;
import com.BPTR.dto.PlaceInfoObj;
import com.BPTR.input.BPTRBean;

public class CalculateBPTR extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
//		super.doGet(req, resp);
		try {
			System.out.println("reached get");
//			while(true)
//			{
//				System.out.println("REACHED HERE");
//			}
			doPost(req, resp);
//			req.getRequestDispatcher("/Output.jsp").forward(req, resp);
//			return;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
//			log(e.printStackTrace());
		}
	
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
//		super.doPost(req, resp);
	
			BPTRBean ref=new BPTRBean();
			
			ref.setMonthlyIncome(req.getParameter("income"));
			
			if(((String)req.getParameter("loc")).equalsIgnoreCase("Downtown"))
			{
				ref.setLocation(true);
			}
			else
			{
				ref.setLocation(false);
			}
			
			
			if(((String)req.getParameter("hp")).equalsIgnoreCase("Leased"))
			{
				ref.setHousingPref(true);
			}
			else
			{
				ref.setHousingPref(false);
			}
			System.out.println("VALUE OF ME ==>"+req.getParameter("me"));
			
			
			if(((String)req.getParameter("me")).equalsIgnoreCase("no"))
			{
				ref.setMedical_expenses(false);
			}
			else
			{
				ref.setMedical_expenses(true);
			}
			
			ref.setHouseCost(Long.parseLong(req.getParameter("hi")));
			String[] prefLocation=null;
			if(req.getParameterValues("HousingPreferences")!=null)
			 prefLocation=req.getParameterValues("HousingPreferences");
			List<String> houseOpt=new ArrayList<String>();
			
			
			if(prefLocation!=null)
			for(String pref:prefLocation)
			{
				System.out.println("PREF ==>"+pref);
				if(pref.equalsIgnoreCase("Apartment"))
				{
					houseOpt.add("APT_BASIC");
				}
				if(pref.equalsIgnoreCase("Duplex Apartment"))
				{
					houseOpt.add("APT_LUXURY");
				}
				if(pref.equalsIgnoreCase("Duplex House"))
				{
					houseOpt.add("HOUSE_BASIC");
				}
				if(pref.equalsIgnoreCase("Mansion"))
				{
					houseOpt.add("HOUSE_LUXURY");
				}
			}
			
			if(houseOpt.isEmpty())
			{
				houseOpt.add("APT_BASIC");
				houseOpt.add("HOUSE_BASIC");
			}
			ref.setHousing_type(houseOpt);
			System.out.println("houseOPt==>"+req.getParameter("nor"));
			ref.setNumberOfResults(Integer.parseInt(req.getParameter("nor")));
			ArrayList<PlaceInfoObj> outputDTO=(ArrayList<PlaceInfoObj>)new BPTR_Algo().findPlace(ref);
			System.out.println(outputDTO.size());
			req.setAttribute("output",  outputDTO);
			req.getRequestDispatcher("./Output.jsp").forward(req, resp);
//			return;
	
	}
	
	
	
}
