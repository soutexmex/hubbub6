package edu.acc.j2ee.hubbub6;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class HubbubDAO {
    private String lastError;
    
    private Connection CONN;

    public HubbubDAO(String jdbcUrl) {
        try {
            CONN = DriverManager.getConnection(jdbcUrl);
            lastError = null;
        } catch (SQLException sqle) {
            lastError = sqle.getMessage();
        }
    }
    
    public String getLastError() { return lastError; }
    
    protected void addPost(Post post) {
        String sql = "INSERT INTO POSTS (content, authorid, postdate) VALUES (?,?,?)";
        PreparedStatement pstat = null;
        try {
            pstat = CONN.prepareStatement(sql);
            pstat.setString(1, post.getContent());
            pstat.setInt(2, post.getAuthor().getProfile().getId());
            pstat.setDate(3, new java.sql.Date(post.getPostDate().getTime()));
            pstat.executeUpdate();
            lastError = null;
        } catch (SQLException sqle) {
            lastError = sqle.getMessage();
        } finally {
            if (pstat != null)
                try {
                    pstat.close();
                } catch (SQLException sqle) {}
        }
    }

    public void addPost(String content, User user) {
        Post post = new Post(content, new Date());
        post.setAuthor(user);
        addPost(post);
    }
    
    public List<Post> getSortedPosts() {
        return getSortedPostsFor("%");
    }

    public List<Post> getSortedPostsFor(String userName) {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM Posts JOIN Profiles ON Posts.authorid = ";
        sql += "Profiles.id JOIN users ON profiles.id = users.profileid WHERE username LIKE '%s' ORDER BY postdate DESC";
        sql = String.format(sql, userName);
        Statement stat = null;
        ResultSet rs = null;
        try {
            stat = CONN.createStatement();
            rs = stat.executeQuery(sql);
            while (rs.next()) {
                Post p = new Post(
                        rs.getString("content"),
                        new Date(rs.getDate("postdate").getTime()),
                        getUserById(rs.getInt("authorid")),
                        rs.getInt(4)
                );
                posts.add(p);
            }
            lastError = null;
        } catch (SQLException sqle) {
            lastError = sqle.getMessage();
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException sqle) {}
            if (stat != null)
                try {
                    stat.close();
                } catch (SQLException sqle) {}            
        }
        return posts;   
    }
    
    public User getUserById(int id) {
        String sql = "SELECT * FROM USERS WHERE profileid = " + id;
        Statement stat = null;
        ResultSet rs = null;
        User user = null;
        try {
            stat = CONN.createStatement();
            rs = stat.executeQuery(sql);
            if (rs.next()) {
                user = new User(rs.getString("username"));
                user.getProfile().setId(rs.getInt("id"));
            }
            lastError = null;
        } catch (SQLException sqle) {
            lastError = sqle.getMessage();
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException sqle) {}
            if (stat != null)
                try {
                    stat.close();
                } catch (SQLException sqle) {}            
        }
        return user;
    }
    
    public Profile getProfileFor(int userId) {
        Profile profile = null;
        String sql = "SELECT * FROM Profiles WHERE id=" + userId;
        Statement stat = null;
        ResultSet rs = null;
        try {
            stat = CONN.createStatement();
            rs = stat.executeQuery(sql);
            if (rs.next()) {
                profile = new Profile(
                    new Date(rs.getDate("joindate").getTime()),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getString("email"),
                    rs.getString("zip")
                );
                profile.setBiography(rs.getString("biography"));
                profile.setId(rs.getInt("id"));
            }
        } catch (SQLException sqle) {
            lastError = sqle.getMessage();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException sqle) {}
            if (stat != null) try { stat.close(); } catch (SQLException sqle) {}
        }
        return profile;
    }

public Profile getProfileFor(String userName) {
        Profile profile = null;
        String sql = "SELECT * FROM Profiles JOIN Users ON Profiles.id = Users.profileid WHERE Users.username='%s'";
        sql = String.format(sql, userName);
        Statement stat = null;
        ResultSet rs = null;
        try {
            stat = CONN.createStatement();
            rs = stat.executeQuery(sql);
            if (rs.next()) {
                profile = new Profile(
                    new Date(rs.getDate("joindate").getTime()),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getString("email"),
                    rs.getString("zip")
                );
                profile.setBiography(rs.getString("biography"));
                profile.setId(rs.getInt(8));
            }
        } catch (SQLException sqle) {
            lastError = sqle.getMessage();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException sqle) {}
            if (stat != null) try { stat.close(); } catch (SQLException sqle) {}
        }
        return profile;
    }

    public User authenticate(String userName, String password) {
        User user = null;
        String sql = "SELECT * FROM USERS WHERE username = '%s' AND password = '%s'";
        sql = String.format(sql, userName, password);
        Statement stat = null;
        ResultSet rs = null;
        try {
            stat = CONN.createStatement();
            rs = stat.executeQuery(sql);
            if (rs.next()) {
                user = new User(rs.getString("username"));
                user.getProfile().setId(rs.getInt("id"));
            }
            lastError = null;
        } catch (SQLException sqle) {
            lastError = sqle.getMessage();
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException sqle) {}
            if (stat != null)
                try {
                    stat.close();
                } catch (SQLException sqle) {}
        }
        return user;
    }
    
    public int register(RegistrationBean bean) {
        
        String profSql = "INSERT INTO PROFILES (firstname,lastname,";
        profSql += "email,zip) VALUES (?,?,?,?)";
        String userSql = "INSERT INTO USERS (username,password, profileid) VALUES (?,?,?)";
        //String updateSql = "UPDATE Users SET profileid = ? WHERE id = ?";
        PreparedStatement pstatUser = null, pstatProf = null;
        ResultSet profRs = null;
        int id = 0;
        try {
            pstatProf = CONN.prepareStatement(profSql, Statement.RETURN_GENERATED_KEYS);
            pstatProf.setString(1, bean.getFirstName());
            pstatProf.setString(2, bean.getLastName());
            pstatProf.setString(3, bean.getEmail());
            pstatProf.setString(4, bean.getZipCode());            
            pstatProf.executeUpdate();
            profRs = pstatProf.getGeneratedKeys();
            
            //profRs = pstatUser.getGeneratedKeys();
            if (profRs.next())
                id = profRs.getInt(1);
            pstatUser = CONN.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);
            pstatUser.setString(1, bean.getUserName());
            pstatUser.setString(2, bean.getPassword1());
            pstatUser.setInt(3, id);
            pstatUser.executeUpdate();
            lastError = null;
        } catch (SQLException sqle) {
            lastError = sqle.getMessage();
        } finally {
            if (profRs != null)
                try { profRs.close(); } catch (SQLException sqle){}
            if (pstatUser != null)
                try { pstatUser.close(); } catch (SQLException sqle) {}
            if (pstatProf != null)
                try { pstatProf.close(); } catch (SQLException sqle) {}
            }
        return id;
    }
    
    public void updateProfile(Profile profile, ProfileBean bean, User user) {
        String sql = "UPDATE Profiles SET email='%s', zip='%s', biography='%s'";
        sql += " WHERE id=%d";
        sql = String.format(sql, bean.getEmail(), bean.getZip(),
                bean.getBiography(), profile.getId());
        Statement stat = null;
        try {
            stat = CONN.createStatement();
            stat.executeUpdate(sql);            
        } catch (SQLException sqle) {
            lastError = sqle.getMessage();
            return;
        } finally {
            if (stat != null) try {stat.close();} catch (SQLException sqle){}
        } 
        if (bean.getPassword1().length() > 0) {
            sql = "UPDATE Users SET password='" + bean.getPassword1() + "'";
            sql += " WHERE profileid=" + user.getProfile().getId();
            try {
                stat = CONN.createStatement();
                stat.executeUpdate(sql);
            } catch (SQLException sqle) {
                lastError = sqle.getMessage();            
            } finally {
                if (stat != null) try {stat.close();} catch (SQLException sqle){}            
            }
        }
    }

    public void close() {
        if(CONN != null)
            try {
                CONN.close();
            } catch (SQLException sqle) {}
    }
}
