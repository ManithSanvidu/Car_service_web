package com.carservice.servlets;

import com.carservice.models.Booking;
import com.carservice.models.BookingList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/EditBookingServlet")
public class EditBookingServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String number = request.getParameter("number");
        String date = request.getParameter("date");
        String serviceType = request.getParameter("serviceType");
        String vehicle = request.getParameter("vehicle");

        String filePath = getServletContext().getRealPath("/txtfiles/bookings.txt");
        String loggedInUserName = (String) request.getSession().getAttribute("username");


        List<Map<String, String>> bookings = BookingManager.loadAndSortBookings(filePath, loggedInUserName);

        boolean updated = false;

        for (Map<String, String> booking : bookings) {
            if (booking.get("number").equals(number)) {
                booking.put("date", date);
                booking.put("service", serviceType);
                booking.put("vehicle", vehicle);
                updated = true;
                break;
            }
        }

        if (updated) {
            BookingList updatedList = new BookingList();
            for (Map<String, String> map : bookings) {
                Booking booking = new Booking(
                        map.get("name"),
                        map.get("number"),
                        map.get("vehicle"),
                        map.get("make"),
                        map.get("date"),
                        map.get("service")
                );
                updatedList.add(booking);
            }

            BookingManager.saveBookings(filePath, updatedList);
        }

        response.sendRedirect("serviceHistory.jsp");
    }
}
