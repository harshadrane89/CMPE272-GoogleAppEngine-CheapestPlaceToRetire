package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.BPTR.db.BPTR_Algo;
import com.BPTR.db.DataSource;
import com.BPTR.dto.BPTREvalObject;
import com.BPTR.input.BPTRBean;

public class CalculateCPTR extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
//		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub.
//		super.doPost(req, resp);
//		BPTRBean ref=new BPTRBean();
//		
//		ref.setMonthlyIncome(req.getParameter("income"));
//		
//		if(((String)req.getParameter("loc")).equalsIgnoreCase("Downtown"))
//		{
//			ref.setLocation(true);
//		}
//		else
//		{
//			ref.setLocation(false);
//		}
//		
//		
//		if(((String)req.getParameter("hp")).equalsIgnoreCase("Leased"))
//		{
//			ref.setHousingPref(true);
//		}
//		else
//		{
//			ref.setHousingPref(false);
//		}
//		
//		ref.setHouseCost(Long.parseLong(req.getParameter("hi")));
//		List<String> houseOpt=new ArrayList<String>();
//		
//		if((Boolean)(req.getParameter("hp1")!=null))
//		{
//			houseOpt.add("APT_BASIC");
//		}
//		if((Boolean)(req.getParameter("hp2")!=null))
//		{
//			houseOpt.add("APT_LUXURY");
//		}
//		if((Boolean)(req.getParameter("hp3")!=null))
//		{
//			houseOpt.add("HOUSE_BASIC");
//		}
//		if((Boolean)(req.getParameter("hp4")!=null))
//		{
//			houseOpt.add("HOUSE_LUXURY");
//		}
//		ref.setHousing_type(houseOpt);
//		ref.setNumberOfResults(Integer.parseInt(req.getParameter("nor")));
//		req.setAttribute("output",  new BPTR_Algo().findPlace(ref));
//		resp.setContentType("text/plain");
//        resp.getWriter().println("Hello, world");
		req.getRequestDispatcher("/Output.jsp").forward(req, resp);

	}
	

	
 	

}
