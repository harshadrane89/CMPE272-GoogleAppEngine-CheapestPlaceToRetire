package servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.BPTR.db.BPTR_Algo;
import com.BPTR.db.TrendsValue;
import com.BPTR.dto.PlaceInfoObj;

public class Trend extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
//		super.doGet(req, resp);
		System.out.println("REACHED GET METHOD OF THE SERVLET");
		
		ArrayList<PlaceInfoObj> outputDTO=(ArrayList<PlaceInfoObj>)new TrendsValue().getTrends();;
		System.out.println(outputDTO.size());
		req.setAttribute("output",  outputDTO);
		req.getRequestDispatcher("./Trend.jsp").forward(req, resp);
		
	}

}
