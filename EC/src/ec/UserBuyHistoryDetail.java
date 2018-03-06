package ec;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.BuyDataBeans;
import beans.BuyDetailDataBeans;
import beans.ItemDataBeans;
import dao.BuyDetailDAO;
import dao.ItemDAO;

/**
 * 購入履歴画面
 * @author d-yamaguchi
 *
 */
@WebServlet("/UserBuyHistoryDetail")

public class UserBuyHistoryDetail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*文字化け対策*/
		request.setCharacterEncoding("UTF-8");

		//セッション開始
		HttpSession session = request.getSession();

		BuyDataBeans bdb = new BuyDataBeans();

		bdb.setDeliveryMethodName(request.getParameter("deliveryMethodName"));
		bdb.setTotalPrice(Integer.parseInt(request.getParameter("totalPrice")));
		bdb.setDeliveryMethodPrice(Integer.parseInt(request.getParameter("deliveryMethodPrice")));
		bdb.setId(Integer.parseInt(request.getParameter("id")));
		String formatDate = request.getParameter("formatDate");

		//System.out.println(bdb.getId());
		BuyDetailDAO bdd = new BuyDetailDAO();
		try {
			ArrayList<BuyDetailDataBeans> bddb = bdd.getBuyDataBeansListByBuyId(bdb.getId());
			//商品のデータを得る
			ArrayList<ItemDataBeans>  itemList = new ArrayList<ItemDataBeans>();

			for (BuyDetailDataBeans data: bddb) {
				ItemDataBeans item = ItemDAO.getItemByItemID(data.getItemId());

				itemList.add(item);
			}


		request.setAttribute("buyDetail",bdb);
		request.setAttribute("formatDate", formatDate);
		request.setAttribute("itemList",itemList);

		request.getRequestDispatcher(EcHelper.USER_BUY_HISTORY_DETAIL_PAGE).forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("errorMessage", e.toString());
			response.sendRedirect("Error");
		}

	}
}