package edu.acc.j2ee.hubbub6;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringEscapeUtils;

public class Controller extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String action = request.getParameter("action");   
        if (action == null) action = "timeline";
        switch (action) {
            case "timeline": action = timeline(request); break;
            case "wall": action = wall(request); break;
            case "login": action = login(request); break;
            case "post": action = post(request); break;
            case "logout": action = logout(request); break;
            case "register": action = register(request); break;
            case "profile": action = profile(request); break;
            default: action = "timeline";
        }
        request.getRequestDispatcher(action + ".jsp").forward(request,response);
    }
    
    private String timeline(HttpServletRequest request) {
        HubbubDAO db = (HubbubDAO) getServletContext().getAttribute("db");
        List<Post> posts = db.getSortedPosts();
        if (db.getLastError() != null)
            request.setAttribute("flash", db.getLastError());
        request.setAttribute("posts", posts);
        return "timeline";
    }
    
    private String wall(HttpServletRequest request) {
        String wallOwner = request.getParameter("for");
        if (!wallOwner.matches("^\\w{4,12}$")) {
            request.setAttribute("flash", "Invalid user name: " + wallOwner);
        } else {
            HubbubDAO db = (HubbubDAO) getServletContext().getAttribute("db");
            List<Post> posts = db.getSortedPostsFor(wallOwner);
            if (db.getLastError() != null)
                request.setAttribute("flash", db.getLastError());
            request.setAttribute("subject", wallOwner);
            request.setAttribute("posts", posts);
        }
        return "wall";
    }
    
    private String login(HttpServletRequest request) {
        if (request.getMethod().equals("GET")) return "login";
        String userName = request.getParameter("user");
        String password = request.getParameter("pass");
        LoginBean bean = new LoginBean(userName, password);
        if (LoginValidator.validate(bean)) {
            HubbubDAO db = (HubbubDAO) getServletContext().getAttribute("db");
            User user = db.authenticate(userName, password);
            if (user == null) {
                String error = db.getLastError();
                request.setAttribute("flash", (error == null? "Access Denied" : error));
                return "login";
            } else {
                request.getSession().setAttribute("user", user);
                return timeline(request);
            }
        } else {
            request.setAttribute("flash", "Invalid user name or password");
            return "login";
        }
    }
    
    private String post(HttpServletRequest request) {
        if (request.getMethod().equals("GET")) return "post";
        String content = request.getParameter("content");
        if (content == null || content.length() < 1 || content.length() > 140) {
            request.setAttribute("flash", "Content must be between 1 and 140 characters.");
            request.setAttribute("content", content);
            return "post";
        }
        content = StringEscapeUtils.escapeHtml4(content);
        content = content.replace("'", "&#39;");
        HubbubDAO db = (HubbubDAO) this.getServletContext().getAttribute("db");
        User author = (User) request.getSession().getAttribute("user");
        db.addPost(content, author);
        if (db.getLastError() != null) {
            request.setAttribute("flash", db.getLastError());
            return "post";
        }
        return timeline(request);
    }
    
    private String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return timeline(request);
    }
    
    private String register(HttpServletRequest request) {
        if (request.getMethod().equals("GET")) return "register";
        String un = request.getParameter("user");
        String p1 = request.getParameter("pass1");
        String p2 = request.getParameter("pass2");
        String fn = request.getParameter("fname");
        String ln = request.getParameter("lname");
        String e  = request.getParameter("email");
        String z  = request.getParameter("zip");
        RegistrationBean bean = new RegistrationBean(un, p1, p2, fn, ln, e, z);
        if (!RegistrationValidator.isValid(bean)) {
            request.setAttribute("flash", "One or more fields are invalid.");
            request.setAttribute("bean", bean);
            return "register";
        }
        HubbubDAO db = (HubbubDAO)getServletContext().getAttribute("db");
        int id = db.register(bean);
        if (db.getLastError() != null) {
            request.setAttribute("flash", db.getLastError());
            request.setAttribute("bean", bean);
            return "register";
        }
        User user = db.getUserById(id);
        request.getSession().setAttribute("user", user);
        return timeline(request);
    }
    
    private String profile(HttpServletRequest request) {
        String subject = request.getParameter("for");
        if (!subject.matches("^\\w{4,12}$")) {
            request.setAttribute("flash", "Invalid user name");
            return "profile";
        }
        User user = (User)request.getSession().getAttribute("user");
        if (user == null) return timeline(request);
        HubbubDAO db = (HubbubDAO)getServletContext().getAttribute("db");
        Profile profile = db.getProfileFor(subject);
        if (request.getMethod().equals("GET")) {
            request.setAttribute("subject", subject);
            request.setAttribute("profile", profile);
            if (user.getUserName().equals(subject)) return "pedit";
            else return "profile";
        }
        if (!subject.equals(user.getUserName())) {
            request.setAttribute("flash", "Hubbub user and profile owner are different");
            return "profile";
        }
        ProfileBean bean = new ProfileBean();
        bean.setPassword1(request.getParameter("pass1"));
        bean.setPassword2(request.getParameter("pass2"));
        bean.setEmail(request.getParameter("email"));
        bean.setZip(request.getParameter("zip"));
        bean.setBiography(request.getParameter("biography"));
        if (!ProfileValidator.isValid(bean)) {
            request.setAttribute("flash", "One or more fields are invalid.");
            request.setAttribute("profile", bean);
            return "pedit";            
        }
        String bio = bean.getBiography();
        bio = StringEscapeUtils.escapeHtml4(bio);
        bio = bio.replace("'", "&#39;");
        profile.setBiography(bio);
        db.updateProfile(profile, bean, user);
        if (db.getLastError() != null) {
            request.setAttribute("flash", db.getLastError());
            request.setAttribute("profile", bean);
            return "pedit";
        }
        return wall(request);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
