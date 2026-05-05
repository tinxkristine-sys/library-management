package com;

public class CRUDOperation {
    public static void addBook (string title, string author, int categoryId, int quantity){
        String sql = "INSERT INTO BOOOKS (title, author, category_Id, quantity, available)"
                + "VALUES (?,?,?,?,?)";
        Connection conn = DBConnection.getConnection();
        if (conn == null) return;

        try (PreparedStatement ps == conn.prepareStatement (sql)){
            ps.setString(parameterIndex: 1, title);
            ps.setString(parameterIndex: 2, author);
            ps.setInt   (parameterIndex: 3, category_Id);
            ps.setInt   (parameterIndex: 4, quantity);
            ps.setInt   (parameterIndex: 5, quantity);
            int rows = ps. executeUpdate ()
            if (rows > 0)
        }


    
}
